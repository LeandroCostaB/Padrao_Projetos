# Atividade 013 - Padrão Flyweight

Sistema de renderização de florestas, refatorado para o padrão de projeto **Flyweight**, que
reduz o consumo de memória ao compartilhar entre muitos objetos semelhantes os dados que são
idênticos (estado intrínseco), mantendo em cada objeto apenas o que varia (estado extrínseco).

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── TreeType.java         // Flyweight: estado compartilhado e imutável (nome, cor, textura)
├── Tree.java             // contexto: posição e escala + referência ao tipo
├── TreeTypeFactory.java  // Flyweight Factory: cache de tipos
└── Main.java             // demonstração com 200 árvores e 2 tipos
```

- [`TreeType.java`](src/TreeType.java) guarda nome, cor e textura em campos `private final`,
  não possui setters e não devolve o array interno (apenas `getTextureSize()`). O construtor faz
  uma **cópia defensiva** (`texture.clone()`) do array recebido, de modo que alterações feitas
  depois no array de origem não afetam o tipo compartilhado.
- [`Tree.java`](src/Tree.java) armazena apenas `x`, `y`, `scale` e a referência ao `TreeType`.
  Seu `draw()` delega a renderização ao tipo, passando o estado extrínseco.
- [`TreeTypeFactory.java`](src/TreeTypeFactory.java) mantém um `HashMap` de tipos. A chave é o
  record `TreeTypeKey(name, color, textureId)`: por ser um record, `equals`/`hashCode` comparam
  **por valor** campo a campo, sem a ambiguidade que uma concatenação de strings teria (por
  exemplo, `"Oak" + "Green:x"` e `"Oak:Green" + "x"` colidiriam em uma chave concatenada, mas são
  chaves distintas aqui). O carregamento da textura — simulado por um array de 100.000 bytes — é
  feito **somente quando um tipo novo precisa ser criado**.

## Como compilar e executar

Requer JDK 16 ou superior (o cache usa um `record` como chave).

```bash
javac -d out src/*.java
java -cp out Main
```

A saída traz 200 linhas de renderização; o trecho relevante da demonstração é:

```
Loading texture: oak.png (100000 bytes)
Loading texture: pine.png (100000 bytes)
Drawing Oak (Green) at (0, 0), scale: 1.0, texture: 100000 bytes
Drawing Pine (Dark green) at (0, 0), scale: 0.5, texture: 100000 bytes
...
Trees created: 200
Tree types created: 2

Same combination reuses the type: true
Different combinations are distinct types: true
Tree types after requesting Oak again: 2

Loading texture: oak.png (100000 bytes)
A new color creates a new type: true
Tree types after adding the orange Oak: 3

Same type, different positions and scales:
Drawing Oak (Green) at (10, 20), scale: 0.5, texture: 100000 bytes
Drawing Oak (Green) at (30, 40), scale: 2.5, texture: 100000 bytes
```

Observações sobre a saída:

- as mensagens `Loading texture` aparecem **duas vezes** no início, embora o laço peça o tipo
  `Oak` e o tipo `Pine` 100 vezes cada — prova de que a fábrica reutiliza as instâncias;
- 200 árvores existem com apenas 2 tipos compartilhados (contra 200 cópias de 100.000 bytes no
  código original, ou seja, ~20 MB reduzidos a ~200 KB de textura);
- `firstOak == anotherOak` retorna `true`: solicitações equivalentes devolvem **a mesma
  instância**;
- pedir `Oak`/`Orange` cria um terceiro tipo (e só aí uma nova textura é carregada), mostrando
  que combinações diferentes produzem tipos distintos.

## Desafio adicional — escala por árvore

A escala foi adicionada ao estado de [`Tree`](src/Tree.java) e é repassada para
`TreeType.draw(int x, int y, double scale)`. A escala **não entra na chave do cache**, pois é
estado extrínseco: as duas últimas linhas da saída mostram duas árvores com posições e escalas
diferentes (`0.5` e `2.5`) compartilhando o mesmo `TreeType` já existente — nenhuma textura nova
foi carregada. `TreeType` mantém também a sobrecarga `draw(int x, int y)`, que usa escala `1.0`.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Flyweight?**

O construtor original de `Tree` fazia `texture.clone()`, ou seja, **cada árvore guardava sua
própria cópia** de 100.000 bytes, além de duplicar nome e cor. Com 200 árvores isso significa
cerca de 20 MB de dados idênticos; em um cenário completo, com milhares de árvores, o consumo
cresceria linearmente até ficar inviável. Compartilhar o objeto `Tree` inteiro não era solução,
porque cada árvore precisa da sua própria posição. O Flyweight resolve separando o que é comum
(nome, cor, textura) do que é individual (posição, escala): a textura passa a existir uma única
vez por tipo.

**b) Quais dados representam o estado intrínseco e o estado extrínseco na sua implementação?**

- **Estado intrínseco** (compartilhado, imutável, vive em `TreeType`): `name`, `color` e
  `texture`.
- **Estado extrínseco** (único por objeto, vive em `Tree` e é passado na renderização): `x`, `y`
  e `scale`.

A referência de `Tree` para o seu `TreeType` é o elo entre os dois: muitos contextos apontando
para um mesmo flyweight.

**c) Qual é o papel da fábrica e da chave do cache no compartilhamento dos objetos?**

A fábrica é o **único ponto de criação** dos flyweights, e é isso que garante o
compartilhamento: se o cliente pudesse chamar `new TreeType(...)` livremente, nada impediria a
duplicação. `TreeTypeFactory.getTreeType(...)` consulta o cache e só constrói (e carrega a
textura) quando não existe um tipo equivalente. A chave define **o que significa "equivalente"**:
`TreeTypeKey(name, color, textureId)` compara por valor os três atributos intrínsecos, de modo
que requisições iguais convergem para a mesma instância e requisições diferentes produzem tipos
distintos. Usar um record (em vez de concatenar strings) evita colisões causadas por separadores
ambíguos e dá `equals`/`hashCode` corretos de graça.

**d) Por que o estado compartilhado deve ser imutável? O que poderia acontecer se a textura de um tipo fosse alterada?**

Porque um flyweight é compartilhado por um número indeterminado de clientes que não sabem uns dos
outros. Se a textura (ou a cor) de um `TreeType` pudesse ser alterada, a mudança feita pensando em
**uma** árvore se refletiria instantaneamente em **todas** as 100 árvores daquele tipo — um
efeito colateral à distância, difícil de rastrear. Pior: em um cenário multithread, um objeto
mutável compartilhado exigiria sincronização e poderia ser lido em estado inconsistente. A
imutabilidade (campos `private final`, ausência de setters, nenhuma exposição do array interno e
cópia defensiva na criação) garante que o flyweight seja seguro para compartilhar e para usar
concorrentemente.

**e) Em quais situações o Flyweight pode reduzir significativamente o consumo de memória e quais custos de complexidade ele acrescenta?**

O ganho é significativo quando existem **muitos objetos**, com **poucos valores distintos** de
estado intrínseco, e esse estado intrínseco é **grande** em relação ao extrínseco — exatamente o
caso aqui (200 árvores, 2 tipos, textura de 100 KB contra alguns bytes de coordenadas). Outros
exemplos clássicos: glifos/caracteres em um editor de texto, partículas e sprites em jogos,
ícones em uma interface, ou o cache de `Integer` da própria JVM.

Os custos são: (1) uma indireção extra, já que o cliente deixa de criar objetos diretamente e
passa a depender da fábrica; (2) a necessidade de manter o estado compartilhado imutável e de
distinguir conscientemente os dois tipos de estado, o que torna o código menos óbvio; (3) o
estado extrínseco precisa ser passado em cada operação, deixando as assinaturas mais longas;
(4) o cache ocupa memória e, em sistemas de vida longa, pode exigir uma política de descarte;
e (5) se os objetos forem poucos ou se quase todo o estado for extrínseco, o padrão só agrega
complexidade sem benefício real.
