package notification;

public class WhatsAppSender implements NotificationSender {

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending by WhatsApp: " + message);
    }
}
