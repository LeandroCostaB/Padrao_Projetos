# Atividade 008 — Padrão Adapter — Respostas

**a) Qual problema existente na integração com o serviço externo foi resolvido com o Adapter?**

A incompatibilidade de interfaces. O `OrderService` sabia chamar apenas `pay(double amount)`, enquanto o serviço externo exige `makePayment(String currency, double value)`. Sem o Adapter, seria necessário alterar o `OrderService` (e qualquer outro cliente) para conhecer os detalhes do `ExternalPaymentGateway`, aumentando o acoplamento e espalhando lógica de integração externa pelo sistema. O Adapter isola essa tradução em uma única classe, permitindo que o restante do sistema continue enxergando apenas a interface simples `PaymentProcessor`.

**b) Qual classe representa o Adapter na sua implementação?**

A classe `ExternalPaymentGatewayAdapter`. Ela implementa `PaymentProcessor` e, internamente, delega a chamada para uma instância de `ExternalPaymentGateway`, convertendo `processPayment(double amount)` em `makePayment(currency, amount)`.

**c) Qual é a diferença entre a interface esperada pelo sistema e a interface fornecida pelo serviço externo?**

O sistema espera um método simples, com um único parâmetro: `processPayment(double amount)` (originalmente `pay(double amount)`). O serviço externo exige um método com dois parâmetros, incluindo a moeda: `makePayment(String currency, double value)`. Além da assinatura diferente, o serviço externo também exige uma informação (moeda) que o sistema interno, na versão original, nem possuía.

**d) Por que não é recomendado alterar diretamente a classe `ExternalPaymentGateway`?**

Porque ela pertence a outra equipe/plataforma: não está sob o controle do time que desenvolve o `OrderService`. Alterá-la poderia quebrar outros sistemas que já a utilizam, exigir permissão/coordenação com a equipe responsável, ou simplesmente ser impossível caso venha de uma biblioteca externa distribuída em binário. O padrão Adapter resolve o problema sem tocar no código de terceiros, respeitando o princípio Open/Closed (aberto para extensão, fechado para modificação).

**e) Em quais situações o padrão Adapter é mais indicado do que modificar todas as classes clientes?**

Quando existem múltiplos pontos do sistema que dependem da interface antiga (mudar todos eles seria trabalhoso, arriscado e aumentaria o acoplamento com o serviço externo). Também quando a classe a ser integrada não pode ser alterada (é de terceiros, de outra equipe, legada, ou vem de uma biblioteca). E quando se deseja poder trocar facilmente de fornecedor/implementação no futuro (por exemplo, trocar `ExternalPaymentGateway` por outro gateway de pagamento): bastando criar um novo Adapter, sem tocar nas classes clientes, já que todas dependem apenas da abstração `PaymentProcessor`.

## Sobre o desafio adicional (múltiplas moedas)

O `ExternalPaymentGatewayAdapter` passou a receber a moeda (`Currency.BRL`, `Currency.USD` ou `Currency.EUR`) no momento em que é **construído**, não a cada chamada de pagamento. Assim:

- O `OrderService` continua chamando apenas `paymentProcessor.processPayment(amount)` — ele nunca soube e continua não sabendo o que é moeda.
- Quem decide a moeda é quem monta o objeto (`Main`, ou uma fábrica/configuração), na hora de escolher qual `PaymentProcessor` passar para o `OrderService`.
- Usar um `enum Currency` em vez de `String` evita erros de digitação e centraliza, num único lugar, quais moedas são suportadas.

Isso demonstra como o Adapter ajuda a isolar detalhes de integração externa: um novo requisito de moeda foi absorvido inteiramente dentro do Adapter, sem qualquer alteração no `OrderService` nem no `ExternalPaymentGateway`.
