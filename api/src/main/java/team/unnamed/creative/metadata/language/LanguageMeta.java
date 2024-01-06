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
package team.unnamed.creative.metadata.language;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Map;

/**
 * Represents the metadata part of the registered languages
 * for a resource pack
 *
 * @sincePackFormat 1
 * @sinceMinecraft 1.6.1
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface LanguageMeta extends MetadataPart {
    /**
     * Creates a new {@link LanguageMeta} instance from
     * the given languages map
     *
     * @param languages The registered languages
     * @return A new {@link LanguageMeta} instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract("_ -> new")
    static @NotNull LanguageMeta language(final @NotNull Map<String, LanguageEntry> languages) {
        return new LanguageMetaImpl(languages);
    }

    int MAX_LANGUAGE_LENGTH = 16;

    /**
     * Returns an unmodifiable map of the registered
     * custom languages
     *
     * @return The registered languages
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    @NotNull @Unmodifiable Map<String, LanguageEntry> languages();
}
