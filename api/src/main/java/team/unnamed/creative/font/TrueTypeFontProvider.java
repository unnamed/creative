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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.Vector2Float;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

/**
 * A {@link FontProvider} implementation that uses fonts
 * with the True Type Font format
 *
 * @since 1.0.0
 */
public class TrueTypeFontProvider implements FontProvider {

    public static final float DEFAULT_SIZE = 11F;
    public static final float DEFAULT_OVERSAMPLE = 1F;

    private final Key file;
    private final Vector2Float shift;
    private final float size;
    private final float oversample;
    @Unmodifiable private final List<String> skip;

    protected TrueTypeFontProvider(
            Key file,
            Vector2Float shift,
            float size,
            float oversample,
            List<String> skip
    ) {
        requireNonNull(file, "file");
        requireNonNull(shift, "shift");
        requireNonNull(skip, "skip");
        this.file = file;
        this.shift = shift;
        this.size = size;
        this.oversample = oversample;
        this.skip = immutableListOf(skip);
    }

    /**
     * Returns the resource location of the TrueType font
     * file within assets/&lt;namespace&gt;/font
     *
     * @return The True Type Font file location
     */
    public Key file() {
        return file;
    }

    /**
     * Returns the distance by which the characters of this
     * provider are shifted. X: Left shift, Y: Downwards shift
     *
     * @return The characters shift
     */
    public Vector2Float shift() {
        return shift;
    }

    /**
     * Returns the font size to render at
     *
     * @return The font size
     */
    public float size() {
        return size;
    }

    /**
     * Returns the resolution to render at
     *
     * @return The font resolution
     */
    public float oversample() {
        return oversample;
    }

    /**
     * Returns an unmodifiable list of strings
     * to exclude
     *
     * @return The excluded characters
     */
    public @Unmodifiable List<String> skip() {
        return skip;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("file", file),
                ExaminableProperty.of("shift", shift),
                ExaminableProperty.of("size", size),
                ExaminableProperty.of("oversample", oversample),
                ExaminableProperty.of("skip", skip)
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
        TrueTypeFontProvider that = (TrueTypeFontProvider) o;
        return Float.compare(that.size, size) == 0
                && Float.compare(that.oversample, oversample) == 0
                && file.equals(that.file)
                && shift.equals(that.shift)
                && skip.equals(that.skip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, shift, size, oversample, skip);
    }

    /**
     * Mutable and fluent-style builder for {@link TrueTypeFontProvider}
     * instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Key file;
        private Vector2Float shift = Vector2Float.ZERO;
        private float size = DEFAULT_SIZE;
        private float oversample = DEFAULT_OVERSAMPLE;
        private List<String> skip = Collections.emptyList();

        protected Builder() {
        }

        public Builder file(Key file) {
            this.file = requireNonNull(file, "file");
            return this;
        }

        public Builder shift(Vector2Float shift) {
            this.shift = requireNonNull(shift, "shift");
            return this;
        }

        public Builder size(float size) {
            this.size = size;
            return this;
        }

        public Builder oversample(float oversample) {
            this.oversample = oversample;
            return this;
        }

        public Builder skip(List<String> skip) {
            this.skip = requireNonNull(skip, "skip");
            return this;
        }

        public Builder skip(String... skip) {
            requireNonNull(skip, "skip");
            this.skip = Arrays.asList(skip);
            return this;
        }

        /**
         * Finishes building the {@link TrueTypeFontProvider} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created font
         */
        public TrueTypeFontProvider build() {
            return new TrueTypeFontProvider(file, shift, size, oversample, skip);
        }

    }

}
