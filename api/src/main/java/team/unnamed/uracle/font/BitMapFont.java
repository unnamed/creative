/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * Represents a bitmap font (font that uses a set of
 * string characters and PNG images to render) for
 * Minecraft resource packs
 *
 * @since 1.0.0
 */
public class BitMapFont implements Font {

    /**
     * Default bitmap font height
     */
    public static final int DEFAULT_HEIGHT = 8;

    /**
     * The texture location of this bitmap font, must
     * be a PNG image
     */
    private final Key file;

    /**
     * Optional. The height of the character, measured in pixels.
     * Can be negative. This tag is separate from the area used
     * in the source texture and just rescales the displayed
     * result
     */
    private final int height;

    /**
     * The ascent of the character, measured in pixels. This value
     * adds a vertical shift to the displayed result.
     */
    private final int ascent;

    /**
     * A list of strings containing the characters replaced by this
     * provider, as well as their order within the texture. All elements
     * must describe the same number of characters. The texture is split
     * into one equally sized row for each element of this list. Each
     * row is split into one equally sized character for each character
     * within one list element.
     */
    @Unmodifiable private final List<String> characters;

    protected BitMapFont(
            Key file,
            int height,
            int ascent,
            List<String> characters
    ) {
        requireNonNull(characters, "characters");
        this.file = requireNonNull(file, "file");
        this.height = height;
        this.ascent = ascent;
        this.characters = unmodifiableList(new ArrayList<>(characters));
    }

    @Override
    public Type type() {
        return Type.BITMAP;
    }

    /**
     * Returns the texture location of this
     * bitmap font
     *
     * @return The font texture
     */
    public Key file() {
        return file;
    }

    /**
     * Returns the font characters height
     *
     * @return The font characters height
     */
    public int height() {
        return height;
    }

    /**
     * Returns the font characters ascent, this
     * value as a vertical shift to the fonts
     *
     * @return The font characters ascent
     */
    public int ascent() {
        return ascent;
    }

    /**
     * Returns an unmodifiable list of the font
     * characters
     *
     * @return The font characters
     */
    public @Unmodifiable List<String> characters() {
        return characters;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("type", "bitmap"),
                ExaminableProperty.of("height", height),
                ExaminableProperty.of("ascent", ascent),
                ExaminableProperty.of("file", file),
                ExaminableProperty.of("chars", characters)
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
        BitMapFont that = (BitMapFont) o;
        return height == that.height
                && ascent == that.ascent
                && file.equals(that.file)
                && characters.equals(that.characters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, height, ascent, characters);
    }

    /**
     * Mutable and fluent-style builder for {@link BitMapFont}
     * instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private int height = DEFAULT_HEIGHT;
        private int ascent;
        private Key file;
        private List<String> characters = Collections.emptyList();

        protected Builder() {
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder ascent(int ascent) {
            this.ascent = ascent;
            return this;
        }

        public Builder file(Key file) {
            this.file = requireNonNull(file, "file");
            return this;
        }

        public Builder characters(List<String> characters) {
            this.characters = requireNonNull(characters, "characters");
            return this;
        }

        /**
         * Finishes building the {@link BitMapFont} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created language
         */
        public BitMapFont build() {
            return new BitMapFont(file, height, ascent, characters);
        }

    }

}
