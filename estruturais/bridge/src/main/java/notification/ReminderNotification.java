package notification;

public class ReminderNotification extends Notification {

    public ReminderNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.sendMessage("[Reminder] " + message);
    }
}
