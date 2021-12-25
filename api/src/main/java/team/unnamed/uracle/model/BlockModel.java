/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.model.block.BlockTexture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

/**
 * Represents the object responsible for specifying
 * a Minecraft block model
 *
 * @since 1.0.0
 */
public class BlockModel implements Model {

    public static final Key BUILTIN_GENERATED = Key.key("builtin/generated");

    private final Key parent;
    private final boolean ambientOcclusion;
    private final Map<ModelDisplay.Type, ModelDisplay> display;
    private final BlockTexture textures;
    @Unmodifiable private final List<Element> elements;

    protected BlockModel(
            Key parent,
            boolean ambientOcclusion,
            Map<ModelDisplay.Type, ModelDisplay> display,
            BlockTexture textures,
            List<Element> elements
    ) {
        requireNonNull(display, "display");
        requireNonNull(elements, "elements");
        this.parent = requireNonNull(parent, "parent");
        this.ambientOcclusion = ambientOcclusion;
        this.display = unmodifiableMap(new HashMap<>(display));
        this.textures = requireNonNull(textures, "textures");
        this.elements = unmodifiableList(new ArrayList<>(elements));
    }

    /**
     * Returns the reference to the parent
     * block model resource location
     *
     * @return The parent block model location
     */
    @Override
    public Key parent() {
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
     * Returns an unmodifiable map that holds the different
     * places where block models can be displayed
     *
     * @return The display specifications for the model
     */
    @Override
    public @Unmodifiable Map<ModelDisplay.Type, ModelDisplay> display() {
        return display;
    }

    /**
     * Returns this model textures
     *
     * @return This block model textures
     */
    public BlockTexture textures() {
        return textures;
    }

    /**
     * Returns an unmodifiable list that contains
     * all the elements of the model. They can only
     * have cubic forms. If both "parent" and "elements"
     * are set, the "elements" tag overrides the
     * "elements" tag from the previous model
     *
     * @return The model elements
     */
    @Override
    public @Unmodifiable List<Element> elements() {
        return elements;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("parent", parent),
                ExaminableProperty.of("ambientocclusion", ambientOcclusion),
                ExaminableProperty.of("display", display),
                ExaminableProperty.of("textures", textures),
                ExaminableProperty.of("elements", elements)
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
        BlockModel that = (BlockModel) o;
        return ambientOcclusion == that.ambientOcclusion
                && parent.equals(that.parent)
                && display.equals(that.display)
                && textures.equals(that.textures)
                && elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, ambientOcclusion, display, textures, elements);
    }

    public static class Builder {

        private Key parent;
        private boolean ambientOcclusion;
        private Map<ModelDisplay.Type, ModelDisplay> display = Collections.emptyMap();
        private BlockTexture textures;
        private List<Element> elements = Collections.emptyList();

        protected Builder() {
        }

        public Builder parent(Key parent) {
            this.parent = requireNonNull(parent, "parent");
            return this;
        }

        public Builder ambientOcclusion(boolean ambientOcclusion) {
            this.ambientOcclusion = ambientOcclusion;
            return this;
        }

        public Builder display(Map<ModelDisplay.Type, ModelDisplay> display) {
            this.display = requireNonNull(display, "display");
            return this;
        }

        public Builder textures(BlockTexture textures) {
            this.textures = requireNonNull(textures, "textures");
            return this;
        }

        public Builder elements(List<Element> elements) {
            this.elements = requireNonNull(elements, "elements");
            return this;
        }

        public BlockModel build() {
            return new BlockModel(
                    parent, ambientOcclusion, display, textures, elements
            );
        }

    }

}
