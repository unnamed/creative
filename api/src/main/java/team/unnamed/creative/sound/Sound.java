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
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.texture.Texture;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Minecraft sound (.OGG) in the resource-pack,
 * analog to {@link Texture}.
 *
 * @since 1.0.0
 */
public final class Sound implements Keyed, Examinable {

    private final Key key;
    private final Writable data;

    private Sound(Key key, Writable data) {
        this.key = requireNonNull(key, "key");
        this.data = requireNonNull(data, "data");
    }

    /**
     * Returns this sound's key (location), the key contains
     * the sound namespace and path, so that the full sound
     * path is formatted like {@code assets/<namespace>/sounds/<path>}
     *
     * <p>Example key for a sound: <pre>{@code
     *
     *   Key key = Key.key("minecraft", "ambient/cave");
     * }</pre>
     * Note that the key value must not include the sound extension</p>
     *
     * @return The sound key
     */
    @Override
    public @NotNull Key key() {
        return key;
    }

    /**
     * Returns the sound's OGG sound data, as a
     * {@link Writable} object, never null
     *
     * @return The OGG sound data
     * @since 1.0.0
     */
    public Writable data() {
        return data;
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
        return key.equals(sound.key) && data.equals(sound.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, data);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("data", data)
        );
    }

    public static Sound of(Key key, Writable data) {
        return new Sound(key, data);
    }

}
