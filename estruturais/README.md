# Padrões estruturais

Padrões que tratam da **composição de classes e objetos** para formar estruturas maiores: em vez
de herança rígida, usam composição e delegação através de abstrações comuns, mantendo as partes
independentes e substituíveis.

| Pasta | Padrão | Atividade | Intenção | Cenário |
| --- | --- | --- | --- | --- |
| [`adapter`](adapter) | Adapter | 008 | **Converter** a interface de uma classe em outra que o cliente espera | Integração com um gateway de pagamento externo incompatível |
| [`bridge`](bridge) | Bridge | 009 | **Separar** uma abstração de sua implementação para que variem independentemente | Notificações: tipo de notificação × canal de envio |
| [`composite`](composite) | Composite | 010 | Tratar objetos individuais e composições de forma **uniforme**, em árvore | Arquivos e pastas com subpastas em vários níveis |
| [`decorator`](decorator) | Decorator | 011 | **Acrescentar** responsabilidades a um objeto dinamicamente | Bebidas de cafeteria com adicionais combináveis |
| [`facade`](facade) | Facade | 012 | Oferecer uma interface **única e simplificada** para um subsistema | Iniciar e encerrar uma sessão de home theater |
| [`flyweight`](flyweight) | Flyweight | 013 | **Compartilhar** estado comum entre muitos objetos semelhantes | Renderização de milhares de árvores em um cenário |
| [`proxy`](proxy) | Proxy | 014 | **Controlar o acesso** a um objeto (criação tardia, permissão) | Galeria de imagens de alta resolução sob demanda |

## Como se diferenciam

Vários desses padrões têm estruturas parecidas — um objeto que implementa uma abstração e
encaminha chamadas a outro — mas intenções distintas:

- **Adapter** muda a *interface* de algo que já existe, para encaixá-lo onde não encaixava.
- **Decorator** mantém a interface e *soma comportamento* ao objeto envolvido, podendo ser
  empilhado.
- **Proxy** mantém a interface e controla *se, quando e sob quais condições* a chamada chega ao
  objeto real — podendo criá-lo tardiamente ou nem delegar.
- **Facade** não implementa a interface dos subsistemas: ele cria uma interface **nova e mais
  simples** por cima de vários objetos.
- **Bridge** se parece com o Adapter, mas é aplicado **por projeto, antes do fato**, para evitar a
  explosão combinatória entre duas dimensões de variação — e não para corrigir uma
  incompatibilidade já existente.
- **Composite** e **Flyweight** tratam de quantidade: o Composite organiza objetos em uma
  **hierarquia** tratada uniformemente; o Flyweight reduz o **custo de memória** quando há muitos
  objetos com dados idênticos.

Cada pasta contém o código-fonte, as instruções de execução e as respostas às questões para
reflexão da respectiva atividade. Os padrões criacionais estão em [`../criacionais`](../criacionais).
