/*
 * This file is part of creative, licensed under the MIT license
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
package team.unnamed.creative.sound;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.file.SerializableResource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

/**
 * Represents a sound event, a compound of {@link Sound}
 * instances
 *
 * @since 1.0.0
 */
public class SoundEvent implements SerializableResource {

    public static final boolean DEFAULT_REPLACE = false;

    private final boolean replace;
    @Nullable private final String subtitle;
    @Unmodifiable private final List<Sound> sounds;

    private SoundEvent(
            boolean replace,
            @Nullable String subtitle,
            List<Sound> sounds
    ) {
        requireNonNull(sounds, "sounds");
        this.replace = replace;
        this.subtitle = subtitle;
        this.sounds = immutableListOf(sounds);
    }

    /**
     * Returns true if the sounds listed in {@link SoundEvent#sounds}
     * should replace the sounds listed in the default
     * sounds.json for this sound event. False if the
     * sounds listed should be added to the list of
     * default sounds
     *
     * @return True to replace default sounds.json sounds
     */
    public boolean replace() {
        return replace;
    }

    /**
     * Returns the sound event subtitle, which is translated as the
     * subtitle of the sound if Show Subtitles" is enabled in-game
     *
     * @return The sound subtitle
     */
    public @Nullable String subtitle() {
        return subtitle;
    }

    /**
     * The sounds this sound event uses, one of the listed sounds
     * is randomly selected to play when this sound event is triggered
     *
     * @return An unmodifiable list of the sound that this event uses
     */
    public @Unmodifiable List<Sound> sounds() {
        return sounds;
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.startObject();

        if (replace != DEFAULT_REPLACE) {
            // only write if not default (false)
            writer.key("replace").value(replace);
        }

        if (subtitle != null) {
            writer.key("subtitle").value(subtitle);
        }
        if (!sounds.isEmpty()) {
            writer.key("sounds").startArray();
            for (Sound sound : sounds) {
                sound.serialize(writer);
            }
            writer.endArray();
        }
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("replace", replace),
                ExaminableProperty.of("subtitle", subtitle),
                ExaminableProperty.of("sounds", sounds)
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
        SoundEvent that = (SoundEvent) o;
        return replace == that.replace
                && Objects.equals(subtitle, that.subtitle)
                && Objects.equals(sounds, that.sounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replace, subtitle, sounds);
    }

    /**
     * Creates a new {@link SoundEvent} from the
     * given values
     *
     * @param replace True to replace default sounds
     * @param subtitle The sound event subtitle
     * @param sounds The sound event sounds
     * @return A new {@link SoundEvent} instance
     * @since 1.0.0
     */
    public static SoundEvent of(
            boolean replace,
            @Nullable String subtitle,
            @Nullable List<Sound> sounds
    ) {
        return new SoundEvent(replace, subtitle, sounds);
    }

    /**
     * Creates a new {@link Builder} instance,
     * it eases the creation of {@link SoundEvent}
     * objects
     *
     * @return A new {@link Builder} instance
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder implementation for ease the
     * construction of {@link SoundEvent}
     * instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private boolean replace = DEFAULT_REPLACE;
        private String subtitle;
        private List<Sound> sounds = Collections.emptyList();

        private Builder() {
        }

        public Builder replace(boolean replace) {
            this.replace = replace;
            return this;
        }

        public Builder subtitle(@Nullable String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder sounds(List<Sound> sounds) {
            this.sounds = requireNonNull(sounds, "sounds");
            return this;
        }

        public Builder sounds(Sound... sounds) {
            requireNonNull(sounds, "sounds");
            this.sounds = Arrays.asList(sounds);
            return this;
        }

        /**
         * Finishes building the {@link SoundEvent} instance
         * using the previously set values
         *
         * @return A new {@link SoundEvent instance}
         */
        public SoundEvent build() {
            return new SoundEvent(replace, subtitle, sounds);
        }

    }

}
