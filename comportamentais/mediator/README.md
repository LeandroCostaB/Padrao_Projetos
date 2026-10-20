# Atividade 019 - Padrão Mediator

Sistema de sala de chat, refatorado do modelo original em que cada participante mantinha sua
própria lista de contatos para o padrão de projeto **Mediator**, que concentra as regras de
comunicação em um objeto central e elimina as dependências diretas entre os participantes.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── ChatMediator.java  // abstração do mediador (Mediator)
├── ChatRoom.java      // mediador concreto: registro e encaminhamento
├── ChatUser.java      // participante (Colleague)
└── Main.java          // demonstração dos fluxos e das recusas
```

- [`ChatUser.java`](src/ChatUser.java) recebe o nome e o mediador pelo construtor, ambos
  `final`. **Não existe mais a lista de contatos nem o método `addContact`**: `send` apenas
  delega ao mediador, passando `this` como remetente, e `receive` só exibe a mensagem — nunca
  provoca um novo envio, o que descarta qualquer risco de ciclo.
- [`ChatRoom.java`](src/ChatRoom.java) mantém a coleção de participantes e concentra **todas** as
  regras: quem pode registrar, quem pode enviar, para quem a mensagem vai e quais tentativas são
  recusadas.
- O usuário conhece apenas a abstração `ChatMediator`, e o mediador conhece os participantes. Por
  isso **nenhum usuário tem referência para outro usuário**.
- Criar um usuário não o registra: o construtor apenas guarda a associação, e o registro acontece
  explicitamente no código cliente. É o que permite demonstrar a recusa de envio de alguém que
  conhece a sala mas nunca entrou nela.

### Regras centralizadas no mediador

| Operação | Validações feitas em `ChatRoom` |
| --- | --- |
| `register` | recusa `null`, a mesma instância já registrada e um nome já em uso (preservando o cadastro original) |
| `send` | recusa remetente não registrado; encaminha uma vez a cada participante, exceto ao próprio remetente (comparação por instância, `!=`) |
| `sendPrivate` | recusa remetente não registrado, destinatário inexistente e destinatário igual ao remetente; busca o destinatário por nome exato |

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
Ana joined the room
Bruno joined the room
Carla joined the room

-- Registering the same Bruno instance again --
Registration refused: Bruno is already registered

-- Ana sends a message to the room --
Bruno received from Ana: Hello, everyone!
Carla received from Ana: Hello, everyone!

-- Diego joins and Bruno sends a message --
Diego joined the room
Ana received from Bruno: Hello, Ana!
Carla received from Bruno: Hello, Ana!
Diego received from Bruno: Hello, Ana!

-- Eduardo knows the room but was never registered --
Message refused: Eduardo is not registered in the room

-- Another user tries to take the name Ana --
Registration refused: the name Ana is already taken
Message refused: Ana is not registered in the room

-- Private message from Ana to Carla --
Carla received from Ana: Hi Carla, just between us

-- Private message to a name that does not exist --
Private message refused: there is no user named Diana in the room

-- Private message to Ana herself --
Private message refused: Ana cannot send a private message to themselves

-- Names are compared exactly, so lower case does not match --
Private message refused: there is no user named carla in the room
```

Pontos a observar na saída:

- a mensagem de Ana chega **somente** a Bruno e a Carla, uma vez cada — Ana não recebe a própria
  mensagem;
- após o registro de Diego, a mensagem de Bruno alcança os três outros participantes, **sem que
  nenhuma lista de contatos precisasse ser atualizada**;
- no bloco do nome duplicado, a segunda recusa (`Message refused: Ana ...`) refere-se à **segunda
  instância** chamada Ana, que não foi registrada — a Ana original continua na sala e segue
  enviando e recebendo normalmente, como as mensagens privadas seguintes comprovam;
- a última tentativa usa `"carla"` em minúsculas e é recusada, confirmando a comparação exata de
  nomes exigida pelo enunciado.

## Desafio adicional — mensagens privadas

Foram acrescentados `sendPrivate(String message, ChatUser sender, String recipientName)` ao
contrato do mediador e `sendPrivate(String recipientName, String message)` ao usuário, que apenas
delega. A busca do destinatário e as três validações ficam inteiramente em `ChatRoom`: o usuário
informa **um nome**, nunca uma referência, e continua sem conhecer os demais participantes.

Para viabilizar a busca por nome, o registro passou a recusar nomes repetidos, preservando o
cadastro original. Note a assimetria que o desafio revela: acrescentar um **participante** não
exigiu alterar nada, mas acrescentar uma **operação de comunicação** obrigou a mexer no contrato
`ChatMediator`, na implementação `ChatRoom` e em `ChatUser` (o ponto de delegação) — exatamente o
mesmo tipo de custo observado ao acrescentar uma operação no [State](../state).

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Mediator?**

A topologia de dependências **todos-com-todos**. Cada `ChatUser` mantinha uma lista com todos os
outros, de modo que N participantes exigiam N×(N−1) associações — para três usuários, as seis
chamadas a `addContact` que o `Main` original precisava fazer manualmente. Os problemas
decorrentes: (1) a entrada de uma pessoa obrigava a atualizar a lista de **todos** os demais, e
uma configuração incompleta criava silenciosamente participantes que recebiam mensagens mas não
eram vistos por outros; (2) a regra de encaminhamento ficava **replicada** dentro de cada usuário,
então mudá-la significava revisar a classe `ChatUser` e seu comportamento; e (3) cada usuário
acumulava duas responsabilidades — ser um participante e ser um roteador de mensagens. O Mediator
troca essa malha por uma estrela: cada participante tem **uma** dependência (o mediador), e o
registro acontece em um único lugar.

