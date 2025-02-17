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
package team.unnamed.creative.serialize.minecraft.sound;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEntry;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SoundRegistrySerializationTest {
    @Test
    void test() {
        // No sounds, all other options are default
        assertSerializes(
                "{\"sound1\":{\"sounds\":[]}}",
                SoundRegistry.soundRegistry()
                        .namespace("creative")
                        .sound(SoundEvent.soundEvent(Key.key("creative", "sound1"), false, null, Collections.emptyList()))
        );

        // Has sounds, all other options are default
        assertSerializes(
                "{\"sound1\":{\"sounds\":[\"example\",\"custom:example2\"]}}",
                SoundRegistry.soundRegistry()
                        .namespace("help")
                        .sound(SoundEvent.soundEvent(Key.key("help", "sound1"), false, null, Arrays.asList(
                                SoundEntry.soundEntry(Sound.sound(Key.key("example"), Writable.EMPTY)),
                                SoundEntry.soundEntry(Sound.sound(Key.key("custom:example2"), Writable.EMPTY))
                        )))
        );

        // No sounds, custom options
        assertSerializes(
                "{\"sound1\":{\"replace\":true,\"subtitle\":\"The Sound 1\",\"sounds\":[]}}",
                SoundRegistry.soundRegistry()
                        .namespace("creative")
                        .sound(SoundEvent.soundEvent(Key.key("creative", "sound1"), true, "The Sound 1", Collections.emptyList()))
        );
    }

    void assertSerializes(final @NotNull String expected, final @NotNull SoundRegistry.Builder builder) {
        final SoundRegistry soundRegistry;
        try {
            soundRegistry = builder.build();
        } catch (final Exception e) {
            fail("Failed to build SoundRegistry!", e);
            throw e;
        }

        try {
            assertEquals(expected, SoundRegistrySerializer.INSTANCE.serializeToJsonString(soundRegistry));
        } catch (final IOException e) {
            fail("Exception caught trying to serialize SoundRegistry!", e);
            throw new RuntimeException(e);
        }
    }
}
