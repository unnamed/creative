/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

/**
 * Object representing a Minecraft's Resource
 * Pack language addition
 *
 * <p>Resource packs can create language files of
 * the type .json in the folder assets/&lt;namespace&gt;/lang.
 * Each file either replaces information from a file of the
 * same name in the default or a lower pack, or it creates a
 * new language as defined by pack.mcmeta. </p>
 *
 * @since 1.0.0
 */
public class Language implements Element, Element.Part, Keyed, Examinable {

    /**
     * The language JSON file location inside
     * assets/&lt;namespace&gt;/lang, the resource
     * location path also identifies this language
     */
    private final Key key;

    /**
     * The full name of this language, shown in the
     * default client Minecraft language menu
     */
    private final String name;

    /**
     * The country or region name, shown in the default
     * Minecraft client language menu
     */
    private final String region;

    /**
     * If true, the language reads right
     * to left
     */
    private final boolean bidirectional;

    /**
     * Map containing all the translations for this language,
     * where the key is the translation key (yeah) and the
     * value is the actual translation, in example, there could
     * be a translation for the Stone block
     *
     * <p>"block.minecraft.stone" -> "Stone"</p>
     */
    @Unmodifiable private final Map<String, String> translations;

    private Language(
            Key key,
            String name,
            String region,
            boolean bidirectional,
            Map<String, String> translations
    ) {
        requireNonNull(translations, "translations");
        this.key = requireNonNull(key, "key");
        this.name = requireNonNull(name, "name");
        this.region = requireNonNull(region, "region");
        this.bidirectional = bidirectional;
        // create a copy and wrap into a unmodifiable map to
        // avoid modifications
        this.translations = unmodifiableMap(new HashMap<>(translations));
    }

    /**
     * Returns the resource location and identifier
     * for this language
     *
     * @return The language resource location
     */
    @Override
    public @NotNull Key key() {
        return key;
    }

    /**
     * Returns the language full name, shown in the
     * Minecraft language menu
     *
     * @return The language full name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the region or country of this language
     *
     * @return The language region or country
     */
    public String region() {
        return region;
    }

    /**
     * Determines if this language is bidirectional, in
     * that case, it must be read from right to left
     *
     * @return True if this language is bidirectional
     */
    public boolean bidirectional() {
        return bidirectional;
    }

    /**
     * Returns an unmodifiable map of the language
     * translations
     *
     * @return The language translations
     */
    public @Unmodifiable Map<String, String> translations() {
        return translations;
    }

    /**
     * Implementation of {@link Element#write(TreeWriter)}
     * for {@link Language} instances, languages are composed
     * by two parts, the first part must be written into the
     * pack meta file, and the second part has its own file.
     *
     * <p>Only the second part (which has its own file) is
     * created and written by this method, registration must
     * be done by calling {@link Language#write(TreeWriter.Context)}</p>
     *
     * @param writer The target tree writer
     */
    @Override
    public void write(TreeWriter writer) {
        try (TreeWriter.Context context = writer.join(key, "lang")) {
            // JSON object is formatted like
            // {
            //    "translation.key": "The actual translation"
            // }
            context.startObject();
            for (Map.Entry<String, String> translation : translations.entrySet()) {
                String key = translation.getKey();
                String value = translation.getValue();
                context.writeStringField(key, value);
            }
            context.endObject();
        }
    }

    /**
     * Writes the language registration to an already created
     * {@link TreeWriter.Context}, it must belong to a pack
     * meta file
     *
     * @param context The context where to write the registration
     */
    @Override
    public void write(TreeWriter.Context context) {
        context.startObject();
        context.writeStringField("name", name);
        context.writeStringField("region", region);
        context.writeBooleanField("bidirectional", bidirectional);
        context.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("name", name),
                ExaminableProperty.of("region", region),
                ExaminableProperty.of("bidirectional", bidirectional)
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
        return bidirectional == language.bidirectional
                && key.equals(language.key)
                && name.equals(language.name)
                && region.equals(language.region)
                && translations.equals(language.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                key, name, region,
                bidirectional, translations
        );
    }

    /**
     * Creates a new Minecraft {@link Language} instance
     *
     * @param key The language resource location and
     *                 identifier
     * @param name The language full name
     * @param region The language region or country
     * @param bidirectional True if read from right to left
     * @param translations The language translations
     */
    public static Language of(
            Key key,
            String name,
            String region,
            boolean bidirectional,
            Map<String, String> translations
    ) {
        return new Language(
                key, name, region,
                bidirectional, translations
        );
    }

    /**
     * Static factory method for our builder implementation
     * @return A new builder for {@link Language} instances
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Mutable and fluent-style builder for {@link Language}
     * instances, since it has a lot of parameters, we create
     * a builder for ease its creation
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Key key;
        private String name;
        private String region;
        private boolean bidirectional = false;
        private Map<String, String> translations = Collections.emptyMap();

        private Builder() {
        }

        public Builder key(Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        public Builder name(String name) {
            this.name = requireNonNull(name, "name");
            return this;
        }

        public Builder region(String region) {
            this.region = requireNonNull(region, "region");
            return this;
        }

        public Builder bidirectional(boolean bidirectional) {
            this.bidirectional = bidirectional;
            return this;
        }

        public Builder translations(Map<String, String> translations) {
            this.translations =  requireNonNull(translations, "translations");;
            return this;
        }

        /**
         * Finishes building the {@link Language} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created language
         */
        public Language build() {
            return new Language(
                    key, name, region,
                    bidirectional, translations
            );
        }

    }

}
