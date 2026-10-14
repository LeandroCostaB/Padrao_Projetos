import java.util.HashMap;
import java.util.Map;

public class TreeTypeFactory {

    private record TreeTypeKey(String name, String color, String textureId) {
    }

    private static final int TEXTURE_SIZE = 100_000;

    private final Map<TreeTypeKey, TreeType> cache = new HashMap<>();

    public TreeType getTreeType(String name, String color, String textureId) {
        TreeTypeKey key = new TreeTypeKey(name, color, textureId);

        return cache.computeIfAbsent(key,
                missing -> new TreeType(missing.name(), missing.color(), loadTexture(missing.textureId())));
    }

    public int getTypeCount() {
        return cache.size();
    }

    private byte[] loadTexture(String textureId) {
        System.out.println("Loading texture: " + textureId + " (" + TEXTURE_SIZE + " bytes)");
        return new byte[TEXTURE_SIZE];
    }
}
