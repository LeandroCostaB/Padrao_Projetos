/**
 * Abstração (Target) utilizada pelo sistema para processar pagamentos.
 * O OrderService depende apenas desta interface, nunca de uma implementação concreta.
 */
public interface PaymentProcessor {
    void processPayment(double amount);
}
