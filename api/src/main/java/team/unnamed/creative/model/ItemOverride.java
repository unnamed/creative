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
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

/**
 * Represents an item model override, which determines cases in
 * which a different model should be used based on item tags. All
 * cases are evaluated in order from top to bottom and last predicate
 * that matches overrides.
 *
 * <p>Overrides are ignored if it has been already overridden once,
 * for example this avoids recursion on overriding to the same model</p>
 *
 * @sincePackFormat 2
 * @sinceMinecraft 1.9
 * @since 1.0.0
 */
public class ItemOverride implements Examinable {

    private final Key model;
    @Unmodifiable private final List<ItemPredicate> predicate;

    private ItemOverride(
            Key model,
            List<ItemPredicate> predicate
    ) {
        requireNonNull(model, "model");
        requireNonNull(predicate, "predicate");
        this.model = model;
        this.predicate = immutableListOf(predicate);
    }

    /**
     * Returns the resource location of the new
     * item model if the case is met
     *
     * @return The item override model
     */
    public Key model() {
        return model;
    }

    /**
     * Returns an unmodifiable list of the cases
     * where this item model override should be
     * used
     *
     * @return The item override predicate
     */
    public @Unmodifiable List<ItemPredicate> predicate() {
        return predicate;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("predicate", predicate),
                ExaminableProperty.of("model", model)
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
        ItemOverride that = (ItemOverride) o;
        return predicate.equals(that.predicate)
                && model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicate, model);
    }

    /**
     * Creates a new {@link ItemOverride} instance
     * from the given values
     *
     * @param model      The new item model
     * @param predicates The item override predicates
     * @return A new {@link ItemOverride} instance
     * @since 1.0.0
     */
    public static ItemOverride of(
            Key model,
            List<ItemPredicate> predicates
    ) {
        return new ItemOverride(model, predicates);
    }

    /**
     * Creates a new {@link ItemOverride} instance
     * from the given values
     *
     * @param model      The new item model
     * @param predicates The item override predicates
     * @return A new {@link ItemOverride} instance
     * @since 1.0.0
     */
    public static ItemOverride of(
            Key model,
            ItemPredicate... predicates
    ) {
        return new ItemOverride(model, Arrays.asList(predicates));
    }

}
