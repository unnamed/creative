package team.unnamed.uracle.lang;

import team.unnamed.uracle.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public class Language {

    /**
     * The language JSON file location inside
     * assets/&lt;namespace&gt;/lang, the resource
     * location path also identifies this language
     */
    private final ResourceLocation resource;

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
    private final Map<String, String> translations;

    private Language(
            ResourceLocation resource,
            String name,
            String region,
            boolean bidirectional,
            Map<String, String> translations
    ) {
        requireNonNull(translations, "translations");
        this.resource = requireNonNull(resource, "resouce");
        this.name = requireNonNull(name, "name");
        this.region = requireNonNull(region, "region");
        this.bidirectional = bidirectional;
        // create a copy to avoid modifications
        this.translations = new HashMap<>(translations);
    }

    /**
     * Returns the resource location and identifier
     * for this language
     *
     * @return The language resource location
     */
    public ResourceLocation getResource() {
        return resource;
    }

    /**
     * Returns the language full name, shown in the
     * Minecraft language menu
     *
     * @return The language full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the region or country of this language
     *
     * @return The language region or country
     */
    public String getRegion() {
        return region;
    }

    /**
     * Determines if this language is bidirectional, in
     * that case, it must be read from right to left
     *
     * @return True if this language is bidirectional
     */
    public boolean isBidirectional() {
        return bidirectional;
    }

    /**
     * Returns an unmodifiable map of the language
     * translations
     *
     * @return The language translations
     */
    public Map<String, String> getTranslations() {
        return Collections.unmodifiableMap(translations);
    }

    @Override
    public String toString() {
        return "Language(" + resource + ") {"
                + "name = " + name
                + ", region = " + region
                + ", bidirectional = " + bidirectional
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return bidirectional == language.bidirectional
                && resource.equals(language.resource)
                && name.equals(language.name)
                && region.equals(language.region)
                && translations.equals(language.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                resource, name, region,
                bidirectional, translations
        );
    }

    /**
     * Creates a new Minecraft {@link Language} instance
     *
     * @param resource The language resource location and
     *                 identifier
     * @param name The language full name
     * @param region The language region or country
     * @param bidirectional True if read from right to left
     * @param translations The language translations
     */
    public static Language of(
            ResourceLocation resource,
            String name,
            String region,
            boolean bidirectional,
            Map<String, String> translations
    ) {
        return new Language(
                resource, name, region,
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

        private ResourceLocation resource;
        private String name;
        private String region;
        private boolean bidirectional = false;
        private Map<String, String> translations = Collections.emptyMap();

        private Builder() {
        }

        public Builder resource(ResourceLocation resource) {
            this.resource = requireNonNull(resource, "resource");
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
                    resource, name, region,
                    bidirectional, translations
            );
        }

    }

}
