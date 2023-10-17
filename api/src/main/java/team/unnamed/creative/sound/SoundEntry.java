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
package team.unnamed.creative.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a possible sound for a {@link SoundEvent},
 * when a sound event is called, a random sound is
 * selected to be played
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface SoundEntry extends Keyed, Examinable {
    /**
     * Creates a new {@link SoundEntry} from the given
     * properties, using {@link Type#FILE} as
     * sound type
     *
     * @param path                The sound file path
     * @param volume              The sound volume (0-1)
     * @param pitch               The sound pitch
     * @param weight              The sound weight
     * @param stream              True if the sound should be streamed
     * @param attenuationDistance The sound attenuation distance
     * @param preload             True if the sound should be preloaded
     * @return A new sound
     * @since 1.0.0
     * @deprecated Use the builder instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull SoundEntry sound(final @NotNull Key path, final float volume, final float pitch, final int weight, final boolean stream, final int attenuationDistance, final boolean preload) {
        return new SoundEntryImpl(path, volume, pitch, weight, stream, attenuationDistance, preload, Type.FILE);
    }

    /**
     * Creates a new {@link SoundEntry} from the given
     * {@link Sound sound}.
     *
     * @param sound The sound
     * @return A new sound entry
     * @since 1.1.0
     */
    static @NotNull SoundEntry soundEntry(final @NotNull Sound sound) {
        return soundEntry().type(Type.FILE).key(sound.key()).build();
    }

    /**
     * Creates a new {@link SoundEntry} from the given
     * {@link SoundEvent sound event}.
     *
     * @param soundEvent The sound event
     * @return A new sound entry
     * @since 1.1.0
     */
    static @NotNull SoundEntry soundEntry(final @NotNull SoundEvent soundEvent) {
        return soundEntry().type(Type.EVENT).key(soundEvent.key()).build();
    }

    /**
     * Creates a new {@link SoundEntry} from the given
     * {@link Sound sound}.
     *
     * @param sound The sound
     * @return A new sound entry
     * @since 1.0.0
     * @deprecated Use {@link #soundEntry(Sound)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull SoundEntry withDefaultValues(final @NotNull Sound sound) {
        return builder().nameSound(sound.key()).build();
    }

    /**
     * Creates a new {@link SoundEntry} from the given
     * properties, using {@link Type#EVENT} as
     * sound type
     *
     * @param name                The sound event name
     * @param volume              The sound volume (0-1)
     * @param pitch               The sound pitch
     * @param weight              The sound weight
     * @param stream              True if the sound should be streamed
     * @param attenuationDistance The sound attenuation distance
     * @param preload             True if the sound should be preloaded
     * @return A new sound
     * @since 1.0.0
     * @deprecated Use the builder instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull SoundEntry event(final @NotNull Key name, final float volume, final float pitch, final int weight, final boolean stream, final int attenuationDistance, final boolean preload) {
        return new SoundEntryImpl(name, volume, pitch, weight, stream, attenuationDistance, preload, Type.EVENT);
    }

    /**
     * Creates a new {@link SoundEntry} from the given
     * properties, using {@link Type#EVENT} as
     * sound type
     *
     * @param name                The sound event name
     * @param volume              The sound volume (0-1)
     * @param pitch               The sound pitch
     * @param weight              The sound weight
     * @param stream              True if the sound should be streamed
     * @param attenuationDistance The sound attenuation distance
     * @param preload             True if the sound should be preloaded
     * @return A new sound
     * @since 1.0.0
     * @deprecated Use the builder instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static SoundEntry event(final @NotNull @Subst("minecraft:sound") String name, final float volume, final float pitch, final int weight, final boolean stream, final int attenuationDistance, final boolean preload) {
        return SoundEntry.event(Key.key(name), volume, pitch, weight, stream, attenuationDistance, preload);
    }

    /**
     * Static factory method for our builder implementation
     *
     * @return A new builder for {@link SoundEntry} instances
     * @since 1.1.0
     */
    static @NotNull Builder soundEntry() {
        return new SoundEntryImpl.BuilderImpl();
    }

    /**
     * Static factory method for our builder implementation
     *
     * @return A new builder for {@link SoundEntry} instances
     * @since 1.0.0
     * @deprecated Use {@link #soundEntry()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Builder builder() {
        return soundEntry();
    }

    float DEFAULT_VOLUME = 1.0F;
    float DEFAULT_PITCH = 1.0F;
    int DEFAULT_WEIGHT = 1;
    boolean DEFAULT_STREAM = false;
    int DEFAULT_ATTENUATION_DISTANCE = 16;
    boolean DEFAULT_PRELOAD = false;
    Type DEFAULT_TYPE = Type.FILE;

    /**
     * Returns the path to this sound file, starting
     * from assets/&lt;namespace&gt;/sounds folder or
     * another sound event key
     *
     * <p>If type is FILE, the returned key is a
     * {@link Sound} key</p>
     *
     * <p>Doesn't include the file extension (.ogg)</p>
     *
     * <p>May instead be the key of another sound
     * event, according to value of {@code type}</p>
     *
     * @return The sound path or sound event key
     * @since 1.0.0
     */
    @Override
    @NotNull Key key();

    /**
     * @return The sound entry's name ({@link Key#value()})
     * @deprecated Use {@link SoundEntry#key()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    default @NotNull String name() {
        return key().value();
    }

    /**
     * Returns the volume for playing this sound.
     * Value is a decimal between zero and one
     *
     * @return The sound volume
     * @since 1.0.0
     */
    float volume();

    /**
     * Returns the sound pitch, can be higher or
     * lower than zero or one
     *
     * @return The sound pitch
     * @since 1.0.0
     */
    float pitch();

    /**
     * Returns the chance that this sound is selected to
     * play when this sound event is triggered
     *
     * <p>An example: putting 2 in for the value would be
     * like placing in the name twice</p>
     *
     * @return The sound weight
     * @since 1.0.0
     */
    int weight();

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
     * @since 1.0.0
     */
    boolean stream();

    /**
     * Returns the attenuation distance, which modifies the
     * sound reduction rate based on distance
     *
     * <p>It is used by portals, beacons, and conduits</p>
     *
     * @return The sound attenuation distance
     * @since 1.0.0
     */
    int attenuationDistance();

    /**
     * Determines whether this sound should be loaded when
     * loading the pack instead of when the sound is played
     *
     * <p>It is used by the underwater ambience</p>
     *
     * @return True to preload
     * @since 1.0.0
     */
    boolean preload();

    /**
     * Returns the sound type, which determines how
     * to interpret the {@link SoundEntry#name} property
     *
     * @return The sound type
     * @since 1.0.0
     */
    @NotNull Type type();

    /**
     * Determines if the sound has all its properties
     * with the default values
     *
     * @return True if all the properties are default
     * @since 1.0.0
     */
    boolean allDefault();

    /**
     * Specifies the type of {@link SoundEntry}
     *
     * @since 1.0.0
     */
    enum Type {
        /**
         * Causes the value of {@link SoundEntry#name} to be
         * interpreted as the name of a file
         *
         * @since 1.0.0
         */
        FILE,

        /**
         * Causes the value of {@link SoundEntry#name} to be
         * interpreted as the name of an already defined
         * sound event
         *
         * @since 1.0.0
         */
        EVENT
    }

    /**
     * Mutable and fluent-style builder for {@link SoundEntry}
     * instances, since it has a lot of parameters, we create
     * a builder for ease its creation
     *
     * @since 1.0.0
     */
    interface Builder {

        /**
         * Sets the sound type for the sound entry.
         *
         * <p>If type is {@link Type#EVENT}, the set
         * key must be a {@link SoundEvent} key.</p>
         *
         * <p>If type is {@link Type#FILE}, the set
         * key must be a {@link Sound} key.</p>
         *
         * @param type The sound type
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder type(final @NotNull Type type);

        /**
         * Sets the sound key for the sound entry.
         *
         * @param key The sound key
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        /**
         * Sets the {@link Sound} key for the sound entry.
         *
         * @param key The sound key
         * @return This builder
         * @since 1.0.0
         * @deprecated Set {@link #type} and {@link #key} separately instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        @Contract("_ -> this")
        default @NotNull Builder nameSound(final @NotNull Key key) {
            return type(Type.FILE).key(key);
        }

        /**
         * Sets the {@link SoundEvent} key for the sound entry.
         *
         * @param key The sound event key
         * @return This builder
         * @since 1.0.0
         * @deprecated Set {@link #type} and {@link #key} separately instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        @Contract("_ -> this")
        default @NotNull Builder nameEvent(final @NotNull Key key) {
            return type(Type.EVENT).key(key);
        }

        /**
         * Sets the {@link SoundEvent} name for the sound entry.
         *
         * @param name The sound event name
         * @return This builder
         * @since 1.0.0
         * @deprecated Set {@link #type} and {@link #key} separately instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        @Contract("_ -> this")
        default @NotNull Builder nameEvent(final @NotNull @Subst("minecraft:sound") String name) {
            return nameEvent(Key.key(name));
        }

        /**
         * Sets the volume for the sound.
         *
         * @param volume The sound volume
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder volume(final float volume);

        /**
         * Sets the pitch for the sound.
         *
         * @param pitch The sound pitch
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder pitch(final float pitch);

        /**
         * Sets the weight for the sound.
         *
         * @param weight The sound weight
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder weight(final int weight);

        /**
         * Sets whether the sound should be streamed.
         *
         * @param stream True if the sound should be streamed
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder stream(final boolean stream);

        /**
         * Sets the attenuation distance for the sound.
         *
         * @param attenuationDistance The attenuation distance
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder attenuationDistance(final int attenuationDistance);

        /**
         * Sets the preload value for the sound.
         *
         * @param preload True if the sound should be preloaded
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder preload(final boolean preload);

        /**
         * Finishes building the {@link SoundEntry} instance,
         * this method may fail if values were not correctly
         * provided.
         *
         * @return The recently created sound
         * @since 1.0.0
         */
        @NotNull SoundEntry build();
    }
}
