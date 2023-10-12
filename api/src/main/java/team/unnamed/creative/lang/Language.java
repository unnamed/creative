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
import net.kyori.adventure.translation.Translatable;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Represents a set of translations for a specific
 * language.
 *
 * <p>Custom language additions require a {@link LanguageEntry}
 * being added to the {@link LanguageMeta} instance of the resource
 * pack.</p>
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface Language extends ResourcePackPart, Keyed, Examinable {
    /**
     * Creates a new {@link Language} object which holds
     * the given translations in a Map.
     *
     * @param key          The language key
     * @param translations The translations
     * @return The language
     * @since 1.1.0
     */
    static @NotNull Language language(final @NotNull Key key, final @NotNull Map<String, String> translations) {
        return new LanguageImpl(key, translations);
    }

    /**
     * Creates a new {@link Language} instance builder.
     *
     * @return The created builder
     * @since 1.0.0
     */
    static @NotNull Builder language() {
        return new LanguageImpl.BuilderImpl();
    }

    /**
     * Creates a new {@link Language} object which holds
     * the given translations in a Map
     *
     * @param translations The language translations
     * @return The language
     * @since 1.0.0
     * @deprecated Use {@link Language#language(Key, Map)} instead,
     * it is better for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Language of(final @NotNull Key key, final @NotNull Map<String, String> translations) {
        return new LanguageImpl(key, translations);
    }

    /**
     * Creates a new {@link Language} instance builder.
     *
     * @return The created builder
     * @since 1.0.0
     * @deprecated Use {@link Language#language()} instead,
     * it is better for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Builder builder() {
        return new LanguageImpl.BuilderImpl();
    }

    /**
     * Returns the language key.
     *
     * <p>For example, the English language key is "minecraft:en_us"</p>
     *
     * @return The language key
     * @since 1.0.0
     */
    @Override
    @NotNull Key key();

    /**
     * Returns a map containing all the translations for this language, where
     * the key is the translation key (yeah) and the value is the actual translation,
     * in example, there could be a translation for the Stone block
     *
     * <p>"block.minecraft.stone" -> "Stone"</p>
     *
     * @return The language translations
     * @since 1.0.0
     */
    @NotNull Map<String, String> translations();

    /**
     * Returns the translation for the given key, or null if there is
     * no translation.
     *
     * @param key The translation key
     * @return The translation
     * @since 1.1.0
     */
    default @Nullable String translation(final @NotNull String key) {
        requireNonNull(key, "key");
        return translations().get(key);
    }

    /**
     * Returns the translation for the given {@link Translatable} object,
     * or null if there is no translation.
     *
     * @param translatable The translatable object
     * @return The translation
     * @since 1.1.0
     */
    default @Nullable String translation(final @NotNull Translatable translatable) {
        requireNonNull(translatable, "translatable");
        return translation(translatable.translationKey());
    }

    /**
     * Adds this language to the given resource container.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.language(this);
    }

    /**
     * A builder for {@link Language} instances.
     *
     * @since 1.0.0
     */
    interface Builder {
        /**
         * Sets the language key.
         *
         * @param key The language key
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        /**
         * Sets the language translations.
         *
         * @param translations The translations
         * @return This builder
         * @since 1.0.0
         */
        @NotNull Builder translations(final @NotNull Map<String, String> translations);

        /**
         * Adds a translation to the language.
         *
         * @param key   The translation key
         * @param value The translation value
         * @return This builder
         * @since 1.0.0
         */
        @NotNull Builder translation(final @NotNull String key, final @NotNull String value);

        /**
         * Builds the language instance.
         *
         * @return The created language
         * @since 1.0.0
         */
        @NotNull Language build();
    }
}
