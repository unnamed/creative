/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.overlays.OverlayEntry;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.serialize.minecraft.base.PackFormatSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class OverlaysMetaCodec implements MetadataPartCodec<OverlaysMeta> {

    static final MetadataPartCodec<OverlaysMeta> INSTANCE = new OverlaysMetaCodec();

    private OverlaysMetaCodec() {
    }

    @Override
    public @NotNull Class<OverlaysMeta> type() {
        return OverlaysMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "overlays";
    }

    @Override
    public @NotNull OverlaysMeta read(final @NotNull JsonObject node) {
        final JsonArray entries = node.getAsJsonArray("entries");
        final List<OverlayEntry> overlays = new ArrayList<>();
        for (final JsonElement entryNode : entries) {
            final JsonObject entryObject = entryNode.getAsJsonObject();
            final PackFormat formats = PackFormatSerializer.deserialize(entryObject.get("formats"));
            @Subst("dir") final String directory = entryObject.get("directory").getAsString();
            overlays.add(OverlayEntry.of(formats, directory));
        }
        return OverlaysMeta.of(overlays);
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull OverlaysMeta overlays) throws IOException {
        writer.beginObject();
        writer.name("entries");
        writer.beginArray();
        for (final OverlayEntry overlay : overlays.entries()) {
            writer.beginObject();
            writer.name("formats");
            PackFormatSerializer.serialize(overlay.formats(), writer);
            writer.name("directory").value(overlay.directory());

            // Formats higher than 64 are required to have min_format and max_format fields for overlays
            if (overlay.formats().min() > 64 || overlay.formats().max() > 64) {
                writer.name("min_format");
                writer.value(overlay.formats().min());
                writer.name("max_format");
                writer.value(overlay.formats().max());
            }

            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }

}