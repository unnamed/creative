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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.Validate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

/**
 * A {@link FontProvider} implementation that uses Unifont
 * HEX files
 *
 * @since 1.0.0
 * @sincePackFormat 15
 */
public class UnihexFontProvider implements FontProvider {

    private final Key file;
    private final @Unmodifiable List<SizeOverride> sizes;

    protected UnihexFontProvider(Key file, List<SizeOverride> sizes) {
        requireNonNull(file, "file");
        requireNonNull(sizes, "sizes");
        this.file = file;
        this.sizes = immutableListOf(sizes);
    }

    /**
     * Returns the location of a ZIP file containing the
     * HEX files. All the files that end with ".hex" are
     * loaded. If a file doesn't end with ".hex", it is
     * silently ignored.
     *
     * <p>The file path is relative to <code>assets/&lt;namespace&gt;/</code>
     * <i>(Note that it is not inside "fonts")</i></p>
     *
     * @return The location of the ZIP file of HEX files
     */
    public Key file() {
        return file;
    }

    /**
     * A list of size overrides, an override contains an
     * (inclusive) range of codepoints and their custom
     * dimensions
     *
     * @return The size overrides
     */
    public @Unmodifiable List<SizeOverride> sizes() {
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
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnihexFontProvider that = (UnihexFontProvider) o;
        if (!file.equals(that.file)) return false;
        return sizes.equals(that.sizes);
    }

    @Override
    public int hashCode() {
        int result = file.hashCode();
        result = 31 * result + sizes.hashCode();
        return result;
    }

    public static class SizeOverride implements Examinable {

        private final int from;
        private final int to;
        private final int left;
        private final int right;

        private SizeOverride(int from, int to, int left, int right) {
            this.from = from;
            this.to = to;
            this.left = left;
            this.right = right;
            validate();
        }

        private void validate() {
            Validate.isTrue(from < to, "Invalid range: [%s;%s]", from, to);
        }

        public int from() {
            return from;
        }

        public int to() {
            return to;
        }

        public int left() {
            return left;
        }

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
        public String toString() {
            return examine(StringExaminer.simpleEscaping());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SizeOverride override = (SizeOverride) o;
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

        public static SizeOverride of(int from, int to, int left, int right) {
            return new SizeOverride(from, to, left, right);
        }

    }

    /**
     * Mutable and fluent-style builder for {@link UnihexFontProvider}
     * instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Key file;
        private List<SizeOverride> sizes = Collections.emptyList();

        protected Builder() {
        }

        public Builder file(Key file) {
            this.file = requireNonNull(file, "file");
            return this;
        }

        public Builder sizes(List<SizeOverride> sizes) {
            this.sizes = requireNonNull(sizes, "sizes");
            return this;
        }

        public UnihexFontProvider build() {
            return new UnihexFontProvider(file, sizes);
        }

    }

}
