public class OrderService {
    private final ComboFactory comboFactory;

    public OrderService(ComboFactory comboFactory) {
        this.comboFactory = comboFactory;
    }

    public void createCombo() {
        Coffee coffee = comboFactory.createCoffee();
        Dessert dessert = comboFactory.createDessert();

        coffee.prepare();
        dessert.prepare();
    }
}
