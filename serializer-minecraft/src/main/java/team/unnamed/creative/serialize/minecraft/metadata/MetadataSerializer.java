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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.metadata.overlays.OverlayEntry;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.metadata.texture.TextureMeta;
import team.unnamed.creative.metadata.villager.VillagerMeta;
import team.unnamed.creative.metadata.animation.AnimationFrame;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.serialize.minecraft.GsonUtil;
import team.unnamed.creative.serialize.minecraft.base.KeyPatternSerializer;
import team.unnamed.creative.serialize.minecraft.base.PackFormatSerializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MetadataSerializer implements JsonResourceSerializer<Metadata> {

    public static final MetadataSerializer INSTANCE = new MetadataSerializer();

    private static final String ANIMATION_FIELD = "animation";
    private static final String FILTER_FIELD = "filter";
    private static final String LANGUAGE_FIELD = "language";
    private static final String PACK_FIELD = "pack";
    private static final String TEXTURE_FIELD = "texture";
    private static final String VILLAGER_FIELD = "villager";
    private static final String OVERLAYS_FIELD = "overlays";

    @Override
    public void serializeToJson(Metadata metadata, JsonWriter writer) throws IOException {
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
            } else if (part instanceof OverlaysMeta) {
                writer.name(OVERLAYS_FIELD);
                writeOverlays(writer, (OverlaysMeta) part);
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
                case OVERLAYS_FIELD:
                    builder.add(readOverlays(partObject));
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
        for (KeyPattern pattern : filter.patterns()) {
            KeyPatternSerializer.serialize(pattern, writer);
        }
        writer.endArray().endObject();
    }

    private static FilterMeta readFilter(JsonObject node) {
        List<KeyPattern> patterns = new ArrayList<>();
        for (JsonElement filterNode : node.getAsJsonArray("block")) {
            patterns.add(KeyPatternSerializer.deserialize(filterNode.getAsJsonObject()));
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

    private static PackMeta readPack(JsonObject node) {
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

    //#region Overlays metadata section serialization
    private static void writeOverlays(final @NotNull JsonWriter writer, final @NotNull OverlaysMeta overlays) throws IOException {
        writer.beginObject();
        writer.name("entries");
        writer.beginArray();
        for (final OverlayEntry overlay : overlays.entries()) {
            writer.beginObject();
            writer.name("formats");
            PackFormatSerializer.serialize(overlay.formats(), writer);
            writer.name("directory").value(overlay.directory());
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }

    private static @NotNull OverlaysMeta readOverlays(final @NotNull JsonObject node) {
        final JsonArray entries = node.getAsJsonArray("entries");
        final List<OverlayEntry> overlays = new ArrayList<>();
        for (final JsonElement entryNode : entries) {
            final JsonObject entryObject = entryNode.getAsJsonObject();
            final PackFormat formats = PackFormatSerializer.deserialize(entryObject.get("formats"));
            @Subst("dir")
            final String directory = entryObject.get("directory").getAsString();
            overlays.add(OverlayEntry.of(formats, directory));
        }
        return OverlaysMeta.of(overlays);
    }
    //#endregion

}