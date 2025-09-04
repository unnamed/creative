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

public class FormatVersion implements Comparable<FormatVersion> {
    private final int major;
    private final int minor;

    private FormatVersion(final int major, final int minor) {
        if (major < 0 || minor < 0) {
            throw new IllegalArgumentException("PackVersion cannot be negative: " + major + "." + minor);
        }
        this.major = major;
        this.minor = minor;
    }

    public static @NotNull FormatVersion of(final int major) {
        return new FormatVersion(major, 0);
    }

    public static @NotNull FormatVersion of(final int major, final int minor) {
        return new FormatVersion(major, minor);
    }

    public static @NotNull FormatVersion parse(final @NotNull String value) {
        final String[] parts = value.trim().split("\\.");
        if (parts.length == 1) {
            return of(Integer.parseInt(parts[0]));
        } else if (parts.length == 2) {
            return of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        throw new IllegalArgumentException("Invalid pack version: " + value);
    }

    public int major() {
        return this.major;
    }

    public int minor() {
        return this.minor;
    }

    public static @NotNull FormatVersion min(final @NotNull FormatVersion a, final @NotNull FormatVersion b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static @NotNull FormatVersion max(final @NotNull FormatVersion a, final @NotNull FormatVersion b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    @Override
    public int compareTo(@NotNull FormatVersion o) {
        final int c = Integer.compare(this.major, o.major);
        if (c != 0) return c;
        return Integer.compare(this.minor, o.minor);
    }

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
