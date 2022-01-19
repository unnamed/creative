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
package team.unnamed.creative.texture;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.file.FileResource;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.metadata.Metadata;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Texture implements Keyed, FileResource {

    private final Key key;
    private final Writable data;
    private final Metadata meta;

    private Texture(
            Key key,
            Writable data,
            Metadata meta
    ) {
        this.key = requireNonNull(key, "key");
        this.data = requireNonNull(data, "data");
        this.meta = requireNonNull(meta, "meta");
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public Writable data() {
        return data;
    }

    @Override
    public Metadata meta() {
        return meta;
    }

    @Override
    public String path() {
        return "assets/" + key.namespace() + "/textures/" + key.value() + ".png";
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.write(data);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("data", data),
                ExaminableProperty.of("meta", meta)
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
        Texture texture = (Texture) o;
        return key.equals(texture.key)
                && data.equals(texture.data)
                && meta.equals(texture.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, data, meta);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key key;
        private Writable data;
        private Metadata meta = Metadata.EMPTY;

        private Builder() {
        }

        public Builder key(Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        public Builder data(Writable data) {
            this.data = requireNonNull(data, "data");
            return this;
        }

        public Builder meta(Metadata meta) {
            this.meta = requireNonNull(meta, "meta");
            return this;
        }

        public Texture build() {
            return new Texture(key, data, meta);
        }

    }

}
