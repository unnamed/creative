package team.unnamed.creative.font;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.util.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

/**
 * A font provider for adding custom spacing, without needing to use bitmaps.
 * This font provider consists of a map of codepoints (characters) and integers (how many pixels to shift by)
 * If a character is used in a space font provider, it is not rendered, and is instead used as spacing.
 * You can not shift vertically with this font provider, for vertical offsets use {@link BitMapFontProvider}
 */
public class SpaceFontProvider implements FontProvider {

    private final Map<Character,Integer> advances;

    protected SpaceFontProvider(
            Map<Character,Integer> advances
    ) {
        requireNonNull(advances,"advances");
        this.advances = immutableMapOf(advances);
        validate();
    }

    private void validate() {
        for (Map.Entry<Character,Integer> entry : advances.entrySet()) {
            Character character = entry.getKey();
            Integer offset = entry.getValue();
            Validate.isNotNull(character, "An element from the character list is null");
            Validate.isNotNull(offset,"Integer object is null");
        }
    }

    public Map<Character,Integer> advances() {
        return advances;
    }
    public SpaceFontProvider advances(Map<Character,Integer> advances) {
        return new SpaceFontProvider(advances);
    }

    @Override
    public String name() {
        return "space";
    }

    public SpaceFontProvider.Builder toBuilder() {
        return FontProvider.space();
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.startObject()
                .key("type").value("space")
                .key("advances").startObject();
        for (Map.Entry<Character, Integer> entry : advances.entrySet()) {
            writer.key(Character.toString(entry.getKey())).value(entry.getValue());
        }
        writer.endObject().endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("advances", advances)
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
        SpaceFontProvider that = (SpaceFontProvider) o;
        return advances.equals(that.advances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advances);
    }

    /**
     * Mutable and fluent-style builder for {@link SpaceFontProvider}
     * instances
     */
    public static class Builder {

        private Map<Character,Integer> advances;

        protected Builder() {
        }

        public Builder advances(Map<Character,Integer> entries) {
            requireNonNull(entries, "entries");
            advances = entries;
            return this;
        }

        public Builder advance(Character key, Integer value) {
            requireNonNull(key, "key");
            requireNonNull(value, "value");
            if (this.advances == null) {
                this.advances = new HashMap<>();
            }
            this.advances.put(key, value);
            return this;
        }

        /**
         * Finishes building the {@link SpaceFontProvider} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created font
         */
        public SpaceFontProvider build() {
            return new SpaceFontProvider(advances);
        }

    }
}
