# Atividade 016 - Padrão Template Method

Sistema de geração de relatórios de vendas, refatorado para o padrão de projeto **Template
Method**, que define o esqueleto de um algoritmo em uma classe base e deixa que as subclasses
especializem apenas as etapas que variam, sem alterar a ordem do processo.

## Estrutura da solução

Todas as classes estão em [`src/`](src/):

```
src/
├── ReportGenerator.java  // classe abstrata com o Template Method generate()
├── CsvReport.java        // formato CSV      -> report.csv
├── HtmlReport.java       // formato HTML     -> report.html
├── TextReport.java       // texto simples    -> report.txt (desafio adicional)
└── Main.java             // usa referências do tipo ReportGenerator
```

A classe base [`ReportGenerator.java`](src/ReportGenerator.java) organiza as quatro etapas:

| Etapa | Declaração | Papel |
| --- | --- | --- |
| `generate()` | `public final` | **Template Method**: fixa a sequência das etapas |
| `readData()` | `protected final` | etapa **comum**, idêntica em todos os formatos |
| `formatData(String[] data)` | `protected abstract` | etapa **obrigatória**, varia por formato |
| `saveReport(String content)` | `protected abstract` | etapa **obrigatória**, varia por formato |
| `afterGeneration()` | `protected` com corpo | **hook**: opcional, tem comportamento padrão |

As subclasses implementam apenas `formatData` e `saveReport` — nenhuma delas repete a sequência
de geração, e a classe base não tem nenhuma condicional por formato.

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Saída obtida:

```
Reading sales data
Saving report.csv
Product
Notebook
Mouse
Report generated successfully

Reading sales data
Saving report.html
<ul><li>Notebook</li><li>Mouse</li></ul>
Report generated successfully

Reading sales data
Saving report.txt
Notebook
Mouse
Report generated successfully
Notification: report.txt is available
```

As mensagens confirmam que os três relatórios executam **as mesmas quatro etapas, na mesma
ordem**, variando apenas o conteúdo formatado e o nome do arquivo — e que só o `TextReport`
acrescenta a notificação final.

## Desafio adicional — relatório em texto e hook sobrescrito

[`TextReport.java`](src/TextReport.java) formata um produto por linha e simula o salvamento em
`report.txt`. Ele é a única subclasse que sobrescreve o hook, chamando primeiro o comportamento
padrão e depois acrescentando a notificação:

```java
@Override
protected void afterGeneration() {
    super.afterGeneration();
    System.out.println("Notification: report.txt is available");
}
```

Nem `ReportGenerator`, nem `CsvReport`, nem `HtmlReport` precisaram de qualquer alteração: o novo
formato e a etapa opcional especializada entraram sem tocar na sequência definida em `generate()`.

## Questões para reflexão

**a) Qual problema existente no código inicial foi resolvido com o Template Method?**

A **duplicação do fluxo** do algoritmo. `CsvReport` e `HtmlReport` repetiam integralmente as
quatro etapas, inclusive trechos idênticos de leitura dos dados e a mensagem de conclusão. Isso
significava que uma mudança na leitura (passar a buscar os dados de um banco, por exemplo) ou na
ordem das etapas teria que ser replicada em todas as classes, com risco de alguma ficar para trás.
Pior: nada garantia que um formato novo seguisse a mesma sequência — um desenvolvedor poderia
inverter etapas ou simplesmente esquecer o registro da conclusão. O Template Method elimina a
duplicação e, principalmente, **transforma a sequência correta em algo imposto pela classe base**,
não em uma convenção que cada subclasse precisa lembrar de respeitar.

**b) Qual método representa o Template Method e quais etapas são comuns, abstratas ou hooks na sua implementação?**

O Template Method é `generate()`, em `ReportGenerator`. As etapas se dividem em:

- **Comum (concreta e `final`):** `readData()` — igual para todos os formatos e não sobrescrevível.
- **Abstratas (obrigatórias):** `formatData(String[] data)` e `saveReport(String content)` — cada
  subclasse é obrigada a fornecer sua versão.
- **Hook (opcional):** `afterGeneration()` — já funciona na classe base; `TextReport` escolheu
  estendê-lo, `CsvReport` e `HtmlReport` apenas herdam o comportamento padrão.

**c) Por que o método `generate()` foi declarado como `final`?**

Porque o esqueleto do algoritmo é exatamente o que o padrão quer **proteger**. Se uma subclasse
pudesse sobrescrever `generate()`, ela poderia pular a leitura dos dados, inverter a ordem das
etapas ou omitir o registro da conclusão — e o problema do código original estaria de volta, agora
com a falsa impressão de que a classe base garante o fluxo. O `final` torna essa garantia real,
verificada pelo compilador: as subclasses podem variar **o que** cada etapa faz, nunca **quais
etapas existem nem em que ordem**. A mesma lógica se aplica ao `readData()` ser `final`: é uma
etapa que, por decisão de projeto, não deve variar entre os formatos.

**d) Qual é a diferença entre uma etapa abstrata obrigatória e um hook com implementação padrão?**

Uma etapa **abstrata** não tem corpo na classe base: o compilador obriga toda subclasse concreta a
implementá-la. Ela representa algo sem o qual o algoritmo não faz sentido — não existe relatório
sem definir como formatar e onde salvar. Um **hook** já vem com implementação (possivelmente até
vazia): a subclasse pode sobrescrevê-lo para se encaixar em um ponto de extensão, mas não é
obrigada a nada. Ele representa um comportamento **opcional ou com padrão razoável** — aqui, a
mensagem de conclusão serve para todos, e só o `TextReport` precisou acrescentar algo. Na prática:
a etapa abstrata declara "isto varia e você precisa decidir"; o hook declara "isto já funciona,
mas você pode interferir se quiser".

**e) Como Template Method e Strategy diferem quanto ao uso de herança ou composição e à possibilidade de variar o comportamento em tempo de execução?**

Os dois padrões permitem variar partes de um algoritmo, mas por mecanismos opostos:

- O **Template Method** usa **herança**. A variação é escolhida no momento em que o objeto é
  criado (`new CsvReport()`) e fica **fixa pelo resto da vida daquele objeto** — não há como um
  `CsvReport` virar um `HtmlReport`. A classe base controla o fluxo e chama as subclasses
  ("*don't call us, we'll call you*" — inversão de controle), o que permite compartilhar código
  comum (`readData()`), mas cria um acoplamento forte: as subclasses dependem da estrutura interna
  da classe base, e Java só permite estender uma classe.
- O **Strategy** usa **composição**. O objeto delega a um colaborador que pode ser **substituído
  em tempo de execução** — foi exatamente o que a [Atividade 015](../strategy) demonstrou, trocando
  quatro modalidades de frete na mesma calculadora via `setStrategy(...)`. Em compensação, o
  Strategy troca o algoritmo **inteiro**: não há um esqueleto compartilhado, e código comum entre
  estratégias precisa ser extraído à parte.

Resumindo: Template Method varia **etapas** de um fluxo fixo, em tempo de compilação, por herança;
Strategy varia o **algoritmo inteiro**, em tempo de execução, por composição. Neste sistema a
herança se justifica porque as etapas comuns (leitura e conclusão) são justamente o que se queria
reaproveitar e padronizar.
