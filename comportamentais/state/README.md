# Atividade 017 - Padrão State

Sistema de acompanhamento de pedidos de uma loja virtual, refatorado do modelo original baseado
em um `String status` com condicionais espalhadas para o padrão de projeto **State**, que
encapsula em um objeto o comportamento de cada momento do ciclo de vida do pedido, incluindo as
transições permitidas.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── OrderState.java      // abstração dos estados (State)
├── PendingState.java    // PENDING
├── PaidState.java       // PAID
├── ShippedState.java    // SHIPPED
├── CancelledState.java  // CANCELLED
├── DeliveredState.java  // DELIVERED (desafio adicional)
├── Order.java           // o contexto (Context)
└── Main.java            // demonstração dos fluxos e das recusas
```

- [`Order.java`](src/Order.java) guarda uma referência `OrderState`, inicializada com
  `PendingState`. Suas operações públicas (`pay()`, `ship()`, `cancel()`, `deliver()`) apenas
  **delegam ao estado atual**, passando `this` como contexto. `getStatus()` delega a `getName()`.
  Não há nenhuma condicional por nome ou tipo de estado na classe.
- `changeState(OrderState)` existe para que os estados concretos façam as transições e rejeita
  `null` com `Objects.requireNonNull`. O `Main` nunca a chama diretamente — usa só as ações do
  pedido.
- Cada estado concreto decide, para cada ação, se ela é permitida: em caso positivo exibe a
  mensagem e troca o estado do pedido; em caso negativo exibe a recusa e **não altera nada**.

### Tabela de transições

| Estado | `pay()` | `ship()` | `cancel()` | `deliver()` |
| --- | --- | --- | --- | --- |
| **PENDING** | → PAID | recusado | → CANCELLED | recusado |
| **PAID** | recusado | → SHIPPED | → CANCELLED | recusado |
| **SHIPPED** | recusado | recusado | recusado | → DELIVERED |
| **CANCELLED** | recusado | recusado | recusado | recusado |
| **DELIVERED** | recusado | recusado | recusado | recusado |

`CancelledState` e `DeliveredState` são estados **terminais**: nenhuma ação é aceita a partir
deles.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
-- Order 1: ship before paying, then pay, ship and deliver --
Shipping not allowed
Status: PENDING
Payment confirmed
Status: PAID
Order shipped
Status: SHIPPED
Cancellation not allowed
Status: SHIPPED
Order delivered
Status: DELIVERED
Cancellation not allowed
Status: DELIVERED
Delivery not allowed
Status: DELIVERED

-- Order 2: deliver before shipping, then cancel while pending --
Delivery not allowed
Status: PENDING
Order cancelled
Status: CANCELLED
Payment not allowed
Status: CANCELLED

-- Order 3: pay, try to deliver without shipping, then cancel --
Payment confirmed
Status: PAID
Delivery not allowed
Status: PAID
Order cancelled
Status: CANCELLED
```

A saída cobre todos os cenários pedidos, e o `Status:` após cada ação comprova que **toda recusa
preserva o estado anterior**:

| Cenário | Onde aparece |
| --- | --- |
| Envio antes do pagamento | Pedido 1, 1ª ação (`PENDING` mantido) |
| Fluxo de pagamento e envio | Pedido 1 (`PENDING` → `PAID` → `SHIPPED`) |
| Recusa de cancelamento após o envio | Pedido 1, 4ª ação (`SHIPPED` mantido) |
| Fluxo completo até a entrega | Pedido 1 (`SHIPPED` → `DELIVERED`) |
| Cancelamento após a entrega | Pedido 1, 6ª ação (`DELIVERED` mantido) |
| Entrega antes do envio | Pedido 2 (em `PENDING`) e Pedido 3 (em `PAID`) |
| Cancelamento de pedido pendente | Pedido 2 (`PENDING` → `CANCELLED`) |
| Pagamento de pedido cancelado | Pedido 2, 3ª ação (`CANCELLED` mantido) |
| Cancelamento de pedido pago | Pedido 3 (`PAID` → `CANCELLED`) |

## Desafio adicional — entrega do pedido

Foram acrescentados `deliver()` em `Order`, `deliver(Order order)` em `OrderState` e a classe
[`DeliveredState.java`](src/DeliveredState.java). Apenas `ShippedState` aceita a entrega e faz a
transição para `DeliveredState`; os demais estados recusam sem alterar nada, e `DeliveredState`
recusa todas as ações, inclusive uma nova entrega.

Note a assimetria revelada pelo exercício: acrescentar o **estado** `DeliveredState` não exigiu
tocar em nenhuma classe existente, mas acrescentar a **operação** `deliver` obrigou a alterar o
contrato `OrderState` e, por consequência, todas as suas implementações. Isso é discutido na
questão (e).

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o State?**

