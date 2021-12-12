package team.unnamed.uracle;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Immutable representation of a Minecraft Resource Pack's
 * resource location, composed by an optional namespace and
 * a path.
 *
 * <p>Resource locations (also known as namespaced IDs,
 * namespaced identifiers, resource identifiers, or
 * namespaced strings) are a way to declare and identify
 * built-in and user-defined game objects in Minecraft without
 * potential ambiguity or conflict</p>
 *
 * <p>See <a href="https://minecraft.fandom.com/wiki/Resource_location">
 * Resource Location</a> from Minecraft Wiki</p>
 *
 * @since 1.0.0
 */
public class ResourceLocation {

    /**
     * Default and Minecraft-reserved namespace, when a
     * resource location doesn't specify one, it falls
     * back to 'minecraft'
     */
    public static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Represents this resource location namespace,
     * namespaces are a way to identify resources
     * and avoiding conflicts
     *
     * <p>Most things in the game has a namespace, so that
     * if we add something and a mod (or map, or whatever)
     * adds something, they're both different somethings.
     * Whenever you're asked to name something, for example
     * a loot table, you're expected to also provide what
     * namespace that thing comes from. If you don't specify
     * - Dinnerbone on namespaces</p>
     */
    @Nullable private final String namespace;

    /**
     * Represents this resource location path inside
     * the namespace folder and, depending on the context,
     * may be inside other folders
     */
    private final String path;

    private ResourceLocation(
            @Nullable String namespace,
            String path
    ) {
        this.namespace = namespace;
        this.path = requireNonNull(path, "path");
    }

    /**
     * Returns this resource location namespace, which
     * may be null
     * @return This resource location namespace
     */
    public @Nullable String getNamespace() {
        return namespace;
    }

    /**
     * Returns this resource location path, it is never
     * null
     * @return This resource location path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns this resource location represented as a
     * string, separating the namespace and path by a
     * colon if there is a namespace
     *
     * @return This resource location string representation
     */
    @Override
    public String toString() {
        return namespace == null
                ? path
                : (namespace + ':' + path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLocation that = (ResourceLocation) o;
        return Objects.equals(namespace, that.namespace)
                && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    /**
     * Creates a new {@link ResourceLocation} instance that
     * has a namespace and path
     *
     * @param namespace The resource location namespace,
     *                  it cannot be null
     * @param path The resource location path
     * @return A new {@link ResourceLocation} instance
     */
    public static ResourceLocation of(String namespace, String path) {
        return new ResourceLocation(
                requireNonNull(namespace, "namespace"),
                path
        );
    }

    /**
     * Creates a new {@link ResourceLocation} instance that
     * only has a path, it falls back to the default Minecraft's
     * namespace ({@link ResourceLocation#DEFAULT_NAMESPACE})
     *
     * <p>This method creates a {@link ResourceLocation} that
     * has a {@code null} namespace, not a default namespace</p>
     *
     * @param path The resource location path
     * @return A new {@link ResourceLocation} instance
     */
    public static ResourceLocation of(String path) {
        return new ResourceLocation(null, path);
    }

}
