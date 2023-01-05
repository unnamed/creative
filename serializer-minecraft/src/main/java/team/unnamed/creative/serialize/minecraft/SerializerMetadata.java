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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
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
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static com.google.gson.stream.JsonToken.END_OBJECT;

final class SerializerMetadata extends TypeAdapter<Metadata> {

    static final SerializerMetadata INSTANCE = new SerializerMetadata();

    static final String ANIMATION_FIELD = "animation";
    static final String FILTER_FIELD = "filter";
    static final String LANGUAGE_FIELD = "language";
    static final String PACK_FIELD = "pack";
    static final String TEXTURE_FIELD = "texture";
    static final String VILLAGER_FIELD = "villager";

    @Override
    public void write(JsonWriter writer, Metadata metadata) throws IOException {
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

    @Override
    public Metadata read(JsonReader reader) throws IOException {
        reader.beginObject();
        Metadata.Builder builder = Metadata.builder();
        while (reader.peek() != END_OBJECT) {
            String key = reader.nextName();
            switch (key) {
                case ANIMATION_FIELD:
                    builder.add(readAnimation(reader));
                case FILTER_FIELD:
                    builder.add(readFilter(reader));
                case LANGUAGE_FIELD:
                    builder.add(readLanguage(reader));
                case PACK_FIELD:
                    builder.add(readPack(reader));
                case TEXTURE_FIELD:
                    builder.add(readTexture(reader));
                case VILLAGER_FIELD:
                    builder.add(readVillager(reader));
            }
        }
        reader.endObject();
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

    private static AnimationMeta readAnimation(JsonReader reader) throws IOException {
        reader.beginObject();
        AnimationMeta.Builder animation = AnimationMeta.builder();
        while (reader.peek() != END_OBJECT) {
            String key = reader.nextName();
            switch (key) {
                case "interpolate":
                    animation.interpolate(reader.nextBoolean());
                    break;
                case "width":
                    animation.width(reader.nextInt());
                    break;
                case "height":
                    animation.height(reader.nextInt());
                    break;
                case "frametime":
                    animation.frameTime(reader.nextInt());
                    break;
                case "frames": {
                    reader.beginArray();
                    JsonToken tok;
                    List<AnimationFrame> frames = new ArrayList<>();
                    while ((tok = reader.peek()) != JsonToken.END_ARRAY) {
                        if (tok == JsonToken.NUMBER) {
                            // only a number, represents an index
                            frames.add(AnimationFrame.of(reader.nextInt()));
                        } else if (tok == JsonToken.BEGIN_OBJECT) {
                            reader.beginObject();
                            int index = 0;
                            int time = AnimationFrame.DELEGATE_FRAME_TIME;
                            while (reader.peek() != END_OBJECT) {
                                String name = reader.nextName();
                                switch (name) {
                                    case "index":
                                        index = reader.nextInt();
                                        break;
                                    case "time":
                                        time = reader.nextInt();
                                        break;
                                    default:
                                        throw new IllegalStateException("Unknown animation meta frame property: " + name);
                                }
                            }
                            frames.add(AnimationFrame.of(index, time));
                            reader.endObject();
                        } else {
                            throw new IllegalStateException("Expected a NUMBER or an OBJECT, found: " + tok);
                        }
                    }
                    animation.frames(frames);
                    reader.endArray();
                    break;
                }
                default:
                    throw new IllegalStateException("Unknown animation meta property: " + key);
            }
        }
        reader.endObject();
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

    private static FilterMeta readFilter(JsonReader reader) throws IOException {
        List<FilterPattern> patterns = new ArrayList<>();
        reader.beginObject();
        String name = reader.nextName();
        if (!"block".equals(name)) {
            throw new IllegalStateException("Unknown filter meta property: " + name);
        }
        reader.beginArray();
        reader.endArray();
        reader.endObject();
        return FilterMeta.of(patterns);
    }
    //#endregion

    //#region Language metadata section serialization
    private static void writeLanguage(JsonWriter writer, LanguageMeta language) throws IOException {
        writer.beginObject();
        for (Map.Entry<Key, LanguageEntry> entry : language.languages().entrySet()) {
            LanguageEntry value = entry.getValue();
            writer.name(Keys.toString(entry.getKey()))
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

    private static LanguageMeta readLanguage(JsonReader reader) throws IOException {
        Map<Key, LanguageEntry> languages = new HashMap<>();
        reader.beginObject();
        while (reader.peek() != END_OBJECT) {
            Key key = Key.key(reader.nextName());
            LanguageEntry.Builder entry = LanguageEntry.builder();
            reader.beginObject();
            while (reader.peek() != END_OBJECT) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        entry.name(reader.nextString());
                        break;
                    case "region":
                        entry.region(reader.nextString());
                        break;
                    case "bidirectional":
                        entry.bidirectional(reader.nextBoolean());
                        break;
                    default:
                        throw new IllegalStateException("Unknown language entry property: " + name);
                }
            }
            reader.endObject();
            languages.put(key, entry.build());
        }
        reader.endObject();
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

    private static PackMeta readPack(JsonReader reader) throws IOException {
        Integer format = null;
        String description = null;
        reader.beginObject();
        while (reader.peek() != END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case "pack_format":
                    format = reader.nextInt();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                default:
                    throw new IllegalStateException("Unknown pack meta property: " + name);
            }
        }
        reader.endObject();
        if (format == null || description == null) {
            throw new IllegalStateException("No 'pack_format' nor 'description' required properties found");
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

    private static TextureMeta readTexture(JsonReader reader) throws IOException {
        boolean blur = TextureMeta.DEFAULT_BLUR;
        boolean clamp = TextureMeta.DEFAULT_CLAMP;
        reader.beginObject();
        while (reader.peek() != END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case "blur":
                    blur = reader.nextBoolean();
                    break;
                case "clamp":
                    clamp = reader.nextBoolean();
                    break;
                default:
                    throw new IllegalStateException("Unknown texture meta property: " + name);
            }
        }
        reader.endObject();
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

    private static VillagerMeta readVillager(JsonReader reader) throws IOException {
        reader.beginObject();
        String name = reader.nextName();
        if (!"hat".equals(name)) {
            throw new IllegalStateException("Unknown villager meta property: " + name);
        }
        VillagerMeta.Hat hat = VillagerMeta.Hat.valueOf(reader.nextString().toUpperCase(Locale.ROOT));
        reader.endObject();
        return VillagerMeta.of(hat);
    }
    //#endregion

}