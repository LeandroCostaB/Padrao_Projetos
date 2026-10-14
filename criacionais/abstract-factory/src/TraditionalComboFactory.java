public class TraditionalComboFactory implements ComboFactory {
    @Override
    public Coffee createCoffee() {
        return new TraditionalCoffee();
    }

    @Override
    public Dessert createDessert() {
        return new TraditionalDessert();
    }
}
