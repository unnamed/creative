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
package team.unnamed.creative.serialize.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.RegExp;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.TextureMeta;
import team.unnamed.creative.metadata.VillagerMeta;
import team.unnamed.creative.metadata.animation.AnimationFrame;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.filter.FilterPattern;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

final class SerializerMetadata implements JsonFileStreamWriter<Metadata> {

    static final SerializerMetadata INSTANCE = new SerializerMetadata();

    static final String ANIMATION_FIELD = "animation";
    static final String FILTER_FIELD = "filter";
    static final String LANGUAGE_FIELD = "language";
    static final String PACK_FIELD = "pack";
    static final String TEXTURE_FIELD = "texture";
    static final String VILLAGER_FIELD = "villager";

    @Override
    public void serialize(Metadata metadata, JsonWriter writer) throws IOException {
        writer.beginObject();
        for (MetadataPart part : metadata.parts()) {
            if (part instanceof AnimationMeta) {
                writer.name(ANIMATION_FIELD);
                writeAnimation(writer, (AnimationMeta) part);
            } else if (part instanceof FilterMeta) {
                writer.name(FILTER_FIELD);
                writeFilter(writer, (FilterMeta) part);
            } else if (part instanceof LanguageMeta) {
                writer.name(LANGUAGE_FIELD);
                writeLanguage(writer, (LanguageMeta) part);
            } else if (part instanceof PackMeta) {
                writer.name(PACK_FIELD);
                writePack(writer, (PackMeta) part);
            } else if (part instanceof TextureMeta) {
                writer.name(TEXTURE_FIELD);
                writeTexture(writer, (TextureMeta) part);
            } else if (part instanceof VillagerMeta) {
                writer.name(VILLAGER_FIELD);
                writeVillager(writer, (VillagerMeta) part);
            } else {
                // TODO: extensibility
                throw new IllegalStateException("Cannot write unknown metadata part: " + part);
            }
        }
        writer.endObject();
    }

