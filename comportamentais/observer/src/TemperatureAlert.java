public class TemperatureAlert implements TemperatureObserver {

    private static final double THRESHOLD = 35.0;

    @Override
    public void update(double temperature) {
        if (temperature >= THRESHOLD) {
            System.out.println("High temperature alert: " + temperature + " C");
        }
    }
}
