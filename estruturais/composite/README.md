# Atividade 010 - Padrão Composite

Refatoração de um sistema de arquivos e pastas utilizando o padrão de projeto **Composite**, permitindo que arquivos e pastas sejam tratados de forma uniforme através de uma abstração comum.

## Estrutura

```
src/
├── FileSystemComponent.java  // abstração comum (Component)
├── DocumentFile.java         // arquivo individual (Leaf)
├── Folder.java                // pasta que agrupa componentes (Composite)
└── Main.java                  // demonstração da hierarquia
```

## Como executar

```bash
javac -d out src/*.java
java -cp out Main
```

## Resumo da solução

- `FileSystemComponent` define o contrato comum: `getName()`, `getSize()` e `display(String indent)`.
- `DocumentFile` implementa essa interface como uma folha (leaf), sem filhos.
- `Folder` implementa a mesma interface como um composto (composite): guarda uma lista de `FileSystemComponent` (podendo ser arquivos ou outras pastas), permite `add`/`remove`, e calcula `getSize()` somando recursivamente o tamanho de todos os seus elementos.
- `Main` demonstra um arquivo isolado, uma pasta vazia e uma hierarquia com pelo menos dois níveis de subpastas (`documents` → `finance` → `invoices`), consultando tudo por meio da abstração `FileSystemComponent`, sem nenhum `instanceof`.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Composite?**

No código original, `Folder` só conseguia armazenar `DocumentFile`, então não era possível ter pastas dentro de pastas. Além disso, arquivos e pastas eram tratados por operações diferentes (`file.getSize()` vs. `folder.getTotalSize()`), então qualquer código que precisasse lidar com os dois tipos teria que verificar o tipo concreto e aplicar lógica separada para cada um. O Composite resolve isso ao fazer arquivos e pastas implementarem a mesma abstração (`FileSystemComponent`), permitindo hierarquias com múltiplos níveis e uma única operação (`getSize()`) que funciona igualmente para qualquer elemento da árvore.

**b) Quais elementos representam o componente, a folha e o composto na sua implementação?**

- **Componente (Component):** a interface `FileSystemComponent`, que declara as operações comuns (`getName()`, `getSize()`, `display()`).
- **Folha (Leaf):** a classe `DocumentFile`, que representa um arquivo individual e não possui filhos.
- **Composto (Composite):** a classe `Folder`, que mantém uma coleção de `FileSystemComponent` (arquivos e/ou outras pastas) e implementa as operações delegando/agregando o resultado dos seus elementos filhos.

**c) Como o polimorfismo permite tratar arquivos e pastas de maneira uniforme?**

Como `DocumentFile` e `Folder` implementam a mesma interface `FileSystemComponent`, qualquer código cliente (como `Main` ou a própria `Folder`) pode manipular uma referência do tipo `FileSystemComponent` sem saber se, por trás, existe um arquivo ou uma pasta. Ao chamar `getSize()` ou `display()`, o Java despacha a chamada para a implementação concreta correta em tempo de execução (dispatch dinâmico), então não é necessário nenhum `if`/`instanceof` para diferenciar os tipos — o próprio objeto sabe como se comportar.

**d) Qual é o papel da recursão no cálculo do tamanho das pastas?**

O método `getSize()` de `Folder` percorre sua lista de filhos e soma o retorno de `child.getSize()` para cada um. Quando um filho é outra `Folder`, essa chamada aciona novamente o mesmo método `getSize()`, que por sua vez soma os tamanhos dos seus próprios filhos, e assim por diante até chegar a uma folha (`DocumentFile`), que apenas retorna seu tamanho armazenado. É essa recursão que permite calcular o tamanho total de uma árvore com qualquer profundidade de subpastas sem precisar conhecer a estrutura completa de antemão. Uma pasta sem filhos simplesmente não soma nada e retorna 0.

**e) Quais são as vantagens e limitações de manter as operações de adicionar e remover filhos apenas na classe `Folder`?**

*Vantagens:*
- Mantém a interface `FileSystemComponent` enxuta, expondo apenas o que é comum e relevante para o cliente (`getName()`, `getSize()`, `display()`).
- Evita que `DocumentFile` precise implementar métodos sem sentido para uma folha (como `add`/`remove`), o que impediria erros em tempo de compilação (ex.: tentar adicionar um filho a um arquivo).

*Limitações:*
- O cliente que possui apenas uma referência do tipo `FileSystemComponent` não consegue chamar `add`/`remove` diretamente; é preciso saber que o objeto é especificamente uma `Folder` (fazendo um downcast) para gerenciar seus filhos, o que quebra um pouco a transparência total do padrão Composite (a variante "segura", em contraposição à variante "transparente", que colocaria `add`/`remove` na própria interface `Component`, lançando exceção nas folhas).
- Essa é uma troca consciente entre **segurança de tipos** (o que foi escolhido aqui) e **transparência de interface** (tratar tudo de forma 100% uniforme, inclusive a montagem da árvore).

---

Feito com o padrão Composite (GoF).
