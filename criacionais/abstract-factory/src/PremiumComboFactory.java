public class PremiumComboFactory implements ComboFactory {
    @Override
    public Coffee createCoffee() {
        return new PremiumCoffee();
    }

    @Override
    public Dessert createDessert() {
        return new PremiumDessert();
    }
}
