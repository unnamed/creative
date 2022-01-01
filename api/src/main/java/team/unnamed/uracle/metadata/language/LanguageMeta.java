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
package team.unnamed.uracle.metadata.language;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.metadata.MetadataPart;
import team.unnamed.uracle.serialize.AssetWriter;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * Represents the metadata part of the registered languages
 * for a resource pack
 *
 * @since 1.0.0
 */
public class LanguageMeta implements MetadataPart {

    @Unmodifiable private final Map<Key, LanguageEntry> languages;

    private LanguageMeta(Map<Key, LanguageEntry> languages) {
        requireNonNull(languages, "languages");
        this.languages = immutableMapOf(languages);
    }

    /**
     * Returns an unmodifiable map of the registered
     * custom languages
     *
     * @return The registered languages
     */
    public @Unmodifiable Map<Key, LanguageEntry> languages() {
        return languages;
    }

    @Override
    public void serialize(AssetWriter writer) {

        if (languages.isEmpty()) {
            // do not write anything if no
            // languages were registered
            return;
        }

        writer.key("language").startObject();

        for (Map.Entry<Key, LanguageEntry> entry : languages.entrySet()) {
            LanguageEntry language = entry.getValue();
            writer.key(entry.getKey().asString());
            language.serialize(writer);
        }

        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("languages", languages)
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
        LanguageMeta that = (LanguageMeta) o;
        return languages.equals(that.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languages);
    }

    /**
     * Creates a new {@link LanguageMeta} instance from
     * the given languages map
     *
     * @param languages The registered languages
     * @return A new {@link LanguageMeta} instance
     * @since 1.0.0
     */
    public static LanguageMeta of(Map<Key, LanguageEntry> languages) {
        return new LanguageMeta(languages);
    }

}
