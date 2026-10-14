package notification;

public class EmailSender implements NotificationSender {

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending by email: " + message);
    }
}
