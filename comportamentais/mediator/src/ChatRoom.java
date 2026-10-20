import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatRoom implements ChatMediator {

    private final List<ChatUser> users = new ArrayList<>();

    @Override
    public void register(ChatUser user) {
        Objects.requireNonNull(user, "user must not be null");

        if (isRegistered(user)) {
            System.out.println("Registration refused: " + user.getName() + " is already registered");
            return;
        }

        if (findByName(user.getName()) != null) {
            System.out.println("Registration refused: the name " + user.getName() + " is already taken");
            return;
        }

        users.add(user);
        System.out.println(user.getName() + " joined the room");
    }

    @Override
    public void send(String message, ChatUser sender) {
        if (!isRegistered(sender)) {
            System.out.println("Message refused: " + sender.getName() + " is not registered in the room");
            return;
        }

        for (ChatUser user : users) {
            if (user != sender) {
                user.receive(sender.getName(), message);
            }
        }
    }

    @Override
    public void sendPrivate(String message, ChatUser sender, String recipientName) {
        if (!isRegistered(sender)) {
            System.out.println("Private message refused: " + sender.getName() + " is not registered in the room");
            return;
        }

        ChatUser recipient = findByName(recipientName);

        if (recipient == null) {
            System.out.println("Private message refused: there is no user named " + recipientName + " in the room");
            return;
        }

        if (recipient == sender) {
            System.out.println("Private message refused: " + sender.getName() + " cannot send a private message to themselves");
            return;
        }

        recipient.receive(sender.getName(), message);
    }

    private boolean isRegistered(ChatUser user) {
        for (ChatUser registered : users) {
            if (registered == user) {
                return true;
            }
        }

        return false;
    }

    private ChatUser findByName(String name) {
        for (ChatUser user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }

        return null;
    }
}
