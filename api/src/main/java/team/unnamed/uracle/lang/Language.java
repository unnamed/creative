/*
 * This file is part of uracle, licensed under the MIT license
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
package team.unnamed.uracle.lang;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.metadata.language.LanguageMeta;
import team.unnamed.uracle.metadata.language.LanguageEntry;
import team.unnamed.uracle.serialize.AssetWriter;
import team.unnamed.uracle.serialize.SerializableResource;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * Represents a set of translations for a specific
 * language. Custom language additions require a
 * {@link LanguageEntry} being added to the {@link LanguageMeta}
 * instance of the resource pack
 *
 * @since 1.0.0
 */
public class Language implements SerializableResource {

    @Unmodifiable private final Map<String, String> translations;

    private Language(Map<String, String> translations) {
        requireNonNull(translations, "translations");
        this.translations = immutableMapOf(translations);
    }

    /**
     * Returns an unmodifiable map containing all the translations for this
     * language, where the key is the translation key (yeah) and the value is
     * the actual translation, in example, there could be a translation for the
     * Stone block
     *
     * <p>"block.minecraft.stone" -> "Stone"</p>
     *
     * @return The language translations
     */
    public @Unmodifiable Map<String, String> translations() {
        return translations;
    }

    @Override
    public void serialize(AssetWriter writer) {
        writer.startObject();
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            writer.key(entry.getKey()).value(entry.getValue());
        }
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
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
        Language language = (Language) o;
        return translations.equals(language.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translations);
    }

    /**
     * Creates a new {@link Language} object which holds
     * the given translations in a Map
     *
     * @param translations The language translations
     * @return The language
     * @since 1.0.0
     */
    public static Language of(Map<String, String> translations) {
        return new Language(translations);
    }

}
