/*
 * This file is part of uracle, licensed under the MIT license
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
package team.unnamed.uracle.sound;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.serialize.AssetWriter;
import team.unnamed.uracle.serialize.SerializableResource;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * Represents a registry of {@link SoundEvent}, or
 * "sounds.json" in the resource-pack
 *
 * @since 1.0.0
 */
public class SoundRegistry implements SerializableResource {

    private final Map<String, SoundEvent> sounds;

    private SoundRegistry(Map<String, SoundEvent> sounds) {
        requireNonNull(sounds, "sounds");
        this.sounds = immutableMapOf(sounds);
    }

    public @Unmodifiable Map<String, SoundEvent> sounds() {
        return sounds;
    }

    @Override
    public void serialize(AssetWriter writer) {
        writer.startObject();
        for (Map.Entry<String, SoundEvent> entry : sounds.entrySet()) {
            SoundEvent event = entry.getValue();
            writer.key(entry.getKey());
            event.serialize(writer);
        }
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
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
        SoundRegistry that = (SoundRegistry) o;
        return sounds.equals(that.sounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sounds);
    }

    /**
     * Creates a new registry from the given
     * sounds
     *
     * @param sounds The registered sounds
     * @return A new sound registry instance
     */
    public static SoundRegistry of(
            Map<String, SoundEvent> sounds
    ) {
        return new SoundRegistry(sounds);
    }

}
