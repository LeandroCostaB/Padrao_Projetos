# Atividade 018 - Padrão Observer

Sistema de monitoramento meteorológico, refatorado para o padrão de projeto **Observer**, que
permite a um objeto notificar vários interessados sobre mudanças de estado sem conhecer suas
classes concretas, com inscrições que podem ser criadas e canceladas em tempo de execução.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── TemperatureSubject.java        // abstração do sujeito (Subject)
├── TemperatureObserver.java       // abstração do observador (Observer)
├── WeatherStation.java            // sujeito concreto (Concrete Subject)
├── CurrentConditionsDisplay.java  // painel: exibe toda temperatura
├── TemperatureAlert.java          // alerta: só a partir de 35 C
├── AverageTemperatureDisplay.java // painel de média (desafio adicional)
└── Main.java                      // cliente: cadastra, remove e registra temperaturas
```

- [`WeatherStation.java`](src/WeatherStation.java) implementa `TemperatureSubject` e mantém a
  temperatura atual e uma `List<TemperatureObserver>`. **O construtor não recebe nenhum painel**:
  a estação nasce sem observadores e nunca menciona um tipo concreto.
- `addObserver` recusa `null` com `Objects.requireNonNull` e ignora o cadastro repetido da mesma
  instância, sem disparar nenhuma notificação imediata. `removeObserver` simplesmente não faz
  nada quando a instância não está cadastrada.
- A comparação usa `==` (identidade) em vez de `equals`, porque o requisito trata de **a mesma
  instância**: dois painéis distintos, mesmo que fossem considerados equivalentes por um
  `equals`, são dois interessados legítimos e devem receber a notificação separadamente.
- `setTemperature` atualiza o estado e chama `notifyObservers()`, que percorre a lista chamando
  `update(temperature)` uma vez para cada observador — sem condicionais por tipo e sem comparar a
  temperatura com a anterior.
- A regra dos 35 °C permanece dentro de [`TemperatureAlert.java`](src/TemperatureAlert.java), como
  exigido: a estação não sabe que existe um limite.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
-- Registering the same display instance again --
Observer already registered

-- Rejecting a null observer --
Refused: observer must not be null

-- Temperature 28 C --
Current temperature: 28.0 C

-- Temperature 36 C --
Current temperature: 36.0 C
High temperature alert: 36.0 C

-- Alert removed, temperature 38 C --
Current temperature: 38.0 C

-- Removing the alert again has no effect --

-- Display removed, temperature 40 C: no observers left --
Station still works, current temperature: 40.0 C

-- New station with the average display --
Average temperature: 20.0 C
Average temperature: 25.0 C
Average temperature: 30.0 C
```

A saída comprova cada ponto exigido:

- a segunda tentativa de cadastrar o **mesmo** painel é recusada, e por isso cada temperatura
  aparece **uma única vez** no painel (28, 36 e 38 °C);
- o alerta aparece **somente** para 36 °C — não para 28 °C (abaixo do limite) nem para 38 °C (já
  havia sido removido);
- remover o alerta uma segunda vez não produz efeito nem erro;
- com todos os observadores removidos, `setTemperature(40.0)` não falha: a estação continua
  registrando o estado, apenas não há ninguém para notificar.

## Desafio adicional — painel de média

[`AverageTemperatureDisplay.java`](src/AverageTemperatureDisplay.java) acumula a soma e a
quantidade de atualizações **recebidas por ele** e exibe a média a cada notificação. Cadastrado em
uma estação nova, com 20, 30 e 40 °C, produz exatamente 20 °C, 25 °C e 30 °C.

O painel não consulta nenhum histórico da estação — e a estação não guarda histórico algum. Cada
observador mantém o estado que lhe interessa, o que é justamente o que permitiu acrescentá-lo sem
alterar `WeatherStation`, as abstrações ou os outros dois observadores: a única classe nova é a
dele próprio.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Observer?**

O acoplamento direto da estação a cada destinatário concreto. `WeatherStation` declarava campos
dos tipos `CurrentConditionsDisplay` e `TemperatureAlert`, recebia os dois no construtor e chamava
**métodos com nomes diferentes** (`showTemperature` e `checkTemperature`) — ou seja, conhecia não
só os tipos, mas a interface particular de cada um. Isso trazia três limitações: (1) cada novo
interessado exigia modificar a estação (novo campo, novo parâmetro, nova chamada); (2) a lista de
destinatários era **fixa**, definida na construção, sem qualquer forma de cadastrar ou remover
interessados durante a execução; e (3) a estação acumulava a responsabilidade de saber quem deve
ser avisado. Com o Observer, a estação depende apenas de `TemperatureObserver` e de uma coleção
dinâmica: quem se interessa se inscreve, e a estação não precisa ser alterada nunca mais.

