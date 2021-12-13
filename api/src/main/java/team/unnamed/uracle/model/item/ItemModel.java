package team.unnamed.uracle.model.item;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.model.Model;

import java.util.List;
import java.util.Map;

public class ItemModel implements Model {

    public static final Key ITEM_GENERATED = Key.key("item/generated");
    public static final Key BUILT_IN_ENTITY = Key.key("builtin/entity");

    private final Key location;

    /**
     * Loads a different model from the given path, in form
     * of a resource location. If both "parent" and "elements"
     * are set, the "elements" tag overrides the "elements" tag
     * from the previous model.
     *
     * <p>Can be set to "item/generated" to use a model that is
     * created out of the specified icon</p>
     *
     * <p>Can be set to "builtin/entity" to load a model from
     * an entity file. As you cannot specify the entity, this
     * doesn't work for all items (only for chests, ender chests,
     * mob heads, shields, banners and tridents</p>
     */
    private final Key parent;

    /**
     * Holds the different places where item models are displayed
     */
    private final Map<ItemDisplay.Type, ItemDisplay> display;

    private final ItemTexture textures;

    /**
     * Can be {@link GuiLight#FRONT} or {@link GuiLight#SIDE}. If set
     * to "side", the model is rendered like a block. If set to "front",
     * model is shaded like a flat item. Defaults to "side"
     */
    @Nullable private final GuiLight guiLight;

    /**
     * Determines cases in which a different model should be used based on
     * item tags. All cases are evaluated in order from top to bottom and
     * last predicate that matches overrides. However, overrides are ignored
     * if it has been already overridden once, for example this avoids recursion
     * on overriding to the same model.
     */
    private final List<ItemOverride> overrides;


    public enum GuiLight {
        FRONT,
        SIDE
    }

}
