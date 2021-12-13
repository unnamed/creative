package team.unnamed.uracle.resourcepack;

/**
 * Represents the object responsible for providing
 * {@link ResourcePack} from simple {@link UrlAndHash}
 *
 * <p>Implementations may take necessary properties
 * from other sources like configurations</p>
 */
@FunctionalInterface
public interface ResourcePackProvider {

    /**
     * Creates a {@link ResourcePack} from the given
     * {@code location}, all other extra {@link ResourcePack}
     * properties are arbitrarily obtained by implementation
     *
     * <p>This method is responsible for "filling" an incomplete
     * resource pack that only has a location</p>
     *
     * @param location The resource pack location
     * @return The resource pack
     */
    ResourcePack of(UrlAndHash location);

}
