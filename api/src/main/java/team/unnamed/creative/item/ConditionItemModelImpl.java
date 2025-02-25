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
package team.unnamed.creative.item;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.item.condition.ItemCondition;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class ConditionItemModelImpl implements ConditionItemModel {
    private final ItemCondition condition;
    private final ItemModel onTrue;
    private final ItemModel onFalse;

    ConditionItemModelImpl(final @NotNull ItemCondition condition, final @NotNull ItemModel onTrue, final @NotNull ItemModel onFalse) {
        this.condition = requireNonNull(condition, "condition");
        this.onTrue = requireNonNull(onTrue, "onTrue");
        this.onFalse = requireNonNull(onFalse, "onFalse");
    }

    @Override
    public @NotNull ItemCondition condition() {
        return condition;
    }

    @Override
    public @NotNull ItemModel onTrue() {
        return onTrue;
    }

    @Override
    public @NotNull ItemModel onFalse() {
        return onFalse;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
            ExaminableProperty.of("condition", condition),
            ExaminableProperty.of("onTrue", onTrue),
            ExaminableProperty.of("onFalse", onFalse)
        );
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final ConditionItemModelImpl that = (ConditionItemModelImpl) o;
        return condition.equals(that.condition) && onTrue.equals(that.onTrue) && onFalse.equals(that.onFalse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, onTrue, onFalse);
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }
}
