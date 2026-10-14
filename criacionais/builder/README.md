# Atividade Pratica — Padrao Builder

Implementacao de um sistema de montagem de pizzas em Java utilizando o padrao
de projeto **Builder**.

## Estrutura

- `Pizza.java`: produto final, imutavel depois de construido.
- `PizzaBuilder.java`: Builder com um metodo para cada configuracao e o metodo
  `build()`.
- `Main.java`: exemplos de pizzas Calabresa, Portuguesa, Personalizada e Doce.

## Como executar

Com o JDK instalado, execute na raiz do projeto:

```powershell
javac -d out src\*.java
java -cp out Main
```

## Questoes para reflexao

### a) Qual problema existente na criacao da pizza foi resolvido pelo Builder?

O Builder eliminou a necessidade de usar um construtor muito grande, no qual
era dificil lembrar a ordem e o significado de cada parametro booleano. Agora
cada opcao e identificada pelo nome do metodo usado para configura-la.

### b) Qual a principal vantagem do Builder em relacao a um construtor com muitos parametros?

A principal vantagem e tornar a criacao do objeto mais legivel e flexivel.
Apenas as opcoes desejadas precisam ser configuradas, e novos ingredientes
podem ser acrescentados sem aumentar ainda mais um construtor posicional.

### c) Onde esta o Builder na sua implementacao?

O Builder e a classe `PizzaBuilder`. Ela guarda temporariamente as
configuracoes escolhidas e oferece metodos encadeaveis como `size()`,
`dough()`, `cheese()` e `chocolate()`.

### d) Qual o papel do metodo `build()`?

O metodo `build()` finaliza a configuracao, valida os dados obrigatorios e cria
uma nova instancia imutavel de `Pizza` com os valores armazenados no Builder.

### e) Em quais situacoes o padrao Builder e mais indicado do que um construtor tradicional?

Ele e indicado quando um objeto possui muitos atributos, principalmente
opcionais, quando existem varias combinacoes validas de configuracao ou quando
a construcao precisa ocorrer em etapas. Para objetos pequenos com poucos dados
obrigatorios, um construtor tradicional costuma ser suficiente.
