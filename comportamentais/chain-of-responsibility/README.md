# Atividade 021 - Padrão Chain of Responsibility

Sistema de aprovação de despesas, refatorado do modelo original em que uma única classe concentrava
todos os limites em uma cadeia de `if/else` para o padrão de projeto **Chain of Responsibility**,
que organiza os possíveis responsáveis em uma sequência onde cada um decide se trata a solicitação
ou a encaminha ao próximo.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── ExpenseRequest.java          // a solicitação (Request)
├── ExpenseApprover.java         // tratador abstrato: elo e encaminhamento
├── TeamLeaderApprover.java      // limite R$ 500,00
├── ManagerApprover.java         // limite R$ 2.000,00
├── DirectorApprover.java        // limite R$ 10.000,00
├── PresidentApprover.java       // limite R$ 50.000,00 (desafio adicional)
├── ExpenseApprovalService.java  // apenas delega ao início da cadeia
└── Main.java                    // monta a cadeia e envia as solicitações
```

[`ExpenseApprover.java`](src/ExpenseApprover.java) é uma classe abstrata que concentra o que é
comum a todos os responsáveis — a referência para o próximo elo e a lógica de encaminhamento:

```java
public final void approve(ExpenseRequest request) {
    if (request.getAmountInCents() <= getLimitInCents()) {
        System.out.println(getTitle() + " approved: " + request.getDescription());
        return;
    }

    if (next == null) {
        System.out.println("No approver available: " + request.getDescription());
        return;
    }

    next.approve(request);
}
```

Assim, cada responsável concreto declara **apenas o próprio limite e o próprio nome**, sem
conhecer os tipos ou os limites dos demais. O `approve` é `final` pelo mesmo motivo do
[Template Method](../template-method): nenhuma subclasse pode quebrar a regra de encerrar o
processamento após aprovar nem a de encaminhar quando o valor excede seu limite.

- A comparação usa `<=`, tornando os limites **inclusivos** como exigido.
- [`ExpenseApprovalService.java`](src/ExpenseApprovalService.java) foi preservada, mas perdeu toda
  a lógica de seleção: hoje guarda uma referência `ExpenseApprover` e apenas delega.
- Os valores são sempre inteiros em centavos, o que evita erros de arredondamento de ponto
  flutuante na comparação com os limites.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
-- Chain: team leader -> manager -> director --
Team leader approved: Office supplies (R$ 300.00)
Manager approved: Training (R$ 1,500.00)
Director approved: Equipment (R$ 8,000.00)
No approver available: Office renovation (R$ 15,000.00)

-- Amounts exactly on each limit --
Team leader approved: Exactly R$ 500.00
Manager approved: Exactly R$ 2,000.00
Director approved: Exactly R$ 10,000.00

-- The president joins the end of the same chain --
President approved: Office renovation (R$ 15,000.00)
President approved: Exactly R$ 50,000.00
No approver available: One cent above R$ 50,000.00

-- Smaller amounts keep their original approvers --
Team leader approved: Office supplies (R$ 300.00)
Manager approved: Training (R$ 1,500.00)
Director approved: Equipment (R$ 8,000.00)

-- Request validations --
Refused: Description is required
Refused: Amount must be positive
```

Cada solicitação produz **uma única linha** de saída, o que comprova que o processamento termina no
primeiro responsável capaz de aprovar e que ninguém aprova acima do próprio limite. Os valores
exatamente iguais aos limites são aprovados pelo responsável correspondente, confirmando a
inclusividade.

## Desafio adicional — aprovação pela presidência

A única classe nova é [`PresidentApprover.java`](src/PresidentApprover.java), e a única alteração
fora dela é **uma linha na montagem da cadeia**:

```java
director.setNext(new PresidentApprover());
```

Nenhum aprovador existente foi modificado. A demonstração usa deliberadamente **a mesma instância**
de `ExpenseApprovalService` e os mesmos aprovadores antes e depois dessa linha, o que evidencia
dois pontos:

- a despesa de R$ 15.000,00, que antes não tinha aprovador, passa a ser aprovada pela presidência;
- R$ 50.000,00 é aprovado no limite e R$ 50.000,01 (um centavo acima) fica sem aprovador;
- as despesas menores continuam sendo tratadas pelos mesmos responsáveis de antes — o bloco final
  repete as três primeiras despesas e produz exatamente as mesmas aprovações.

A montagem inicial também chama `director.setNext(null)` explicitamente, representando o fim da
cadeia antes da extensão.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Chain of Responsibility?**

A centralização de todas as regras de aprovação em uma classe que conhecia **todos** os níveis
hierárquicos. `ExpenseApprovalService` tinha os quatro limites codificados em uma sequência fixa de
`if/else`, com três consequências: (1) incluir um responsável ou alterar um limite exigia modificar
essa classe, que precisava ser reaberta a cada mudança organizacional; (2) a ordem de verificação
estava **embutida no código**, então não havia como montar fluxos diferentes (por filial, por tipo
de despesa) combinando os responsáveis disponíveis; e (3) o conhecimento sobre um responsável
estava fora dele — o limite do diretor não morava em nenhuma classe "diretor". Na versão
refatorada, cada responsável encapsula o próprio limite e o fluxo passa a ser **dado de
configuração**, montado por composição no cliente.

