/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

/**
 * Object holding a {@link Model} textures,
 * every texture is stored in form of a {@link Key}
 * (resource location)
 *
 * @since 1.0.0
 */
public class ModelTextures implements Examinable {
    static final ModelTextures EMPTY = new ModelTextures(Collections.emptyList(), null, Collections.emptyMap());

    @Unmodifiable private final List<ModelTexture> layers;
    @Nullable private final ModelTexture particle;
    @Unmodifiable private final Map<String, ModelTexture> variables;

    private ModelTextures(
            List<ModelTexture> layers,
            @Nullable ModelTexture particle,
            Map<String, ModelTexture> variables
    ) {
        requireNonNull(layers, "layers");
        requireNonNull(variables, "variables");
        this.layers = immutableListOf(layers);
        this.particle = particle;
        this.variables = immutableMapOf(variables);
    }

    /**
     * Returns the item texture layers. Only used
     * to specify the icon of the item used in the
     * inventory.
     *
     * <p>There can be more than just one layer (e.g.
     * for spawn eggs), but the amount of possible
     * layers is hardcoded for each item.</p>
     *
     * <p>Works only in combination with
     * {@link Model#ITEM_GENERATED}</p>
     *
     * @return The item texture layers
     */
    public @Unmodifiable List<ModelTexture> layers() {
        return layers;
    }

    /**
     * Returns what texture to load particles from. Used to
     * determine the "crumb" particles generated by
     * food items, as well as to determine the barrier
     * particle (but it always uses items/barrier.png
     * as blockbreaking particle), which otherwise uses
     * the layer 0 texture
     *
     * @return The particle texture
     */
    public @Nullable ModelTexture particle() {
        return particle;
    }

    /**
     * Returns an unmodifiable map of the texture
     * variable definitions and assignations
     *
     * @return The texture variables
     */
    public @Unmodifiable Map<String, ModelTexture> variables() {
        return variables;
    }

    public @NotNull Builder toBuilder() {
        return builder()
                .layers(layers)
                .particle(particle)
                .variables(variables);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("layers", layers),
                ExaminableProperty.of("particle", particle),
                ExaminableProperty.of("variables", variables)
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
        ModelTextures that = (ModelTextures) o;
        return layers.equals(that.layers)
                && Objects.equals(particle, that.particle)
                && variables.equals(that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layers, particle, variables);
    }

    /**
     * Creates a new {@link ModelTextures} from
     * the given values
     *
     * @param layers    The texture layers
     * @param particle  The particle texture
     * @param variables The texture variables
     * @return A new {@link ModelTextures} instance
     * @since 1.0.0
     */
    public static ModelTextures of(
            List<ModelTexture> layers,
            @Nullable ModelTexture particle,
            Map<String, ModelTexture> variables
    ) {
        return new ModelTextures(layers, particle, variables);
    }

    /**
     * Static factory method for instantiating our
     * builder implementation, which eases the construction
     * of new {@link ModelTextures} instances
     *
     * @return A new {@link Builder} instance
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder implementation for {@link ModelTextures}
     * objects
     *
     * @since 1.0.0
     */
    public static class Builder {

        private List<ModelTexture> layers = null;
        private ModelTexture particle = null;
        private Map<String, ModelTexture> variables = null;

        private Builder() {
        }

        @Contract("_ -> this")
        public @NotNull Builder layers(final @NotNull List<ModelTexture> layers) {
            requireNonNull(layers, "layers");
            this.layers = new ArrayList<>(layers);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder layers(final @NotNull ModelTexture @NotNull ... layers) {
            requireNonNull(layers, "layers");
            this.layers = new ArrayList<>(Arrays.asList(layers));
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder addLayer(final @NotNull ModelTexture layer) {
            requireNonNull(layer, "layer");
            if (layers == null) {
                layers = new ArrayList<>();
            }
            layers.add(layer);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder addLayers(final @NotNull ModelTexture @NotNull ... layers) {
            requireNonNull(layers, "layers");
            if (this.layers == null) {
                this.layers = new ArrayList<>();
            }
            Collections.addAll(this.layers, layers);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder particle(final @Nullable ModelTexture particle) {
            this.particle = particle;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder variables(final @NotNull Map<String, ModelTexture> variables) {
            requireNonNull(variables, "variables");
            this.variables = new LinkedHashMap<>(variables);
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder addVariable(final @NotNull String id, final @NotNull ModelTexture texture) {
            requireNonNull(id, "id");
            requireNonNull(texture, "texture");
            if (variables == null) {
                variables = new LinkedHashMap<>();
            }
            variables.put(id, texture);
            return this;
        }

        /**
         * Finishes the building of the {@link ModelTextures}
         * instance by instantiating it using the previously
         * set values
         *
         * @return A new {@link ModelTextures} instance
         */
        @Contract("-> new")
        public @NotNull ModelTextures build() {
            return new ModelTextures(layers == null ? Collections.emptyList() : layers, particle, variables == null ? Collections.emptyMap() : variables);
        }

    }

}
