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

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.util.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

/**
 * A font provider for adding custom spacing, without needing to use bitmaps.
 * This font provider consists of a map of codepoints (characters) and integers (how many pixels to shift by)
 * If a character is used in a space font provider, it is not rendered, and is instead used as spacing.
 * You can not shift vertically with this font provider, for vertical offsets use {@link BitMapFontProvider}
 */
public class SpaceFontProvider implements FontProvider {

    private final Map<Character, Integer> advances;

    protected SpaceFontProvider(
            Map<Character, Integer> advances
    ) {
        requireNonNull(advances,"advances");
        this.advances = immutableMapOf(advances);
        validate();
    }

    private void validate() {
        for (Map.Entry<Character, Integer> entry : advances.entrySet()) {
            Character character = entry.getKey();
            Integer offset = entry.getValue();
            Validate.isNotNull(character, "An element from the character list is null");
            Validate.isNotNull(offset, "Integer object is null");
        }
    }

    public Map<Character, Integer> advances() {
        return advances;
    }

    public SpaceFontProvider advances(Map<Character, Integer> advances) {
        return new SpaceFontProvider(advances);
    }

    @Override
    public String name() {
        return "space";
    }

    public SpaceFontProvider.Builder toBuilder() {
        return FontProvider.space()
                .advances(advances);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("advances", advances)
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
        SpaceFontProvider that = (SpaceFontProvider) o;
        return advances.equals(that.advances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advances);
    }

    /**
     * Mutable and fluent-style builder for {@link SpaceFontProvider}
     * instances
     */
    public static class Builder {

        private Map<Character, Integer> advances;

        protected Builder() {
        }

        public Builder advances(Map<Character, Integer> entries) {
            requireNonNull(entries, "entries");
            advances = entries;
            return this;
        }

        public Builder advance(char key, int value) {
            if (this.advances == null) {
                this.advances = new HashMap<>();
            }
            this.advances.put(key, value);
            return this;
        }

        /**
         * Finishes building the {@link SpaceFontProvider} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created font
         */
        public SpaceFontProvider build() {
            return new SpaceFontProvider(advances);
        }

    }

}
