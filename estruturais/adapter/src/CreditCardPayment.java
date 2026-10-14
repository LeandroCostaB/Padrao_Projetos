/**
 * Implementação original do sistema, hoje adaptada para também satisfazer
 * a abstração PaymentProcessor. O método pay(double) foi mantido intacto
 * para não quebrar nenhum código legado que ainda dependa dele.
 */
public class CreditCardPayment implements PaymentProcessor {

    public void pay(double amount) {
        System.out.println("Payment approved: $" + amount);
    }

    @Override
    public void processPayment(double amount) {
        pay(amount);
    }
}
