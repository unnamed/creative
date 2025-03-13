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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.item.ConditionItemModel;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.item.RangeDispatchItemModel;
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

import static java.util.Objects.requireNonNull;

@ApiStatus.Experimental
@SuppressWarnings("deprecation") // we know, we're trying to update it
public final class ItemOverrideConverter {
    private final Map<ItemProperty, ItemModel> byProperty = new LinkedHashMap<>();
    private final @NotNull ItemModel fallback;

    private ItemOverrideConverter(final @NotNull ItemModel fallback) {
        this.fallback = requireNonNull(fallback, "fallback");
    }

    public void load(final @NotNull ItemModel model) {
        if (model instanceof ConditionItemModel) {
            byProperty.put(((ConditionItemModel) model).condition(), model);
        } else if (model instanceof RangeDispatchItemModel) {
            byProperty.put(((RangeDispatchItemModel) model).property(), model);
        } else if (model instanceof SelectItemModel) {
            byProperty.put(((SelectItemModel) model).property(), model);
        } else {
            throw new IllegalArgumentException("Unsupported item model type: " + model.getClass());
        }
    }

    private void addPredicate(final @NotNull ItemBooleanProperty property, final @NotNull ItemModel model) {
        if (byProperty.containsKey(property)) {
            throw new IllegalArgumentException("Multiple overrides for the same property: " + property);
        }
        byProperty.put(property, ItemModel.conditional(property, model, fallback));
    }

    private void addPredicate(final @NotNull ItemNumericProperty property, final @NotNull ItemModel model, final @NotNull ItemPredicate predicate) {
        // not sure if this is really needed, since "value" could also be an Integer,
        // casting it directly to a float like "(float) value" could throw a ClassCastException
        Object value = predicate.value();
        float floatValue;
        if (value instanceof Number) {
            floatValue = ((Number) value).floatValue();
        } else {
            throw new IllegalArgumentException("Invalid numeric value: " + value);
        }

        RangeDispatchItemModel.Entry entry = RangeDispatchItemModel.Entry.entry(floatValue, model);
        RangeDispatchItemModel rangeDispatch = (RangeDispatchItemModel) byProperty.get(property);
        if (rangeDispatch == null) {
            byProperty.put(property, ItemModel.rangeDispatch(property, RangeDispatchItemModel.DEFAULT_SCALE, Collections.singletonList(entry), fallback));
        } else {
            byProperty.put(property, rangeDispatch.toBuilder().addEntry(entry).build());
        }
    }

