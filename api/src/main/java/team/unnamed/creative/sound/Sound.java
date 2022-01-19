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

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.file.SerializableResource;
import team.unnamed.creative.util.Keys;
import team.unnamed.creative.util.Validate;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a possible sound for a {@link SoundEvent},
 * when a sound event is called, a random sound is
 * selected to be played
 *
 * @since 1.0.0
 */
public class Sound implements SerializableResource {

    public static final float DEFAULT_VOLUME = 1.0F;
    public static final float DEFAULT_PITCH = 1.0F;
    public static final int DEFAULT_WEIGHT = 1;
    public static final boolean DEFAULT_STREAM = false;
    public static final int DEFAULT_ATTENUATION_DISTANCE = 16;
    public static final boolean DEFAULT_PRELOAD = false;
    public static final Type DEFAULT_TYPE = Type.FILE;

    private final String name;
    private final float volume;
    private final float pitch;
    private final int weight;
    private final boolean stream;
    private final int attenuationDistance;
    private final boolean preload;
    private final Type type;

    private Sound(
            String name,
            float volume,
            float pitch,
            int weight,
            boolean stream,
            int attenuationDistance,
            boolean preload,
            Type type
    ) {
        this.name = requireNonNull(name, "name");
        this.volume = volume;
        this.pitch = pitch;
        this.weight = weight;
        this.stream = stream;
        this.attenuationDistance = attenuationDistance;
        this.preload = preload;
        this.type = requireNonNull(type, "type");
        validate();
    }

    private void validate() {
        Validate.isTrue(volume > 0, "Zero or negative volume");
        Validate.isTrue(pitch > 0, "Zero or negative pitch");
        Validate.isTrue(weight > 0, "Zero or negative weight");
    }

    /**
     * Returns the path to this sound file, starting
     * from assets/&lt;namespace&gt;/sounds folder or
     * another sound name
     *
     * <p>Doesn't include the file extension (.ogg)</p>
     *
     * <p>May instead be the name of another sound
     * event, according to value of {@code type}</p>
     *
     * @return The sound name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the volume for playing this sound.
     * Value is a decimal between zero and one
     *
     * @return The sound volume
     */
    public float volume() {
        return volume;
    }

    /**
     * Returns the sound pitch, can be higher or
     * lower than zero or one
     *
     * @return The sound pitch
     */
    public float pitch() {
        return pitch;
    }

    /**
     * Returns the chance that this sound is selected to
     * play when this sound event is triggered
     *
     * <p>An example: putting 2 in for the value would be
     * like placing in the name twice</p>
     *
     * @return The sound weight
     */
    public int weight() {
        return weight;
    }

    /**
     * Returns true if this sound should be streamed from its file.
     * It is recommended that this is set to "true" for sounds
     * that have a duration longer than a few seconds to avoid
     * lag
     *
     * <p>It is used for all sounds in the "music" and "record"
     * categories (except Note Block sounds), as (almost) all the
     * sounds that belong to those categories are over a minute-
     * long</p>
     *
     * <p>Setting this to false allows many more instances of
     * the sound to be run at the same time while setting it to
     * true only allows 4 instances (of that type) to be run at
     * the same time</p>
     *
     * @return True if this sound is streamed from its file
     */
    public boolean stream() {
        return stream;
    }

    /**
     * Returns the attenuation distance, which modifies the
     * sound reduction rate based on distance
     *
     * <p>It is used by portals, beacons, and conduits</p>
     *
     * @return The sound attenuation distance
     */
    public int attenuationDistance() {
        return attenuationDistance;
    }

    /**
     * Determines whether this sound should be loaded when
     * loading the pack instead of when the sound is played
     *
     * <p>It is used by the underwater ambience</p>
     *
     * @return True to preload
     */
    public boolean preload() {
        return preload;
    }

    /**
     * Returns the sound type, which determines how
     * to interpret the {@link Sound#name} property
     *
     * @return The sound type
     */
    public Type type() {
        return type;
    }

    /**
     * Determines if the sound has all its properties
     * with the default values
     *
     * @return True if all the properties are default
     */
    public boolean allDefault() {
        return volume == DEFAULT_VOLUME
                && pitch == DEFAULT_PITCH
                && weight == DEFAULT_WEIGHT
                && stream == DEFAULT_STREAM
                && attenuationDistance == DEFAULT_ATTENUATION_DISTANCE
                && preload == DEFAULT_PRELOAD
                && type == DEFAULT_TYPE;
    }

    /**
     * Specifies the type of {@link Sound}
     *
     * @since 1.0.0
     */
    public enum Type {
        /**
         * Causes the value of {@link Sound#name} to be
         * interpreted as the name of a file
         */
        FILE,

