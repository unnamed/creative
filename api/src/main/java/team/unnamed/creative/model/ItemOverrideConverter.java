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
package team.unnamed.creative.model;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.item.ConditionItemModel;
import team.unnamed.creative.item.Item;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.item.RangeDispatchItemModel;
import team.unnamed.creative.item.ReferenceItemModel;
import team.unnamed.creative.item.SelectItemModel;
import team.unnamed.creative.item.property.CompassItemNumericProperty;
import team.unnamed.creative.item.property.ItemBooleanProperty;
import team.unnamed.creative.item.property.ItemNumericProperty;
import team.unnamed.creative.item.property.ItemProperty;
import team.unnamed.creative.item.property.ItemStringProperty;
import team.unnamed.creative.item.property.TimeItemNumericProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class ItemOverrideConverter {

    public static void patchPack(ResourcePack resourcePack) {
        for (Model model : resourcePack.models()) {
            if (!model.key().namespace().equals("minecraft")) continue;
            if (!model.key().value().startsWith("block/") && !model.key().value().startsWith("item/")) continue;

            Key itemKey = Key.key(model.key().namespace(), model.key().value().replace("block/", "").replace("item/", ""));
            Item standardItem = resourcePack.item(itemKey);
            if (standardItem != null && standardItem.model() instanceof ReferenceItemModel) standardItem = null;

            if (standardItem == null) {
                RangeDispatchItemModel.Builder builder = ItemModel.rangeDispatch().property(ItemNumericProperty.customModelData());
                builder.scale(1f).fallback(ItemModel.reference(model.key()));

                for (ItemOverride override : model.overrides()) {
                    Integer cmd = getCustomModelData(override);
                    if (cmd == null) continue;

                    builder.addEntry(cmd, ItemModel.reference(override.model()));
                }

                resourcePack.item(Item.item(itemKey, builder.build()));
            } else {
                String modelValue = model.key().value();
                if (modelValue.endsWith("crossbow")) {

                } else if (modelValue.endsWith("bow")) {

                } else if (modelValue.endsWith("player_head")) {

                } else if (modelValue.endsWith("shulker_box")) {

                } else {
                    // cases
                    if (standardItem.model() instanceof SelectItemModel) {
                        SelectItemModel.Builder builder = ItemModel.select();
                        builder.property(ItemStringProperty.customModelData()).fallback(standardItem.model());
                        for (ItemOverride override : model.overrides()) {
                            Integer cmd = getCustomModelData(override);
                            if (cmd == null) continue;
                            builder.addCase(ItemModel.reference(override.model()), cmd.toString());
                        }

                        resourcePack.item(Item.item(itemKey, builder.build()));
                    }
                    // onTrue onFalse
                    if (standardItem.model() instanceof ConditionItemModel) {
                        ConditionItemModel conditionItemModel = (ConditionItemModel) standardItem.model();

                        Map<Integer, List<ItemOverride>> grouped = model.overrides().stream().filter(o -> getCustomModelData(o) != 0)
                                .collect(Collectors.groupingBy(ItemOverrideConverter::getCustomModelData));

                        //onTrue Model
                        List<ItemOverride> trueOverrides = grouped.values().stream().map(overrides -> overrides.get(overrides.size() - 1)).collect(Collectors.toList());
                        RangeDispatchItemModel.Builder trueBuilder = ItemModel.rangeDispatch();
                        List<ItemOverride> falseOverrides = grouped.values().stream().map(overrides -> overrides.get(0)).collect(Collectors.toList());
                        RangeDispatchItemModel.Builder falseBuilder = ItemModel.rangeDispatch();

                        trueBuilder.addEntry(RangeDispatchItemModel.Entry.entry(0f, conditionItemModel.onTrue()));
                        falseBuilder.addEntry(RangeDispatchItemModel.Entry.entry(0f, conditionItemModel.onFalse()));

                        for (ItemOverride override : trueOverrides) {
                            Integer cmd = getCustomModelData(override);
                            if (cmd == null) continue;
                            trueBuilder.addEntry(cmd, ItemModel.reference(override.model()));
                        }

                        for (ItemOverride override : falseOverrides) {
                            Integer cmd = getCustomModelData(override);
                            if (cmd == null) continue;
                            falseBuilder.addEntry(cmd, ItemModel.reference(override.model()));
                        }

                        conditionItemModel = ItemModel.conditional(conditionItemModel.condition(), trueBuilder.build(), falseBuilder.build());
                        resourcePack.item(Item.item(itemKey, conditionItemModel));
                    }

                    if (standardItem.model() instanceof ReferenceItemModel) {
                        ReferenceItemModel referenceItemModel = (ReferenceItemModel) standardItem.model();
                        RangeDispatchItemModel.Builder builder = ItemModel.rangeDispatch();
                        builder.fallback(standardItem.model()).property(ItemNumericProperty.customModelData());

                        for (ItemOverride override : model.overrides()) {
                            Integer cmd = getCustomModelData(override);
                            if (cmd == null) continue;
                            builder.addEntry(cmd, ItemModel.reference(override.model(), referenceItemModel.tints()));
                        }

                        resourcePack.item(Item.item(itemKey, builder.build()));
                    }
                }
            }
        }
    }

    private static Integer getCustomModelData(ItemOverride override) {
        return override.predicate().stream().filter(p -> p.name().equals("custom_model_data")).findFirst().map(p -> Integer.parseInt(p.value().toString())).orElse(null);
    }
}
