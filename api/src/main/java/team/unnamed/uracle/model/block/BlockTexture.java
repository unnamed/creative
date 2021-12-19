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
package team.unnamed.uracle.model.block;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

/**
 * Represents a {@link BlockModel} textures
 * object, holds the model textures and texture
 * variables
 *
 * @since 1.0.0
 */
public class BlockTexture implements Examinable {

    /**
     * What texture to load particles from. This
     * texture is used if you are in a nether portal.
     * Also used for water and lava's still textures.
     *
     * <p>All breaking particles from non-model blocks
     * are hard-coded</p>
     */
    @Nullable private final Key particle;

    /**
     * Map of texture variable definitions and texture
     * assignations
     */
    @Unmodifiable private final Map<String, Key> variables;

    private BlockTexture(
            @Nullable Key particle,
            Map<String, Key> variables
    ) {
        requireNonNull(variables, "variables");
        this.particle = particle;
        this.variables = unmodifiableMap(new HashMap<>(variables));
    }

    /**
     * Returns the texture location for the
     * block particles
     *
     * @return The particle location
     */
    public @Nullable Key particle() {
        return particle;
    }

    /**
     * Returns the map of variable definitions
     * and textures
     *
     * @return An unmodifiable map of texture variables
     */
    public @Unmodifiable Map<String, Key> variables() {
        return variables;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
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
        BlockTexture that = (BlockTexture) o;
        return Objects.equals(particle, that.particle)
                && variables.equals(that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(particle, variables);
    }

    /**
     * Creates a new {@link BlockTexture} from
     * the given particle texture and texture
     * variables
     *
     * @param particle The particle texture location
     * @param variables The texture variables
     * @return A new {@link BlockTexture} instance
     */
    public static BlockTexture of(
            Key particle,
            Map<String, Key> variables
    ) {
        requireNonNull(particle, "particle");
        return new BlockTexture(particle, variables);
    }

    /**
     * Creates a new {@link BlockTexture} from
     * the given texture variables. No particle
     * texture is set
     *
     * @param variables The texture variables
     * @return A new {@link BlockTexture} instance
     */
    public static BlockTexture of(Map<String, Key> variables) {
        return new BlockTexture(null, variables);
    }

}
