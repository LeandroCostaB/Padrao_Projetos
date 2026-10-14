public class ImageProxy implements Image {

    private final String filename;
    private HighResolutionImage image;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (image == null) {
            image = new HighResolutionImage(filename);
        }

        image.display();
    }
}
