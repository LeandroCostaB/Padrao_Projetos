public class Main {

    public static void main(String[] args) {

        Projector projector = new Projector();
        SoundSystem sound = new SoundSystem();
        StreamingPlayer player = new StreamingPlayer();
        RoomLights lights = new RoomLights();
        ProjectionScreen screen = new ProjectionScreen();

        HomeTheaterFacade homeTheater =
                new HomeTheaterFacade(projector, sound, player, lights, screen);

        homeTheater.watchMovie("Design Patterns: The Movie");

        System.out.println();

        homeTheater.endMovie();
    }
}
