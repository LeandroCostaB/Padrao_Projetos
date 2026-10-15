# Padrões comportamentais

Padrões que tratam da **comunicação e da divisão de responsabilidades entre objetos**: em vez de
concentrar decisões em condicionais dentro de uma única classe, distribuem o comportamento entre
objetos colaboradores, definindo quem faz o quê e como as chamadas fluem entre eles.

| Pasta | Padrão | Atividade | Intenção | Cenário |
| --- | --- | --- | --- | --- |
| [`strategy`](strategy) | Strategy | 015 | Encapsular **algoritmos intercambiáveis** e trocá-los em tempo de execução | Cálculo de frete por modalidade de entrega |
| [`template-method`](template-method) | Template Method | 016 | Definir o **esqueleto de um algoritmo**, deixando etapas para as subclasses | Geração de relatórios em CSV, HTML e texto |
| [`state`](state) | State | 017 | Alterar o comportamento de um objeto conforme seu **estado interno** | Ciclo de vida de um pedido de loja virtual |

## Como se diferenciam

Os três atacam o mesmo sintoma — condicionais que crescem a cada novo caso — mas de formas
distintas:

- **Strategy** e **State** têm estrutura quase idêntica (um contexto delegando a um objeto atrás
  de uma interface), mas intenções opostas: as estratégias são **alternativas equivalentes**
  escolhidas de fora pelo cliente, enquanto os estados são **momentos de um ciclo de vida** que
  conhecem seus sucessores e trocam a si mesmos, de dentro.
- **Template Method** resolve por **herança** o que o Strategy resolve por **composição**: ele fixa
  a ordem das etapas na classe base e varia apenas o que cada etapa faz, com a escolha definida em
  tempo de compilação — em troca, permite compartilhar o código comum entre as variações, algo que
  o Strategy não oferece.

Cada pasta contém o código-fonte, as instruções de execução e as respostas às questões para
reflexão da respectiva atividade. As outras categorias estão em [`../criacionais`](../criacionais)
e [`../estruturais`](../estruturais).
