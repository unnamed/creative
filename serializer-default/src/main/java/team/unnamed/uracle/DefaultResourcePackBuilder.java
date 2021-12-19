/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle;

import net.kyori.adventure.key.Key;
import team.unnamed.uracle.font.BitMapFont;
import team.unnamed.uracle.font.Font;
import team.unnamed.uracle.font.LegacyUnicodeFont;
import team.unnamed.uracle.font.TrueTypeFont;
import team.unnamed.uracle.lang.Language;
import team.unnamed.uracle.lang.LanguageEntry;
import team.unnamed.uracle.sound.Sound;
import team.unnamed.uracle.sound.SoundEvent;
import team.unnamed.uracle.sound.SoundRegistry;
import team.unnamed.uracle.texture.AnimationMeta;
import team.unnamed.uracle.texture.Texture;
import team.unnamed.uracle.texture.TextureMeta;
import team.unnamed.uracle.texture.VillagerMeta;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * The default {@link ResourcePackBuilder} implementation
 * that outputs the information to a delegated {@link TreeOutputStream}.
 *
 * @since 1.0.0
 */
public class DefaultResourcePackBuilder implements ResourcePackBuilder {

    private static final String ASSETS = "assets/";
    private static final String JSON_EXT = ".json";
    private static final String PNG_EXT = ".png";
    private static final String MCMETA_EXT = ".mcmeta";

    private final TreeOutputStream output;

    public DefaultResourcePackBuilder(TreeOutputStream output) {
        this.output = output;
    }

    private void bitMapFont(BitMapFont font) throws IOException {
        writeStringField("type", "bitmap");
        writeStringField("file", font.file().asString());

        if (font.height() != BitMapFont.DEFAULT_HEIGHT) {
            // only write if height is not equal to
            // the default height
            writeIntField("height", font.height());
        }
        writeIntField("ascent", font.ascent());

        writeKey("chars");
        startArray();
        for (String character : font.characters()) {
            writeStringValue(character);
        }
        endArray();
    }

    private void legacyUnicodeFont(LegacyUnicodeFont font) throws IOException {
        writeStringField("sizes", font.sizes().asString());
        writeStringField("template", font.template().asString());
    }

    private void ttfFont(TrueTypeFont font) throws IOException {
        writeStringField("file", font.file().asString());

        writeKey("shift");
        startArray();
        writeFloatValue(font.shift().x());
        writeFloatValue(font.shift().y());
        endArray();

        writeFloatField("size", font.size());
        writeFloatField("oversample", font.oversample());

        writeKey("skip");
        startArray();
        for (String toSkip : font.skip()) {
            writeStringValue(toSkip);
        }
        endArray();
    }

