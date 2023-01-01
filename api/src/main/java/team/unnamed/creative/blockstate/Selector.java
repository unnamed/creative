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
package team.unnamed.creative.blockstate;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.file.SerializableResource;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Can be used instead of direct {@link Variant} to
 * combine models based on block state attributes
 *
 * <p>Contains a set of conditions that must be met to use
 * the {@code variant} model</p>
 *
 * @since 1.0.0
 */
public class Selector implements SerializableResource {

    private final Condition condition;
    @Unmodifiable private final MultiVariant variant;

    private Selector(
            Condition condition,
            MultiVariant variant
    ) {
        this.condition = requireNonNull(condition, "condition");
        this.variant = requireNonNull(variant, "variant");
    }

    public Condition condition() {
        return condition;
    }

    public @Unmodifiable MultiVariant variant() {
        return variant;
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.startObject();

        if (condition != Condition.NONE) {
            writer.key("when").startObject();
            condition.serialize(writer, true);
            writer.endObject();
        }

        writer.key("apply").value(variant);
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("condition", condition),
                ExaminableProperty.of("variant", variant)
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
        Selector selector = (Selector) o;
        return condition.equals(selector.condition)
                && variant.equals(selector.variant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, variant);
    }

    public static Selector of(Condition condition, MultiVariant variant) {
        return new Selector(condition, variant);
    }

    public static Selector of(MultiVariant variant) {
        return new Selector(Condition.NONE, variant);
    }

}
