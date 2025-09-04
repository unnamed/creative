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
package team.unnamed.creative.metadata.pack;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

final class PackFormatImpl implements PackFormat {

    private final @NotNull FormatVersion format;
    private final @NotNull FormatVersion min;
    private final @NotNull FormatVersion max;

    @Deprecated
    PackFormatImpl(final int format, final int min, final int max) {
        this(FormatVersion.of(format), FormatVersion.of(min), FormatVersion.of(max));
    }

    PackFormatImpl(final @NotNull FormatVersion format, final @NotNull FormatVersion min, final @NotNull FormatVersion max) {
        this.format = format;
        this.min = min;
        this.max = max;

        // validate arguments
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimum " + min + " is greater than maximum " + max);
        }
        if (format.compareTo(min) < 0 || format.compareTo(max) > 0) {
            throw new IllegalArgumentException("Format " + format + " is not in the range [" + min + ", " + max + "]");
        }
    }

    @Override
    public FormatVersion formatVersion() {
        return format;
    }

    @Override
    @Deprecated
    public int format() {
        return format.major();
    }

    @Override
    public FormatVersion minVersion() {
        return min;
    }

    @Override
    @Deprecated
    public int min() {
        return min.major();
    }

    @Override
    public FormatVersion maxVersion() {
        return max;
    }

    @Override
    @Deprecated
    public int max() {
        return max.major();
    }

    @Override
    public boolean isSingle() {
        return format.equals(min) && min.equals(max);
    }

    @Override
    @Deprecated
    public boolean isInRange(int format) {
        return isInRange(FormatVersion.of(format));
    }

    @Override
    public boolean isInRange(final @NotNull FormatVersion format) {
        return format.compareTo(min) >= 0 && format.compareTo(max) <= 0;
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
        if (!format.equals(that.format)) return false;
        if (!min.equals(that.min)) return false;
        return max.equals(that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, min, max);
    }

}
