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
package team.unnamed.creative.metadata.gui;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

final class GuiBorderImpl implements GuiBorder {
    private final int top;
    private final int bottom;
    private final int left;
    private final int right;

    GuiBorderImpl(final int top, final int bottom, final int left, final int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        validate();
    }

    private void validate() {
        if (top < 0)
            throw new IllegalArgumentException("top border size must be greater than or equal to 0, got " + top);
        if (bottom < 0)
            throw new IllegalArgumentException("bottom border size must be greater than or equal to 0, got " + bottom);
        if (left < 0)
            throw new IllegalArgumentException("left border size must be greater than or equal to 0, got " + left);
        if (right < 0)
            throw new IllegalArgumentException("right border size must be greater than or equal to 0, got " + right);
    }

    @Override
    public int top() {
        return top;
    }

    @Override
    public int bottom() {
        return bottom;
    }

    @Override
    public int left() {
        return left;
    }

    @Override
    public int right() {
        return right;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("top", top),
                ExaminableProperty.of("bottom", bottom),
                ExaminableProperty.of("left", left),
                ExaminableProperty.of("right", right)
        );
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiBorderImpl)) return false;
        final GuiBorderImpl guiBorder = (GuiBorderImpl) o;
        return top == guiBorder.top && bottom == guiBorder.bottom && left == guiBorder.left && right == guiBorder.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(top, bottom, left, right);
    }
}
