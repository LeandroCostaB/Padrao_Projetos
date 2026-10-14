public class TreeType {

    private final String name;
    private final String color;
    private final byte[] texture;

    public TreeType(String name, String color, byte[] texture) {
        this.name = name;
        this.color = color;
        this.texture = texture.clone();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getTextureSize() {
        return texture.length;
    }

    public void draw(int x, int y) {
        draw(x, y, 1.0);
    }

    public void draw(int x, int y, double scale) {
        System.out.println("Drawing " + name + " (" + color + ") at ("
                + x + ", " + y + "), scale: " + scale
                + ", texture: " + texture.length + " bytes");
    }
}
