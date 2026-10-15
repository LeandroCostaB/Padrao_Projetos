public class CancelledState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("Payment not allowed");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Shipping not allowed");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Cancellation not allowed");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Delivery not allowed");
    }

    @Override
    public String getName() {
        return "CANCELLED";
    }
}
