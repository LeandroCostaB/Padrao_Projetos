/**
 * Adapter: traduz a chamada simples esperada pelo sistema
 * (processPayment(double amount), herdada de PaymentProcessor)
 * para a chamada exigida pelo serviço externo
 * (makePayment(String currency, double value)).
 *
 * A moeda é uma decisão tomada na hora de MONTAR o adapter (fora do
 * OrderService), então quem usa PaymentProcessor nunca precisa saber
 * qual moeda está sendo usada por baixo dos panos.
 */
public class ExternalPaymentGatewayAdapter implements PaymentProcessor {

    private final ExternalPaymentGateway gateway;
    private final Currency currency;

    /** Cria e gerencia sua própria instância do gateway externo. */
    public ExternalPaymentGatewayAdapter(Currency currency) {
        this(new ExternalPaymentGateway(), currency);
    }

    /** Recebe uma instância já existente do gateway externo (injeção de dependência). */
    public ExternalPaymentGatewayAdapter(ExternalPaymentGateway gateway, Currency currency) {
        this.gateway = gateway;
        this.currency = currency;
    }

    @Override
    public void processPayment(double amount) {
        gateway.makePayment(currency.name(), amount);
    }
}
