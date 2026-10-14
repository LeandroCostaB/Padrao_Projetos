public class Main {
    public static void main(String[] args) {
        demonstrate("Traditional Combo", new TraditionalComboFactory());
        demonstrate("Fitness Combo", new FitnessComboFactory());
        demonstrate("Premium Combo", new PremiumComboFactory());
        demonstrate("Vegan Combo", new VeganComboFactory());
    }

    private static void demonstrate(String comboName, ComboFactory factory) {
        System.out.println("--- " + comboName + " ---");
        new OrderService(factory).createCombo();
        System.out.println();
    }
}
