# Atividade 011 - Padrão Decorator

Sistema de pedidos de bebidas de uma cafeteria, refatorado do modelo original baseado em
herança (uma classe para cada combinação de bebida e adicionais) para o padrão de projeto
**Decorator**, que permite adicionar responsabilidades a um objeto dinamicamente, em tempo
de execução, sem a explosão de subclasses.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

- [`Beverage.java`](src/Beverage.java) — abstração comum (o **Component**), com as operações
  `getDescription()` e `getCost()`.
- [`Coffee.java`](src/Coffee.java) e [`Tea.java`](src/Tea.java) — bebidas básicas (**Concrete
  Components**), R$ 5,00 e R$ 4,00 respectivamente.
- [`BeverageDecorator.java`](src/BeverageDecorator.java) — decorador base (**Decorator**),
  abstrato, que implementa `Beverage`, guarda uma referência a outro `Beverage` (recebida no
  construtor) e delega `getDescription()`/`getCost()` a ele por padrão.
- [`MilkDecorator.java`](src/MilkDecorator.java) (+R$ 1,50),
  [`ChocolateDecorator.java`](src/ChocolateDecorator.java) (+R$ 2,00),
  [`WhippedCreamDecorator.java`](src/WhippedCreamDecorator.java) (+R$ 2,50) e
  [`CaramelDecorator.java`](src/CaramelDecorator.java) (+R$ 1,00) — decoradores concretos,
  cada um complementando a descrição e o custo da bebida envolvida.
- [`Main.java`](src/Main.java) — demonstração de uso.

Um mesmo decorador pode envolver outro decorador (ou uma bebida) quantas vezes for
necessário, permitindo combinar e repetir adicionais livremente, sempre programando apenas
contra a abstração `Beverage`.

## Como compilar e executar

```bash
cd src
javac *.java
java Main
```

Saída esperada:

```
Coffee
Total: R$ 5.0

Coffee, milk, chocolate
Total: R$ 8.5

Tea, milk
Total: R$ 5.5

Coffee, chocolate, chocolate
Total: R$ 9.0

Coffee, milk, chocolate, caramel
Total: R$ 9.5
```

## Desafio adicional — CaramelDecorator

Para adicionar o caramelo (R$ 1,00) bastou criar a nova classe
[`CaramelDecorator.java`](src/CaramelDecorator.java), estendendo `BeverageDecorator` do mesmo
jeito que os demais adicionais — nenhuma classe existente (`Beverage`, `Coffee`, `Tea`,
`BeverageDecorator` ou os outros decoradores) precisou ser alterada. O uso combinado com
outros adicionais é demonstrado em `Main.java`:

```java
Beverage coffeeWithMilkChocolateAndCaramel =
        new CaramelDecorator(new ChocolateDecorator(new MilkDecorator(new Coffee())));
```

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Decorator?**

O código original criava uma classe concreta para cada combinação possível de bebida e
adicionais (`CoffeeWithMilk`, `CoffeeWithMilkAndChocolate` etc.). Isso gera explosão
combinatória de classes a cada novo adicional ou nova bebida, além de duplicar em cada
classe a lógica de cálculo de preço e montagem da descrição — qualquer alteração no cardápio
(ex.: mudar o preço do leite) precisaria ser replicada em várias classes. O Decorator resolve
isso ao representar cada adicional como um envoltório independente que pode ser combinado
livremente em tempo de execução, eliminando a necessidade de uma classe por combinação.

**b) Quais classes representam o componente, os componentes concretos, o decorador base e os decoradores concretos na sua implementação?**

- **Component**: `Beverage` (interface).
- **Concrete Components**: `Coffee` e `Tea`.
- **Decorator base**: `BeverageDecorator` (implementa `Beverage` e mantém a referência ao
  `Beverage` decorado).
- **Concrete Decorators**: `MilkDecorator`, `ChocolateDecorator`, `WhippedCreamDecorator` e
  `CaramelDecorator`.

**c) Por que o decorador deve implementar a mesma abstração do objeto que ele envolve?**

Porque isso é o que permite empilhar decoradores e tratar o resultado de forma uniforme: como
`BeverageDecorator` também é um `Beverage`, ele pode envolver tanto uma bebida básica quanto
outro decorador, e o código cliente continua manipulando tudo através da interface `Beverage`,
sem precisar saber quantas camadas de decoração existem por trás. É essa transparência de tipo
que possibilita a composição recursiva de adicionais.

**d) Como a composição e a delegação permitem combinar adicionais sem criar uma subclasse para cada combinação?**

Em vez de herança (que fixa a combinação em tempo de compilação, exigindo uma subclasse por
combinação), cada decorador guarda uma referência (composição) a um `Beverage` e, ao
implementar `getDescription()`/`getCost()`, delega a chamada ao objeto envolvido e depois
soma sua própria contribuição. Como cada decorador é independente e aceita qualquer
`Beverage` no construtor, os objetos podem ser encadeados dinamicamente em qualquer ordem e
quantidade (`new ChocolateDecorator(new MilkDecorator(new Coffee()))`), montando a combinação
desejada em tempo de execução sem precisar de uma classe nova para cada arranjo.

**e) Como a inclusão de um novo adicional se relaciona com o princípio Open/Closed (OCP)?**

O sistema está **aberto para extensão**: para adicionar o caramelo bastou criar a classe
`CaramelDecorator`, reaproveitando a abstração `Beverage` e a classe base
`BeverageDecorator`. E está **fechado para modificação**: nenhuma classe existente (`Coffee`,
`Tea`, `BeverageDecorator`, `MilkDecorator`, `ChocolateDecorator`,
`WhippedCreamDecorator` ou `Main`) precisou ser alterada para que o novo adicional passasse a
funcionar e pudesse ser combinado com os demais. Isso é exatamente o que o OCP recomenda:
novas funcionalidades são adicionadas por meio de novo código, não por edição do código já
existente e testado.
