/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.uracle.mock.MockTreeOutputStream;
import team.unnamed.uracle.sound.Sound;
import team.unnamed.uracle.sound.SoundEvent;
import team.unnamed.uracle.sound.SoundRegistry;

import java.util.Arrays;
import java.util.HashMap;

public class SoundSerializationTest {

    @Test
    @DisplayName("Test if SoundRegistry serialization works")
    public void test() {
        MockTreeOutputStream stream = new MockTreeOutputStream();
        ResourcePackWriter builder = new DefaultResourcePackSerializer(stream);

        SoundRegistry registry = SoundRegistry.of(new HashMap<String, SoundEvent>() {{
            put("entity.enderman.stare", SoundEvent.builder()
                    .sounds(Arrays.asList(
                            Sound.builder()
                                    .nameSound(Key.key("minecraft:idk"))
                                    .stream(true)
                                    .volume(0.5F)
                                    .pitch(2)
                                    .build(),
                            Sound.builder()
                                    .nameEvent("entity.enderman.stare")
                                    .volume(0.5F)
                                    .build()
                    ))
                    .build());

            put("entity.enderman.idk", SoundEvent.builder()
                    .replace(true)
                    .subtitle("Hello world")
                    .build());
        }});

        builder.sounds("uracle", registry);

        Assertions.assertEquals(
                "assets/uracle/sounds.json:\n" +
                        "{" +
                            "\"entity.enderman.idk\":{" +
                                "\"replace\":true," +
                                "\"subtitle\":\"Hello world\"" +
                            "}," +
                            "\"entity.enderman.stare\":{" +
                                "\"sounds\":[" +
                                    "{" +
                                        "\"name\":\"minecraft:idk\"," +
                                        "\"volume\":0.5," +
                                        "\"pitch\":2.0," +
                                        "\"stream\":true" +
                                    "}," +
                                    "{" +
                                        "\"name\":\"entity.enderman.stare\"," +
                                        "\"volume\":0.5," +
                                        "\"type\":\"event\"" +
                                    "}" +
                                "]"+
                            "}" +
                        "}\n",
                stream.output()
        );
    }

}
