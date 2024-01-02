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
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class SoundEntryImpl implements SoundEntry {

    private final Key key;
    private final float volume;
    private final float pitch;
    private final int weight;
    private final boolean stream;
    private final int attenuationDistance;
    private final boolean preload;
    private final SoundEntry.Type type;

    SoundEntryImpl(
            final @NotNull Key key,
            final float volume,
            final float pitch,
            final int weight,
            final boolean stream,
            final int attenuationDistance,
            final boolean preload,
            final @NotNull SoundEntry.Type type
    ) {
        this.key = requireNonNull(key, "key");
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
        if (volume <= 0) throw new IllegalArgumentException("Zero or negative volume");
        if (pitch <= 0) throw new IllegalArgumentException("Zero or negative pitch");
        if (weight <= 0) throw new IllegalArgumentException("Zero or negative weight");
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public float volume() {
        return volume;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public boolean stream() {
        return stream;
    }

    @Override
    public int attenuationDistance() {
        return attenuationDistance;
    }

    @Override
    public boolean preload() {
        return preload;
    }

    @Override
    public @NotNull Type type() {
        return type;
    }

    @Override
    public boolean allDefault() {
        return volume == DEFAULT_VOLUME
                && pitch == DEFAULT_PITCH
                && weight == DEFAULT_WEIGHT
                && stream == DEFAULT_STREAM
                && attenuationDistance == DEFAULT_ATTENUATION_DISTANCE
                && preload == DEFAULT_PRELOAD
                && type == DEFAULT_TYPE;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return SoundEntry.soundEntry().key(key).volume(volume).pitch(pitch).weight(weight).stream(stream).attenuationDistance(attenuationDistance).preload(preload).type(type);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("volume", volume),
                ExaminableProperty.of("pitch", pitch),
                ExaminableProperty.of("weight", weight),
                ExaminableProperty.of("stream", stream),
                ExaminableProperty.of("attenuationDistance", attenuationDistance),
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
        SoundEntryImpl sound = (SoundEntryImpl) o;
        return Float.compare(sound.volume, volume) == 0
                && Float.compare(sound.pitch, pitch) == 0
                && weight == sound.weight
                && stream == sound.stream
                && attenuationDistance == sound.attenuationDistance
                && preload == sound.preload
                && key.equals(sound.key)
                && type == sound.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                key, volume, pitch, weight, stream,
                attenuationDistance, preload, type
        );
    }

    static final class BuilderImpl implements Builder {

        private Key key;
        private float volume = DEFAULT_VOLUME;
        private float pitch = DEFAULT_PITCH;
        private int weight = DEFAULT_WEIGHT;
        private boolean stream = DEFAULT_STREAM;
        private int attenuationDistance = DEFAULT_ATTENUATION_DISTANCE;
        private boolean preload = DEFAULT_PRELOAD;
        private Type type = DEFAULT_TYPE;

        @Override
        public @NotNull Builder type(final @NotNull Type type) {
            this.type = requireNonNull(type, "type");
            return this;
        }

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder volume(final float volume) {
            this.volume = volume;
            return this;
        }

        @Override
        public @NotNull Builder pitch(final float pitch) {
            this.pitch = pitch;
            return this;
        }

        @Override
        public @NotNull Builder weight(final int weight) {
            this.weight = weight;
            return this;
        }

        @Override
        public @NotNull Builder stream(final boolean stream) {
            this.stream = stream;
            return this;
        }

        @Override
        public @NotNull Builder attenuationDistance(final int attenuationDistance) {
            this.attenuationDistance = attenuationDistance;
            return this;
        }

        @Override
        public @NotNull Builder preload(final boolean preload) {
            this.preload = preload;
            return this;
        }

        @Override
        public @NotNull SoundEntry build() {
            return new SoundEntryImpl(key, volume, pitch, weight, stream, attenuationDistance, preload, type);
        }
    }
}
