public class PendingState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("Payment confirmed");
        order.changeState(new PaidState());
    }

    @Override
    public void ship(Order order) {
        System.out.println("Shipping not allowed");
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
        return "PENDING";
    }
}
