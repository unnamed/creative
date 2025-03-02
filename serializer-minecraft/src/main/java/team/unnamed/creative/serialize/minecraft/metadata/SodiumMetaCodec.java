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
package team.unnamed.creative.serialize.minecraft.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.sodium.SodiumMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SodiumMetaCodec implements MetadataPartCodec<SodiumMeta> {

    static final MetadataPartCodec<SodiumMeta> INSTANCE = new SodiumMetaCodec();

    private SodiumMetaCodec() {
    }

    @Override
    public @NotNull Class<SodiumMeta> type() {
        return SodiumMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "sodium";
    }

    @Override
    public @NotNull SodiumMeta read(final @NotNull JsonObject node) {
        List<String> ignoredShaders = new ArrayList<>();
        if (node.has("ignored_shaders")) {
            JsonElement el = node.get("ignored_shaders");
            if (el.isJsonArray()) {
                el.getAsJsonArray().asList().forEach(element -> ignoredShaders.add(element.getAsString()));
            }
        }

        return SodiumMeta.of(ignoredShaders);
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull SodiumMeta sodium) throws IOException {
        writer.beginObject().name("ignored_shaders").beginArray();
        for (String shader : sodium.ignoredShaders()) writer.value(shader);
        writer.endArray().endObject();
    }
}
