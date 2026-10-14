# Atividade 012 - Padrão Facade

Sistema de controle de um home theater, refatorado para o padrão de projeto **Facade**, que
oferece uma interface única e simplificada para um conjunto de subsistemas, encapsulando a
ordem correta das operações necessárias para iniciar e encerrar uma sessão de cinema.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── Projector.java          // subsistema: projetor
├── SoundSystem.java        // subsistema: som
├── StreamingPlayer.java    // subsistema: player de streaming
├── RoomLights.java         // subsistema: iluminação da sala
├── ProjectionScreen.java   // subsistema: tela motorizada (desafio adicional)
├── HomeTheaterFacade.java  // a fachada (Facade)
└── Main.java               // cliente: usa apenas a fachada
```

- [`HomeTheaterFacade.java`](src/HomeTheaterFacade.java) recebe todos os equipamentos pelo
  construtor e expõe apenas duas operações: `watchMovie(String movie)` e `endMovie()`.
- Cada equipamento continua responsável pela sua própria operação. A fachada **apenas
  coordena** a sequência de chamadas — ela não reproduz internamente o comportamento de
  nenhum equipamento, nem precisa ser conhecida por eles.
- [`Main.java`](src/Main.java) cria os equipamentos e a fachada diretamente, mas o fluxo de
  iniciar e encerrar a sessão passa exclusivamente pela fachada.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
Lights dimmed
Projection screen lowered
Projector on
Sound system on
Volume: 20
Playing: Design Patterns: The Movie

Playback stopped
Sound system off
Projector off
Projection screen raised
Lights on
```

As mensagens confirmam que todas as etapas foram executadas na ordem prevista.

## Desafio adicional — tela de projeção motorizada

Foi criada a classe [`ProjectionScreen.java`](src/ProjectionScreen.java), com as operações
`lower()` e `raise()`, e integrada à fachada:

- em `watchMovie(String movie)`, a tela é **abaixada antes de ligar o projetor**;
- em `endMovie()`, a tela é **recolhida depois de desligar o projetor**.

As assinaturas `watchMovie(String movie)` e `endMovie()` foram preservadas, então o código que
assiste a um filme continua idêntico — apenas a **montagem** da fachada mudou, pois o
construtor passou a receber um parâmetro a mais. Isso mostra que o Facade absorve mudanças na
coordenação dos subsistemas sem alterar as operações usadas pelo cliente.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Facade?**

No código inicial, o cliente precisava conhecer quatro classes de equipamento e, principalmente,
a **ordem correta** das operações para iniciar e encerrar uma sessão. Esse conhecimento ficava
duplicado em todo ponto do sistema que quisesse assistir a um filme, e qualquer mudança na
preparação da sala (como a inclusão da tela motorizada) exigiria alterar vários lugares, com
risco de esquecer uma etapa ou executá-la fora de ordem. O Facade resolve isso ao concentrar
essa sequência em um único lugar: o cliente passa a dizer *o que* quer (`watchMovie`), não
*como* fazer.

**b) Quais classes representam a fachada e os subsistemas na sua implementação?**

- **Facade:** `HomeTheaterFacade`.
- **Subsistemas:** `Projector`, `SoundSystem`, `StreamingPlayer`, `RoomLights` e
  `ProjectionScreen`.
- **Cliente:** `Main`, que depende apenas da fachada para executar a sessão.

**c) Como a fachada reduz o acoplamento do código cliente com os equipamentos?**

Antes, o cliente dependia de cinco tipos concretos e de um protocolo implícito (a ordem das
chamadas). Agora ele depende de um único tipo (`HomeTheaterFacade`) e de duas operações. Isso
reduz o acoplamento em duas frentes: o cliente não precisa mais conhecer a existência dos
equipamentos nem suas interfaces, e deixa de depender da ordem das operações. Como consequência,
incluir, remover ou trocar um equipamento afeta a fachada, e não cada ponto do sistema que
inicia uma sessão.

**d) Qual é a diferença entre coordenar operações na fachada e transferir toda a lógica dos equipamentos para ela?**

Coordenar significa que a fachada apenas **delega**, na ordem certa, para objetos que continuam
donos do seu próprio comportamento — foi o que foi feito aqui: `HomeTheaterFacade` não imprime
"Projector on", ela chama `projector.on()`. Transferir a lógica para a fachada a transformaria em
uma classe que faz tudo (um *god object*): os equipamentos ficariam anêmicos, a fachada cresceria
de forma desproporcional, perderia-se o encapsulamento de cada equipamento e qualquer alteração em
um deles passaria a exigir mudanças na fachada. A intenção do Facade é **simplificar o acesso** a
um subsistema, não substituí-lo.

**e) O padrão Facade impede o acesso direto aos subsistemas? Em quais situações esse acesso ainda pode ser necessário?**

Não impede. A fachada é uma via de acesso adicional e conveniente, não um bloqueio: as classes dos
equipamentos continuam públicas e podem ser usadas diretamente (em `Main`, inclusive, elas são
instanciadas). Esse acesso direto permanece necessário quando se precisa de uma operação fora dos
fluxos oferecidos pela fachada (por exemplo, ajustar apenas o volume durante o filme, ou testar um
equipamento isoladamente), em cenários de manutenção/diagnóstico, ou em testes unitários de um
equipamento específico. Quando o objetivo é realmente **restringir** o acesso, o padrão adequado
seria outro (como o Proxy de proteção) — o Facade trata de conveniência e redução de acoplamento.
