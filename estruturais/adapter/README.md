# Adapter

Atividade 008 — Padrão de projeto **Adapter**.

Refatoração de um sistema de pagamentos para integrar uma plataforma externa
(`ExternalPaymentGateway`, com interface incompatível) sem alterar o código
existente do time nem espalhar detalhes da integração pelo sistema.

## Estrutura

```
src/
├── PaymentProcessor.java            # abstração (Target) usada pelo OrderService
├── CreditCardPayment.java           # implementação original do sistema
├── ExternalPaymentGateway.java      # serviço externo (Adaptee), não alterado
├── ExternalPaymentGatewayAdapter.java # Adapter: liga PaymentProcessor ao gateway externo
├── Currency.java                    # moedas suportadas (BRL, USD, EUR)
├── OrderService.java                # cliente, depende só da abstração
└── Main.java                        # demonstração
```

## Como executar

```bash
cd src
javac *.java -d ../out
java -cp ../out Main
```

Saída esperada:

```
Payment approved: $150.0
External payment approved: BRL 200.0
External payment approved: USD 99.9
External payment approved: EUR 75.5
```

As respostas às questões de reflexão estão em [`RESPOSTAS.md`](./RESPOSTAS.md).