    @Override
    public ResourcePackBuilder font(Key location, Font font) {
        try (Closeable ignored = output.useEntry(location.toString())) {
            startObject();
            switch (font.type()) {
                case BITMAP:
                    bitMapFont((BitMapFont) font);
                    break;
                case LEGACY_UNICODE:
                    legacyUnicodeFont((LegacyUnicodeFont) font);
                    break;
                case TTF:
                    ttfFont((TrueTypeFont) font);
                    break;
            }
            endObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackBuilder language(Key location, Language language) {
        // create the JSON file path (assets/<namespace>/lang/file.json)
        String path = ASSETS + location.namespace() + "/lang/" + location.value() + JSON_EXT;
        try (Closeable ignored = output.useEntry(path)) {
            startObject();
            for (Map.Entry<String, String> entry : language.translations().entrySet()) {
                writeStringField(
                        entry.getKey(),
                        entry.getValue()
                );
            }
            endObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackBuilder texture(Key location, Texture texture) {

        String path = ASSETS + location.namespace() + "/textures/" + location.value() + PNG_EXT;

        // write the actual texture PNG image
        try (Closeable ignored = output.useEntry(path)) {
            texture.data().write(output);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write texture", e);
        }

        TextureMeta meta = texture.meta();
        AnimationMeta animation = texture.animation();
        VillagerMeta villager = texture.villager();

        boolean hasMeta = meta != null;
        boolean hasAnimation = animation != null;
        boolean hasVillager = villager != null;

        // write the metadata
        if (hasMeta || hasAnimation || hasVillager) {
            try (Closeable ignored = output.useEntry(path + MCMETA_EXT)) {
                startObject();

                if (hasMeta) {
                    writeKey("texture");
                    startObject();
                    writeBooleanField("blur", meta.blur());
                    writeBooleanField("clamp", meta.clamp());
                    writeKey("mipmaps");
                    startArray();
                    for (int mipmap : meta.mipmaps()) {
                        writeIntValue(mipmap);
                    }
                    endArray();
                    endObject();
                }

                if (hasAnimation) {
                    int frameTime = animation.frameTime();

                    writeKey("animation");
                    startObject();
                    writeBooleanField("interpolate", animation.interpolate());
                    writeIntField("width", animation.width());
                    writeIntField("height", animation.height());
                    writeIntField("frameTime", frameTime);
                    writeKey("frames");
                    startArray();

                    for (AnimationMeta.Frame frame : animation.frames()) {
                        int index = frame.index();
                        int time = frame.frameTime();

                        if (frameTime == time) {
                            // same as default frameTime, we can
                            // skip it
                            writeIntValue(index);
                        } else {
                            // specific frameTime, write as
                            // an object
                            startObject();
                            writeIntField("index", index);
                            writeIntField("time", time);
                            endObject();
                        }
                    }

                    endArray();
                    endObject();
                }

                if (hasVillager) {
                    String hat = villager.hat();
                    writeKey("villager");
                    startObject();
                    if (hat != null) {
                        writeStringField("hat", hat);
                    }
                    endObject();
                }

                endObject();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return this;
    }

    @Override
    public ResourcePackBuilder sounds(String namespace, SoundRegistry registry) {
        String path = ASSETS + namespace + "/sounds" + JSON_EXT;

        try (Closeable ignored = output.useEntry(path)) {
            startObject();
            for (Map.Entry<String, SoundEvent> entry : registry.sounds().entrySet()) {
                SoundEvent event = entry.getValue();

                writeKey(entry.getKey());
                startObject();
                writeBooleanField("replace", event.replace());
                if (event.subtitle() != null) {
                    writeStringField("subtitle", event.subtitle());
                }
                if (event.sounds() != null) {
                    writeKey("sounds");
                    startArray();
                    for (Sound sound : event.sounds()) {
                        // in order to make some optimizations, we
                        // have to do this
                        if (sound.allDefault()) {
                            // everything is default, just write the name
                            writeStringValue(sound.name());
                        } else {
                            startObject();
                            writeStringField("name", sound.name());
                            if (sound.volume() != Sound.DEFAULT_VOLUME) {
                                writeFloatField("volume", sound.volume());
                            }
                            if (sound.pitch() != Sound.DEFAULT_PITCH) {
                                writeFloatField("pitch", sound.pitch());
                            }
                            if (sound.weight() != Sound.DEFAULT_WEIGHT) {
                                writeIntField("weight", sound.weight());
                            }
                            if (sound.stream() != Sound.DEFAULT_STREAM) {
                                writeBooleanField("stream", sound.stream());
                            }
                            if (sound.attenuationDistance() != Sound.DEFAULT_ATTENUATION_DISTANCE) {
                                writeIntField("attenuation_distance", sound.attenuationDistance());
                            }
                            if (sound.preload() != Sound.DEFAULT_PRELOAD) {
                                writeBooleanField("preload", sound.preload());
                            }
                            if (sound.type() != Sound.DEFAULT_TYPE) {
                                writeStringField("type", sound.type().name().toLowerCase(Locale.ROOT));
                            }
                            endObject();
                        }
                    }
                    endArray();
                }
                endObject();
            }
            endObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackBuilder meta(PackMeta meta) {
        try (Closeable ignored = output.useEntry("pack.mcmeta")) {
            startObject();
            writeKey("pack");
            startObject();
            writeIntField("format", meta.pack().format());
            writeStringField("description", meta.pack().description());
            endObject();

            if (!meta.languages().isEmpty()) {
                writeKey("language");
                startObject();

                for (Map.Entry<Key, LanguageEntry> entry : meta.languages().entrySet()) {
                    writeKey(entry.getKey().asString());

                    LanguageEntry language = entry.getValue();
                    startObject();
                    writeStringField("name", language.name());
                    writeStringField("region", language.region());
                    writeBooleanField("bidirectional", language.bidirectional());
                    endObject();
                }

                endObject();
            }
            endObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackBuilder file(String path, Writable data) {
        try {
            output.useEntry(path);
            data.write(output);
            output.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    // utility methods to write json
    private void startObject() throws IOException {
        output.write('{');
    }

    private void endObject() throws IOException {
        output.write('}');
    }

    private void startArray() throws IOException {
        output.write('[');
    }

    private void endArray() throws IOException {
        output.write(']');
    }

    private void writeStringValue(String string) throws IOException {
        output.write('"');
        output.write(encode(escape(string)));
        output.write('"');
    }

    private void writeKey(String key) throws IOException {
        writeStringValue(key);
        output.write(':');
    }

    private void writeIntField(String name, int value) throws IOException {
        writeKey(name);
        writeIntValue(value);
    }

    private void writeFloatValue(float value) throws IOException {
        output.write(encode(Float.toString(value)));
    }

    private void writeIntValue(int value) throws IOException {
        output.write(encode(Integer.toString(value)));
    }

    private void writeFloatField(String name, float value) throws IOException {
        writeKey(name);
        output.write(encode(Float.toString(value)));
    }

    private void writeStringField(String name, String value) throws IOException {
        writeKey(name);
        writeStringValue(value);
    }

    private void writeBooleanField(String name, boolean value) throws IOException {
        writeKey(name);
        output.write(encode(Boolean.toString(value)));
    }

    private byte[] encode(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String str) {
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"') {
                builder.append('\\').append(c);
            } else if (c == '\n') {
                builder.append("\\n");
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

}
