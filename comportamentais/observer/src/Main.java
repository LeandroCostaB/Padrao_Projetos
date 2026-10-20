public class Main {

    public static void main(String[] args) {

        WeatherStation station = new WeatherStation();

        CurrentConditionsDisplay display = new CurrentConditionsDisplay();
        TemperatureAlert alert = new TemperatureAlert();

        station.addObserver(display);
        station.addObserver(alert);

        System.out.println("-- Registering the same display instance again --");
        station.addObserver(display);

        System.out.println();
        System.out.println("-- Rejecting a null observer --");

        try {
            station.addObserver(null);
        } catch (NullPointerException exception) {
            System.out.println("Refused: " + exception.getMessage());
        }

        System.out.println();
        System.out.println("-- Temperature 28 C --");
        station.setTemperature(28.0);

        System.out.println();
        System.out.println("-- Temperature 36 C --");
        station.setTemperature(36.0);

        System.out.println();
        System.out.println("-- Alert removed, temperature 38 C --");
        station.removeObserver(alert);
        station.setTemperature(38.0);

        System.out.println();
        System.out.println("-- Removing the alert again has no effect --");
        station.removeObserver(alert);

        System.out.println();
        System.out.println("-- Display removed, temperature 40 C: no observers left --");
        station.removeObserver(display);
        station.setTemperature(40.0);
        System.out.println("Station still works, current temperature: " + station.getTemperature() + " C");

        System.out.println();
        System.out.println("-- New station with the average display --");
        WeatherStation averageStation = new WeatherStation();
        averageStation.addObserver(new AverageTemperatureDisplay());

        averageStation.setTemperature(20.0);
        averageStation.setTemperature(30.0);
        averageStation.setTemperature(40.0);
    }
}
