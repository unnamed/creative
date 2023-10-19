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
package team.unnamed.creative.serialize.minecraft.model;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Axis3D;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Readable;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.ElementRotation;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.texture.TextureUV;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelDeserializationTest {

    @Test
    void test_deserialize_cross() throws Exception {
        Model model = ModelSerializer.INSTANCE.deserialize(
                Readable.resource(getClass().getClassLoader(), "model/cross.json"),
                Key.key("block/cross")
        );

        assertEquals(
                Model.builder()
                        .key(Key.key("minecraft:block/cross"))
                        .ambientOcclusion(false)
                        .textures(ModelTextures.builder()
                                .particle(ModelTexture.ofReference("cross"))
                                .build())
                        .elements(
                                Element.element()
                                        .from(new Vector3Float(0.8f, 0.0f, 8.0f))
                                        .to(new Vector3Float(15.2f, 16.0f, 8.0f))
                                        .rotation(
                                                ElementRotation.builder()
                                                        .origin(new Vector3Float(8.0f, 8.0f, 8.0f))
                                                        .axis(Axis3D.Y)
                                                        .angle(45.0f)
                                                        .rescale(true)
                                                        .build()
                                        )
                                        .shade(false)
                                        .faces(new HashMap<CubeFace, ElementFace>() {{
                                            put(CubeFace.SOUTH, ElementFace.face(
                                                    TextureUV.uv(0.0f, 0.0f, 1.0f, 1.0f),
                                                    "#cross",
                                                    null,
                                                    0,
                                                    -1
                                            ));
                                            put(CubeFace.NORTH, ElementFace.face(
                                                    TextureUV.uv(0.0f, 0.0f, 1.0f, 1.0f),
                                                    "#cross",
                                                    null,
                                                    0,
                                                    -1
                                            ));
                                        }})
                                        .build(),
                                Element.element()
                                        .from(new Vector3Float(8.0f, 0.0f, 0.8f))
                                        .to(new Vector3Float(8.0f, 16.0f, 15.2f))
                                        .rotation(
                                                ElementRotation.builder()
                                                        .origin(new Vector3Float(8.0f, 8.0f, 8.0f))
                                                        .axis(Axis3D.Y)
                                                        .angle(45.0f)
                                                        .rescale(true)
                                                        .build()
                                        )
                                        .shade(false)
                                        .faces(new HashMap<CubeFace, ElementFace>() {{
                                            put(CubeFace.WEST, ElementFace.face(
                                                    TextureUV.uv(0.0f, 0.0f, 1.0f, 1.0f),
                                                    "#cross",
                                                    null,
                                                    0,
                                                    -1
                                            ));
                                            put(CubeFace.EAST, ElementFace.face(
                                                    TextureUV.uv(0.0f, 0.0f, 1.0f, 1.0f),
                                                    "#cross",
                                                    null,
                                                    0,
                                                    -1
                                            ));
                                        }})
                                        .build()
                        )
                        .build(),
                model
        );
    }

}
