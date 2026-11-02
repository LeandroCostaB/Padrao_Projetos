# Atividade 020 - Padrão Command

Sistema de controle de iluminação, refatorado do modelo original em que o controle remoto decidia
a ação por texto (`if ("ON".equals(action))`) para o padrão de projeto **Command**, que encapsula
cada solicitação em um objeto que reúne a operação e o destinatário responsável por executá-la.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── Command.java                // abstração: execute() e undo()
├── Light.java                  // receptor: estado e operações da lâmpada
├── TurnOnLightCommand.java     // comando concreto: ligar
├── TurnOffLightCommand.java    // comando concreto: desligar
├── RemoteControl.java          // invocador: botões -> comandos
└── Main.java                   // cliente: monta as associações
```

- [`RemoteControl.java`](src/RemoteControl.java) mantém um `Map<String, Command>` e **não tem
  nenhuma referência a `Light`** — nem no construtor, nem em campo. `pressButton` apenas busca o
  comando e chama `execute()`, sem qualquer condicional por nome de ação ou tipo de comando.
- O `HashMap` já faz a comparação exata de nomes exigida pelo requisito 5 (via `equals` de
  `String`), então `"KITCHEN_ON"` e `"kitchen_on"` são botões diferentes.
- `setCommand` recusa nome nulo ou vazio e comando nulo, e substitui o comando anterior quando o
  botão já existe. Configurar um botão **nunca** executa o comando.
- [`Light.java`](src/Light.java) permanece dona do estado e das operações. Ligar uma lâmpada já
  ligada mantém o estado e exibe a mensagem novamente, como o enunciado permite.
- Cada comando guarda o estado anterior da lâmpada **antes** de executar, e `undo()` restaura esse
  estado em vez de aplicar a operação oposta.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
-- Configuring the buttons runs no command --
States: Living room=false, Kitchen=false

-- Pressing the three configured buttons --
Living room: light on
Living room: light off
Kitchen: light on
States: Living room=false, Kitchen=true

-- LIVING_ON reconfigured to turn the kitchen off --
Kitchen: light off
States: Living room=false, Kitchen=false

-- Button that was never configured --
Button not configured: BEDROOM_ON

-- Names are compared exactly, so lower case does not match --
Button not configured: kitchen_on

-- Rejecting an invalid configuration --
Refused: button must not be empty
Refused: command must not be null

-- Undo after turning on a light that was off --
Kitchen: light on
States: Living room=false, Kitchen=true
Kitchen: light off
States: Living room=false, Kitchen=false

-- Undo after turning on a light that was already on --
Kitchen: light on
Kitchen: light on
States: Living room=false, Kitchen=true
The last action survives an unknown button and a reconfiguration:
Button not configured: BEDROOM_ON
Kitchen: light on
States: Living room=false, Kitchen=true

-- Undoing again, with no action available --
Nothing to undo
```

Pontos a observar na saída:

- após configurar os três botões, as duas lâmpadas continuam **desligadas**: a configuração não
  executa nada;
- ao reconfigurar `LIVING_ON` para desligar a cozinha, o acionamento afeta **somente a cozinha** —
  a sala permanece em `false`, provando que o botão passou a apontar para outro destinatário;
- botão inexistente e botão em minúsculas apenas avisam, sem executar ação alguma.

## Desafio adicional — desfazer a última ação

`Command` ganhou `undo()`, os comandos passaram a guardar `previousState` antes de executar e
`RemoteControl` passou a ter `undoLast()`, que desfaz o último comando executado **por um botão
válido** e depois limpa a referência.

A diferença entre restaurar e inverter fica visível nos dois blocos de desfazer:

| Cenário | Estado antes | Ação | `undo()` | Resultado |
| --- | --- | --- | --- | --- |
| Lâmpada desligada | `false` | ligar | restaura `false` | apagada |
| Lâmpada já ligada | `true` | ligar | restaura `true` | **continua acesa** |

No segundo caso, inverter a operação apagaria a luz — um estado em que ela nunca estivera. A
saída confirma `Kitchen=true` após o desfazer.

A demonstração também cobre as regras de preservação da referência: entre a execução e o
`undoLast()` há um botão não configurado e uma reconfiguração de botão, e nenhum dos dois apaga ou
substitui a última ação. Depois do desfazer, a segunda tentativa exibe `Nothing to undo`.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Command?**

O acoplamento do invocador ao receptor e a inexistência da solicitação como objeto.
`RemoteControl` declarava um campo `Light`, recebia a lâmpada no construtor e decidia, por
`if/else` sobre uma `String`, qual método chamar — ou seja, conhecia o dispositivo **e** sua
interface. As consequências: (1) cada nova ação ou dispositivo exigia modificar as condicionais do
controle; (2) a solicitação existia apenas como texto, sem reunir operação e destinatário, o que
impedia guardá-la, enfileirá-la ou desfazê-la; e (3) os botões eram fixos — não havia como
reconfigurar um botão para outra ação, já que o comportamento estava codificado no `pressButton`.
Com o Command, a solicitação passa a ser um objeto de primeira classe, e o controle se torna um
acionador genérico que serve para qualquer dispositivo.

