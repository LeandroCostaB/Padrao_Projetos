import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherStation implements TemperatureSubject {

    private final List<TemperatureObserver> observers = new ArrayList<>();
    private double temperature;

    @Override
    public void addObserver(TemperatureObserver observer) {
        Objects.requireNonNull(observer, "observer must not be null");

        if (isRegistered(observer)) {
            System.out.println("Observer already registered");
            return;
        }

        observers.add(observer);
    }

    @Override
    public void removeObserver(TemperatureObserver observer) {
        observers.removeIf(registered -> registered == observer);
    }

    @Override
    public void notifyObservers() {
        for (TemperatureObserver observer : observers) {
            observer.update(temperature);
        }
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        notifyObservers();
    }

    public double getTemperature() {
        return temperature;
    }

    private boolean isRegistered(TemperatureObserver observer) {
        for (TemperatureObserver registered : observers) {
            if (registered == observer) {
                return true;
            }
        }

        return false;
    }
}
