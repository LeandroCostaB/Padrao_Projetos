public class Main {

    public static void main(String[] args) {

        Light livingRoom = new Light("Living room");
        Light kitchen = new Light("Kitchen");

        Command livingRoomOn = new TurnOnLightCommand(livingRoom);
        Command livingRoomOff = new TurnOffLightCommand(livingRoom);
        Command kitchenOn = new TurnOnLightCommand(kitchen);
        Command kitchenOff = new TurnOffLightCommand(kitchen);

        RemoteControl remote = new RemoteControl();

        System.out.println("-- Configuring the buttons runs no command --");
        remote.setCommand("LIVING_ON", livingRoomOn);
        remote.setCommand("LIVING_OFF", livingRoomOff);
        remote.setCommand("KITCHEN_ON", kitchenOn);
        printStates(livingRoom, kitchen);

        System.out.println();
        System.out.println("-- Pressing the three configured buttons --");
        remote.pressButton("LIVING_ON");
        remote.pressButton("LIVING_OFF");
        remote.pressButton("KITCHEN_ON");
        printStates(livingRoom, kitchen);

        System.out.println();
        System.out.println("-- LIVING_ON reconfigured to turn the kitchen off --");
        remote.setCommand("LIVING_ON", kitchenOff);
        remote.pressButton("LIVING_ON");
        printStates(livingRoom, kitchen);

        System.out.println();
        System.out.println("-- Button that was never configured --");
        remote.pressButton("BEDROOM_ON");

        System.out.println();
        System.out.println("-- Names are compared exactly, so lower case does not match --");
        remote.pressButton("kitchen_on");

        System.out.println();
        System.out.println("-- Rejecting an invalid configuration --");

        try {
            remote.setCommand("", kitchenOn);
        } catch (IllegalArgumentException exception) {
            System.out.println("Refused: " + exception.getMessage());
        }

        try {
            remote.setCommand("KITCHEN_OFF", null);
        } catch (NullPointerException exception) {
            System.out.println("Refused: " + exception.getMessage());
        }

        System.out.println();
        System.out.println("-- Undo after turning on a light that was off --");
        remote.pressButton("KITCHEN_ON");
        printStates(livingRoom, kitchen);
        remote.undoLast();
        printStates(livingRoom, kitchen);

        System.out.println();
        System.out.println("-- Undo after turning on a light that was already on --");
        remote.pressButton("KITCHEN_ON");
        remote.pressButton("KITCHEN_ON");
        printStates(livingRoom, kitchen);

        System.out.println("The last action survives an unknown button and a reconfiguration:");
        remote.pressButton("BEDROOM_ON");
        remote.setCommand("LIVING_ON", livingRoomOn);

        remote.undoLast();
        printStates(livingRoom, kitchen);

        System.out.println();
        System.out.println("-- Undoing again, with no action available --");
        remote.undoLast();
    }

    private static void printStates(Light livingRoom, Light kitchen) {
        System.out.println("States: " + livingRoom.getLocation() + "=" + livingRoom.isOn()
                + ", " + kitchen.getLocation() + "=" + kitchen.isOn());
    }
}
