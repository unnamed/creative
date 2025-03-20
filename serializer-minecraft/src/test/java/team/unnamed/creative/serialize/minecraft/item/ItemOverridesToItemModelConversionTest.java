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
package team.unnamed.creative.serialize.minecraft.item;

import net.kyori.adventure.key.Key;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.model.ItemOverride;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.minecraft.model.ModelSerializer;

import java.util.List;

@SuppressWarnings("deprecation")
class ItemOverridesToItemModelConversionTest {
//    @Test
//    void test() throws Exception {
//        final Model bowModel = ModelSerializer.INSTANCE.deserializeFromJsonString("{\n" +
//                "  \"parent\": \"item/generated\",\n" +
//                "  \"textures\": {\n" +
//                "    \"layer0\": \"item/bow\"\n" +
//                "  },\n" +
//                "  \"display\": {\n" +
//                "    \"thirdperson_righthand\": {\n" +
//                "      \"rotation\": [\n" +
//                "        -80,\n" +
//                "        260,\n" +
//                "        -40\n" +
//                "      ],\n" +
//                "      \"translation\": [\n" +
//                "        -1,\n" +
//                "        -2,\n" +
//                "        2.5\n" +
//                "      ],\n" +
//                "      \"scale\": [\n" +
//                "        0.9,\n" +
//                "        0.9,\n" +
//                "        0.9\n" +
//                "      ]\n" +
//                "    },\n" +
//                "    \"thirdperson_lefthand\": {\n" +
//                "      \"rotation\": [\n" +
//                "        -80,\n" +
//                "        -280,\n" +
//                "        40\n" +
//                "      ],\n" +
//                "      \"translation\": [\n" +
//                "        -1,\n" +
//                "        -2,\n" +
//                "        2.5\n" +
//                "      ],\n" +
//                "      \"scale\": [\n" +
//                "        0.9,\n" +
//                "        0.9,\n" +
//                "        0.9\n" +
//                "      ]\n" +
//                "    },\n" +
//                "    \"firstperson_righthand\": {\n" +
//                "      \"rotation\": [\n" +
//                "        0,\n" +
//                "        -90,\n" +
//                "        25\n" +
//                "      ],\n" +
//                "      \"translation\": [\n" +
//                "        1.13,\n" +
//                "        3.2,\n" +
//                "        1.13\n" +
//                "      ],\n" +
//                "      \"scale\": [\n" +
//                "        0.68,\n" +
//                "        0.68,\n" +
//                "        0.68\n" +
//                "      ]\n" +
//                "    },\n" +
//                "    \"firstperson_lefthand\": {\n" +
//                "      \"rotation\": [\n" +
//                "        0,\n" +
//                "        90,\n" +
//                "        -25\n" +
//                "      ],\n" +
//                "      \"translation\": [\n" +
//                "        1.13,\n" +
//                "        3.2,\n" +
//                "        1.13\n" +
//                "      ],\n" +
//                "      \"scale\": [\n" +
//                "        0.68,\n" +
//                "        0.68,\n" +
//                "        0.68\n" +
//                "      ]\n" +
//                "    }\n" +
//                "  },\n" +
//                "  \"overrides\": [\n" +
//                "    {\n" +
//                "      \"predicate\": {\n" +
//                "        \"pulling\": 1\n" +
//                "      },\n" +
//                "      \"model\": \"item/bow_pulling_0\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"predicate\": {\n" +
//                "        \"pulling\": 1,\n" +
//                "        \"pull\": 0.65\n" +
//                "      },\n" +
//                "      \"model\": \"item/bow_pulling_1\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"predicate\": {\n" +
//                "        \"pulling\": 1,\n" +
//                "        \"pull\": 0.9\n" +
//                "      },\n" +
//                "      \"model\": \"item/bow_pulling_2\"\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}", Key.key("minecraft", "item/bow"));
//
//        final List<ItemModel> models = ItemOverride.toItemModels(
//                ItemModel.reference(bowModel.key()),
//                bowModel.overrides()
//        );

//        // todo: smarter conversion
//        models.forEach(model -> model.examine(MultiLineStringExaminer.simpleEscaping()).forEach(System.out::println));
    //}
}
