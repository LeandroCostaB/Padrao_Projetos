package notification;

public abstract class Notification {

    protected final NotificationSender sender;

    protected Notification(NotificationSender sender) {
        this.sender = sender;
    }

    public abstract void send(String message);
}
