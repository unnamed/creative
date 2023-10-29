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

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class NineSliceGuiScalingImpl implements NineSliceGuiScaling {
    private final int width;
    private final int height;
    private final GuiBorder border;

    NineSliceGuiScalingImpl(final int width, final int height, final @NotNull GuiBorder border) {
        this.width = width;
        this.height = height;
        this.border = requireNonNull(border, "border");
        validate();
    }

    private void validate() {
        if (width <= 0)
            throw new IllegalArgumentException("Width must be positive! Got " + width);
        if (height <= 0)
            throw new IllegalArgumentException("Height must be positive! Got " + height);
        if (border.left() + border.right() >= width)
            throw new IllegalArgumentException("Horizontal borders too big: " + border.left() + " + " + border.right() + " >= " + width);
        if (border.top() + border.bottom() >= height) {
            throw new IllegalArgumentException("Vertical borders too big: " + border.top() + " + " + border.bottom() + " >= " + height);
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public @NotNull GuiBorder border() {
        return border;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("width", width),
                ExaminableProperty.of("height", height),
                ExaminableProperty.of("border", border)
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
        final NineSliceGuiScalingImpl that = (NineSliceGuiScalingImpl) o;
        if (width != that.width) return false;
        if (height != that.height) return false;
        return border.equals(that.border);
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + border.hashCode();
        return result;
    }
}
