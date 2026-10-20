public class Main {

    public static void main(String[] args) {

        ChatRoom room = new ChatRoom();

        ChatUser ana = new ChatUser("Ana", room);
        ChatUser bruno = new ChatUser("Bruno", room);
        ChatUser carla = new ChatUser("Carla", room);

        room.register(ana);
        room.register(bruno);
        room.register(carla);

        System.out.println();
        System.out.println("-- Registering the same Bruno instance again --");
        room.register(bruno);

        System.out.println();
        System.out.println("-- Ana sends a message to the room --");
        ana.send("Hello, everyone!");

        System.out.println();
        System.out.println("-- Diego joins and Bruno sends a message --");
        ChatUser diego = new ChatUser("Diego", room);
        room.register(diego);
        bruno.send("Hello, Ana!");

        System.out.println();
        System.out.println("-- Eduardo knows the room but was never registered --");
        ChatUser eduardo = new ChatUser("Eduardo", room);
        eduardo.send("Can anyone hear me?");

        System.out.println();
        System.out.println("-- Another user tries to take the name Ana --");
        ChatUser otherAna = new ChatUser("Ana", room);
        room.register(otherAna);
        otherAna.send("I am the other Ana");

        System.out.println();
        System.out.println("-- Private message from Ana to Carla --");
        ana.sendPrivate("Carla", "Hi Carla, just between us");

        System.out.println();
        System.out.println("-- Private message to a name that does not exist --");
        ana.sendPrivate("Diana", "Are you there?");

        System.out.println();
        System.out.println("-- Private message to Ana herself --");
        ana.sendPrivate("Ana", "Talking to myself");

        System.out.println();
        System.out.println("-- Names are compared exactly, so lower case does not match --");
        ana.sendPrivate("carla", "Hi again");
    }
}
