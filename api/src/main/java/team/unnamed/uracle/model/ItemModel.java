/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.uracle.model;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.model.item.ItemOverride;
import team.unnamed.uracle.model.item.ItemPredicate;
import team.unnamed.uracle.model.item.ItemTexture;
import team.unnamed.uracle.serialize.AssetWriter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableListOf;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * Represents the object responsible for specifying
 * a Minecraft item model
 *
 * @since 1.0.0
 */
public class ItemModel extends AbstractModel implements Model  {

    /**
     * An {@link ItemModel} can be set to extend this key to use
     * a model that is created out of the specified icon
     */
    public static final Key ITEM_GENERATED = Key.key("item/generated");

    /**
     * An {@link ItemModel} can be set to extend this key to load
     * a model from an entity file. As you cannot specify the entity,
     * this does not work for all items (only for chests, ender chests,
     * mob heads, shields, banners and tridents)
     */
    public static final Key BUILT_IN_ENTITY = Key.key("builtin/entity");

    private final Key parent;
    @Unmodifiable private final Map<ModelDisplay.Type, ModelDisplay> display;
    private final ItemTexture textures;
    @Nullable private final GuiLight guiLight;
    @Unmodifiable private final List<Element> elements;
    @Unmodifiable private final List<ItemOverride> overrides;

    private ItemModel(
            Key parent,
            Map<ModelDisplay.Type, ModelDisplay> display,
            ItemTexture textures,
            @Nullable GuiLight guiLight,
            List<Element> elements,
            List<ItemOverride> overrides
    ) {
        requireNonNull(parent, "parent");
        requireNonNull(display, "display");
        requireNonNull(textures, "textures");
        requireNonNull(elements, "elements");
        requireNonNull(overrides, "overrides");
        this.parent = parent;
        this.display = immutableMapOf(display);
        this.textures = textures;
        this.guiLight = guiLight;
        this.elements = immutableListOf(elements);
        this.overrides = immutableListOf(overrides);
    }

    /**
     * Returns the parent item model of this item model
     * in form os a resource location
     *
     * <p>If both "parent" and "elements" are set, the "elements"
     * tag overrides the "elements" tag from the previous model.</p>
     *
     * <p>Can be set to "item/generated" to use a model that is
     * created out of the specified icon</p>
     *
     * <p>Can be set to "builtin/entity" to load a model from
     * an entity file. As you cannot specify the entity, this
     * doesn't work for all items (only for chests, ender chests,
     * mob heads, shields, banners and tridents</p>
     *
     * @return The parent item model location
     */
    @Override
    public Key parent() {
        return parent;
    }

    /**
     * Returns an unmodifiable map that holds the different places
     * where item models are displayed
     *
     * @return The model displays
     */
    @Override
    public @Unmodifiable Map<ModelDisplay.Type, ModelDisplay> display() {
        return display;
    }

    /**
     * Returns the textures that this item model instance
     * uses
     *
     * @return This item model textures
     */
    public ItemTexture textures() {
        return textures;
    }

    /**
     * Returns the way how the item is rendered, can be "side"
     * or "front"
     *
     * <p>If set to "side", the model is rendered like a block.
     * If set to "front", model is shaded like a flat item</p>
     *
     * @return Value that determines how to render this item
     */
    public @Nullable GuiLight guiLight() {
        return guiLight;
    }

    /**
     * Returns an unmodifiable list that contains all the
     * cubic elements for this model
     *
     * @return This model elements
     */
    @Override
    public @Unmodifiable List<Element> elements() {
        return elements;
    }

    /**
     * Returns an unmodifiable list of item overrides, item overrides
     * determine cases in which a different model should be used based on
     * item tags
     *
     * <p>All cases are evaluated in order from top to bottom and
     * last predicate that matches overrides. However, overrides are ignored
     * if it has been already overridden once, for example this avoids recursion
     * on overriding to the same model</p>
     *
     * @return This item model overrides
     */
    public @Unmodifiable List<ItemOverride> overrides() {
        return overrides;
    }

    /**
     * Enum of possible "gui_light" property
     * values
     *
     * @since 1.0.0
     */
    public enum GuiLight {
        FRONT,
        SIDE
    }

    @Override
    protected void serializeOwnProperties(AssetWriter writer) {
        // textures
        writer.key("textures");
        textures.serialize(writer);

        if (guiLight != ItemModel.GuiLight.SIDE) {
            // only write if not default
            writer.key("gui_light").value(guiLight.name().toLowerCase(Locale.ROOT));
        }

        // overrides
        writer.key("overrides").startArray();
        for (ItemOverride override : overrides) {
            override.serialize(writer);
        }
        writer.endArray();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("parent", parent),
                ExaminableProperty.of("display", display),
                ExaminableProperty.of("textures", textures),
                ExaminableProperty.of("guiLight", guiLight),
                ExaminableProperty.of("elements", elements),
                ExaminableProperty.of("overrides", overrides)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemModel itemModel = (ItemModel) o;
        return parent.equals(itemModel.parent)
                && display.equals(itemModel.display)
                && textures.equals(itemModel.textures)
                && guiLight == itemModel.guiLight
                && elements.equals(itemModel.elements)
                && overrides.equals(itemModel.overrides);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, display, textures, guiLight, elements, overrides);
    }

    /**
     * Builder implementation for {@link ItemModel}
     * objects
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Key parent;
        private Map<ModelDisplay.Type, ModelDisplay> display = Collections.emptyMap();
        private ItemTexture textures;
        private GuiLight guiLight = GuiLight.SIDE;
        private List<Element> elements = Collections.emptyList();
        private List<ItemOverride> overrides = Collections.emptyList();

        protected Builder() {
        }

        public Builder parent(Key parent) {
            this.parent = requireNonNull(parent, "parent");
            return this;
        }

        public Builder display(Map<ModelDisplay.Type, ModelDisplay> display) {
            this.display = requireNonNull(display, "display");
            return this;
        }

        public Builder textures(ItemTexture textures) {
            this.textures = requireNonNull(textures, "textures");
            return this;
        }

        public Builder guiLight(@Nullable GuiLight guiLight) {
            this.guiLight = guiLight;
            return this;
        }

        public Builder elements(List<Element> elements) {
            this.elements = requireNonNull(elements, "elements");
            return this;
        }

        public Builder overrides(List<ItemOverride> overrides) {
            this.overrides = requireNonNull(overrides, "overrides");
            return this;
        }

        /**
         * Finishes the construction of a {@link ItemModel}
         * instance by instantiating it with the previously
         * set values
         *
         * @return A new {@link ItemModel} instance
         */
        public ItemModel build() {
            return new ItemModel(parent, display, textures, guiLight, elements, overrides);
        }

    }

}
