public class HomeTheaterFacade {

    private final Projector projector;
    private final SoundSystem sound;
    private final StreamingPlayer player;
    private final RoomLights lights;
    private final ProjectionScreen screen;

    public HomeTheaterFacade(Projector projector,
                             SoundSystem sound,
                             StreamingPlayer player,
                             RoomLights lights,
                             ProjectionScreen screen) {
        this.projector = projector;
        this.sound = sound;
        this.player = player;
        this.lights = lights;
        this.screen = screen;
    }

    public void watchMovie(String movie) {
        lights.dim();
        screen.lower();
        projector.on();
        sound.on();
        sound.setVolume(20);
        player.play(movie);
    }

    public void endMovie() {
        player.stop();
        sound.off();
        projector.off();
        screen.raise();
        lights.on();
    }
}
