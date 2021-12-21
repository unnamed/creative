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
package team.unnamed.uracle.model.item;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * @since 1.0.0
 */
public class ItemOverride {

    /**
     * Holds the cases to determine whether an
     * item model should be overridden
     */
    @Unmodifiable private final List<ItemPredicate> predicate;

    /**
     * Model key for the new item
     */
    private final Key model;

    private ItemOverride(
            List<ItemPredicate> predicate,
            Key model
    ) {
        requireNonNull(predicate, "predicate");
        this.predicate = unmodifiableList(new ArrayList<>(predicate));
        this.model = requireNonNull(model, "model");
    }

    public @Unmodifiable List<ItemPredicate> predicate() {
        return predicate;
    }

    public Key model() {
        return model;
    }

    /**
     * Creates a new {@link ItemOverride} instance
     * from the given values
     *
     * @param predicate The item override predicate
     * @param model The new item model
     * @return A new {@link ItemOverride} instance
     * @since 1.0.0
     */
    public static ItemOverride of(
            List<ItemPredicate> predicate,
            Key model
    ) {
        return new ItemOverride(predicate, model);
    }

}
