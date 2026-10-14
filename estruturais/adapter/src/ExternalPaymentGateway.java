/**
 * Classe do serviço externo (Adaptee). Pertence a outra equipe e NÃO pode
 * ser alterada: sua interface original (makePayment) é mantida como está.
 */
public class ExternalPaymentGateway {
    public void makePayment(String currency, double value) {
        System.out.println("External payment approved: " + currency + " " + value);
    }
}
