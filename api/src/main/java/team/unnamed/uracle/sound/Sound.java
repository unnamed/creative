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
package team.unnamed.uracle.sound;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;

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
public class Sound implements Element.Part, Examinable {

    private static final float DEFAULT_VOLUME = 1.0F;
    private static final float DEFAULT_PITCH = 1.0F;
    private static final int DEFAULT_WEIGHT = 1;
    private static final boolean DEFAULT_STREAM = false;
    private static final int DEFAULT_ATTENUATION_DISTANCE = 16;
    private static final boolean DEFAULT_PRELOAD = false;
    private static final Type DEFAULT_TYPE = Type.SOUND;

    /**
     * The path to this sound file, starting from
     * assets/&lt;namespace&gt;/sounds folder.
     * Doesn't include the file extension (.ogg)
     *
     * <p>May instead be the name of another sound
     * event, according to value of {@code type}</p>
     */
    private final String name;

    /**
     * The volume for playing this sound. Value
     * is a decimal between zero and one
     */
    private final float volume;

    /**
     * Plays the pitch at the specified value
     */
    private final float pitch;

    /**
     * The chance that this sound is selected to play
     * when this sound event is triggered. An example:
     * putting 2 in for the value would be like placing
     * in the name twice
     */
    private final int weight;

    /**
     * True if this sound should be streamed from its file.
     * It is recommended that this is set to "true" for sounds
     * that have a duration longer than a few seconds to avoid
     * lag. Used for all sounds in the "music" and "record"
     * categories (except Note Block sounds), as (almost) all the
     * sounds that belong to those categories are over a minute-
     * long. Setting this to false allows many more instances of
     * the sound to be run at the same time while setting it to
     * true only allows 4 instances (of that type) to be run at
     * the same time.
     */
    private final boolean stream;

    /**
     * Modify sound reduction rate based on distance. Used by
     * portals, beacons, and conduits
     */
    private final int attenuationDistance;

    /**
     * True if this sound should be loaded when loading the pack
     * instead of when the sound is played. Used by the underwater
     * ambience
     */
    private final boolean preload;

    /**
     * Specifies how the {@link Sound#name} value should
     * be interpreted
     *
     * @see Type
     */
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
    }

    /**
     * Returns the name of this sound, which can be
     * a file path or another sound event name
     *
     * @return The sound name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the sound volume, its value is between
     * 0.0 and 1.0
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
     * Returns the sound weight, which makes it
     * more possible to be selected when a sound
     * event is fired
     *
     * @return The sound weight
     */
    public int weight() {
        return weight;
    }

    /**
     * Returns the distance to modify the sound
     * reduction rate
     *
     * @return The attenuation distance
     */
    public float attenuationDistance() {
        return attenuationDistance;
    }

    /**
     * Determines whether the sound should be loaded
     * when the resource-pack is loaded, or when the
     * sound is played
     *
     * @return True if preload
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
     * Specifies the type of {@link Sound}
     *
     * @since 1.0.0
     */
    enum Type {
        /**
         * Causes the value of {@link Sound#name} to be
         * interpreted as the name of a file
         */
        SOUND,

        /**
         * Causes the value of {@link Sound#name} to be
         * interpreted as the name of an already defined
         * sound event
         */
        EVENT
    }

    @Override
    public void write(TreeWriter.Context context) {
        // in order to make some optimizations, we
        // have to do this
        if (
                volume == DEFAULT_VOLUME
                        && pitch == DEFAULT_PITCH
                        && weight == DEFAULT_WEIGHT
                        && stream == DEFAULT_STREAM
                        && attenuationDistance == DEFAULT_ATTENUATION_DISTANCE
                        && preload == DEFAULT_PRELOAD
                        && type == DEFAULT_TYPE
        ) {
            // everything is default, just write the name
            context.writeStringValue(name);
        } else {
            context.startObject();
            context.writeStringField("name", name);
            if (volume != DEFAULT_VOLUME) {
                context.writeFloatField("volume", volume);
            }
            if (pitch != DEFAULT_PITCH) {
                context.writeFloatField("pitch", pitch);
            }
            if (weight != DEFAULT_WEIGHT) {
                context.writeIntField("weight", weight);
            }
            if (stream != DEFAULT_STREAM) {
                context.writeBooleanField("stream", stream);
            }
            if (attenuationDistance != DEFAULT_ATTENUATION_DISTANCE) {
                context.writeIntField("attenuation_distance", attenuationDistance);
            }
            if (preload != DEFAULT_PRELOAD) {
                context.writeBooleanField("preload", preload);
            }
            if (type != DEFAULT_TYPE) {
                context.writeStringField("type", type.name().toLowerCase(Locale.ROOT));
            }
            context.endObject();
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
     * properties, using {@link Type#SOUND} as
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
                path.asString(), volume, pitch, weight, stream,
                attenuationDistance, preload, Type.SOUND
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
            this.type = Type.SOUND;
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
