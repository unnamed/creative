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
package team.unnamed.creative.serialize.minecraft.sound;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import team.unnamed.creative.serialize.minecraft.GsonUtil;
import team.unnamed.creative.serialize.minecraft.base.KeySerializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;
import team.unnamed.creative.sound.SoundEntry;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class SoundRegistrySerializer implements JsonResourceSerializer<SoundRegistry> {

    public static final SoundRegistrySerializer INSTANCE = new SoundRegistrySerializer();

    @Override
    public void serializeToJson(SoundRegistry registry, JsonWriter writer) throws IOException {
        writer.beginObject();
        for (SoundEvent event : registry.sounds()) {
            writer.name(event.key().value())
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

            List<SoundEntry> sounds = event.sounds();
            if (!sounds.isEmpty()) {
                writer.name("sounds").beginArray();
                for (SoundEntry sound : sounds) {
                    // in order to make some optimizations,
                    // we have to do this
                    if (sound.allDefault()) {
                        // everything is default, just write the name
                        writer.value(KeySerializer.toString(sound.key()));
                    } else {
                        writer.beginObject()
                                .name("name").value(KeySerializer.toString(sound.key()));
                        float volume = sound.volume();
                        if (volume != SoundEntry.DEFAULT_VOLUME) {
                            writer.name("volume").value(volume);
                        }
                        float pitch = sound.pitch();
                        if (pitch != SoundEntry.DEFAULT_PITCH) {
                            writer.name("pitch").value(pitch);
                        }
                        float weight = sound.weight();
                        if (weight != SoundEntry.DEFAULT_WEIGHT) {
                            writer.name("weight").value(weight);
                        }
                        boolean stream = sound.stream();
                        if (stream != SoundEntry.DEFAULT_STREAM) {
                            writer.name("stream").value(stream);
                        }
                        int attenuationDistance = sound.attenuationDistance();
                        if (attenuationDistance != SoundEntry.DEFAULT_ATTENUATION_DISTANCE) {
                            writer.name("attenuation_distance").value(attenuationDistance);
                        }
                        boolean preload = sound.preload();
                        if (preload != SoundEntry.DEFAULT_PRELOAD) {
                            writer.name("preload").value(preload);
                        }
                        SoundEntry.Type type = sound.type();
                        if (type != SoundEntry.DEFAULT_TYPE) {
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

    public SoundRegistry readFromTree(JsonElement node, @Subst("minecraft") String namespace) {
        Set<SoundEvent> soundEvents = new LinkedHashSet<>();
        JsonObject objectNode = node.getAsJsonObject();

        for (Map.Entry<String, JsonElement> soundEventEntry : objectNode.entrySet()) {
            @Subst("entity.enderman.stare")
            String eventKey = soundEventEntry.getKey();
            JsonObject eventNode = soundEventEntry.getValue().getAsJsonObject();
            SoundEvent.Builder event = SoundEvent.soundEvent()
                    .key(Key.key(namespace, eventKey));

            event.replace(GsonUtil.getBoolean(eventNode, "replace", SoundEvent.DEFAULT_REPLACE));

            if (!GsonUtil.isNullOrAbsent(eventNode, "subtitle")) {
                event.subtitle(eventNode.get("subtitle").getAsString());
            }

            if (eventNode.has("sounds")) {
                List<SoundEntry> sounds = new ArrayList<>();

                for (JsonElement soundNode : eventNode.getAsJsonArray("sounds")) {
                    if (soundNode.isJsonObject()) {
                        // complete sound object
                        JsonObject soundObjectNode = soundNode.getAsJsonObject();

                        SoundEntry.Builder sound = SoundEntry.soundEntry()
                                .key(Key.key(soundObjectNode.get("name").getAsString()))
                                .volume(GsonUtil.getFloat(soundObjectNode, "volume", SoundEntry.DEFAULT_VOLUME))
                                .pitch(GsonUtil.getFloat(soundObjectNode, "pitch", SoundEntry.DEFAULT_PITCH))
                                .weight(GsonUtil.getInt(soundObjectNode, "weight", SoundEntry.DEFAULT_WEIGHT))
                                .stream(GsonUtil.getBoolean(soundObjectNode, "stream", SoundEntry.DEFAULT_STREAM))
                                .attenuationDistance(GsonUtil.getInt(soundObjectNode, "attenuation_distance", SoundEntry.DEFAULT_ATTENUATION_DISTANCE))
                                .preload(GsonUtil.getBoolean(soundObjectNode, "preload", SoundEntry.DEFAULT_PRELOAD));

                        if (soundObjectNode.has("type")) {
                            String typeName = soundObjectNode.get("type").getAsString();
                            SoundEntry.Type type = SoundEntry.Type.valueOf(typeName.toUpperCase(Locale.ROOT));
                            sound.type(type);
                        }
                        sounds.add(sound.build());
                    } else {
                        // everything is default, just read the name
                        sounds.add(SoundEntry.soundEntry()
                                .key(Key.key(soundNode.getAsString()))
                                .type(SoundEntry.Type.FILE)
                                .build());
                    }
                }

                event.sounds(sounds);
            }

            soundEvents.add(event.build());
        }
        return SoundRegistry.soundRegistry(namespace, soundEvents);
    }

}