**b) Quais abstrações e classes representam o tratador, os tratadores concretos, a solicitação e o cliente na sua implementação?**

- **Handler (tratador abstrato):** a classe abstrata `ExpenseApprover`, que mantém o elo `next` e
  implementa o encaminhamento.
- **Concrete Handlers:** `TeamLeaderApprover`, `ManagerApprover`, `DirectorApprover` e
  `PresidentApprover`.
- **Request (solicitação):** `ExpenseRequest`, imutável, com descrição não vazia e valor positivo
  em centavos.
- **Cliente:** `Main`, que monta a cadeia e envia as solicitações ao primeiro elo — opcionalmente
  através de `ExpenseApprovalService`, que hoje é apenas um ponto de entrada que delega.

**c) O que aconteceria com as despesas de menor valor se o diretor fosse colocado no início da cadeia? Por que a ordem dos responsáveis faz parte da configuração do fluxo?**

O diretor passaria a aprovar **tudo** até R$ 10.000,00, incluindo os R$ 300,00 de material de
escritório: como seu limite é inclusivo e abrange os limites dos outros, ele seria sempre o
primeiro capaz de tratar a solicitação e encerraria o processamento ali mesmo. Líder e gerente
nunca receberiam nada — ficariam inalcançáveis na cadeia, sem que nenhum erro fosse acusado.

A ordem importa porque a cadeia implementa a política de "o **primeiro** responsável capaz
resolve", e os limites aqui são **cumulativos** (cada nível superior cobre o inferior). Isso
significa que a hierarquia só é respeitada se os responsáveis estiverem ordenados do menor para o
maior limite. Em outras palavras, a correção do sistema depende de duas coisas: do limite de cada
responsável (que está na classe) e da **sequência** em que foram ligados (que está na montagem).
Essa é a contrapartida da flexibilidade do padrão: montar a cadeia por composição permite fluxos
diferentes sem tocar no código, mas transfere para quem monta a responsabilidade de ordená-la
corretamente — um erro de configuração não é detectado pelo compilador.

**d) Como a solução trata uma solicitação que nenhum responsável consegue aprovar e por que esse caso precisa estar previsto?**

Quando o valor excede o limite e `next` é `null`, o último elo exibe
`No approver available: <descrição>` e encerra. A mensagem aparece **uma única vez**, porque é o
fim da recursão: cada elo anterior já havia delegado e não imprime nada por conta própria.

Esse caso precisa estar previsto porque é um resultado **legítimo e esperado** do processo, não uma
anomalia: nada garante que algum responsável cubra qualquer valor possível, e foi exatamente o que
aconteceu com os R$ 15.000,00 antes da entrada da presidência. Se o caso não fosse tratado, a
cadeia terminaria em silêncio — o pior desfecho possível, porque o solicitante não saberia se a
despesa foi aprovada, recusada ou perdida, e o sistema não teria como distinguir "ninguém pode
aprovar" de "está tudo certo". Tratar explicitamente o fim da cadeia garante que **toda**
solicitação receba uma resposta. Em um sistema real, esse ponto seria o lugar natural para
registrar a ocorrência em log, notificar o solicitante ou escalar para uma aprovação manual.

**e) Como o encaminhamento entre responsáveis difere da notificação de todos os interessados no Observer? Por que esta cadeia deve parar após a primeira aprovação?**

No [Observer](../observer), o sujeito percorre **toda** a lista e notifica **todos** os inscritos:
a difusão é o objetivo, os observadores são independentes entre si, não há ordem significativa e
nenhum deles pode "consumir" o evento impedindo que os outros o recebam. Quando a estação registrou
36 °C, o painel e o alerta reagiram ambos.

No Chain of Responsibility, a travessia é **condicional e exclusiva**: cada elo decide se trata a
solicitação (e então a cadeia para) ou se a repassa (e então sai de cena). Os elos estão ligados
**entre si**, não a um distribuidor central, e a ordem é parte da regra de negócio. O objetivo não
é informar, é **encontrar um responsável**.

A cadeia precisa parar após a primeira aprovação porque, do contrário, uma despesa de R$ 300,00
seria aprovada pelo líder, depois pelo gerente, depois pelo diretor e pela presidência — quatro
aprovações para um único pedido. No mundo real isso significaria quatro autorizações de pagamento
para a mesma despesa, com risco de reembolso duplicado, além de destruir o sentido da hierarquia:
se todos aprovam tudo, não existem níveis de alçada. A regra "o primeiro responsável capaz encerra
o processamento" é o que garante **exatamente uma** decisão por solicitação — e é por isso que o
`return` após a aprovação, e o `approve` ser `final`, são detalhes essenciais, não estilísticos.
