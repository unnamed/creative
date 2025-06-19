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
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.item.ConditionItemModel;
import team.unnamed.creative.item.Item;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.item.RangeDispatchItemModel;
import team.unnamed.creative.item.ReferenceItemModel;
import team.unnamed.creative.item.property.ItemBooleanProperty;
import team.unnamed.creative.item.property.ItemNumericProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ItemSerializationTest {
    @Test
    void test_air_deserialization() throws Exception {
        final @Language("JSON") String input = "{\n" +
                "  \"model\": {\n" +
                "    \"type\": \"minecraft:model\",\n" +
                "    \"model\": \"minecraft:item/air\"\n" +
                "  }\n" +
                "}";

        final Item item = ItemSerializer.INSTANCE.deserializeFromJsonString(input, Key.key("minecraft", "air"));

        assertEquals(Key.key("minecraft", "air"), item.key());
        assertEquals(Item.DEFAULT_HAND_ANIMATION_ON_SWAP, item.handAnimationOnSwap());
        assertEquals(Item.DEFAULT_OVERSIZED_IN_GUI, item.oversizedInGui());

        final ItemModel model = item.model();
        assertInstanceOf(ReferenceItemModel.class, model);

        final ReferenceItemModel reference = (ReferenceItemModel) model;
        assertEquals(Key.key("minecraft:item/air"), reference.model());
        assertEquals(0, reference.tints().size());
    }

    @Test
    void test_bow_deserialization() throws Exception {
        final @Language("JSON") String input = "{\n" +
                "  \"model\": {\n" +
                "    \"type\": \"minecraft:condition\",\n" +
                "    \"on_false\": {\n" +
                "      \"type\": \"minecraft:model\",\n" +
                "      \"model\": \"minecraft:item/bow\"\n" +
                "    },\n" +
                "    \"on_true\": {\n" +
                "      \"type\": \"minecraft:range_dispatch\",\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"model\": {\n" +
                "            \"type\": \"minecraft:model\",\n" +
                "            \"model\": \"minecraft:item/bow_pulling_1\"\n" +
                "          },\n" +
                "          \"threshold\": 0.65\n" +
                "        },\n" +
                "        {\n" +
                "          \"model\": {\n" +
                "            \"type\": \"minecraft:model\",\n" +
                "            \"model\": \"minecraft:item/bow_pulling_2\"\n" +
                "          },\n" +
                "          \"threshold\": 0.9\n" +
                "        }\n" +
                "      ],\n" +
                "      \"fallback\": {\n" +
                "        \"type\": \"minecraft:model\",\n" +
                "        \"model\": \"minecraft:item/bow_pulling_0\"\n" +
                "      },\n" +
                "      \"property\": \"minecraft:use_duration\",\n" +
                "      \"scale\": 0.05\n" +
                "    },\n" +
                "    \"property\": \"minecraft:using_item\"\n" +
                "  }\n" +
                "}";
        final Item item = ItemSerializer.INSTANCE.deserializeFromJsonString(input, Key.key("minecraft", "bow"));

        assertEquals(Key.key("minecraft", "bow"), item.key());
        assertEquals(Item.DEFAULT_HAND_ANIMATION_ON_SWAP, item.handAnimationOnSwap());
        assertEquals(Item.DEFAULT_OVERSIZED_IN_GUI, item.oversizedInGui());

        final ItemModel model = item.model();
        assertInstanceOf(ConditionItemModel.class, model);

        final ConditionItemModel condition = (ConditionItemModel) model;
        final ItemBooleanProperty property = condition.condition();

        assertEquals(ItemBooleanProperty.usingItem(), property);

        final ItemModel onFalse = condition.onFalse();
        assertInstanceOf(ReferenceItemModel.class, onFalse);

        final ReferenceItemModel reference = (ReferenceItemModel) onFalse;
        assertEquals(Key.key("minecraft:item/bow"), reference.model());
        assertEquals(0, reference.tints().size());

        final ItemModel onTrue = condition.onTrue();
        assertInstanceOf(RangeDispatchItemModel.class, onTrue);

        final RangeDispatchItemModel rangeDispatch = (RangeDispatchItemModel) onTrue;
        assertEquals(ItemNumericProperty.useDuration(), rangeDispatch.property());
        assertEquals(0.05f, rangeDispatch.scale());

        final ItemModel fallback = rangeDispatch.fallback();
        assertInstanceOf(ReferenceItemModel.class, fallback);

        final ReferenceItemModel fallbackReference = (ReferenceItemModel) fallback;
        assertEquals(Key.key("minecraft:item/bow_pulling_0"), fallbackReference.model());
        assertEquals(0, fallbackReference.tints().size());

        assertEquals(2, rangeDispatch.entries().size());

        final RangeDispatchItemModel.Entry entry1 = rangeDispatch.entries().get(0);
        assertEquals(0.65f, entry1.threshold());

        final ItemModel model1 = entry1.model();
        assertInstanceOf(ReferenceItemModel.class, model1);

        final ReferenceItemModel reference1 = (ReferenceItemModel) model1;
        assertEquals(Key.key("minecraft:item/bow_pulling_1"), reference1.model());
        assertEquals(0, reference1.tints().size());

        final RangeDispatchItemModel.Entry entry2 = rangeDispatch.entries().get(1);
        assertEquals(0.9f, entry2.threshold());

        final ItemModel model2 = entry2.model();
        assertInstanceOf(ReferenceItemModel.class, model2);

        final ReferenceItemModel reference2 = (ReferenceItemModel) model2;
        assertEquals(Key.key("minecraft:item/bow_pulling_2"), reference2.model());
        assertEquals(0, reference2.tints().size());
    }
}
