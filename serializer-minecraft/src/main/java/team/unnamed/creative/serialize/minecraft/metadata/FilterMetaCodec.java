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
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.serialize.minecraft.base.KeyPatternSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class FilterMetaCodec implements MetadataPartCodec<FilterMeta> {

    @Override
    public @NotNull Class<FilterMeta> type() {
        return FilterMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "filter";
    }

    @Override
    public @NotNull FilterMeta read(final @NotNull JsonObject node) {
        List<KeyPattern> patterns = new ArrayList<>();
        for (JsonElement filterNode : node.getAsJsonArray("block")) {
            patterns.add(KeyPatternSerializer.deserialize(filterNode.getAsJsonObject()));
        }
        return FilterMeta.of(patterns);
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull FilterMeta filter) throws IOException {
        writer.beginObject()
                .name("block")
                .beginArray();
        for (KeyPattern pattern : filter.patterns()) {
            KeyPatternSerializer.serialize(pattern, writer);
        }
        writer.endArray().endObject();
    }

}