    public Metadata readFromTree(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        Metadata.Builder builder = Metadata.builder();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String partName = entry.getKey();
            JsonObject partObject = entry.getValue().getAsJsonObject();

            switch (partName) {
                case ANIMATION_FIELD:
                    builder.add(readAnimation(partObject));
                    break;
                case FILTER_FIELD:
                    builder.add(readFilter(partObject));
                    break;
                case LANGUAGE_FIELD:
                    builder.add(readLanguage(partObject));
                    break;
                case PACK_FIELD:
                    builder.add(readPack(partObject));
                    break;
                case TEXTURE_FIELD:
                    builder.add(readTexture(partObject));
                    break;
                case VILLAGER_FIELD:
                    builder.add(readVillager(partObject));
                    break;
                default:
                    System.err.println("Unknown metadata part name: " + partName);
                    break;
            }
        }
        return builder.build();
    }

    //#region Animation metadata section serialization
    private static void writeAnimation(JsonWriter writer, AnimationMeta animation) throws IOException {
        writer.beginObject();
        boolean interpolate = animation.interpolate();
        if (interpolate != AnimationMeta.DEFAULT_INTERPOLATE) {
            writer.name("interpolate").value(interpolate);
        }
        int width = animation.width();
        if (width != AnimationMeta.DEFAULT_WIDTH) {
            writer.name("width").value(width);
        }
        int height = animation.height();
        if (height != AnimationMeta.DEFAULT_HEIGHT) {
            writer.name("height").value(height);
        }
        int frameTime = animation.frameTime();
        if (frameTime != AnimationMeta.DEFAULT_FRAMETIME) {
            writer.name("frametime").value(frameTime);
        }
        List<AnimationFrame> frames = animation.frames();
        if (!frames.isEmpty()) {
            writer.name("frames").beginArray();
            for (AnimationFrame frame : frames) {
                int index = frame.index();
                int time = frame.frameTime();
                if (frameTime == time || frameTime == AnimationFrame.DELEGATE_FRAME_TIME) {
                    // same as default frameTime, we can skip it
                    writer.value(index);
                } else {
                    // specific frameTime, write as an object
                    writer.beginObject()
                            .name("index").value(index)
                            .name("time").value(time)
                            .endObject();
                }
            }
            writer.endArray();
        }

        writer.endObject();
    }

    private static AnimationMeta readAnimation(JsonObject node) {
        AnimationMeta.Builder animation = AnimationMeta.builder()
                .interpolate(GsonUtil.getBoolean(node, "interpolate", AnimationMeta.DEFAULT_INTERPOLATE))
                .width(GsonUtil.getInt(node, "width", AnimationMeta.DEFAULT_WIDTH))
                .height(GsonUtil.getInt(node, "height", AnimationMeta.DEFAULT_HEIGHT))
                .frameTime(GsonUtil.getInt(node, "frametime", AnimationMeta.DEFAULT_FRAMETIME));

        if (node.has("frames")) {
            List<AnimationFrame> frames = new ArrayList<>();
            for (JsonElement frameNode : node.get("frames").getAsJsonArray()) {
                if (frameNode.isJsonObject()) {
                    JsonObject frameObject = frameNode.getAsJsonObject();
                    // represents complete frame (index and frame time)
                    int time = GsonUtil.getInt(frameObject, "time", AnimationFrame.DELEGATE_FRAME_TIME);
                    int index = frameObject.get("index").getAsInt(); // required
                    frames.add(AnimationFrame.of(index, time));
                } else {
                    // represents the index only
                    int index = frameNode.getAsInt();
                    frames.add(AnimationFrame.of(index));
                }
            }
            animation.frames(frames);
        }

        return animation.build();
    }
    //#endregion

    //#region Filter metadata section serialization
    private static void writeFilter(JsonWriter writer, FilterMeta filter) throws IOException {
        writer.beginObject()
                .name("block")
                .beginArray();
        for (FilterPattern pattern : filter.patterns()) {
            Pattern namespace = pattern.namespace();
            Pattern value = pattern.value();

            writer.beginObject();
            if (namespace != null) {
                writer.name("namespace").value(namespace.pattern());
            }
            if (value != null) {
                writer.name("path").value(value.pattern());
            }
            writer.endObject();
        }
        writer.endArray().endObject();
    }

    private static FilterMeta readFilter(JsonObject node) {
        List<FilterPattern> patterns = new ArrayList<>();
        for (JsonElement filterNode : node.getAsJsonArray("block")) {
            JsonObject filterObjectNode = filterNode.getAsJsonObject();
            @RegExp String namespace = null;
            @RegExp String path = null;
            if (filterObjectNode.has("namespace")) {
                namespace = filterObjectNode.get("namespace").getAsString();
            }
            if (filterObjectNode.has("path")) {
                path = filterObjectNode.get("path").getAsString();
            }
            patterns.add(FilterPattern.of(namespace, path));
        }
        return FilterMeta.of(patterns);
    }
    //#endregion

    //#region Language metadata section serialization
    private static void writeLanguage(JsonWriter writer, LanguageMeta language) throws IOException {
        writer.beginObject();
        for (Map.Entry<String, LanguageEntry> entry : language.languages().entrySet()) {
            LanguageEntry value = entry.getValue();
            writer.name(entry.getKey())
                    .beginObject()
                    .name("name").value(value.name())
                    .name("region").value(value.region());
            boolean bidirectional = value.bidirectional();
            if (bidirectional != LanguageEntry.DEFAULT_BIDIRECTIONAL) {
                // only write if not default
                writer.name("bidirectional").value(bidirectional);
            }
            writer.endObject();
        }
        writer.endObject();
    }

    private static LanguageMeta readLanguage(JsonObject node) {
        Map<String, LanguageEntry> languages = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : node.entrySet()) {
            String code = entry.getKey();
            JsonObject languageEntryNode = entry.getValue().getAsJsonObject();

            LanguageEntry languageEntry = LanguageEntry.builder()
                    .name(languageEntryNode.get("name").getAsString())
                    .region(languageEntryNode.get("region").getAsString())
                    .bidirectional(GsonUtil.getBoolean(languageEntryNode, "bidirectional", LanguageEntry.DEFAULT_BIDIRECTIONAL))
                    .build();
            languages.put(code, languageEntry);
        }
        return LanguageMeta.of(languages);
    }
    //#endregion

    //#region Pack metadata section serialization
    private static void writePack(JsonWriter writer, PackMeta pack) throws IOException {
        writer.beginObject()
                .name("pack_format").value(pack.format())
                .name("description").value(pack.description()) // TODO: components!
                .endObject();
    }

    private static PackMeta readPack(JsonObject node) {
        int format = node.get("pack_format").getAsInt();
        String description = node.get("description").getAsString();
        return PackMeta.of(format, description);
    }
    //#endregion

    //#region Texture metadata section serialization
    private static void writeTexture(JsonWriter writer, TextureMeta texture) throws IOException {
        writer.beginObject();
        boolean blur = texture.blur();
        if (blur != TextureMeta.DEFAULT_BLUR) {
            writer.name("blur").value(blur);
        }
        boolean clamp = texture.clamp();
        if (clamp != TextureMeta.DEFAULT_CLAMP) {
            writer.name("clamp").value(clamp);
        }
        writer.endObject();
    }

    private static TextureMeta readTexture(JsonObject node) {
        boolean blur = GsonUtil.getBoolean(node, "blur", TextureMeta.DEFAULT_BLUR);
        boolean clamp = GsonUtil.getBoolean(node, "clamp", TextureMeta.DEFAULT_CLAMP);
        return TextureMeta.of(blur, clamp);
    }
    //#endregion

    //#region Villager metadata section serialization
    private static void writeVillager(JsonWriter writer, VillagerMeta villager) throws IOException {
        writer.beginObject();
        VillagerMeta.Hat hat = villager.hat();
        if (hat != VillagerMeta.Hat.NONE) {
            // only write if not default value
            writer.name("hat").value(hat.name().toLowerCase(Locale.ROOT));
        }
        writer.endObject();
    }

    private static VillagerMeta readVillager(JsonObject node) {
        String hatName = node.get("hat").getAsString();
        VillagerMeta.Hat hat = VillagerMeta.Hat.valueOf(hatName.toUpperCase(Locale.ROOT));
        return VillagerMeta.of(hat);
    }
    //#endregion

}