    public void addOverride(final @NotNull ItemOverride override) {
        final ItemModel model = ItemModel.reference(override.model());
        for (final ItemPredicate predicate : override.predicate()) {
            switch (predicate.name()) {
                case "angle": {
                    // ("angle" -> float) predicate converts to compass property, targetting the spawn location, value is the same
                    ItemNumericProperty property = ItemNumericProperty.compass(CompassItemNumericProperty.Target.SPAWN);
                    RangeDispatchItemModel rangeDispatch = (RangeDispatchItemModel) byProperty.get(property);
                    RangeDispatchItemModel.Entry entry = RangeDispatchItemModel.Entry.entry((float) predicate.value(), model);
                    if (rangeDispatch == null) {
                        // create the range dispatch item model, use default scale, and pass our fallback
                        byProperty.put(property, ItemModel.rangeDispatch(property, RangeDispatchItemModel.DEFAULT_SCALE, Collections.singletonList(entry), fallback));
                    } else {
                        byProperty.put(property, rangeDispatch.toBuilder().addEntry(entry).build());
                    }
                    break;
                }

                case "blocking":
                case "pulling":
                case "throwing": {
                    // ("blocking" -> 1 || "pulling" -> 1 || "throwing" -> 1) predicate converts to boolean property, value is ignored

                    ItemBooleanProperty property = ItemBooleanProperty.usingItem();

                    if (byProperty.containsKey(property)) {
                        // warning here! using multiple, "blocking", "pulling" and "throwing"
                        // wouldn't make sense in previous version, though it was allowed,
                        // now we have to skip this case
                        // TODO: Better error handling (since it's very unlikely to happen, we just print it)
                        System.err.println("Skipping multiple 'blocking', 'pulling' or 'throwing' predicates in the same item override");
                        continue;
                    }

                    byProperty.put(property, ItemModel.conditional(property, model, fallback));
                    break;
                }

                case "charged":
                case "firework": {
                    // ("charged" -> 1 || "firework" -> 1) predicate converts to string property, value is ignored
                    // "charged" matches both "rocket" and "arrow"
                    // "firework" matches only "rocket"
                    ItemStringProperty property = ItemStringProperty.chargeType();
                    List<String> when = predicate.name().equals("charged")
                            ? Arrays.asList("rocket", "arrow")
                            : Collections.singletonList("rocket");

                    SelectItemModel selectItemModel = (SelectItemModel) byProperty.get(property);
                    SelectItemModel.Case _case = SelectItemModel.Case._case(model, when);

                    if (selectItemModel == null) {
                        // new select item model
                        byProperty.put(property, ItemModel.select(property, Collections.singletonList(_case), fallback));
                    } else {
                        // add a new case to the existing select item model
                        byProperty.put(property, selectItemModel.toBuilder().addCase(_case).build());
                    }
                    break;
                }

                case "lefthanded": {
                    // ("lefthanded" -> "true") predicate converts to string property, value is ignored
                    ItemStringProperty property = ItemStringProperty.mainHand();
                    SelectItemModel selectItemModel = (SelectItemModel) byProperty.get(property);
                    SelectItemModel.Case _case = SelectItemModel.Case._case(model, "left");
                    if (selectItemModel == null) {
                        // new select item model
                        byProperty.put(property, ItemModel.select(property, Collections.singletonList(_case), fallback));
                    } else {
                        // add a new case to the existing select item model
                        byProperty.put(property, selectItemModel.toBuilder().addCase(_case).build());
                    }
                    break;
                }

                case "trim_type": {
                    ItemStringProperty property = ItemStringProperty.trimMaterial();
                    SelectItemModel selectItemModel = (SelectItemModel) byProperty.get(property);
                    String when = override.model().asString().split("_")[2];
                    SelectItemModel.Case _case = SelectItemModel.Case._case(model, when);
                    if (selectItemModel == null) {
                        byProperty.put(property, ItemModel.select(property, Collections.singletonList(_case), fallback));
                    } else {
                        byProperty.put(property, selectItemModel.toBuilder().addCase(_case).build());
                    }
                    break;
                }

                // plain boolean properties, values are ignored
                case "broken": {
                    addPredicate(ItemBooleanProperty.broken(), model);
                    break;
                }
                case "cast": {
                    addPredicate(ItemBooleanProperty.fishingRodCast(), model);
                    break;
                }
                case "damaged": {
                    addPredicate(ItemBooleanProperty.damaged(), model);
                    break;
                }

                // plain numeric properties, values are taken as-is
                case "cooldown": {
                    addPredicate(ItemNumericProperty.cooldown(), model, predicate);
                    break;
                }
                case "damage": {
                    addPredicate(ItemNumericProperty.damage(), model, predicate);
                    break;
                }
                case "time": {
                    addPredicate(ItemNumericProperty.time(TimeItemNumericProperty.Source.DAYTIME), model, predicate);
                    break;
                }
                case "custom_model_data": {
                    addPredicate(ItemNumericProperty.customModelData(), model, predicate);
                    break;
                }
                case "pull": {
                    // TODO: this might not be supported
                    addPredicate(ItemNumericProperty.useDuration(), model, predicate);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown item predicate: " + predicate.name() + ". Don't know how to convert it to an item property!");
            }
        }
    }

    public @NotNull List<ItemModel> convert() {
        // return as a list
        return new ArrayList<>(byProperty.values());
    }

    public static @NotNull ItemOverrideConverter converter(final @NotNull ItemModel fallback) {
        return new ItemOverrideConverter(fallback);
    }
}
