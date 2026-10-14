public class Main {

    public static void main(String[] args) {

        Beverage plainCoffee = new Coffee();
        printOrder(plainCoffee);

        Beverage coffeeWithMilkAndChocolate = new ChocolateDecorator(new MilkDecorator(new Coffee()));
        printOrder(coffeeWithMilkAndChocolate);

        Beverage teaWithMilk = new MilkDecorator(new Tea());
        printOrder(teaWithMilk);

        Beverage coffeeWithDoubleChocolate = new ChocolateDecorator(new ChocolateDecorator(new Coffee()));
        printOrder(coffeeWithDoubleChocolate);

        Beverage coffeeWithMilkChocolateAndCaramel =
                new CaramelDecorator(new ChocolateDecorator(new MilkDecorator(new Coffee())));
        printOrder(coffeeWithMilkChocolateAndCaramel);
    }

    private static void printOrder(Beverage beverage) {
        System.out.println(beverage.getDescription());
        System.out.println("Total: R$ " + beverage.getCost());
        System.out.println();
    }
}
