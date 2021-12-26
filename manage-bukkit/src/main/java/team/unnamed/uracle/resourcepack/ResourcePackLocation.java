package team.unnamed.uracle.resourcepack;

/**
 * Abstraction for objects that contain a
 * resource-pack location (URL and hash)
 *
 * <p>This interface properties complement
 * {@link ResourcePackApplication} to build
 * a complete Resource Pack Send packet</p>
 *
 * See https://wiki.vg/Protocol#Resource_Pack_Send
 *
 * @since 1.0.0
 */
public interface ResourcePackLocation {

    /**
     * Returns the resource pack universal
     * resource location
     *
     * @return The resource-pack URL
     * @since 1.0.0
     */
    String url();

    /**
     * Returns the SHA-1 hash of the resource
     * pack.
     *
     * <p>If the downloaded resource-pack hash
     * and this hash doesn't match, the client
     * rejects the resource-pack</p>
     *
     * <p>Must be lower-case in order to work</p>
     *
     * @return The resource-pack SHA-1 hash
     * @since 1.0.0
     */
    String hash();

    /**
     * Creates a new {@link ResourcePackLocation}
     * instance from the simplest implementation,
     * with the specified values
     *
     * @param url The resource-pack URL
     * @param hash The resource-pack SHA-1 hash
     * @return A new {@link ResourcePackLocation} instance
     * @since 1.0.0
     */
    static ResourcePackLocation of(String url, String hash) {
        return new ResourcePackLocationImpl(url, hash);
    }

}
