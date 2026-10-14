# Atividade 014 - Padrão Proxy

Sistema de galeria de imagens de alta resolução, refatorado para o padrão de projeto **Proxy**,
na modalidade **Proxy Virtual**: cada imagem é representada por um objeto intermediário leve, que
cria a imagem real somente na primeira vez em que ela é realmente exibida.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── Image.java                 // abstração comum (Subject)
├── HighResolutionImage.java   // objeto real e custoso (Real Subject)
├── ImageProxy.java            // Proxy Virtual: carregamento sob demanda
├── ProtectedImageProxy.java   // Proxy de Proteção (desafio adicional)
└── Main.java                  // demonstração
```

- [`Image.java`](src/Image.java) declara a única operação do cliente: `display()`.
- [`HighResolutionImage.java`](src/HighResolutionImage.java) implementa `Image` e mantém o
  comportamento original: carrega no construtor e exibe em `display()`.
- [`ImageProxy.java`](src/ImageProxy.java) também implementa `Image`. Seu construtor **apenas
  registra o nome do arquivo**; a referência para a imagem real nasce `null` e é preenchida na
  primeira chamada de `display()`, sendo reutilizada nas chamadas seguintes.
- [`Main.java`](src/Main.java) monta a galeria como uma `List<Image>` contendo proxies, e o
  método `show(Image image)` depende somente da abstração — não há nenhum `instanceof` nem
  verificação de tipo concreto.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
Gallery created

-- Displaying the first image twice --
Loading image from disk: forest.jpg
Displaying image: forest.jpg
Displaying image: forest.jpg

-- Displaying the second image --
Loading image from disk: beach.jpg
Displaying image: beach.jpg

-- The third image was never displayed, so it was never loaded --

-- Protected proxy without permission --
Access denied

-- Protected proxy with permission --
Loading image from disk: vacation.jpg
Displaying image: vacation.jpg
Displaying image: vacation.jpg
```

A saída comprova os pontos exigidos:

- `Gallery created` aparece **sem nenhuma mensagem de carregamento antes** — montar a galeria
  com três proxies não carregou nada (no código original, as três imagens eram carregadas antes
  dessa linha);
- exibir a primeira imagem duas vezes produziu **um** carregamento e **duas** exibições;
- exibir a segunda imagem carregou apenas `beach.jpg`;
- `mountain.jpg` nunca foi carregada.

## Desafio adicional — Proxy de proteção

[`ProtectedImageProxy.java`](src/ProtectedImageProxy.java) implementa `Image` e recebe, pelo
construtor, uma `Image` e um `boolean` de permissão. Sem permissão, ele imprime `Access denied`
e **não delega** a chamada; com permissão, delega normalmente.

Como ele envolve um `ImageProxy`, a tentativa sem permissão nem chega ao proxy virtual: na saída,
o bloco "Protected proxy without permission" não tem nenhuma linha `Loading image from disk`, ou
seja, a imagem real de `private.jpg` **nunca foi criada**. Já no bloco com permissão, a cadeia
`ProtectedImageProxy → ImageProxy → HighResolutionImage` funciona por completo e o carregamento
acontece apenas uma vez, mesmo com duas exibições. Isso só é possível porque os dois proxies
compartilham a mesma abstração e, por isso, podem ser encadeados.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Proxy Virtual?**

O carregamento prematuro (*eager loading*) de recursos custosos. No código inicial, as três
imagens eram lidas do disco no momento em que seus objetos eram criados — antes mesmo da mensagem
`Gallery created` — ainda que apenas a primeira fosse exibida. Com centenas de imagens, abrir a
galeria ficaria lento e consumiria memória com dados que talvez nunca fossem usados. A alternativa
ingênua, espalhar verificações de "já carregou?" pelo código cliente, apenas transferiria o
problema: cada ponto de exibição passaria a conhecer os detalhes de criação da imagem. O Proxy
resolve concentrando essa decisão em um único lugar, invisível para o cliente.

**b) Quais classes representam o sujeito, o sujeito real e o proxy na sua implementação?**

- **Subject (sujeito):** a interface `Image`.
- **Real Subject (sujeito real):** `HighResolutionImage`.
- **Proxy:** `ImageProxy` (proxy virtual) e `ProtectedImageProxy` (proxy de proteção).
- **Cliente:** `Main`, que manipula tudo como `Image`.

**c) Por que o proxy e o objeto real devem implementar a mesma abstração?**

Porque é isso que torna o proxy **transparente** para o cliente: como `ImageProxy` também é uma
`Image`, ele pode ocupar o lugar de um `HighResolutionImage` em qualquer `List<Image>` ou
parâmetro, sem que o código cliente mude ou precise testar o tipo concreto. Se as interfaces
fossem diferentes, cada ponto de uso teria que saber qual dos dois está manipulando, o que
reintroduziria exatamente o acoplamento que o padrão quer eliminar. Essa substituibilidade
(princípio de Liskov) também é o que permite empilhar proxies, como `ProtectedImageProxy`
envolvendo um `ImageProxy`.

**d) Como o carregamento sob demanda altera o comportamento da galeria? Qual custo ainda existe na primeira exibição de uma imagem?**

A criação da galeria passa a ser praticamente instantânea e proporcional apenas ao número de
proxies (objetos leves, que guardam só um nome de arquivo), e o consumo de memória passa a ser
proporcional às imagens **efetivamente visualizadas**, não às cadastradas. O custo não desaparece:
ele é apenas **deslocado no tempo**. Na primeira chamada de `display()`, o cliente paga o
carregamento completo — ou seja, a primeira exibição é mais lenta que as seguintes, podendo gerar
uma latência perceptível justamente no momento da interação do usuário. Em um sistema real, isso
pode ser mitigado com pré-carregamento em segundo plano ou miniaturas, mas há sempre esse
*trade-off* entre custo adiantado e latência no primeiro uso. Outros pontos a considerar: o estado
`image` mutável exigiria sincronização em acesso concorrente, e cada proxy mantém sua própria
imagem real (não há cache global entre proxies distintos).

**e) Embora Proxy e Decorator possam envolver outro objeto com a mesma interface, como suas intenções se diferenciam?**

A estrutura é parecida — composição mais delegação através de uma abstração comum — mas a
**intenção** é diferente:

- O **Decorator** quer *acrescentar comportamento* ao objeto envolvido, somando
  responsabilidades a uma funcionalidade que já existe e é executada (como na
  [Atividade 011](../decorator), em que cada adicional soma preço e descrição à bebida). Ele
  sempre delega, e normalmente é empilhado dinamicamente pelo cliente, que escolhe a composição.
- O **Proxy** quer *controlar o acesso* ao objeto: decide **se**, **quando** e **em que
  condições** a chamada chega ao objeto real. Ele pode nem delegar (como o
  `ProtectedImageProxy` sem permissão) e frequentemente **gerencia o ciclo de vida** do objeto
  real, criando-o por conta própria — algo que um decorador não faz, já que recebe pronto o
  objeto que decora.

Em resumo: o Decorator muda *o que* o objeto faz; o Proxy muda *como e se* o objeto é alcançado.
Daí as variações de Proxy serem sobre acesso — virtual (criação tardia), de proteção
(autorização), remoto (localização) e *smart reference* (contagem de referências, cache).
