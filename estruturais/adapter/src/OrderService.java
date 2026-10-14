/**
 * Serviço de pedidos. Depende apenas da abstração PaymentProcessor,
 * então não sabe (nem precisa saber) se o pagamento é feito com o
 * CreditCardPayment original ou com o gateway externo adaptado,
 * e muito menos em qual moeda a cobrança acontece.
 */
public class OrderService {

    private final PaymentProcessor paymentProcessor;

    public OrderService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void checkout(double amount) {
        paymentProcessor.processPayment(amount);
    }
}
