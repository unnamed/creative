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
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.serialize.minecraft.base.PackFormatSerializer;

import java.io.IOException;

public class PackMetaCodec implements MetadataPartCodec<PackMeta> {

    @Override
    public Class<PackMeta> type() {
        return PackMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "pack";
    }

    @Override
    public @NotNull PackMeta read(final @NotNull JsonObject node) {
        int singleFormat = node.get("pack_format").getAsInt();
        String description = node.get("description").getAsString();

        PackFormat format;
        if (node.has("supported_formats")) { // since Minecraft 1.20.2 (pack format 18)
            JsonElement el = node.get("supported_formats");
            format = PackFormatSerializer.deserialize(el, singleFormat);
        } else {
            format = PackFormat.format(singleFormat);
        }

        return PackMeta.of(format, description);
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull PackMeta pack) throws IOException {
        writer.beginObject()
                .name("pack_format").value(pack.formats().format())
                .name("description").value(pack.description()); // TODO: components!

        if (!pack.formats().isSingle()) { // since Minecraft 1.20.2 (pack format 18)
            // only write min and max values if not single
            // "supported_formats": [16, 17]
            writer.name("supported_formats");
            PackFormatSerializer.serialize(pack.formats(), writer);
        }

        writer.endObject();
    }

}
