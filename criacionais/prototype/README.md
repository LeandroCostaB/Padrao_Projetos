# Padrão Prototype — Sistema de Personagens

Este projeto demonstra a refatoração da criação de personagens de um jogo com o
padrão de projeto **Prototype**. Em vez de repetir todos os parâmetros no
construtor, o jogo mantém modelos previamente configurados e cria novos
personagens clonando esses modelos.

## Estrutura da solução

- `Prototype<T>`: contrato genérico para objetos que podem ser clonados;
- `Character`: representa um personagem e implementa a clonagem;
- `CharacterPrototypes`: catálogo dos protótipos Warrior, Mage e Archer;
- `Main`: demonstra a criação e a personalização dos clones.

A lista de habilidades é mutável. Por isso, o construtor de cópia de
`Character` cria uma nova `ArrayList` para cada clone. Essa cópia profunda
impede que a alteração das habilidades de um clone modifique o protótipo ou
outro clone.

## Como executar

Requer Java 17 ou superior e Maven 3.8 ou superior.

```bash
mvn clean verify
mvn exec:java
```

A execução cria personagens a partir dos três protótipos, altera atributos dos
clones e exibe os clones e os modelos originais. O método também contém
verificações que lançam uma exceção caso um clone compartilhe indevidamente a
lista de habilidades com o protótipo.

## Questões para reflexão

### a) Qual problema existente na criação dos personagens foi resolvido com o Prototype?

O Prototype eliminou a repetição da configuração completa de personagens
semelhantes. Antes, cada personagem exigia que todos os seus atributos fossem
informados novamente no construtor, o que aumentava a duplicação e o risco de
valores inconsistentes. Agora, as características padrão ficam centralizadas
em protótipos, e cada novo personagem altera somente o que for necessário.

### b) Qual é a principal vantagem de criar objetos a partir de protótipos?

A principal vantagem é reutilizar uma configuração já pronta sem acoplar o
código cliente aos detalhes de construção do objeto. Isso torna a criação mais
rápida, uniforme e fácil de manter: uma mudança no modelo padrão é feita em um
único lugar e passa a valer para os clones criados posteriormente.

### c) Onde está o Prototype na sua implementação?

O contrato do padrão está na interface `Prototype<T>`. A classe `Character` é
o protótipo concreto porque implementa `Prototype<Character>` e seu método
`clone()` devolve uma nova instância por meio do construtor de cópia. A classe
`CharacterPrototypes` mantém as instâncias-modelo de Warrior, Mage e Archer, e
o `Main` atua como cliente ao solicitar clones desses modelos.

### d) Qual a diferença entre cópia rasa e cópia profunda?

Na cópia rasa, os campos simples são copiados, mas objetos internos mutáveis
continuam compartilhados entre original e clone. Assim, se a mesma lista de
habilidades fosse reutilizada, adicionar uma habilidade ao clone também
alteraria o protótipo. Na cópia profunda, os objetos internos mutáveis também
são copiados. Nesta implementação, `new ArrayList<>(prototype.skills)` cria
uma lista independente para cada clone. Como as habilidades são `String`, que
é imutável, não é necessário copiar cada elemento da lista.

### e) Em quais situações o padrão Prototype é mais indicado do que criar objetos diretamente com `new`?

O Prototype é especialmente indicado quando:

- a criação exige muitos parâmetros ou uma configuração custosa;
- várias instâncias compartilham a maior parte dos valores;
- os modelos possíveis são definidos ou alterados em tempo de execução;
- o cliente não deve conhecer a classe concreta ou os detalhes de construção;
- é útil manter um catálogo de objetos preconfigurados para gerar variações.

Para objetos simples, baratos e sem configurações compartilhadas, a criação
direta com `new` normalmente continua sendo a opção mais clara.
