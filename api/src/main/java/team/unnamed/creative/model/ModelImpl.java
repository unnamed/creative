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
import net.kyori.adventure.util.TriState;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class ModelImpl implements Model {
    private final Key key;
    private final Key parent;
    private final TriState ambientOcclusion;
    private final Map<ItemTransform.Type, ItemTransform> display;
    private final ModelTextures textures;
    private final GuiLight guiLight;
    private final List<Element> elements;
    private final List<ItemOverride> overrides;

    ModelImpl(
            final @NotNull Key key,
            final @Nullable Key parent,
            final @NotNull TriState ambientOcclusion,
            final @NotNull Map<ItemTransform.Type, ItemTransform> display,
            final @NotNull ModelTextures textures,
            final @Nullable GuiLight guiLight,
            final @NotNull List<Element> elements,
            final @NotNull List<ItemOverride> overrides
    ) {
        this.key = requireNonNull(key, "key");
        this.parent = parent;
        this.ambientOcclusion = requireNonNull(ambientOcclusion, "ambientOcclusion");
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

    @Override
    public @Nullable Key parent() {
        return parent;
    }

    @Override
    public @NotNull TriState ambientOcclusion() {
        return ambientOcclusion;
    }

    @Override
    public @NotNull @Unmodifiable Map<ItemTransform.Type, ItemTransform> display() {
        return display;
    }

    @Override
    public @NotNull ModelTextures textures() {
        return textures;
    }

    @Override
    public @Nullable GuiLight guiLight() {
        return guiLight;
    }

    @Override
    public @NotNull @Unmodifiable List<Element> elements() {
        return elements;
    }

    @Override
    public @NotNull List<ItemOverride> overrides() {
        return overrides;
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
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ModelImpl that = (ModelImpl) o;
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

    static final class BuilderImpl implements Builder {
        private Key key;
        private Key parent;
        private TriState ambientOcclusion = TriState.NOT_SET;
        private Map<ItemTransform.Type, ItemTransform> display = new HashMap<>();
        private ModelTextures textures = ModelTextures.EMPTY;
        private GuiLight guiLight;
        private List<Element> elements = new ArrayList<>();
        private List<ItemOverride> overrides = new ArrayList<>();

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder parent(final @Nullable Key parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public @NotNull Builder ambientOcclusion(final @NotNull TriState ambientOcclusion) {
            this.ambientOcclusion = requireNonNull(ambientOcclusion, "ambientOcclusion");
            return this;
        }

        @Override
        public @NotNull Builder display(final @NotNull Map<ItemTransform.Type, ItemTransform> display) {
            requireNonNull(display, "display");
            this.display = new HashMap<>(display);
            return this;
        }

        @Override
        public @NotNull Builder textures(final @NotNull ModelTextures textures) {
            this.textures = requireNonNull(textures, "textures");
            return this;
        }

        @Override
        public @NotNull Builder guiLight(final @Nullable GuiLight guiLight) {
            this.guiLight = guiLight;
            return this;
        }

        @Override
        public @NotNull Builder elements(final @NotNull List<Element> elements) {
            requireNonNull(elements, "elements");
            this.elements = new ArrayList<>(elements);
            return this;
        }

        @Override
        public @NotNull Builder addElement(final @NotNull Element element) {
            requireNonNull(element, "element");
            elements.add(element);
            return this;
        }

        @Override
        public @NotNull Builder overrides(final @NotNull List<ItemOverride> overrides) {
            requireNonNull(overrides, "overrides");
            this.overrides = new ArrayList<>(overrides);
            return this;
        }

        @Override
        public @NotNull Builder addOverride(final @NotNull ItemOverride override) {
            requireNonNull(override, "override");
            overrides.add(override);
            return this;
        }

        @Override
        public @NotNull Model build() {
            return new ModelImpl(key, parent, ambientOcclusion, display, textures, guiLight, elements, overrides);
        }
    }
}
