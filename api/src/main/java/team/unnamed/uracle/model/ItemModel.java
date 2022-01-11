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
import team.unnamed.uracle.model.item.ItemOverride;
import team.unnamed.uracle.model.item.ItemTexture;
import team.unnamed.uracle.serialize.AssetWriter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents the object responsible for specifying
 * a Minecraft item model
 *
 * @since 1.0.0
 */
public class ItemModel
        extends AbstractModel
        implements Model  {

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

    private final Key key;
    @Nullable private final Key parent;
    private final Map<ModelDisplay.Type, ModelDisplay> display;
    private final ItemTexture textures;
    @Nullable private final GuiLight guiLight;
    private final List<Element> elements;
    private final List<ItemOverride> overrides;

    private ItemModel(
            Key key,
            @Nullable Key parent,
            Map<ModelDisplay.Type, ModelDisplay> display,
            ItemTexture textures,
            @Nullable GuiLight guiLight,
            List<Element> elements,
            List<ItemOverride> overrides
    ) {
        this.key = requireNonNull(key, "key");
        this.parent = parent;
        this.display = requireNonNull(display, "display");
        this.textures = requireNonNull(textures, "textures");
        this.guiLight = guiLight;
        this.elements = requireNonNull(elements, "elements");
        this.overrides = requireNonNull(overrides, "oveerrides");
    }

    @Override
    public @NotNull Key key() {
        return key;
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
    public @Nullable Key parent() {
        return parent;
    }

    /**
     * Returns a map that holds the different places
     * where item models are displayed
     *
     * @return The model displays
     */
    @Override
    public Map<ModelDisplay.Type, ModelDisplay> display() {
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
     * Returns a list that contains all the
     * cubic elements for this model
     *
     * @return This model elements
     */
    @Override
    public List<Element> elements() {
        return elements;
    }

    /**
     * Returns a list of item overrides, item overrides determine cases
     * in which a different model should be used based on item tags
     *
     * <p>All cases are evaluated in order from top to bottom and
     * last predicate that matches overrides. However, overrides are ignored
     * if it has been already overridden once, for example this avoids recursion
     * on overriding to the same model</p>
     *
     * @return This item model overrides
     */
    public List<ItemOverride> overrides() {
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
    public String path() {
        return "assets/" + key.namespace() + "/models/" + key.value() + ".json";
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

        writer.key("overrides").value(overrides);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
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
        return key.equals(itemModel.key)
                && Objects.equals(parent, itemModel.parent)
                && display.equals(itemModel.display)
                && textures.equals(itemModel.textures)
                && guiLight == itemModel.guiLight
                && elements.equals(itemModel.elements)
                && overrides.equals(itemModel.overrides);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, parent, display, textures, guiLight, elements, overrides);
    }

    /**
     * Builder implementation for {@link ItemModel}
     * objects
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Key key;
        private Key parent;
        private Map<ModelDisplay.Type, ModelDisplay> display = Collections.emptyMap();
        private ItemTexture textures;
        private GuiLight guiLight = GuiLight.SIDE;
        private List<Element> elements = Collections.emptyList();
        private List<ItemOverride> overrides = Collections.emptyList();

        protected Builder() {
        }

        public Builder key(Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        public Builder parent(@Nullable Key parent) {
            this.parent = parent;
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
            return new ItemModel(key, parent, display, textures, guiLight, elements, overrides);
        }

    }

}