**b) Quais abstrações e classes representam o sujeito, o sujeito concreto, o observador e os observadores concretos na sua implementação?**

- **Subject (abstração):** a interface `TemperatureSubject`.
- **Concrete Subject:** `WeatherStation`.
- **Observer (abstração):** a interface `TemperatureObserver`.
- **Concrete Observers:** `CurrentConditionsDisplay`, `TemperatureAlert` e
  `AverageTemperatureDisplay`.
- **Cliente:** `Main`, que monta as inscrições e provoca as mudanças de estado.

**c) Como o cadastro e a remoção de observadores alteram os destinatários das notificações durante a execução?**

A lista de observadores é consultada **no momento da notificação**, não na construção da estação.
Por isso `addObserver` e `removeObserver` mudam o conjunto de destinatários das notificações
**seguintes**, sem afetar as anteriores e sem recriar nada. A demonstração mostra isso de forma
direta: as mesmas três chamadas a `setTemperature` atingem dois observadores (28 e 36 °C), depois
apenas um (38 °C, após remover o alerta) e finalmente nenhum (40 °C). Vale notar que o cadastro
**não** dispara notificação imediata — o observador começa a receber dados a partir da próxima
mudança de estado, o que é o motivo pelo qual o painel de média considera apenas o que veio depois
da sua inscrição.

**d) A solução envia a temperatura no método `update()`, utilizando a abordagem push. Como seria uma alternativa pull, em que o observador consulta o estado do sujeito?**

No modelo **pull**, a notificação seria apenas um aviso de que algo mudou, e o observador buscaria
os dados por conta própria. A assinatura passaria a receber o sujeito em vez do valor:

```java
public interface TemperatureObserver {
    void update(TemperatureSubject subject);
}
```

E cada observador chamaria `((WeatherStation) subject).getTemperature()` — ou, melhor, o getter
seria declarado na própria abstração do sujeito, evitando o *cast*. A estação já expõe
`getTemperature()`, então a mudança seria pequena.

A comparação entre as duas abordagens: o **push** é mais simples, mantém os observadores sem
dependência do tipo do sujeito e deixa claro o que foi notificado — mas se o estado crescer (o
sistema passar a ter umidade, pressão e vento), a assinatura de `update` cresce com ele, e todo
observador recebe tudo, mesmo que use um único valor. O **pull** escala melhor nesse caso, porque
cada observador busca só o que lhe interessa, ao custo de acoplá-lo à interface do sujeito e de
abrir espaço para inconsistências: se o estado mudar novamente antes da consulta, o observador
pode ler um valor diferente do que motivou a notificação. Para um único `double`, o push é a
escolha adequada.

**e) Como um observador lento ou que lança uma exceção pode afetar os demais em uma notificação síncrona? Que cuidados seriam necessários em um sistema real?**

Como `notifyObservers()` percorre a lista chamando `update()` de forma síncrona e sequencial, cada
observador executa **dentro** da chamada de `setTemperature`. As consequências:

- um observador **lento** (que acessasse disco, rede ou banco) atrasa todos os que vêm depois dele
  na lista e também quem chamou `setTemperature`, que fica bloqueado até o último observador
  terminar;
- um observador que **lança uma exceção** interrompe o laço: os observadores restantes
  **simplesmente não são notificados**, e a exceção sobe para quem registrou a temperatura — um
  painel com defeito acabaria derrubando o alerta de temperatura alta, embora os dois não tenham
  nenhuma relação entre si;
- a ordem de notificação passa a ser um comportamento observável, embora o padrão não garanta
  ordem alguma.

Em um sistema real, os cuidados seriam: envolver cada `update()` em `try/catch`, registrando a
falha e seguindo para o próximo observador (isolamento de falhas); impor um limite de tempo ou
tornar a notificação **assíncrona** (fila de eventos, *thread pool*), aceitando que a entrega
deixa de ser imediata; manter os observadores rápidos, delegando trabalho pesado para outro
contexto; e iterar sobre uma **cópia** da lista, já que um observador que se descadastra durante a
notificação causaria `ConcurrentModificationException` — cenário que o enunciado desta atividade
explicitamente deixou de fora, mas que apareceria em produção. Em ambiente concorrente seria
necessário ainda sincronizar o acesso à lista ou usar uma coleção como
`CopyOnWriteArrayList`.
