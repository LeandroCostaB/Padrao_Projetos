public class Main {
    public static void main(String[] args) {
        // 1) Fluxo original: pagamento com cartão de crédito (classe própria do sistema)
        OrderService creditCardOrder = new OrderService(new CreditCardPayment());
        creditCardOrder.checkout(150.00);

        // 2) Novo fluxo: pagamento através da plataforma externa, adaptada para
        //    a interface PaymentProcessor. O OrderService é o mesmo de sempre;
        //    só muda o PaymentProcessor que é passado no construtor.
        OrderService orderInBRL = new OrderService(new ExternalPaymentGatewayAdapter(Currency.BRL));
        orderInBRL.checkout(200.00);

        OrderService orderInUSD = new OrderService(new ExternalPaymentGatewayAdapter(Currency.USD));
        orderInUSD.checkout(99.90);

        OrderService orderInEUR = new OrderService(new ExternalPaymentGatewayAdapter(Currency.EUR));
        orderInEUR.checkout(75.50);
    }
}
