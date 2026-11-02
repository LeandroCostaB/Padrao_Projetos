public class Light {

    private final String location;
    private boolean on;

    public Light(String location) {
        this.location = location;
    }

    public void turnOn() {
        on = true;
        System.out.println(location + ": light on");
    }

    public void turnOff() {
        on = false;
        System.out.println(location + ": light off");
    }

    public boolean isOn() {
        return on;
    }

    public String getLocation() {
        return location;
    }
}
