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
package team.unnamed.creative.metadata.pack;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Represents the format or version of a resource-pack.
 *
 * <p>Since Minecraft 1.20.2 (pack format 18), resource-packs
 * can also describe a range for pack formats that this pack
 * supports.</p>
 *
 * @since 1.1.0
 */
public final class PackFormat implements Examinable {

    private final int format;
    private final int min;
    private final int max;

    private PackFormat(final int format, final int min, final int max) {
        this.format = format;
        this.min = min;
        this.max = max;

        // validate arguments
        if (min > max)
            throw new IllegalArgumentException("Minimum " + min + " is greater than maximum " + max);
        if (!isInRange(format))
            throw new IllegalArgumentException("Format " + format + " is not in the range [" + min + ", " + max + "]");
    }

    /**
     * Returns the pack format.
     *
     * <p>This value is still required for older versions
     * to be able to read the resource-pack.</p>
     *
     * <p>The value is between min and max values.</p>
     *
     * @return The pack format.
     * @since 1.1.0
     */
    public int format() {
        return format;
    }

    /**
     * Returns the minimum supported pack format. (Inclusive)
     *
     * @return The minimum supported pack format (Inclusive)
     * @since 1.1.0
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     */
    public int min() {
        return min;
    }

    /**
     * Returns the maximum supported pack format. (Inclusive)
     *
     * @return The maximum supported pack format (Inclusive)
     * @since 1.1.0
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     */
    public int max() {
        return max;
    }

    /**
     * Determines whether this pack format is a single format
     * and not a range of formats.
     *
     * @return True if format, min and max are equal.
     * @since 1.1.0
     */
    public boolean isSingle() {
        return min == max;
    }

    /**
     * Determines whether the given {@code format} is in the
     * range of this pack format.
     *
     * @param format The format to check.
     * @return True if the format is in the range.
     * @since 1.1.0
     */
    public boolean isInRange(final int format) {
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
        PackFormat that = (PackFormat) o;
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

    /**
     * Create a pack format that supports only a single
     * pack format, specified by the {@code format} parameter.
     *
     * @param format The pack format
     * @return The created pack format
     * @since 1.1.0
     */
    public static @NotNull PackFormat format(final int format) {
        return new PackFormat(format, format, format);
    }

    /**
     * Create a pack format that supports a range of formats
     * specified by the {@code min} and {@code max} parameters.
     *
     * <p>Note: since this range information is ignored by old
     * versions of the game, they will always see a "normal",
     * single-version pack, without any extended compatibility.
     * The single-version for this pack is specified by the
     * {@code format} parameter.</p>
     *
     * <p>Also note that the given {@code format} must be in
     * the bounds of the provided range.</p>
     *
     * @param format The pack format (for older versions)
     * @param min The minimum supported pack format (INCLUSIVE)
     * @param max The maximum supported pack format (INCLUSIVE)
     * @return The created pack format
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.1.0
     */
    public static @NotNull PackFormat format(final int format, final int min, final int max) {
        return new PackFormat(format, min, max);
    }

}
