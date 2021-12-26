package team.unnamed.uracle.resourcepack;

import org.jetbrains.annotations.Nullable;

/**
 * Abstraction for objects that contain resource
 * pack application settings. In other words, it
 * contains the fields of the Resource Pack Send
 * packet that do not require the resource-pack
 * to be uploaded (no url, no hash)
 *
 * @since 1.0.0
 */
public interface ResourcePackApplication {

    /**
     * Determines whether the resource-pack is required to
     * join the server
     *
     * <p>If a player does not accept the server resource-pack,
     * they get kicked from the server</p>
     *
     * <p>This setting is available since Minecraft 1.17</p>
     *
     * @return True if the resource-pack is required
     * @since 1.0.0
     */
    boolean required();

    /**
     * Returns the JSON representation of the resource
     * pack prompt
     *
     * <p>Shown when they are asked to download the
     * server resource-pack</p>
     *
     * <p>This setting is available since Minecraft 1.17</p>
     *
     * @return The JSON representation of the resource
     * pack prompt
     * @since 1.0.0
     */
    @Nullable String prompt();

    /**
     * Creates a new {@link ResourcePackApplication}
     * instance from the simplest implementation,
     * with the specified values
     *
     * @param required Determines if the pack is required
     * @param prompt The resource-pack prompt
     * @return A new {@link ResourcePackApplication} instance
     * @since 1.0.0
     */
    static ResourcePackApplication of(
            boolean required,
            @Nullable String prompt
    ) {
        return new ResourcePackApplicationImpl(required, prompt);
    }

}
