package notification;

public class AlertNotification extends Notification {

    public AlertNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.sendMessage("[Alert] " + message);
    }
}
