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

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Object representing a Minecraft's Resource Pack language
 * registration, it doesn't contain the language translations
 *
 * <p>Resource packs can create language files of
 * the type .json in the folder assets/&lt;namespace&gt;/lang.
 * Each file either replaces information from a file of the
 * same name in the default or a lower pack, or it creates a
 * new language as defined by pack.mcmeta</p>
 *
 * @sincePackFormat 1
 * @sinceMinecraft 1.6.1
 * @since 1.0.0
 */
public interface LanguageEntry extends Examinable {
    /**
     * Creates a new Minecraft {@link LanguageEntry} instance
     *
     * @param name          The language full name
     * @param region        The language region or country
     * @param bidirectional True if read from right to left
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract("_, _, _ -> new")
    static @NotNull LanguageEntry languageEntry(final @NotNull String name, final @NotNull String region, final boolean bidirectional) {
        return new LanguageEntryImpl(name, region, bidirectional);
    }

    /**
     * Static factory method for our builder implementation
     *
     * @return A new builder for {@link LanguageEntry} instances
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract("-> new")
    static @NotNull Builder languageEntry() {
        return new LanguageEntryImpl.BuilderImpl();
    }

    boolean DEFAULT_BIDIRECTIONAL = false;

    /**
     * Returns the language full name, shown in the
     * Minecraft language menu
     *
     * @return The language full name
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    @NotNull String name();

    /**
     * Returns the region or country of this language,
     * shown in the default Minecraft client language
     * menu
     *
     * @return The language region or country
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    @NotNull String region();

    /**
     * Determines if this language is bidirectional, in
     * that case, it must be read from right to left
     *
     * @return True if this language is bidirectional
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    boolean bidirectional();

    /**
     * Mutable and fluent-style builder for {@link LanguageEntry}
     * instances, since it has a lot of parameters, we create
     * a builder for ease its creation
     *
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    interface Builder {
        /**
         * Sets the language full name, shown in the
         * Minecraft language menu
         *
         * @param name The language full name
         * @return This builder instance
         * @sincePackFormat 1
         * @sinceMinecraft 1.6.1
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder name(final @NotNull String name);

        /**
         * Sets the region or country of this language,
         * shown in the default Minecraft client language
         * menu
         *
         * @param region The language region or country
         * @return This builder instance
         * @sincePackFormat 1
         * @sinceMinecraft 1.6.1
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder region(final @NotNull String region);

        /**
         * Sets if this language is bidirectional, in
         * that case, it must be read from right to left
         *
         * @param bidirectional True if this language is bidirectional
         * @return This builder instance
         * @sincePackFormat 1
         * @sinceMinecraft 1.6.1
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder bidirectional(final boolean bidirectional);

        /**
         * Finishes building the {@link LanguageEntry} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created language
         * @sincePackFormat 1
         * @sinceMinecraft 1.6.1
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull LanguageEntry build();
    }
}