**b) Quais abstrações e classes representam o mediador, o mediador concreto e os colegas participantes na sua implementação?**

- **Mediator (abstração):** a interface `ChatMediator`.
- **Concrete Mediator:** `ChatRoom`, que mantém os participantes e as regras de encaminhamento.
- **Colleagues (participantes):** as instâncias de `ChatUser` (Ana, Bruno, Carla, Diego…), que
  conhecem apenas o mediador.
- **Cliente:** `Main`, que cria a sala, cria os usuários e faz os registros.

**c) Como a entrada de um novo participante muda em relação ao código inicial e quais dependências deixam de existir entre os usuários?**

No código inicial, a entrada de Diego exigiria sete chamadas: `addContact` em Diego para cada um
dos três participantes já existentes, e `addContact` em cada um deles apontando para Diego — com
o detalhe de que esquecer uma única linha produziria uma comunicação assimétrica difícil de
perceber. Na versão refatorada, a entrada é **uma linha**:

```java
room.register(diego);
```

E nada mais no sistema muda. As dependências que deixam de existir são todas as referências
**entre usuários**: `ChatUser` não tem mais o campo `List<ChatUser> contacts` nem o método
`addContact`, e passou a depender apenas da abstração `ChatMediator`. O custo de adicionar um
participante cai de O(N) alterações para uma só, e torna-se impossível configurar a sala pela
metade, já que existe uma única fonte de verdade sobre quem está nela.

**d) Por que as regras de encaminhamento e de mensagens privadas devem ficar no mediador? Qual é o risco de concentrar responsabilidades demais nesse objeto?**

Devem ficar no mediador porque são regras **sobre a interação**, não sobre um participante
individual. Decidir que o remetente não recebe a própria mensagem, que um remetente não registrado
é recusado ou que um destinatário privado precisa existir e ser diferente do remetente exige
conhecer **o conjunto** da sala — informação que nenhum usuário tem nem deveria ter. Se essas
regras ficassem nos usuários, cada um precisaria da lista de participantes de volta, e o
acoplamento que o padrão eliminou retornaria. Centralizando, a regra existe em um único lugar: há
um só ponto para ler, testar e alterar o comportamento da sala.

O risco é o mediador se transformar em um ***god object***. Ele é, por natureza, o lugar onde toda
nova regra de interação quer ser colocada — e, à medida que o sistema cresce (histórico, moderação,
bloqueio entre usuários, salas múltiplas, presença, formatação), ele acumula responsabilidades
heterogêneas, fica difícil de testar e passa a ser modificado por motivos diversos, violando o
princípio da responsabilidade única. O problema não desapareceu: foi **movido** da malha de
usuários para um ponto único, que é mais fácil de gerenciar, mas também um gargalo de manutenção
(e, em um sistema real, de desempenho e disponibilidade). O sintoma de alerta é o mediador começar
a crescer sem parar; a mitigação é extraí-lo em colaboradores menores — um registro de
participantes, uma política de entrega, um log — mantendo o mediador como coordenador, não como
implementador de tudo.

**e) Embora uma sala de chat e um sujeito observado possam distribuir mensagens a vários objetos, como Mediator e Observer diferem quanto à intenção e à organização das interações?**

Os dois padrões reduzem acoplamento e envolvem um objeto que alcança vários outros, mas resolvem
problemas distintos:

- A **intenção** do [Observer](../observer) é **difundir uma mudança de estado**: o sujeito avisa
  "minha temperatura mudou" e não tem ideia de quem está ouvindo nem do que cada um fará com o
  aviso. O fluxo é **unidirecional** (sujeito → observadores) e o sujeito não possui regra alguma
  sobre os destinatários: todos os inscritos recebem a mesma notificação. A do **Mediator** é
  **coordenar uma interação** entre objetos que precisam se comunicar entre si: a sala decide
  quem recebe o quê, e o fluxo é **bidirecional** (o participante envia *e* recebe através do
  mediador).
- Quanto à **organização**, no Observer o sujeito é um participante que por acaso tem uma lista de
  interessados, e as regras de interpretação ficam em cada observador (o limite de 35 °C está no
  alerta). No Mediator, o mediador **não é** um dos participantes: ele existe apenas para mediar,
  e as regras de interação ficam nele (quem recebe, quem é recusado), enquanto os participantes
  ficam propositalmente simples.
- Há também uma diferença de **conhecimento**: observadores não sabem da existência uns dos
  outros nem se comunicam entre si, e nem precisariam — eles não interagem. Na sala de chat, os
  participantes precisam se comunicar, e o mediador é justamente o que torna isso possível sem
  que se conheçam.

Em uma frase: o Observer notifica interessados anônimos sobre um evento; o Mediator intermedeia
uma conversa entre participantes identificados. Na prática os dois costumam aparecer juntos — um
mediador pode usar notificações para avisar os participantes, e é comum implementar Mediator com
um mecanismo de eventos.