**b) Quais classes representam o invocador, o receptor, os comandos concretos e o cliente na sua implementação?**

- **Invoker (invocador):** `RemoteControl`, que aciona comandos sem saber o que fazem.
- **Receiver (receptor):** `Light`, que executa o trabalho real e mantém o estado.
- **Command (abstração):** a interface `Command`.
- **Concrete Commands:** `TurnOnLightCommand` e `TurnOffLightCommand`, cada um ligando uma
  operação a um receptor específico.
- **Client (cliente):** `Main`, que cria lâmpadas e comandos e associa os comandos aos botões.

**c) Como seria possível acrescentar um comando para outro dispositivo sem modificar `RemoteControl`?**

Basta criar o novo receptor e um comando que implemente `Command` delegando a ele, e então
associá-lo a um botão no cliente. Por exemplo, para um portão:

```java
public class OpenGateCommand implements Command {

    private final Gate gate;

    public OpenGateCommand(Gate gate) {
        this.gate = Objects.requireNonNull(gate, "gate must not be null");
    }

    @Override
    public void execute() {
        gate.open();
    }

    @Override
    public void undo() {
        gate.close();
    }
}
```

```java
remote.setCommand("GATE_OPEN", new OpenGateCommand(new Gate()));
```

`RemoteControl` não muda em uma única linha, porque ele depende apenas de `Command`: para ele,
acionar uma lâmpada, um portão ou um alarme é exatamente a mesma operação. É o mesmo raciocínio do
Open/Closed visto no [Strategy](../strategy) — o sistema se estende por código novo, não por
edição do código existente. Vale notar que, em um sistema real, `Gate` precisaria decidir como
expor seu estado anterior para que o `undo` fosse uma restauração e não uma inversão, como
discutido na questão seguinte.

**d) Por que desfazer uma ação pode exigir armazenar o estado anterior, em vez de apenas executar a operação oposta?**

Porque a operação oposta só equivale a desfazer quando a ação de fato **mudou** o estado. Ligar uma
lâmpada que já estava acesa não altera nada; desfazer isso executando `turnOff()` levaria o sistema
a um estado em que ele nunca estivera, o que é um efeito colateral, não um desfazer. O problema
aparece sempre que a operação não é uma bijeção: comandos idempotentes (ligar o que já está
ligado), comandos que sobrescrevem um valor anterior (mudar o volume de 20 para 50 — a "operação
oposta" não existe sem saber que era 20), comandos que falham parcialmente, ou que agregam em uma
coleção com duplicatas.

Guardar o estado anterior transforma o desfazer em uma **restauração**: o comando volta ao
*snapshot* que ele mesmo capturou antes de agir, independentemente de ter mudado algo ou não. É por
isso que o requisito pede explicitamente essa abordagem, e é o que a tabela da saída comprova. O
custo é que cada comando precisa carregar os dados necessários — para um `boolean` isso é trivial,
mas para um estado grande seria preciso considerar memória, e aí entram variações como registrar
apenas o *delta* ou usar o padrão Memento, que existe justamente para encapsular esses
instantâneos.

**e) Embora Command e Strategy encapsulem comportamentos, como representar uma solicitação difere de representar um algoritmo intercambiável?**

Os dois padrões envolvem um objeto com uma única operação principal, mas respondem a perguntas
diferentes:

- O **Strategy** ([Atividade 015](../strategy)) representa **como** fazer algo: ele é uma
  alternativa entre várias para a *mesma* tarefa, e o contexto escolhe uma delas para variar seu
  próprio comportamento. `calculate(weight)` recebe os dados de fora e **retorna um resultado**; a
  estratégia não sabe sobre quem está operando e normalmente não guarda estado entre chamadas.
- O **Command** representa **o que** fazer, **para quem** e **quando**: ele já vem com o receptor
  embutido (`new TurnOnLightCommand(kitchen)`), de modo que a solicitação é completa e
  autocontida. `execute()` não recebe parâmetros nem retorna valor — produz um efeito. É isso que
  permite tratá-la como um dado: armazenar em um mapa de botões, enfileirar, agendar, registrar em
  log, transmitir pela rede ou **desfazer**, guardando o estado anterior.

A diferença prática no comparativo das duas atividades: a `ShippingCalculator` tem *uma* estratégia
que define como ela calcula; o `RemoteControl` tem *muitas* solicitações independentes, cada uma
apontando para um alvo diferente, e seu trabalho é só disparar a que o usuário escolher. Por isso o
Strategy raramente precisa de histórico, enquanto desfazer, refazer e filas de execução são temas
naturais do Command.
