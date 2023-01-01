/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
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
package team.unnamed.creative.model;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.file.FileResource;
import team.unnamed.creative.file.ResourceWriter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents the object responsible for specifying
 * a Minecraft block model
 *
 * @since 1.0.0
 */
public class Model implements Keyed, FileResource {

    /**
     * A {@link Model} can be set to extend this key to use
     * a model that is created out of the specified icon
     */
    public static final Key ITEM_GENERATED = Key.key("item/generated");

    public static final Key ITEM_HANDHELD = Key.key("item/handheld");

    /**
     * A {@link Model} can be set to extend this key to load
     * a model from an entity file. As you cannot specify the entity,
     * this does not work for all items (only for chests, ender chests,
     * mob heads, shields, banners and tridents)
     */
    public static final Key BUILT_IN_ENTITY = Key.key("builtin/entity");

    public static final Key BUILT_IN_GENERATED = Key.key("builtin/generated");

    public static final boolean DEFAULT_AMBIENT_OCCLUSION = true;

    private final Key key;
    @Nullable private final Key parent;
    private final boolean ambientOcclusion;
    private final Map<ItemTransform.Type, ItemTransform> display;
    private final ModelTexture textures;
    @Nullable private final GuiLight guiLight;
    private final List<Element> elements;
    private final List<ItemOverride> overrides;

    protected Model(
            Key key,
            @Nullable Key parent,
            boolean ambientOcclusion,
            Map<ItemTransform.Type, ItemTransform> display,
            ModelTexture textures,
            @Nullable GuiLight guiLight,
            List<Element> elements,
            List<ItemOverride> overrides
    ) {
        this.key = requireNonNull(key, "key");
        this.parent = parent;
        this.ambientOcclusion = ambientOcclusion;
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
     * Returns the parent model of this
     * model object
     *
     * @return The parent model location
     */
    public @Nullable Key parent() {
        return parent;
    }

    /**
     * Returns the boolean that determines whether
     * to use ambient occlusion or not for this block
     *
     * @return True to use ambient occlusion
     */
    public boolean ambientOcclusion() {
        return ambientOcclusion;
    }

    /**
     * Returns a map of the different places
     * where the model can be displayed
     *
     * @return An unmodifiable map of displays
     */
    public @Unmodifiable Map<ItemTransform.Type, ItemTransform> display() {
        return display;
    }

    /**
     * Returns this model textures
     *
     * @return This model textures
     */
    public ModelTexture textures() {
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
     * Returns an unmodifiable list containing all
     * the model elements, which can only have cubic
     * forms
     *
     * @return The model elements
     */
    public @Unmodifiable List<Element> elements() {
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

    @Override
    public String path() {
        return "assets/" + key.namespace() + "/models/" + key.value() + ".json";
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.startObject();

        // parent
        if (parent != null) {
            writer.key("parent").value(parent);
        }

        // display
        if (!display.isEmpty()) {
            writer.key("display").startObject();
            for (Map.Entry<ItemTransform.Type, ItemTransform> entry : display().entrySet()) {
                ItemTransform.Type type = entry.getKey();
                ItemTransform display = entry.getValue();

                writer.key(type.name().toLowerCase(Locale.ROOT));
                display.serialize(writer);
            }
            writer.endObject();
        }

        // elements
        if (!elements.isEmpty()) {
            writer.key("elements").startArray();
            for (Element element : elements) {
                element.serialize(writer);
            }
            writer.endArray();
        }

        if (ambientOcclusion != DEFAULT_AMBIENT_OCCLUSION) {
            // only write if not default value
            writer.key("ambientocclusion").value(ambientOcclusion);
        }

        writer.key("textures");
        textures.serialize(writer);

        if (guiLight != null) {
            // only write if not default
            writer.key("gui_light").value(guiLight.name().toLowerCase(Locale.ROOT));
        }

        if (!overrides.isEmpty()) {
            writer.key("overrides").value(overrides);
        }
        writer.endObject();
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
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("parent", parent),
                ExaminableProperty.of("ambientocclusion", ambientOcclusion),
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
        Model that = (Model) o;
        return key.equals(that.key)
                && ambientOcclusion == that.ambientOcclusion
                && Objects.equals(parent, that.parent)
                && display.equals(that.display)
                && textures.equals(that.textures)
                && guiLight == that.guiLight
                && elements.equals(that.elements)
                && overrides.equals(that.overrides);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, parent, ambientOcclusion, display, textures, guiLight, elements, overrides);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key key;
        private Key parent;
        private boolean ambientOcclusion = DEFAULT_AMBIENT_OCCLUSION;
        private Map<ItemTransform.Type, ItemTransform> display = Collections.emptyMap();
        private ModelTexture textures;
        private GuiLight guiLight;
        private List<Element> elements = Collections.emptyList();
        private List<ItemOverride> overrides = Collections.emptyList();

        protected Builder() {
        }

        public Builder key(Key key) {
            this.key = key;
            return this;
        }

        public Builder parent(@Nullable Key parent) {
            this.parent = parent;
            return this;
        }

        public Builder ambientOcclusion(boolean ambientOcclusion) {
            this.ambientOcclusion = ambientOcclusion;
            return this;
        }

        public Builder display(Map<ItemTransform.Type, ItemTransform> display) {
            this.display = requireNonNull(display, "display");
            return this;
        }

        public Builder textures(ModelTexture textures) {
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

        public Builder elements(Element... elements) {
            requireNonNull(elements, "elements");
            this.elements = Arrays.asList(elements);
            return this;
        }

        public Builder overrides(List<ItemOverride> overrides) {
            this.overrides = requireNonNull(overrides, "overrides");
            return this;
        }

        public Builder overrides(ItemOverride... overrides) {
            requireNonNull(overrides, "overrides");
            this.overrides = Arrays.asList(overrides);
            return this;
        }

        public Model build() {
            return new Model(
                    key, parent, ambientOcclusion, display,
                    textures, guiLight, elements, overrides
            );
        }

    }

}
