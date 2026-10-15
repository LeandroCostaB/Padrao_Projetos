public class Main {

    public static void main(String[] args) {

        System.out.println("-- Order 1: ship before paying, then pay, ship and deliver --");
        Order order1 = new Order();
        order1.ship();
        printStatus(order1);
        order1.pay();
        printStatus(order1);
        order1.ship();
        printStatus(order1);
        order1.cancel();
        printStatus(order1);
        order1.deliver();
        printStatus(order1);
        order1.cancel();
        printStatus(order1);
        order1.deliver();
        printStatus(order1);

        System.out.println();
        System.out.println("-- Order 2: deliver before shipping, then cancel while pending --");
        Order order2 = new Order();
        order2.deliver();
        printStatus(order2);
        order2.cancel();
        printStatus(order2);
        order2.pay();
        printStatus(order2);

        System.out.println();
        System.out.println("-- Order 3: pay, try to deliver without shipping, then cancel --");
        Order order3 = new Order();
        order3.pay();
        printStatus(order3);
        order3.deliver();
        printStatus(order3);
        order3.cancel();
        printStatus(order3);
    }

    private static void printStatus(Order order) {
        System.out.println("Status: " + order.getStatus());
    }
}
