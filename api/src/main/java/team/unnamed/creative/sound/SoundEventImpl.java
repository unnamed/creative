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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

final class SoundEventImpl implements SoundEvent {

    private final Key key;
    private final boolean replace;
    private final String subtitle;
    private final List<SoundEntry> sounds;

    SoundEventImpl(
            final @NotNull Key key,
            final boolean replace,
            final @Nullable String subtitle,
            final @NotNull List<SoundEntry> sounds
    ) {
        requireNonNull(key, "key");
        requireNonNull(sounds, "sounds");
        this.key = key;
        this.replace = replace;
        this.subtitle = subtitle;
        this.sounds = immutableListOf(sounds);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public boolean replace() {
        return replace;
    }

    @Override
    public @Nullable String subtitle() {
        return subtitle;
    }

    @Override
    public @Unmodifiable @NotNull List<SoundEntry> sounds() {
        return sounds;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return SoundEvent.soundEvent().key(key).replace(replace).subtitle(subtitle).sounds(sounds);
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
        SoundEventImpl that = (SoundEventImpl) o;
        return replace == that.replace
                && Objects.equals(subtitle, that.subtitle)
                && Objects.equals(sounds, that.sounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replace, subtitle, sounds);
    }

    static final class BuilderImpl implements Builder {

        private Key key;
        private boolean replace = DEFAULT_REPLACE;
        private String subtitle;
        private List<SoundEntry> sounds = new ArrayList<>();

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder replace(final boolean replace) {
            this.replace = replace;
            return this;
        }

        @Override
        public @NotNull Builder subtitle(final @Nullable String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        @Override
        public @NotNull Builder sounds(final @NotNull List<SoundEntry> sounds) {
            requireNonNull(sounds, "sounds");
            this.sounds = new ArrayList<>(sounds);
            return this;
        }

        @Override
        public @NotNull Builder addSound(@NotNull SoundEntry soundEntry) {
            requireNonNull(soundEntry, "soundEntry");
            this.sounds.add(soundEntry);
            return this;
        }

        @Override
        public @NotNull SoundEvent build() {
            return new SoundEventImpl(key, replace, subtitle, sounds);
        }
    }
}
