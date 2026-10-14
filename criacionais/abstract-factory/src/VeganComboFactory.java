public class VeganComboFactory implements ComboFactory {
    @Override
    public Coffee createCoffee() {
        return new VeganCoffee();
    }

    @Override
    public Dessert createDessert() {
        return new VeganDessert();
    }
}
