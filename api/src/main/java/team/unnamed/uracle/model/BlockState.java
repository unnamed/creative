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

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.model.blockstate.StateCase;
import team.unnamed.uracle.model.blockstate.StateVariant;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableListOf;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * There are several variants of some blocks (like doors, which can be
 * open or closed), hence each block has its own block state file, which
 * lists all its existing variants and links them to their corresponding
 * models
 *
 * <p>Blocks can also be compound of several models at the same time,
 * called "multipart". The models are then used depending on the block
 * states of the block</p>
 *
 * @since 1.0.0
 */
public class BlockState implements Examinable {

    @Unmodifiable private final Map<String, List<StateVariant>> variants;
    @Unmodifiable private final List<StateCase> multipart;

    private BlockState(
            Map<String, List<StateVariant>> variants,
            List<StateCase> multipart
    ) {
        requireNonNull(variants, "variants");
        requireNonNull(multipart, "multipart");
        this.variants = immutableMapOf(variants);
        this.multipart = immutableListOf(multipart);
    }

    public @Unmodifiable Map<String, List<StateVariant>> variants() {
        return variants;
    }

    public @Unmodifiable List<StateCase> multipart() {
        return multipart;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("variants", variants),
                ExaminableProperty.of("multipart", multipart)
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
        BlockState that = (BlockState) o;
        return variants.equals(that.variants)
                && multipart.equals(that.multipart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variants, multipart);
    }

    /**
     * Creates a new {@link BlockState} object from
     * the given values
     *
     * @param variants The block state variants
     * @param multipart The block state variants
     *                  (multipart)
     * @return A new {@link BlockState} instance
     * @since 1.0.0
     */
    public static BlockState of(
            Map<String, List<StateVariant>> variants,
            List<StateCase> multipart
    ) {
        return new BlockState(variants, multipart);
    }

    /**
     * Creates a new {@link BlockState} instance with
     * the given block state variants
     *
     * @param variants The block state variants
     * @return A new {@link BlockState} variants
     * @since 1.0.0
     */
    public static BlockState ofVariants(Map<String, List<StateVariant>> variants) {
        return new BlockState(
                variants,
                Collections.emptyList()
        );
    }

    /**
     * Creates a new {@link BlockState} instance from
     * the given block state multipart info
     *
     * @param multipart The block state cases to use
     * @return A new {@link BlockState} instance
     * @since 1.0.0
     */
    public static BlockState ofMultipart(List<StateCase> multipart) {
        return new BlockState(
                Collections.emptyMap(),
                multipart
        );
    }

}