        /**
         * Causes the value of {@link Sound#name} to be
         * interpreted as the name of an already defined
         * sound event
         */
        EVENT
    }

    @Override
    public void serialize(ResourceWriter writer) {
        // in order to make some optimizations,
        // we have to do this
        if (allDefault()) {
            // everything is default, just write the name
            writer.value(name);
        } else {
            writer.startObject()
                    .key("name").value(name);
            if (volume != DEFAULT_VOLUME) {
                writer.key("volume").value(volume);
            }
            if (pitch != DEFAULT_PITCH) {
                writer.key("pitch").value(pitch);
            }
            if (weight != DEFAULT_WEIGHT) {
                writer.key("weight").value(weight);
            }
            if (stream != DEFAULT_STREAM) {
                writer.key("stream").value(stream);
            }
            if (attenuationDistance() != DEFAULT_ATTENUATION_DISTANCE) {
                writer.key("attenuation_distance").value(attenuationDistance);
            }
            if (preload != DEFAULT_PRELOAD) {
                writer.key("preload").value(preload);
            }
            if (type != Sound.DEFAULT_TYPE) {
                writer.key("type").value(type.name().toLowerCase(Locale.ROOT));
            }
            writer.endObject();
        }
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("name", name),
                ExaminableProperty.of("volume", volume),
                ExaminableProperty.of("pitch", pitch),
                ExaminableProperty.of("weight", weight),
                ExaminableProperty.of("stream", stream),
                ExaminableProperty.of("attenuation_distance", attenuationDistance),
                ExaminableProperty.of("preload", preload),
                ExaminableProperty.of("type", type)
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
        Sound sound = (Sound) o;
        return Float.compare(sound.volume, volume) == 0
                && Float.compare(sound.pitch, pitch) == 0
                && weight == sound.weight
                && stream == sound.stream
                && attenuationDistance == sound.attenuationDistance
                && preload == sound.preload
                && name.equals(sound.name)
                && type == sound.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name, volume, pitch, weight, stream,
                attenuationDistance, preload, type
        );
    }

    /**
     * Creates a new {@link Sound} from the given
     * properties, using {@link Type#FILE} as
     * sound type
     *
     * @param path The sound file path
     * @param volume The sound volume (0-1)
     * @param pitch The sound pitch
     * @param weight The sound weight
     * @return A new sound
     */
    public static Sound sound(
            Key path, float volume, float pitch, int weight,
            boolean stream, int attenuationDistance, boolean preload
    ) {
        return new Sound(
                Keys.toString(path), volume, pitch, weight, stream,
                attenuationDistance, preload, Type.FILE
        );
    }

    /**
     * Creates a new {@link Sound} from the given
     * properties, using {@link Type#EVENT} as
     * sound type
     *
     * @param name The sound event name
     * @param volume The sound volume (0-1)
     * @param pitch The sound pitch
     * @param weight The sound weight
     * @return A new sound
     */
    public static Sound event(
            String name, float volume, float pitch, int weight,
            boolean stream, int attenuationDistance, boolean preload
    ) {
        return new Sound(
                name, volume, pitch, weight, stream,
                attenuationDistance, preload, Type.EVENT
        );
    }

    /**
     * Static factory method for our builder implementation
     * @return A new builder for {@link Sound} instances
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Mutable and fluent-style builder for {@link Sound}
     * instances, since it has a lot of parameters, we create
     * a builder for ease its creation
     *
     * @since 1.0.0
     */
    public static class Builder {

        private String name;
        private float volume = DEFAULT_VOLUME;
        private float pitch = DEFAULT_PITCH;
        private int weight = DEFAULT_WEIGHT;
        private boolean stream = DEFAULT_STREAM;
        private int attenuationDistance = DEFAULT_ATTENUATION_DISTANCE;
        private boolean preload = DEFAULT_PRELOAD;
        private Type type;

        private Builder() {
        }

        public Builder nameSound(Key key) {
            this.name = key.asString();
            this.type = Type.FILE;
            return this;
        }

        public Builder nameEvent(String name) {
            this.name = name;
            this.type = Type.EVENT;
            return this;
        }

        public Builder volume(float volume) {
            this.volume = volume;
            return this;
        }

        public Builder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder attenuationDistance(int attenuationDistance) {
            this.attenuationDistance = attenuationDistance;
            return this;
        }

        public Builder preload(boolean preload) {
            this.preload = preload;
            return this;
        }

        /**
         * Finishes building the {@link Sound} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created sound
         */
        public Sound build() {
            return new Sound(
                    name, volume, pitch, weight, stream,
                    attenuationDistance, preload, type
            );
        }

    }

}
