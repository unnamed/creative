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
package team.unnamed.creative.metadata.language;


import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

final class LanguageMetaImpl implements LanguageMeta {
    private final Map<String, LanguageEntry> languages;

    LanguageMetaImpl(final @NotNull Map<String, LanguageEntry> languages) {
        this.languages = immutableMapOf(requireNonNull(languages, "languages"));
        validate();
    }

    private void validate() {
        for (Map.Entry<String, LanguageEntry> language : languages.entrySet()) {
            String code = language.getKey();
            requireNonNull(code, "Language code is null");
            requireNonNull(language.getValue(), "Language entry for " + code + " is null");
            if (code.length() > MAX_LANGUAGE_LENGTH)
                throw new IllegalArgumentException("Language code is more than " + MAX_LANGUAGE_LENGTH + " characters long");
        }
    }

    @Override
    public @NotNull Class<? extends MetadataPart> type() {
        return LanguageMeta.class;
    }

    @Override
    public @NotNull @Unmodifiable Map<String, LanguageEntry> languages() {
        return languages;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("languages", languages)
        );
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LanguageMetaImpl that = (LanguageMetaImpl) o;
        return languages.equals(that.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languages);
    }
}
