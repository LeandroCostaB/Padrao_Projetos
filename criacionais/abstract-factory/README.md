## Abstract Factory — Sistema de Cafeteria

O projeto utiliza o padrão **Abstract Factory** para criar diferentes famílias de combos de uma cafeteria.

Cada combo é formado por um café e uma sobremesa compatíveis entre si.

As famílias implementadas são:

* **Tradicional:** Café Tradicional + Cheesecake
* **Fitness:** Café sem açúcar + Barra de proteína
* **Premium:** Café Especial + Torta Gourmet
* **Vegana:** Café Vegano + Sobremesa Vegana

### Estrutura do padrão

As abstrações dos produtos são:

```java
public interface Coffee {
    void prepare();
}
```

```java
public interface Dessert {
    void prepare();
}
```

A fábrica abstrata é representada pela interface `ComboFactory`:

```java
public interface ComboFactory {

    Coffee createCoffee();

    Dessert createDessert();
}
```

As fábricas concretas são:

```text
TraditionalComboFactory
FitnessComboFactory
PremiumComboFactory
VeganComboFactory
```

Cada fábrica é responsável por criar os produtos correspondentes à sua própria família.

### OrderService

A classe `OrderService` trabalha somente com abstrações e recebe uma `ComboFactory` pelo construtor:

```java
public class OrderService {

    private final ComboFactory factory;

    public OrderService(ComboFactory factory) {
        this.factory = factory;
    }

    public void createCombo() {

        Coffee coffee = factory.createCoffee();
        Dessert dessert = factory.createDessert();

        coffee.prepare();
        dessert.prepare();
    }
}
```

Dessa forma, o `OrderService` não precisa conhecer diretamente as classes concretas de cafés e sobremesas.

## Desafio adicional — Combo Vegano

Para adicionar a nova família Vegana foram criadas apenas as classes:

```text
VeganCoffee
VeganDessert
VeganComboFactory
```

Não foi necessário alterar o `OrderService` nem as abstrações existentes, demonstrando como o Abstract Factory facilita a inclusão de novas famílias de produtos.

## Questões para reflexão

### a) Qual problema foi resolvido?

A criação dos objetos deixou de ficar acoplada às classes concretas. O `OrderService` não precisa utilizar condicionais nem conhecer cada tipo específico de café ou sobremesa.

### b) Factory Method x Abstract Factory

O **Factory Method** normalmente é utilizado para criar um produto, permitindo que subclasses ou implementações decidam qual objeto será criado.

O **Abstract Factory** fornece uma interface para criar uma família completa de produtos relacionados.

Neste exercício, cada fábrica cria um `Coffee` e um `Dessert` da mesma linha.

### c) Onde está a fábrica abstrata?

A fábrica abstrata está na interface:

```java
ComboFactory
```

Ela declara os métodos:

```java
createCoffee()
createDessert()
```

### d) O que é uma família de objetos?

É um conjunto de produtos relacionados pertencentes à mesma linha.

Por exemplo:

```text
TraditionalCoffee + TraditionalDessert
```

formam a família Tradicional.

Da mesma forma:

```text
FitnessCoffee + FitnessDessert
PremiumCoffee + PremiumDessert
VeganCoffee + VeganDessert
```

formam suas respectivas famílias.

### e) Como a consistência é mantida?

Cada fábrica concreta cria apenas os produtos pertencentes à sua própria linha.

Como o `OrderService` recebe uma única fábrica, o café e a sobremesa sempre são criados pela mesma família, evitando combinações incompatíveis.
