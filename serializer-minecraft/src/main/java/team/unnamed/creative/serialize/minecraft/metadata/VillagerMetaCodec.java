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

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.villager.VillagerMeta;

import java.io.IOException;
import java.util.Locale;

public class VillagerMetaCodec implements MetadataPartCodec<VillagerMeta> {

    @Override
    public Class<VillagerMeta> type() {
        return VillagerMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "villager";
    }

    @Override
    public @NotNull VillagerMeta read(final @NotNull JsonObject node) {
        String hatName = node.get("hat").getAsString();
        VillagerMeta.Hat hat = VillagerMeta.Hat.valueOf(hatName.toUpperCase(Locale.ROOT));
        return VillagerMeta.of(hat);
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull VillagerMeta villager) throws IOException {
        writer.beginObject();
        VillagerMeta.Hat hat = villager.hat();
        if (hat != VillagerMeta.Hat.NONE) {
            // only write if not default value
            writer.name("hat").value(hat.name().toLowerCase(Locale.ROOT));
        }
        writer.endObject();
    }

}