A dispersão das regras de estado em condicionais repetidas. Na versão original, cada método de
`Order` reimplementava a pergunta "em que estado eu estou?" — `pay()` testava `PENDING`, `ship()`
testava `PAID`, `cancel()` testava `PENDING || PAID`. O conhecimento sobre um mesmo estado ficava
**espalhado por vários métodos**, em vez de reunido em um lugar: para saber tudo o que um pedido
pago pode fazer, era preciso ler a classe inteira. Com isso, incluir um estado como `DELIVERED`
significaria revisar todas as condicionais de todos os métodos, com risco real de esquecer uma e
permitir uma operação indevida. Além disso, o estado era uma `String`, sujeita a erro de digitação
e sem qualquer verificação do compilador. No State, cada estado é uma classe que concentra as
respostas para todas as ações, e o compilador passa a exigir que nenhuma ação fique sem resposta.

**b) Quais classes representam o contexto, a abstração de estado e os estados concretos na sua implementação?**

- **Context (contexto):** `Order`, que mantém o estado atual e delega as ações a ele.
- **State (abstração):** a interface `OrderState`.
- **Concrete States:** `PendingState`, `PaidState`, `ShippedState`, `CancelledState` e
  `DeliveredState`.
- **Cliente:** `Main`, que interage apenas com as ações públicas do pedido.

**c) Onde ficam as regras de transição e por que uma operação recusada deve preservar o estado atual?**

As regras ficam **dentro de cada estado concreto**: é o `PendingState` que sabe que pagar leva a
`PaidState` e que enviar não é permitido; é o `ShippedState` que sabe que a única continuação
possível é a entrega. O contexto não participa dessa decisão — ele apenas oferece
`changeState(...)` para que o estado aplique a transição que ele mesmo escolheu.

Preservar o estado em uma recusa é essencial porque a operação simplesmente **não aconteceu**: o
pedido continua exatamente como estava antes da tentativa. Se uma recusa alterasse o estado, o
sistema entraria em uma situação inconsistente com a realidade — um pedido enviado que "regride"
porque alguém tentou cancelá-lo, por exemplo, poderia liberar ações indevidas em seguida, ou
estornar um pagamento de uma mercadoria já despachada. A recusa é uma operação **sem efeito
colateral**: informa o cliente e mantém a integridade do ciclo de vida, o que a saída da
demonstração comprova linha a linha.

**d) Embora State e Strategy utilizem composição e delegação, como suas intenções e a responsabilidade pela troca dos objetos se diferenciam neste sistema?**

A estrutura é praticamente a mesma — um contexto que delega a um objeto intercambiável atrás de
uma interface — mas a intenção e, sobretudo, **quem troca o objeto** são diferentes:

- No **Strategy** ([Atividade 015](../strategy)), as estratégias são **alternativas equivalentes**
  para a mesma tarefa: calcular frete por entrega expressa ou econômica produz resultados
  diferentes, mas nenhuma é "o próximo passo" da outra. Quem escolhe é o **cliente**, de fora,
  conforme sua necessidade, e as estratégias não sabem umas das outras — por isso a calculadora
  expõe um `setStrategy(...)` público e a ordem das trocas é arbitrária.
- No **State**, os estados **não são alternativas, são momentos de um ciclo de vida** com uma
  ordem própria: `PENDING` → `PAID` → `SHIPPED` → `DELIVERED`. Quem troca é o **próprio estado**,
  de dentro, como consequência de uma ação do usuário — o cliente chama `order.pay()`, não
  `order.changeState(new PaidState())`. Os estados **conhecem seus sucessores**, algo que seria um
  defeito no Strategy e é a essência do State.

Há ainda uma consequência prática: no Strategy, o comportamento do contexto é sempre o mesmo e só
o resultado muda; no State, a **mesma chamada** (`order.cancel()`) pode cancelar o pedido ou ser
recusada, dependendo do momento — é como se o objeto mudasse de classe em tempo de execução.

**e) Quais partes da solução precisam mudar ao acrescentar um estado e ao acrescentar uma operação comum a todos os estados?**

As duas mudanças têm custos bem diferentes, e o desafio adicional mostrou isso na prática:

- **Acrescentar um estado** é barato: basta criar uma classe implementando `OrderState` e fazer o
  estado anterior apontar para ela na transição apropriada. `Order` não muda, a interface não
  muda e os estados não relacionados não mudam. Se `DeliveredState` fosse o único acréscimo, a
  alteração se limitaria à nova classe e a uma linha em `ShippedState` — próximo do ideal do OCP.
- **Acrescentar uma operação** é caro: incluir `deliver` exigiu alterar a interface `OrderState`,
  **todas as cinco implementações** e também a classe `Order` (para expor o método público). O
  compilador avisa sobre cada implementação faltante, então o risco de esquecer um caso é baixo,
  mas o esforço cresce linearmente com o número de estados.

Em resumo, o State otimiza para sistemas em que **os estados variam mais do que as operações** —
o caso típico de um fluxo de pedidos, em que novas etapas do ciclo de vida surgem com frequência
muito maior do que novas ações. Quando o conjunto de operações é instável, uma classe base
abstrata com recusas como comportamento padrão (deixando cada estado sobrescrever só o que
permite) reduz bastante esse custo, ao preço de perder a verificação do compilador sobre os casos
não tratados.
