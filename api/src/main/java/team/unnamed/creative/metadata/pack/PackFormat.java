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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the format or version of a resource-pack.
 *
 * <p>Since Minecraft 1.20.2 (pack format 18), resource-packs
 * can also describe a range for pack formats that this pack
 * supports.</p>
 *
 * @since 1.1.0
 */
@ApiStatus.NonExtendable
public interface PackFormat extends Examinable {

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
    int format();

    /**
     * Returns the minimum supported pack format. (Inclusive)
     *
     * @return The minimum supported pack format (Inclusive)
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     * @since 1.1.0
     */
    int min();

    /**
     * Returns the maximum supported pack format. (Inclusive)
     *
     * @return The maximum supported pack format (Inclusive)
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     * @since 1.1.0
     */
    int max();

    /**
     * Determines whether this pack format is a single format
     * and not a range of formats.
     *
     * @return True if format, min and max are equal.
     * @since 1.1.0
     */
    boolean isSingle();

    /**
     * Determines whether the given {@code format} is in the
     * range of this pack format.
     *
     * @param format The format to check.
     * @return True if the format is in the range.
     * @since 1.1.0
     */
    boolean isInRange(final int format);

    /**
     * Create a pack format that supports only a single
     * pack format, specified by the {@code format} parameter.
     *
     * @param format The pack format
     * @return The created pack format
     * @since 1.1.0
     */
    static @NotNull PackFormat format(final int format) {
        return format(format, format, format);
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
     * @param min    The minimum supported pack format (INCLUSIVE)
     * @param max    The maximum supported pack format (INCLUSIVE)
     * @return The created pack format
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.1.0
     */
    static @NotNull PackFormat format(final int format, final int min, final int max) {
        return new PackFormatImpl(format, min, max);
    }

}
