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
package team.unnamed.uracle.blockstate;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.serialize.AssetWriter;
import team.unnamed.uracle.serialize.SerializableResource;

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
public class BlockState implements SerializableResource, Examinable {

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

    private static void writeVariant(StateVariant variant, AssetWriter writer, boolean writeWeight) {
        writer
                .startObject()
                .key("model").value(variant.model())
                .key("x").value(variant.x())
                .key("y").value(variant.y())
                .key("uvlock").value(variant.uvLock());
        if (writeWeight) {
            writer.key("weight").value(variant.weight());
        }
        writer.endObject();
    }

    private static void writeVariant(List<StateVariant> variant, AssetWriter writer) {
        if (variant.size() == 1) {
            // single variant, write as an object
            // without the weight
            writeVariant(variant.get(0), writer, false);
        } else {
            // multiple variants, write everything
            writer.startArray();
            for (StateVariant v : variant) {
                writeVariant(v, writer, true);
            }
            writer.endArray();
        }
    }

    @Override
    public void serialize(AssetWriter writer) {

        writer.startObject();

        // write "variants" part if not empty
        if (!variants.isEmpty()) {
            writer.key("variants").startObject();
            for (Map.Entry<String, List<StateVariant>> entry : variants.entrySet()) {
                writer.key(entry.getKey());
                writeVariant(entry.getValue(), writer);
            }
            writer.endObject();
        }

        // write "multipart" part if not empty
        if (!multipart.isEmpty()) {
            writer.key("multipart").startArray();
            for (StateCase stateCase : multipart) {
                writer.startObject()
                        .key("when")
                        .startObject();

                StateCase.When when = stateCase.when();
                List<StateCase.Filter> filters = when.or();

                if (!filters.isEmpty()) {
                    // write "OR" cases if not empty
                    writer.key("or").startArray();
                    for (StateCase.Filter filter : filters) {
                        writer.startObject();
                        for (Map.Entry<String, String> condition : filter.state().entrySet()) {
                            writer.key(condition.getKey()).value(condition.getValue());
                        }
                        writer.endObject();
                    }
                    writer.endArray();
                }

                for (Map.Entry<String, String> condition : when.state().entrySet()) {
                    writer.key(condition.getKey()).value(condition.getValue());
                }
                writer.endObject();

                writer.key("apply");
                writeVariant(stateCase.apply(), writer);
                writer.endObject();
            }
            writer.endArray();
        }

        writer.endObject();
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
