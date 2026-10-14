package notification;

public class SmsSender implements NotificationSender {

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending by SMS: " + message);
    }
}
