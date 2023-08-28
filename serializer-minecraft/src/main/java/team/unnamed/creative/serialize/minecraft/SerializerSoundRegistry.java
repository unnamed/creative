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
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class SerializerSoundRegistry implements JsonResourceSerializer<SoundRegistry> {

    static final SerializerSoundRegistry INSTANCE = new SerializerSoundRegistry();

    @Override
    public void serializeToJson(SoundRegistry registry, JsonWriter writer) throws IOException {
        writer.beginObject();
        for (Map.Entry<String, SoundEvent> entry : registry.sounds().entrySet()) {
            SoundEvent event = entry.getValue();
            writer.name(entry.getKey())
                    .beginObject();

            boolean replace = event.replace();
            if (replace != SoundEvent.DEFAULT_REPLACE) {
                // only write if not default (false)
                writer.name("replace").value(replace);
            }

            String subtitle = event.subtitle();
            if (subtitle != null) {
                writer.name("subtitle").value(subtitle);
            }

            List<Sound> sounds = event.sounds();
            if (!sounds.isEmpty()) {
                writer.name("sounds").beginArray();
                for (Sound sound : sounds) {
                    // in order to make some optimizations,
                    // we have to do this
                    if (sound.allDefault()) {
                        // everything is default, just write the name
                        writer.value(Keys.toString(sound.key()));
                    } else {
                        writer.beginObject()
                                .name("name").value(Keys.toString(sound.key()));
                        float volume = sound.volume();
                        if (volume != Sound.DEFAULT_VOLUME) {
                            writer.name("volume").value(volume);
                        }
                        float pitch = sound.pitch();
                        if (pitch != Sound.DEFAULT_PITCH) {
                            writer.name("pitch").value(pitch);
                        }
                        float weight = sound.weight();
                        if (weight != Sound.DEFAULT_WEIGHT) {
                            writer.name("weight").value(weight);
                        }
                        boolean stream = sound.stream();
                        if (stream != Sound.DEFAULT_STREAM) {
                            writer.name("stream").value(stream);
                        }
                        int attenuationDistance = sound.attenuationDistance();
                        if (attenuationDistance != Sound.DEFAULT_ATTENUATION_DISTANCE) {
                            writer.name("attenuation_distance").value(attenuationDistance);
                        }
                        boolean preload = sound.preload();
                        if (preload != Sound.DEFAULT_PRELOAD) {
                            writer.name("preload").value(preload);
                        }
                        Sound.Type type = sound.type();
                        if (type != Sound.DEFAULT_TYPE) {
                            writer.name("type").value(type.name().toLowerCase(Locale.ROOT));
                        }
                        writer.endObject();
                    }
                }
                writer.endArray();
            }
            writer.endObject();
        }
        writer.endObject();
    }

    public SoundRegistry readFromTree(JsonElement node, String namespace) {
        Map<String, SoundEvent> soundEvents = new HashMap<>();
        JsonObject objectNode = node.getAsJsonObject();

        for (Map.Entry<String, JsonElement> soundEventEntry : objectNode.entrySet()) {
            String eventKey = soundEventEntry.getKey();
            JsonObject eventNode = soundEventEntry.getValue().getAsJsonObject();
            SoundEvent.Builder event = SoundEvent.builder();

            event.replace(GsonUtil.getBoolean(eventNode, "replace", SoundEvent.DEFAULT_REPLACE));

            if (!GsonUtil.isNullOrAbsent(eventNode, "subtitle")) {
                event.subtitle(eventNode.get("subtitle").getAsString());
            }

            if (eventNode.has("sounds")) {
                List<Sound> sounds = new ArrayList<>();

                for (JsonElement soundNode : eventNode.getAsJsonArray("sounds")) {
                    if (soundNode.isJsonObject()) {
                        // complete sound object
                        JsonObject soundObjectNode = soundNode.getAsJsonObject();

                        Sound.Builder sound = Sound.builder()
                                .key(Key.key(soundObjectNode.get("name").getAsString()))
                                .volume(GsonUtil.getFloat(soundObjectNode, "volume", Sound.DEFAULT_VOLUME))
                                .pitch(GsonUtil.getFloat(soundObjectNode, "pitch", Sound.DEFAULT_PITCH))
                                .weight(GsonUtil.getInt(soundObjectNode, "weight", Sound.DEFAULT_WEIGHT))
                                .stream(GsonUtil.getBoolean(soundObjectNode, "stream", Sound.DEFAULT_STREAM))
                                .attenuationDistance(GsonUtil.getInt(soundObjectNode, "attenuation_distance", Sound.DEFAULT_ATTENUATION_DISTANCE))
                                .preload(GsonUtil.getBoolean(soundObjectNode, "preload", Sound.DEFAULT_PRELOAD));

                        if (soundObjectNode.has("type")) {
                            String typeName = soundObjectNode.get("type").getAsString();
                            Sound.Type type = Sound.Type.valueOf(typeName.toUpperCase(Locale.ROOT));
                            sound.type(type);
                        }
                        sounds.add(sound.build());
                    } else {
                        // everything is default, just read the name
                        sounds.add(Sound.builder()
                                .key(Key.key(soundNode.getAsString()))
                                .type(Sound.Type.FILE)
                                .build());
                    }
                }

                event.sounds(sounds);
            }

            soundEvents.put(eventKey, event.build());
        }
        return SoundRegistry.of(namespace, soundEvents);
    }

}