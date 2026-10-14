package notification;

public class WarningNotification extends Notification {

    public WarningNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.sendMessage("[Warning] " + message);
    }
}
