import java.util.Objects;

public class ChatUser {

    private final String name;
    private final ChatMediator mediator;

    public ChatUser(String name, ChatMediator mediator) {
        Objects.requireNonNull(name, "name must not be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be empty");
        }

        this.name = name;
        this.mediator = Objects.requireNonNull(mediator, "mediator must not be null");
    }

    public String getName() {
        return name;
    }

    public void send(String message) {
        mediator.send(message, this);
    }

    public void sendPrivate(String recipientName, String message) {
        mediator.sendPrivate(message, this, recipientName);
    }

    public void receive(String senderName, String message) {
        System.out.println(name + " received from " + senderName + ": " + message);
    }
}
