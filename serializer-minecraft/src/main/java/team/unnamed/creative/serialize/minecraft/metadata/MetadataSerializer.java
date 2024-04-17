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
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetadataSerializer implements JsonResourceSerializer<Metadata> {

    public static final MetadataSerializer INSTANCE = new MetadataSerializer();

    private static final Map<String, MetadataPartCodec<?>> CODECS = new LinkedHashMap<>();

    static {
        registerCodec(AnimationMetaCodec.INSTANCE);
        registerCodec(new FilterMetaCodec());
        registerCodec(new LanguageMetaCodec());
        registerCodec(PackMetaCodec.INSTANCE);
        registerCodec(new TextureMetaCodec());
        registerCodec(new VillagerMetaCodec());
        registerCodec(OverlaysMetaCodec.INSTANCE);
        registerCodec(new GuiMetaCodec());
    }

    @Override
    public void serializeToJson(Metadata metadata, JsonWriter writer) throws IOException {
        writer.beginObject();
        for (MetadataPart part : metadata.parts()) {
            MetadataPartCodec codec = null;
            for (MetadataPartCodec<?> c : CODECS.values()) {
                if (c.type().isInstance(part)) {
                    codec = c;
                    break;
                }
            }
            if (codec == null) {
                throw new IllegalStateException("Cannot find codec for metadata part: " + part);
            }

            writer.name(codec.name());
            codec.write(writer, part);
        }
        writer.endObject();
    }

    public Metadata readFromTree(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        Metadata.Builder builder = Metadata.builder();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String partName = entry.getKey();
            JsonObject partObject = entry.getValue().getAsJsonObject();

            MetadataPartCodec<?> codec = CODECS.get(partName);
            if (codec == null) {
                throw new IllegalArgumentException("Unknown metadata part name: " + partName);
            }
            deserializeAndAdd(builder, codec, partObject);
        }
        return builder.build();
    }

    private <T extends MetadataPart> void deserializeAndAdd(Metadata.Builder builder, MetadataPartCodec<T> codec, JsonObject node) {
        T part = codec.read(node);
        builder.add(codec.type(), part);
    }

    private static void registerCodec(MetadataPartCodec<?> codec) {
        CODECS.put(codec.name(), codec);
    }

}