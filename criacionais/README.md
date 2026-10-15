# Padrões criacionais

Padrões que tratam do **processo de criação de objetos**: eles separam a decisão de *qual* objeto
construir (e *como* construí-lo) do código que vai usá-lo, de modo que o cliente dependa de
abstrações em vez de chamar `new` sobre classes concretas.

| Pasta | Padrão | Atividade | Intenção | Cenário |
| --- | --- | --- | --- | --- |
| [`abstract-factory`](abstract-factory) | Abstract Factory | — | Criar **famílias** de produtos relacionados sem especificar suas classes concretas | Combos de cafeteria: cada fábrica produz um café e uma sobremesa da mesma linha |
| [`builder`](builder) | Builder | — | Construir um objeto complexo **passo a passo**, separando a construção da representação | Montagem de pizzas, substituindo um construtor com muitos parâmetros |
| [`prototype`](prototype) | Prototype | 006 | Criar novos objetos **clonando** um modelo já configurado | Personagens de jogo (Warrior, Mage, Archer) criados a partir de protótipos |
| [`singleton`](singleton) | Singleton | 007 | Garantir **uma única instância** de uma classe e um ponto global de acesso | `AppConfig` compartilhada por toda a aplicação |

## Como se diferenciam

- **Abstract Factory** e **Builder** resolvem problemas opostos: a Abstract Factory escolhe entre
  *variantes* de um conjunto de produtos (a família é fixa, a linha varia), enquanto o Builder
  lida com *muitas combinações* de um mesmo produto (o produto é um só, a configuração varia).
- **Prototype** troca construção por **cópia**: é útil quando configurar um objeto do zero é caro
  ou repetitivo, e exige atenção à profundidade da clonagem (o projeto demonstra cópia profunda da
  lista de habilidades).
- **Singleton** não é sobre *como* criar, mas sobre **quantas vezes**: ele controla a
  multiplicidade da instância.

Cada pasta contém o código-fonte, as instruções de execução e as respostas às questões para
reflexão da respectiva atividade. As outras categorias estão em
[`../estruturais`](../estruturais) e [`../comportamentais`](../comportamentais).
