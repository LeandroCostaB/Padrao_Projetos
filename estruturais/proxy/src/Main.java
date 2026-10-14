import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Image> gallery = new ArrayList<>();

        gallery.add(new ImageProxy("forest.jpg"));
        gallery.add(new ImageProxy("beach.jpg"));
        gallery.add(new ImageProxy("mountain.jpg"));

        System.out.println("Gallery created");

        System.out.println();
        System.out.println("-- Displaying the first image twice --");
        show(gallery.get(0));
        show(gallery.get(0));

        System.out.println();
        System.out.println("-- Displaying the second image --");
        show(gallery.get(1));

        System.out.println();
        System.out.println("-- The third image was never displayed, so it was never loaded --");

        System.out.println();
        System.out.println("-- Protected proxy without permission --");
        Image restricted = new ProtectedImageProxy(new ImageProxy("private.jpg"), false);
        show(restricted);

        System.out.println();
        System.out.println("-- Protected proxy with permission --");
        Image allowed = new ProtectedImageProxy(new ImageProxy("vacation.jpg"), true);
        show(allowed);
        show(allowed);
    }

    private static void show(Image image) {
        image.display();
    }
}
