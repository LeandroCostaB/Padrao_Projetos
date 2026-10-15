public class Main {

    public static void main(String[] args) {

        double weight = 3.0;

        ShippingCalculator calculator = new ShippingCalculator(new StandardShipping());
        System.out.println("Standard: R$ " + calculator.calculate(weight));

        calculator.setStrategy(new ExpressShipping());
        System.out.println("Express: R$ " + calculator.calculate(weight));

        calculator.setStrategy(new StorePickup());
        System.out.println("Pickup: R$ " + calculator.calculate(weight));

        calculator.setStrategy(new EconomyShipping());
        System.out.println("Economy: R$ " + calculator.calculate(weight));

        System.out.println();
        System.out.println("Rejecting a null strategy:");

        try {
            calculator.setStrategy(null);
        } catch (NullPointerException exception) {
            System.out.println("Refused: " + exception.getMessage());
        }

        System.out.println("Strategy still in use: R$ " + calculator.calculate(weight));
    }
}
