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
package team.unnamed.creative.lang;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class LanguageImpl implements Language {

    private final Key key;
    private final Map<String, String> translations;

    LanguageImpl(
            final @NotNull Key key,
            final @NotNull Map<String, String> translations
    ) {
        this.key = requireNonNull(key, "key");
        this.translations = requireNonNull(translations, "translations");
        validate();
    }

    private void validate() {
        translations.forEach((key, value) -> {
            requireNonNull(key, "Translation key cannot be null");
            requireNonNull(value, "Translation cannot be null");
        });
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Map<String, String> translations() {
        return translations;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("translations", translations)
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
        LanguageImpl that = (LanguageImpl) o;
        return key.equals(that.key)
                && translations.equals(that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, translations);
    }

    static final class BuilderImpl implements Builder {

        private Key key;
        private Map<String, String> translations;

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder translations(final @NotNull Map<String, String> translations) {
            this.translations = requireNonNull(translations, "translations");
            return this;
        }

        @Override
        public @NotNull Builder translation(final @NotNull String key, final @NotNull String value) {
            requireNonNull(key, "key");
            requireNonNull(value, "value");
            if (this.translations == null) {
                this.translations = new LinkedHashMap<>();
            }
            this.translations.put(key, value);
            return this;
        }

        @Override
        public @NotNull Language build() {
            return new LanguageImpl(key, translations);
        }
    }
}
