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
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

final class BitMapFontProviderImpl implements BitMapFontProvider {
    private final Key file;
    private final int height;
    private final int ascent;
    @Unmodifiable private final List<String> characters;

    BitMapFontProviderImpl(
            final @NotNull Key file,
            final int height,
            final int ascent,
            final @NotNull List<String> characters
    ) {
        this.file = requireNonNull(file, "file");
        ;
        this.height = height;
        this.ascent = ascent;
        this.characters = immutableListOf(requireNonNull(characters, "characters"));
        validate();
    }

    private void validate() {
        if (ascent > height)
            throw new IllegalArgumentException("Ascent (" + ascent + ") is higher than height (" + height + ")");
        if (characters.isEmpty())
            throw new IllegalArgumentException("Character list is empty");

        String sample = characters.get(0);
        int codePointCount = sample.codePointCount(0, sample.length());
        for (String character : characters) {
            requireNonNull(character, "An element from the character list is null");
            if (character.codePointCount(0, character.length()) != codePointCount)
                throw new IllegalArgumentException("Elements of character list must have the same codepoint count");
        }
    }

    @Override
    public @NotNull Key file() {
        return file;
    }

    @Override
    public @NotNull BitMapFontProvider file(final @NotNull Key file) {
        requireNonNull(file, "file");
        return new BitMapFontProviderImpl(file, this.height, this.ascent, this.characters);
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public @NotNull BitMapFontProvider height(final int height) {
        return new BitMapFontProviderImpl(this.file, height, this.ascent, this.characters);
    }

    @Override
    public int ascent() {
        return ascent;
    }

    @Override
    public @NotNull BitMapFontProvider ascent(final int ascent) {
        return new BitMapFontProviderImpl(this.file, this.height, ascent, this.characters);
    }

    @Override
    public @Unmodifiable @NotNull List<String> characters() {
        return characters;
    }

    @Override
    public @NotNull BitMapFontProvider characters(final @NotNull List<String> characters) {
        requireNonNull(characters, "characters");
        return new BitMapFontProviderImpl(this.file, this.height, this.ascent, characters);
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
        BitMapFontProviderImpl that = (BitMapFontProviderImpl) o;
        return height == that.height
                && ascent == that.ascent
                && file.equals(that.file)
                && characters.equals(that.characters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, height, ascent, characters);
    }

    static final class BuilderImpl implements Builder {
        private Key file;
        private int height = DEFAULT_HEIGHT;
        private int ascent;
        private List<String> characters = Collections.emptyList();

        @Override
        public @NotNull Builder file(final @NotNull Key file) {
            this.file = requireNonNull(file, "file");
            return this;
        }

        @Override
        public @NotNull Builder height(final int height) {
            this.height = height;
            return this;
        }

        @Override
        public @NotNull Builder ascent(final int ascent) {
            this.ascent = ascent;
            return this;
        }

        @Override
        public @NotNull Builder characters(final @NotNull List<String> characters) {
            this.characters = requireNonNull(characters, "characters");
            return this;
        }

        @Override
        public @NotNull BitMapFontProvider build() {
            return new BitMapFontProviderImpl(file, height, ascent, characters);
        }
    }
}
