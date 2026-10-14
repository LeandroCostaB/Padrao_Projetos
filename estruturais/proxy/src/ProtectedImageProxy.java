public class ProtectedImageProxy implements Image {

    private final Image image;
    private final boolean accessAllowed;

    public ProtectedImageProxy(Image image, boolean accessAllowed) {
        this.image = image;
        this.accessAllowed = accessAllowed;
    }

    @Override
    public void display() {
        if (!accessAllowed) {
            System.out.println("Access denied");
            return;
        }

        image.display();
    }
}
