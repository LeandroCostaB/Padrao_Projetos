import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RemoteControl {

    private final Map<String, Command> buttons = new HashMap<>();
    private Command lastExecuted;

    public void setCommand(String button, Command command) {
        Objects.requireNonNull(button, "button must not be null");

        if (button.isBlank()) {
            throw new IllegalArgumentException("button must not be empty");
        }

        Objects.requireNonNull(command, "command must not be null");

        buttons.put(button, command);
    }

    public void pressButton(String button) {
        Command command = buttons.get(button);

        if (command == null) {
            System.out.println("Button not configured: " + button);
            return;
        }

        command.execute();
        lastExecuted = command;
    }

    public void undoLast() {
        if (lastExecuted == null) {
            System.out.println("Nothing to undo");
            return;
        }

        lastExecuted.undo();
        lastExecuted = null;
    }
}
