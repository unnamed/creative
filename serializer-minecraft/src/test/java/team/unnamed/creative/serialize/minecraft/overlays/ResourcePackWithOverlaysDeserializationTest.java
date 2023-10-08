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
package team.unnamed.creative.serialize.minecraft.overlays;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.overlay.Overlay;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;
import team.unnamed.creative.texture.Texture;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourcePackWithOverlaysDeserializationTest {

    @Test
    void test() {
        final ResourcePack resourcePack = MinecraftResourcePackReader.minecraft()
                .readFromDirectory(new File("src/test/resources/with-overlays"));

        assertEquals(1, resourcePack.textures().size(), "expected a single texture");
        final Key textureKey = Key.key("minecraft", "item/apple.png");
        final Texture texture = resourcePack.textures().iterator().next();
        assertEquals(textureKey, texture.key(), "expected a " + textureKey + " texture");
        assertEquals(2, resourcePack.overlays().size(), "expected two overlays");

        final Overlay overlayV19 = resourcePack.overlay("v19");
        assertNotNull(overlayV19, "expected a v19 overlay");
        assertEquals(1, overlayV19.textures().size(), "expected a single texture in v19 overlay");
        final Texture textureV19 = overlayV19.textures().iterator().next();
        assertEquals(textureKey, textureV19.key(), "expected a " + textureKey + " texture in v19 overlay");

        final Overlay overlayV20 = resourcePack.overlay("v20");
        assertNotNull(overlayV20, "expected a v20 overlay");
        assertEquals(1, overlayV20.textures().size(), "expected a single texture in v20 overlay");
        final Texture textureV20 = overlayV20.textures().iterator().next();
        assertEquals(textureKey, textureV20.key(), "expected a " + textureKey + " texture in v20 overlay");
    }

}
