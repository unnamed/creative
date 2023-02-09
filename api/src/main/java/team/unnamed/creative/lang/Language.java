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
package team.unnamed.creative.lang;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.metadata.language.LanguageEntry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a set of translations for a specific
 * language. Custom language additions require a
 * {@link LanguageEntry} being added to the {@link LanguageMeta}
 * instance of the resource pack
 *
 * @since 1.0.0
 */
public class Language implements Keyed, Examinable {

    private final Key key;
    private final Map<String, String> translations;

    private Language(
            Key key,
            Map<String, String> translations
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

    /**
     * Returns a  map containing all the translations for this language, where
     * the key is the translation key (yeah) and the value is the actual translation,
     * in example, there could be a translation for the Stone block
     *
     * <p>"block.minecraft.stone" -> "Stone"</p>
     *
     * @return The language translations
     */
    public Map<String, String> translations() {
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
        Language that = (Language) o;
        return key.equals(that.key)
                && translations.equals(that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, translations);
    }

    /**
     * Creates a new {@link Language} object which holds
     * the given translations in a Map
     *
     * @param translations The language translations
     * @return The language
     * @since 1.0.0
     */
    public static Language of(Key key, Map<String, String> translations) {
        return new Language(key, translations);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key key;
        private Map<String, String> translations;

        private Builder() {
        }

        public Builder key(Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        public Builder translations(Map<String, String> translations) {
            this.translations = requireNonNull(translations, "translations");
            return this;
        }

        public Builder translation(String key, String value) {
            requireNonNull(key, "key");
            requireNonNull(value, "value");
            if (this.translations == null) {
                this.translations = new LinkedHashMap<>();
            }
            this.translations.put(key, value);
            return this;
        }

        public Language build() {
            return new Language(key, translations);
        }

    }

}
