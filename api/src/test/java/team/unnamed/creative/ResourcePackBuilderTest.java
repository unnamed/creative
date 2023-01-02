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
package team.unnamed.creative;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.blockstate.Condition;
import team.unnamed.creative.blockstate.MultiVariant;
import team.unnamed.creative.blockstate.Selector;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.file.FileTree;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.filter.FilterPattern;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class ResourcePackBuilderTest {

    @Test
    public void test() {
        ResourcePackBuilder builder = ResourcePackBuilder.create();

        // Required data
        builder.meta(12, "Hello world!");

        builder.filter(
                FilterPattern.ofNamespace("minecraft"),
                FilterPattern.ofValue("assets/file/here/test.json")
        );

        // Block States
        builder.blockState(
                BlockState.of(
                        Key.key("creative", "the/block_state"),
                        Selector.of(Condition.match("a", "b"), MultiVariant.of(Variant.builder()
                                .model(Key.key("minecraft", "new/block/state"))
                                .uvLock(false)
                                .x(90).y(90)
                                .weight(2)
                                .build()))
                )
        );

        // Fonts
        builder.font(Font.of(
                Key.key(Key.MINECRAFT_NAMESPACE, "default"),
                FontProvider.bitMap()
                        .file(Key.key("creative", "path/to/bitmap"))
                        .height(8)
                        .ascent(7)
                        .characters(
                                "abc",
                                "def"
                        )
                        .build()
        ));

        // Languages
        builder.language(
                Language.builder()
                    .key(Key.key("creative", "the_language"))
                    .translation("ok", "YEAH!")
                    .translation("no", "NO!")
                    .build()
        );

        // Models
        // builder.model(...)

        // Sound Registries
        builder.sounds(SoundRegistry.of(
                "creative",
                new HashMap<String, SoundEvent>() {{
                    put("sound", SoundEvent.of(false, "Subtitle", Arrays.asList(
                            Sound.builder()
                                    .nameSound(Key.key("creative", "path/to/sound"))
                                    .pitch(1.0F)
                                    .volume(5.0F)
                                    .weight(1)
                                    .build()
                    )));
                }}
        ));

        // Sound Files
        builder.sound(Sound.File.of(
                Key.key("custom", "cat/meow"),
                Writable.bytes(new byte[0]) // TODO:
        ));

        // Textures
        builder.texture(
                Key.key("creative", "heart_eyes"),
                Writable.resource(getClass().getClassLoader(), "heart_eyes.png")
        );

        try (FileTree tree = FileTree.directory(new File("/home/nd/Desktop/generated-resource-pack"))){
            builder.writeTo(tree);
        }
    }

}
