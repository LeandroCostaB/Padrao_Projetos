# Padrões de Projeto — Atividades

Repositório único com as atividades práticas de Padrões de Projeto (GoF) implementadas em Java,
organizadas por **categoria do padrão**: padrões **criacionais** (como objetos são criados),
**estruturais** (como objetos e classes são compostos) e **comportamentais** (como objetos
colaboram e dividem responsabilidades).

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

## Padrões comportamentais

Tratam da comunicação e da divisão de responsabilidades entre objetos, substituindo condicionais
por colaboração entre classes especializadas. → [`comportamentais/`](comportamentais/)

| Pasta | Padrão | Atividade | Cenário |
| --- | --- | --- | --- |
| [`comportamentais/strategy`](comportamentais/strategy) | Strategy | 015 | Cálculo de frete por modalidade de entrega |
| [`comportamentais/template-method`](comportamentais/template-method) | Template Method | 016 | Geração de relatórios em CSV, HTML e texto |
| [`comportamentais/state`](comportamentais/state) | State | 017 | Ciclo de vida de um pedido de loja virtual |
| [`comportamentais/observer`](comportamentais/observer) | Observer | 018 | Estação meteorológica e seus painéis |
| [`comportamentais/mediator`](comportamentais/mediator) | Mediator | 019 | Sala de chat com mensagens públicas e privadas |

## Estrutura do repositório

```
.
├── criacionais/
│   ├── README.md
│   ├── abstract-factory/   src/ (pacote padrão)
│   ├── builder/            src/ (pacote padrão)
│   ├── prototype/          Maven — src/main/java/com/example/prototype
│   └── singleton/          src/main/java/br/com/singleton
├── estruturais/
│   ├── README.md
│   ├── adapter/            src/ (pacote padrão) + RESPOSTAS.md
│   ├── bridge/             src/main/java/notification
│   ├── composite/          src/ (pacote padrão)
│   ├── decorator/          src/ (pacote padrão)
│   ├── facade/             src/ (pacote padrão)
│   ├── flyweight/          src/ (pacote padrão)
│   └── proxy/              src/ (pacote padrão)
└── comportamentais/
    ├── README.md
    ├── strategy/           src/ (pacote padrão)
    ├── template-method/    src/ (pacote padrão)
    ├── state/              src/ (pacote padrão)
    ├── observer/           src/ (pacote padrão)
    └── mediator/           src/ (pacote padrão)
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
(Flyweight), 014 (Proxy), 015 (Strategy), 016 (Template Method), 017 (State), 018 (Observer) e
019 (Mediator) foram desenvolvidas diretamente neste repositório.
