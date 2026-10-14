# Padrões de Projeto — Atividades

Repositório único com as atividades práticas de Padrões de Projeto (GoF) implementadas em Java,
organizadas por **categoria do padrão**: padrões **criacionais** (como objetos são criados) e
padrões **estruturais** (como objetos e classes são compostos).

Cada pasta de padrão é um projeto independente, com o seu próprio `README.md` contendo a
descrição da solução, as instruções de compilação/execução e as **respostas às questões para
reflexão** daquela atividade.

## Padrões criacionais

Tratam do processo de criação de objetos, desacoplando o cliente das classes concretas que ele
instancia. → [`criacionais/`](criacionais/)

| Pasta | Padrão | Atividade | Cenário |
| --- | --- | --- | --- |
| [`criacionais/abstract-factory`](criacionais/abstract-factory) | Abstract Factory | — | Combos de uma cafeteria (café + sobremesa compatíveis) |
| [`criacionais/builder`](criacionais/builder) | Builder | — | Montagem de pizzas com muitas opções |
| [`criacionais/prototype`](criacionais/prototype) | Prototype | 006 | Criação de personagens de um jogo por clonagem |
| [`criacionais/singleton`](criacionais/singleton) | Singleton | 007 | Configuração única da aplicação (`AppConfig`) |

## Padrões estruturais

Tratam da composição de classes e objetos para formar estruturas maiores, mantendo a
flexibilidade e reduzindo o acoplamento. → [`estruturais/`](estruturais/)

| Pasta | Padrão | Atividade | Cenário |
| --- | --- | --- | --- |
| [`estruturais/adapter`](estruturais/adapter) | Adapter | 008 | Integração com gateway de pagamento externo |
| [`estruturais/bridge`](estruturais/bridge) | Bridge | 009 | Notificações: tipo × canal de envio |
| [`estruturais/composite`](estruturais/composite) | Composite | 010 | Árvore de arquivos e pastas |
| [`estruturais/decorator`](estruturais/decorator) | Decorator | 011 | Bebidas com adicionais combináveis |
| [`estruturais/facade`](estruturais/facade) | Facade | 012 | Controle de um home theater |
| [`estruturais/flyweight`](estruturais/flyweight) | Flyweight | 013 | Renderização de milhares de árvores |
| [`estruturais/proxy`](estruturais/proxy) | Proxy | 014 | Galeria de imagens com carregamento sob demanda |

## Estrutura do repositório

```
.
├── criacionais/
│   ├── README.md
│   ├── abstract-factory/   src/ (pacote padrão)
│   ├── builder/            src/ (pacote padrão)
│   ├── prototype/          Maven — src/main/java/com/example/prototype
│   └── singleton/          src/main/java/br/com/singleton
└── estruturais/
    ├── README.md
    ├── adapter/            src/ (pacote padrão) + RESPOSTAS.md
    ├── bridge/             src/main/java/notification
    ├── composite/          src/ (pacote padrão)
    ├── decorator/          src/ (pacote padrão)
    ├── facade/             src/ (pacote padrão)
    ├── flyweight/          src/ (pacote padrão)
    └── proxy/              src/ (pacote padrão)
```

Cada projeto preservou o layout de código com que foi entregue, por isso os comandos de
compilação variam um pouco entre as pastas — o `README.md` de cada padrão traz o comando
correto e a saída esperada.

## Como compilar e executar

Requisitos: **JDK 17 ou superior** (a Atividade 013 usa `record`; o projeto Prototype usa
Maven 3.8+).

Para os projetos com código no pacote padrão, dentro da pasta do padrão:

```bash
javac -d out src/*.java
java -cp out Main
```

Para os projetos com código em pacotes (`bridge`, `singleton`), dentro da pasta do padrão
(sintaxe de shell POSIX / Git Bash):

```bash
javac -d out $(find src -name "*.java")
java -cp out notification.Main
```

> Substitua `notification.Main` por `br.com.singleton.Main` no caso do Singleton. O
> `README.md` do Singleton traz também a versão equivalente em PowerShell.

Para o projeto Prototype (Maven), dentro de `criacionais/prototype`:

```bash
mvn clean verify
mvn exec:java
```

## Histórico

As atividades 006 a 011 foram desenvolvidas originalmente em repositórios separados e foram
consolidadas aqui, com o código e as respostas preservados. As atividades 012 (Facade), 013
(Flyweight) e 014 (Proxy) foram desenvolvidas diretamente neste repositório.
