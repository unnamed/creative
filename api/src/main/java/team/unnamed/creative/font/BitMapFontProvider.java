/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.util.Validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

/**
 * Represents a bitmap font (font that uses a set of
 * string characters and PNG images to render) for
 * Minecraft resource packs
 *
 * @since 1.0.0
 */
public class BitMapFontProvider implements FontProvider {

    /**
     * Default bitmap font height
     */
    public static final int DEFAULT_HEIGHT = 8;

    private final Key file;
    private final int height;
    private final int ascent;
    @Unmodifiable private final List<String> characters;

    protected BitMapFontProvider(
            Key file,
            int height,
            int ascent,
            List<String> characters
    ) {
        requireNonNull(file, "file");
        requireNonNull(characters, "characters");
        this.file = file;
        this.height = height;
        this.ascent = ascent;
        this.characters = immutableListOf(characters);
        validate();
    }

    private void validate() {
        Validate.isTrue(ascent <= height, "Ascent (%s) is higher than height (%s)", ascent, height);
        Validate.isTrue(!characters.isEmpty(), "Character list is empty");

        String sample = characters.get(0);
        for (String character : characters) {
            Validate.isNotNull(character, "An element from the character list is null");
            Validate.isTrue(character.length() == sample.length(), "Elements of character list must have the same length");
        }
    }

    @Override
    public String name() {
        return "bitmap";
    }

    /**
     * Returns the texture location of this
     * bitmap font, must be a PNG image
     *
     * @return The font texture
     */
    public Key file() {
        return file;
    }

    public BitMapFontProvider file(Key newFile) {
        return new BitMapFontProvider(newFile, height, ascent, characters);
    }

    /**
     * Returns the height of the character, measured in pixels.
     * Can be negative. This tag is separate from the area used
     * in the source texture and just rescales the displayed
     * result
     *
     * @return The font characters height
     */
    public int height() {
        return height;
    }

    public BitMapFontProvider height(int newHeight) {
        return new BitMapFontProvider(file, newHeight, ascent, characters);
    }

    /**
     * Returns the font characters ascent, measured in
     * pixels, this value as a vertical shift to displayed
     * result
     *
     * @return The font characters ascent
     */
    public int ascent() {
        return ascent;
    }

    public BitMapFontProvider ascent(int newAscent) {
        return new BitMapFontProvider(file, height, newAscent, characters);
    }

    /**
     * Returns a list of strings containing the characters replaced by
     * this provider, as well as their order within the texture. All
     * elements must describe the same number of characters. The texture
     * is split into one equally sized row for each element of this list.
     * Each row is split into one equally sized character for each character
     * within one list element.
     *
     * @return The font characters
     */
    public @Unmodifiable List<String> characters() {
        return characters;
    }

    public BitMapFontProvider characters(List<String> newCharacters) {
        return new BitMapFontProvider(file, height, ascent, newCharacters);
    }

    public BitMapFontProvider.Builder toBuilder() {
        return FontProvider.bitMap()
                .file(file)
                .height(height)
                .ascent(ascent)
                .characters(characters);
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.startObject()
                .key("type").value("bitmap")
                .key("file").value(file);

        if (height != BitMapFontProvider.DEFAULT_HEIGHT) {
            // only write if height is not equal to the default height
            writer.key("height").value(height);
        }

        writer.key("ascent").value(ascent)
                .key("chars").value(characters)
                .endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
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
        BitMapFontProvider that = (BitMapFontProvider) o;
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
     * Mutable and fluent-style builder for {@link BitMapFontProvider}
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

        public Builder characters(String... characters) {
            requireNonNull(characters, "characters");
            this.characters = Arrays.asList(characters);
            return this;
        }

        /**
         * Finishes building the {@link BitMapFontProvider} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created font
         */
        public BitMapFontProvider build() {
            return new BitMapFontProvider(file, height, ascent, characters);
        }

    }

}
