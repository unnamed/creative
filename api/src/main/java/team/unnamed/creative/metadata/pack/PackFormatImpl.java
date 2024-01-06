/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
package team.unnamed.creative.metadata.pack;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

final class PackFormatImpl implements PackFormat {

    private final int format;
    private final int min;
    private final int max;

    PackFormatImpl(final int format, final int min, final int max) {
        this.format = format;
        this.min = min;
        this.max = max;

        // validate arguments
        if (min > max)
            throw new IllegalArgumentException("Minimum " + min + " is greater than maximum " + max);
        if (!isInRange(format))
            throw new IllegalArgumentException("Format " + format + " is not in the range [" + min + ", " + max + "]");
    }

    @Override
    public int format() {
        return format;
    }

    @Override
    public int min() {
        return min;
    }

    @Override
    public int max() {
        return max;
    }

    @Override
    public boolean isSingle() {
        return min == max;
    }

    @Override
    public boolean isInRange(int format) {
        return format >= min && format <= max;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("format", format),
                ExaminableProperty.of("min", min),
                ExaminableProperty.of("max", max)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackFormatImpl that = (PackFormatImpl) o;
        if (format != that.format) return false;
        if (min != that.min) return false;
        return max == that.max;
    }

    @Override
    public int hashCode() {
        int result = format;
        result = 31 * result + min;
        result = 31 * result + max;
        return result;
    }

}
