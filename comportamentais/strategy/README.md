# Atividade 015 - Padrão Strategy

Sistema de cálculo de frete de uma loja virtual, refatorado do modelo original baseado em
condicionais (`if/else` por nome da modalidade) para o padrão de projeto **Strategy**, que
encapsula cada algoritmo em uma classe própria e permite trocá-lo em tempo de execução.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── ShippingStrategy.java   // abstração comum (Strategy)
├── StandardShipping.java   // R$ 10,00 + R$ 2,00/kg
├── ExpressShipping.java    // R$ 20,00 + R$ 5,00/kg
├── StorePickup.java        // retirada na loja: sem frete
├── EconomyShipping.java    // R$ 5,00 + R$ 1,50/kg (desafio adicional)
├── ShippingCalculator.java // o contexto (Context)
└── Main.java               // cliente: escolhe e troca as estratégias
```

- [`ShippingStrategy.java`](src/ShippingStrategy.java) declara a única operação:
  `double calculate(double weight)`.
- Cada estratégia concreta guarda **apenas a sua fórmula** — nenhuma delas conhece as outras
  nem o contexto.
- [`ShippingCalculator.java`](src/ShippingCalculator.java) recebe uma estratégia pelo construtor,
  permite substituí-la com `setStrategy(...)` e delega o cálculo à estratégia atual. Não há
  nenhum `if` por nome, código ou tipo concreto dentro da calculadora. Tanto o construtor quanto
  o `setStrategy` rejeitam `null` com `Objects.requireNonNull`, o que impede que a calculadora
  fique em um estado inválido.
- [`Main.java`](src/Main.java) é quem escolhe e cria as estratégias, usando **a mesma instância**
  da calculadora para as quatro modalidades.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
Standard: R$ 16.0
Express: R$ 35.0
Pickup: R$ 0.0
Economy: R$ 9.5

Rejecting a null strategy:
Refused: strategy must not be null
Strategy still in use: R$ 9.5
```

Para um pedido de 3 kg os valores conferem com o esperado: R$ 16,00 (10 + 3×2), R$ 35,00
(20 + 3×5), R$ 0,00 e R$ 9,50 (5 + 3×1,5). O último bloco mostra que a tentativa de atribuir uma
estratégia nula é recusada e que a calculadora **continua funcionando com a estratégia anterior**.

## Desafio adicional — entrega econômica

Para a nova modalidade bastou criar [`EconomyShipping.java`](src/EconomyShipping.java),
implementando `ShippingStrategy` com a fórmula `5.0 + weight * 1.5`. **Nenhuma classe existente
foi alterada**: nem `ShippingCalculator`, nem a abstração, nem as outras estratégias. A única
linha nova fora da classe criada está em `Main`, que é justamente o lugar onde a escolha deve
acontecer:

```java
calculator.setStrategy(new EconomyShipping());
```

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Strategy?**

A classe `ShippingCalculator` original concentrava **todas as regras de negócio e também a lógica
de seleção** em um único método, encadeando `if/else` por uma `String`. Isso trazia três
problemas: (1) cada nova modalidade exigia **modificar** uma classe já testada, aumentando a
cadeia de condicionais; (2) as fórmulas ficavam misturadas, impossibilitando testar ou reutilizar
um algoritmo isoladamente; e (3) a modalidade era identificada por texto livre, de modo que um
valor errado (`"EXPRES"`, `"express"`) só era descoberto em tempo de execução, via exceção. Com o
Strategy, cada algoritmo vira uma classe independente, a seleção passa a ser **verificada pelo
compilador** (um tipo, não uma string) e a calculadora deixa de ter condicionais.

**b) Quais classes representam o contexto, a estratégia e as estratégias concretas na sua implementação?**

- **Context (contexto):** `ShippingCalculator`, que mantém a referência para a estratégia e
  delega o cálculo.
- **Strategy (abstração):** a interface `ShippingStrategy`.
- **Concrete Strategies:** `StandardShipping`, `ExpressShipping`, `StorePickup` e
  `EconomyShipping`.
- **Cliente:** `Main`, responsável por escolher e injetar a estratégia.

**c) Como a composição e a delegação permitem trocar o algoritmo durante a execução?**

A calculadora **tem uma** estratégia (composição) em vez de **ser** uma implementação específica
(herança). Como esse vínculo é apenas uma referência a uma interface, ele pode ser reapontado a
qualquer momento com `setStrategy(...)` — algo impossível com herança, em que a escolha ficaria
congelada em tempo de compilação, na criação do objeto. Quando `calculate(weight)` é chamado, a
calculadora não decide nada: ela **delega** para o objeto que estiver na referência naquele
instante, e o despacho dinâmico do Java executa a implementação correta. Por isso a demonstração
usa uma única calculadora para quatro modalidades diferentes, sem recriá-la.

**d) Qual é a responsabilidade do cliente na seleção da estratégia e por que essa escolha não deve ficar dentro da calculadora?**

O cliente é quem **conhece o contexto da decisão** (o tipo de entrega escolhido no pedido) e, por
isso, é ele quem cria a estratégia concreta e a entrega à calculadora. Se a seleção ficasse dentro
da calculadora, o problema original voltaria: ela precisaria de um `if`/`switch` sobre algum
identificador, passaria a **depender de todas as estratégias concretas** e teria que ser
modificada a cada nova modalidade — violando o OCP e o princípio da responsabilidade única
(calcular *e* escolher são responsabilidades distintas, com motivos de mudança distintos).
Mantendo a escolha fora, a calculadora depende apenas da abstração e permanece estável.

Vale notar que, em um sistema real, essa seleção normalmente não ficaria espalhada pelo código
cliente: ela seria concentrada em uma **fábrica** ou em um mapa de configuração, que traduz a
opção vinda da interface do usuário para a estratégia correspondente. O ponto do padrão é que esse
`switch` exista no máximo em **um** lugar — o da criação — e não dentro de quem usa o algoritmo.

**e) Como a inclusão de uma nova modalidade de entrega se relaciona com o princípio Open/Closed (OCP)?**

É a demonstração direta do princípio. O sistema está **aberto para extensão**: a modalidade
econômica foi adicionada criando uma classe nova que implementa a abstração já existente. E está
**fechado para modificação**: `ShippingStrategy`, `ShippingCalculator`, `StandardShipping`,
`ExpressShipping` e `StorePickup` não sofreram nenhuma alteração — nenhum código já testado
precisou ser tocado. Na versão original, a mesma mudança exigiria editar o método `calculate`,
acrescentando mais um `else if` e correndo o risco de quebrar as modalidades que já funcionavam.
