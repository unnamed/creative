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
package team.unnamed.creative.metadata.language;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

import static team.unnamed.creative.util.Validate.isNotEmpty;

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
 * @since 1.0.0
 */
public class LanguageEntry implements Examinable {

    public static final boolean DEFAULT_BIDIRECTIONAL = false;

    private final String name;
    private final String region;
    private final boolean bidirectional;

    private LanguageEntry(
            String name,
            String region,
            boolean bidirectional
    ) {
        this.name = isNotEmpty(name, "name");
        this.region = isNotEmpty(region, "region");
        this.bidirectional = bidirectional;
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
     * Returns the region or country of this language,
     * shown in the default Minecraft client language
     * menu
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

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
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
        LanguageEntry entry = (LanguageEntry) o;
        return bidirectional == entry.bidirectional
                && name.equals(entry.name)
                && region.equals(entry.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name, region,
                bidirectional
        );
    }

    /**
     * Creates a new Minecraft {@link LanguageEntry} instance
     *
     * @param name The language full name
     * @param region The language region or country
     * @param bidirectional True if read from right to left
     */
    public static LanguageEntry of(
            String name,
            String region,
            boolean bidirectional
    ) {
        return new LanguageEntry(name, region, bidirectional);
    }

    /**
     * Static factory method for our builder implementation
     * @return A new builder for {@link LanguageEntry} instances
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Mutable and fluent-style builder for {@link LanguageEntry}
     * instances, since it has a lot of parameters, we create
     * a builder for ease its creation
     *
     * @since 1.0.0
     */
    public static class Builder {

        private String name;
        private String region;
        private boolean bidirectional = false;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = isNotEmpty(name, "name");
            return this;
        }

        public Builder region(String region) {
            this.region = isNotEmpty(region, "region");
            return this;
        }

        public Builder bidirectional(boolean bidirectional) {
            this.bidirectional = bidirectional;
            return this;
        }

        /**
         * Finishes building the {@link LanguageEntry} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created language
         */
        public LanguageEntry build() {
            return new LanguageEntry(name, region, bidirectional);
        }

    }

}
