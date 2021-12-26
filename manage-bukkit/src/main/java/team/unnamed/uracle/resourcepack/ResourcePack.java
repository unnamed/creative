package team.unnamed.uracle.resourcepack;

import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a Minecraft resource-pack,
 * it should always represent the newest resource-pack
 * across the Minecraft versions
 *
 * @author yusshu (Andre Roldan)
 */
public final class ResourcePack
        implements ResourcePackLocation, ResourcePackApplication {

    private final String url;
    private final String hash;
    private final boolean required;
    @Nullable private final String prompt;

    public ResourcePack(
            String url,
            String hash,
            boolean required,
            @Nullable String prompt
    ) {
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.prompt = prompt;
    }

    /**
     * Returns the URL sent to players to download the
     * server resource-pack
     */
    @Override
    public String url() {
        return url;
    }

    /**
     * Returns the SHA-1 hash of the server resource-pack
     * contents
     */
    @Override
    public String hash() {
        return hash;
    }

    /**
     * Determines if the resource-pack MUST be applied
     * for all players that join the server. They can't
     * join if they do not accept the resource-pack
     *
     * <p>This setting is available since Minecraft 1.17</p>
     *
     * @return True if this resource pack is required
     */
    @Override
    public boolean required() {
        return required;
    }

    /**
     * Returns the JSON representation of the resource
     * pack prompt
     *
     * <p>This setting is available since Minecraft 1.17</p>
     *
     * @return The JSON representation of the resource
     * pack prompt
     */
    @Override
    public @Nullable String prompt() {
        return prompt;
    }

    /**
     * Creates a new {@link ResourcePack} with the given
     * remote location
     *
     * @param location The resource-pack remote location
     * @return A copy of this resource-pack, with the new,
     * provided location
     */
    public ResourcePack withLocation(ResourcePackLocation location) {
        return new ResourcePack(
                location.url(),
                location.hash(),
                required,
                prompt
        );
    }

}