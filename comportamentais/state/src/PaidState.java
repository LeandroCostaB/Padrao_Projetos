public class PaidState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("Payment not allowed");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Order shipped");
        order.changeState(new ShippedState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Order cancelled");
        order.changeState(new CancelledState());
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Delivery not allowed");
    }

    @Override
    public String getName() {
        return "PAID";
    }
}
