public class FitnessComboFactory implements ComboFactory {
    @Override
    public Coffee createCoffee() {
        return new FitnessCoffee();
    }

    @Override
    public Dessert createDessert() {
        return new FitnessDessert();
    }
}
