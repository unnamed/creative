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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

final class UnihexFontProviderImpl implements UnihexFontProvider {
    private final Key file;
    private final List<SizeOverride> sizes;

    UnihexFontProviderImpl(final @NotNull Key file, final @NotNull List<SizeOverride> sizes) {
        this.file = requireNonNull(file, "file");
        this.sizes = immutableListOf(requireNonNull(sizes, "sizes"));
    }

    @Override
    public @NotNull Key file() {
        return file;
    }

    @Override
    public @Unmodifiable @NotNull List<SizeOverride> sizes() {
        return sizes;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("file", file),
                ExaminableProperty.of("sizes", sizes)
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
        final UnihexFontProviderImpl that = (UnihexFontProviderImpl) o;
        if (!file.equals(that.file)) return false;
        return sizes.equals(that.sizes);
    }

    @Override
    public int hashCode() {
        int result = file.hashCode();
        result = 31 * result + sizes.hashCode();
        return result;
    }

    static final class SizeOverrideImpl implements SizeOverride {

        private final int from;
        private final int to;
        private final int left;
        private final int right;

        SizeOverrideImpl(final int from, final int to, final int left, final int right) {
            this.from = from;
            this.to = to;
            this.left = left;
            this.right = right;
            validate();
        }

        SizeOverrideImpl(final String from, final String to, final int left, final int right) {
            this.left = left;
            this.right = right;

            final int[] fromCodepoints = from.codePoints().toArray();
            if (fromCodepoints.length != 1) {
                throw new IllegalArgumentException("Only one codepoint is allowed for 'from' parameter, got " + fromCodepoints.length + " (" + from + ")");
            }
            this.from = fromCodepoints[0];

            final int[] toCodepoints = to.codePoints().toArray();
            if (toCodepoints.length != 1) {
                throw new IllegalArgumentException("Only one codepoint is allowed for 'to' parameter, got " + toCodepoints.length + " (" + to + ")");
            }
            this.to = toCodepoints[0];

            validate();
        }

        private void validate() {
            if (from >= to)
                throw new IllegalArgumentException("Invalid range: [" + from + ";" + to + "]");
        }

        @Override
        public int from() {
            return from;
        }

        @Override
        public int to() {
            return to;
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
                    ExaminableProperty.of("from", from),
                    ExaminableProperty.of("to", to),
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
            if (o == null || getClass() != o.getClass()) return false;
            final SizeOverrideImpl override = (SizeOverrideImpl) o;
            if (from != override.from) return false;
            if (to != override.to) return false;
            if (left != override.left) return false;
            return right == override.right;
        }

        @Override
        public int hashCode() {
            int result = from;
            result = 31 * result + to;
            result = 31 * result + left;
            result = 31 * result + right;
            return result;
        }
    }

    static final class BuilderImpl implements Builder {
        private Key file;
        private List<SizeOverride> sizes = new ArrayList<>();

        @Override
        public @NotNull Builder file(final @NotNull Key file) {
            this.file = requireNonNull(file, "file");
            return this;
        }

        @Override
        public @NotNull Builder sizes(final @NotNull List<SizeOverride> sizes) {
            requireNonNull(sizes, "sizes");
            this.sizes = new ArrayList<>(sizes);
            return this;
        }

        @Override
        public @NotNull Builder addSize(final @NotNull SizeOverride size) {
            requireNonNull(size, "size");
            sizes.add(size);
            return this;
        }

        @Override
        public @NotNull UnihexFontProvider build() {
            return new UnihexFontProviderImpl(file, sizes);
        }
    }
}
