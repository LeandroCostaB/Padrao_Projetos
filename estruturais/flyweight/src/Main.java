import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        TreeTypeFactory factory = new TreeTypeFactory();
        List<Tree> forest = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            TreeType oak = factory.getTreeType("Oak", "Green", "oak.png");
            TreeType pine = factory.getTreeType("Pine", "Dark green", "pine.png");

            forest.add(new Tree(i, i * 2, 1.0 + (i % 3) * 0.5, oak));
            forest.add(new Tree(i, i * 3, 0.5 + (i % 4) * 0.25, pine));
        }

        for (Tree tree : forest) {
            tree.draw();
        }

        System.out.println();
        System.out.println("Trees created: " + forest.size());
        System.out.println("Tree types created: " + factory.getTypeCount());

        System.out.println();
        TreeType firstOak = forest.get(0).getType();
        TreeType anotherOak = factory.getTreeType("Oak", "Green", "oak.png");
        TreeType firstPine = forest.get(1).getType();

        System.out.println("Same combination reuses the type: " + (firstOak == anotherOak));
        System.out.println("Different combinations are distinct types: " + (firstOak != firstPine));
        System.out.println("Tree types after requesting Oak again: " + factory.getTypeCount());

        System.out.println();
        TreeType autumnOak = factory.getTreeType("Oak", "Orange", "oak.png");
        System.out.println("A new color creates a new type: " + (firstOak != autumnOak));
        System.out.println("Tree types after adding the orange Oak: " + factory.getTypeCount());

        System.out.println();
        System.out.println("Same type, different positions and scales:");
        new Tree(10, 20, 0.5, firstOak).draw();
        new Tree(30, 40, 2.5, firstOak).draw();
    }
}
