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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a pack-format version with <em>major</em> and <em>minor</em> components
 * (for example, {@code 65.2} or {@code 68.0}).
 *
 * <p>Instances are immutable and implement a natural ordering where versions are
 * first compared by their major component and then by their minor component.</p>
 *
 * @sinceMinecraft 1.21.9
 * @sincePackFormat 86
 * @since 1.8.4
 */
public class FormatVersion implements Comparable<FormatVersion> {
    private final int major;
    private final int minor;

    /**
     * Creates a new {@link FormatVersion}.
     *
     * <p>Both {@code major} and {@code minor} must be non-negative.</p>
     *
     * @param major The major component.
     * @param minor The minor component.
     * @throws IllegalArgumentException If {@code major} or {@code minor} is negative.
     */
    private FormatVersion(final int major, final int minor) {
        if (major < 0 || minor < 0) {
            throw new IllegalArgumentException("PackVersion cannot be negative: " + major + "." + minor);
        }
        this.major = major;
        this.minor = minor;
    }

    /**
     * Creates a version with the given {@code major} and a {@code minor} of {@code 0}.
     *
     * @param major The major component (non-negative).
     * @return A new {@link FormatVersion} with {@code minor == 0}.
     * @throws IllegalArgumentException If {@code major} is negative.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public static @NotNull FormatVersion of(final int major) {
        return new FormatVersion(major, 0);
    }

    /**
     * Creates a version with the given {@code major} and {@code minor}.
     *
     * @param major The major component (non-negative).
     * @param minor The minor component (non-negative).
     * @return A new {@link FormatVersion}.
     * @throws IllegalArgumentException If {@code major} or {@code minor} is negative.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public static @NotNull FormatVersion of(final int major, final int minor) {
        return new FormatVersion(major, minor);
    }

    /**
     * Parses a version string in the form {@code M} or {@code M.m}.
     *
     * <p>Whitespace is trimmed. The components must be base-10 integers and non-negative.
     * Examples of valid inputs: {@code "65"}, {@code "65.2"}, {@code "68.0"}.</p>
     *
     * @param value The string to parse.
     * @return The parsed {@link FormatVersion}.
     * @throws IllegalArgumentException If the input is not in {@code M} or {@code M.m} form
     *                                  or contains negative components.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public static @NotNull FormatVersion parse(final @NotNull String value) {
        final String[] parts = value.trim().split("\\.");
        if (parts.length == 1) {
            return of(Integer.parseInt(parts[0]));
        } else if (parts.length == 2) {
            return of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        throw new IllegalArgumentException("Invalid pack version: " + value);
    }

    /**
     * Returns the major component.
     *
     * @return The major component (non-negative).
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public int major() {
        return this.major;
    }

    /**
     * Returns the minor component.
     *
     * @return The minor component (non-negative).
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public int minor() {
        return this.minor;
    }

    /**
     * Returns the minimum (≤) of two versions using natural ordering.
     *
     * @param a The first version.
     * @param b The second version.
     * @return {@code a} if {@code a ≤ b}, otherwise {@code b}.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public static @NotNull FormatVersion min(final @NotNull FormatVersion a, final @NotNull FormatVersion b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    /**
     * Returns the maximum (≥) of two versions using natural ordering.
     *
     * @param a The first version.
     * @param b The second version.
     * @return {@code a} if {@code a ≥ b}, otherwise {@code b}.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    public static @NotNull FormatVersion max(final @NotNull FormatVersion a, final @NotNull FormatVersion b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    /**
     * Compares this version with the specified version for order.
     *
     * <p>Ordering is by major component first; if equal, by minor component.</p>
     *
     * @param o The version to be compared.
     * @return A negative integer, zero, or a positive integer as this version
     *         is less than, equal to, or greater than the specified version.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    @Override
    public int compareTo(@NotNull FormatVersion o) {
        final int c = Integer.compare(this.major, o.major);
        if (c != 0) return c;
        return Integer.compare(this.minor, o.minor);
    }

    /**
     * Returns a canonical string representation.
     *
     * <p>If {@code minor == 0}, returns {@code "M"}; otherwise returns {@code "M.m"}.</p>
     *
     * @return The string representation of this version.
     * @sinceMinecraft 1.21.9
     * @sincePackFormat 86
     * @since 1.8.4
     */
    @Override
    public String toString() {
        return this.minor == 0 ? Integer.toString(this.major) : (this.major + "." + this.minor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormatVersion)) return false;
        FormatVersion that = (FormatVersion) o;
        return this.major == that.major && this.minor == that.minor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.major, this.minor);
    }
}
