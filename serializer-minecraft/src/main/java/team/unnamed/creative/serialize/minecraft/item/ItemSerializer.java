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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.DyeColor;
import team.unnamed.creative.base.HeadType;
import team.unnamed.creative.base.WoodType;
import team.unnamed.creative.item.*;
import team.unnamed.creative.item.property.*;
import team.unnamed.creative.item.special.BannerSpecialRender;
import team.unnamed.creative.item.special.BedSpecialRender;
import team.unnamed.creative.item.special.ChestSpecialRender;
import team.unnamed.creative.item.special.HeadSpecialRender;
import team.unnamed.creative.item.special.NoFieldSpecialRender;
import team.unnamed.creative.item.special.ShulkerBoxSpecialRender;
import team.unnamed.creative.item.special.SignSpecialRender;
import team.unnamed.creative.item.special.SpecialRender;
import team.unnamed.creative.item.tint.ConstantTintSource;
import team.unnamed.creative.item.tint.CustomModelDataTintSource;
import team.unnamed.creative.item.tint.GrassTintSource;
import team.unnamed.creative.item.tint.KeyedAndBackedTintSource;
import team.unnamed.creative.item.tint.TintSource;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.ResourceCategoryImpl;
import team.unnamed.creative.serialize.minecraft.base.KeySerializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ItemSerializer implements JsonResourceSerializer<Item>, JsonResourceDeserializer<Item> {
    public static final ItemSerializer INSTANCE;
    public static final ResourceCategoryImpl<Item> CATEGORY;

    static {
        INSTANCE = new ItemSerializer();
        CATEGORY = new ResourceCategoryImpl<>(
                "items",
                ".json",
                ResourceContainer::items,
                INSTANCE
        );
    }

    private ItemSerializer() {
    }

    private void serializeItemModel(ItemModel model, JsonWriter writer, int targetPackFormat) throws IOException {
        writer.beginObject();
        if (model instanceof EmptyItemModel) {
            writer.name("type").value("empty");
        } else if (model instanceof ReferenceItemModel) {
            writeReference(writer, (ReferenceItemModel) model);
        } else if (model instanceof SpecialItemModel) {
            writeSpecial(writer, (SpecialItemModel) model);
        } else if (model instanceof CompositeItemModel) {
            writeComposite(writer, (CompositeItemModel) model, targetPackFormat);
        } else if (model instanceof ConditionItemModel) {
            writeCondition(writer, (ConditionItemModel) model, targetPackFormat);
        } else if (model instanceof SelectItemModel) {
            writeSelect(writer, (SelectItemModel) model, targetPackFormat);
        } else if (model instanceof RangeDispatchItemModel) {
            writeRangeDispatch(writer, (RangeDispatchItemModel) model, targetPackFormat);
        } else if (model instanceof BundleSelectedItemModel) {
            writer.name("type").value("bundle/selected_item");
        } else {
            throw new IllegalArgumentException("Unknown item model type: " + model.getClass());
        }
        writer.endObject();
    }

    private @NotNull ItemModel deserializeItemModel(JsonElement unknownNode) throws IOException {
        final JsonObject node = unknownNode.getAsJsonObject();
        final Key type = Key.key(node.get("type").getAsString());
        if (!type.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            throw new IllegalArgumentException("Unknown item model type: " + type);
        }
        switch (type.value()) {
            case "empty": return ItemModel.empty();
            case "model": return readReference(node);
            case "special": return readSpecial(node);
            case "composite": return readComposite(node);
            case "condition": return readCondition(node);
            case "select": return readSelect(node);
            case "range_dispatch": return readRangeDispatch(node);
            case "bundle/selected_item": return ItemModel.bundleSelectedItem();
            default:
                throw new IllegalArgumentException("Unknown item model type: " + type);
        }
    }

    @Override
    public void serializeToJson(Item item, JsonWriter writer, int targetPackFormat) throws IOException {
        writer.beginObject();
        writer.name("model");
        serializeItemModel(item.model(), writer, targetPackFormat);

        boolean handAnimationOnSwap = item.handAnimationOnSwap();
        if (handAnimationOnSwap != Item.DEFAULT_HAND_ANIMATION_ON_SWAP) {
            writer.name("hand_animation_on_swap").value(handAnimationOnSwap);
        }

        boolean oversizedInGui = item.oversizedInGui();
        if (oversizedInGui != Item.DEFAULT_OVERSIZED_IN_GUI) {
            writer.name("oversized_in_gui").value(oversizedInGui);
        }

        writer.endObject();
    }

    @Override
    public Item deserializeFromJson(JsonElement node, Key key) throws IOException {
        JsonObject jsonObject = node.getAsJsonObject();
        ItemModel model = deserializeItemModel(jsonObject.get("model"));
        boolean handAnimationOnSwap = jsonObject.has("hand_animation_on_swap")
                ? jsonObject.get("hand_animation_on_swap").getAsBoolean()
                : Item.DEFAULT_HAND_ANIMATION_ON_SWAP;
        boolean oversizedInGui = jsonObject.has("oversized_in_gui")
                ? jsonObject.get("oversized_in_gui").getAsBoolean()
                : Item.DEFAULT_OVERSIZED_IN_GUI;
        return Item.item(key, model, handAnimationOnSwap, oversizedInGui);
    }

    private void writeReference(final @NotNull JsonWriter writer, final @NotNull ReferenceItemModel model) throws IOException {
        writer.name("type").value("model");
        writer.name("model").value(KeySerializer.toString(model.model()));
        writer.name("tints").beginArray();
        for (final TintSource tint : model.tints()) {
            writer.beginObject();
            if (tint instanceof ConstantTintSource) {
                writer.name("type").value("constant");
                writer.name("value").value(((ConstantTintSource) tint).tint());
            } else if (tint instanceof CustomModelDataTintSource) {
                CustomModelDataTintSource customModelDataTintSource = (CustomModelDataTintSource) tint;
                writer.name("type").value("custom_model_data");

                int index = customModelDataTintSource.index();
                if (index != CustomModelDataTintSource.DEFAULT_INDEX) {
                    writer.name("index").value(index);
                }

                writer.name("default").value(customModelDataTintSource.defaultTint());
            } else if (tint instanceof GrassTintSource) {
                GrassTintSource grassTintSource = (GrassTintSource) tint;
                writer.name("type").value("grass");
                writer.name("temperature").value(grassTintSource.temperature());
                writer.name("downfall").value(grassTintSource.downfall());
            } else if (tint instanceof KeyedAndBackedTintSource) {
                KeyedAndBackedTintSource keyedAndBackedTintSource = (KeyedAndBackedTintSource) tint;
                writer.name("type").value(KeySerializer.toString(keyedAndBackedTintSource.key()));
                writer.name("default").value(keyedAndBackedTintSource.defaultTint());
            } else {
                throw new IllegalArgumentException("Unknown tint source type: " + tint.getClass());
            }
            writer.endObject();
        }
        writer.endArray();
    }

    private @NotNull ReferenceItemModel readReference(final @NotNull JsonObject node) {
        final Key model = Key.key(node.get("model").getAsString());
        final List<TintSource> tints = new ArrayList<>();

        if (node.has("tints")) for (JsonElement tintElement : node.getAsJsonArray("tints")) {
            final JsonObject tintObject = tintElement.getAsJsonObject();
            final Key type = Key.key(tintObject.get("type").getAsString());
            if (!type.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
                throw new IllegalArgumentException("Unknown tint source type: " + type);
            }
            switch (type.value()) {
                case "constant":
                    tints.add(TintSource.constant(tintObject.get("value").getAsInt()));
                    break;
                case "custom_model_data":
                    int index = tintObject.has("index")
                            ? tintObject.get("index").getAsInt()
                            : CustomModelDataTintSource.DEFAULT_INDEX;
                    int defaultTint = tintObject.get("default").getAsInt();
                    tints.add(TintSource.customModelData(index, defaultTint));
                    break;
                case "grass":
                    float temperature = tintObject.get("temperature").getAsFloat();
                    float downfall = tintObject.get("downfall").getAsFloat();
                    tints.add(TintSource.grass(temperature, downfall));
                    break;
                case "dye":
                    tints.add(TintSource.dye(tintObject.get("default").getAsInt()));
                    break;
                case "firework":
                    tints.add(TintSource.firework(tintObject.get("default").getAsInt()));
                    break;
                case "map_color":
                    tints.add(TintSource.mapColor(tintObject.get("default").getAsInt()));
                    break;
                case "potion":
                    tints.add(TintSource.potion(tintObject.get("default").getAsInt()));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown tint source type: " + type);
            }
        }
        return ItemModel.reference(model, tints);
    }

    private void writeSpecial(final @NotNull JsonWriter writer, final @NotNull SpecialItemModel model) throws IOException {
        writer.name("type").value("special");
        writer.name("model").beginObject();
        final SpecialRender render = model.render();
        if (render instanceof BannerSpecialRender) {
            final BannerSpecialRender bannerRender = (BannerSpecialRender) render;
            writer.name("type").value("banner");
            writer.name("color").value(bannerRender.color().name().toLowerCase());
        } else if (render instanceof BedSpecialRender) {
            final BedSpecialRender bedRender = (BedSpecialRender) render;
            writer.name("type").value("bed");
            writer.name("texture").value(KeySerializer.toString(bedRender.texture()));
        } else if (render instanceof ChestSpecialRender) {
            final ChestSpecialRender chestRender = (ChestSpecialRender) render;
            writer.name("type").value("chest");
            writer.name("texture").value(KeySerializer.toString(chestRender.texture()));
            final float openness = chestRender.openness();
            if (openness != ChestSpecialRender.DEFAULT_OPENNESS) {
                writer.name("openness").value(openness);
            }
        } else if (render instanceof SignSpecialRender) { // covers both hanging & standing sign types
            final SignSpecialRender signRender = (SignSpecialRender) render;
            writer.name("type").value(signRender.hanging() ? "hanging_sign" : "standing_sign");
            writer.name("wood_type").value(signRender.woodType().name().toLowerCase());
            final Key texture = signRender.texture();
            if (texture != null) {
                writer.name("texture").value(KeySerializer.toString(texture));
            }
        } else if (render instanceof HeadSpecialRender) {
            final HeadSpecialRender headRender = (HeadSpecialRender) render;
            writer.name("type").value("head");
            writer.name("kind").value(headRender.kind().name().toLowerCase());

            final Key texture = headRender.texture();
            if (texture != null) {
                writer.name("texture").value(KeySerializer.toString(texture));
            }

            final float animation = headRender.animation();
            if (animation != HeadSpecialRender.DEFAULT_ANIMATION) {
                writer.name("animation").value(animation);
            }
        } else if (render instanceof ShulkerBoxSpecialRender) {
            final ShulkerBoxSpecialRender shulkerBoxRender = (ShulkerBoxSpecialRender) render;
            writer.name("type").value("shulker_box");
            writer.name("texture").value(KeySerializer.toString(shulkerBoxRender.texture()));

            final float openness = shulkerBoxRender.openness();
            if (openness != ShulkerBoxSpecialRender.DEFAULT_OPENNESS) {
                writer.name("openness").value(openness);
            }

            final CubeFace orientation = shulkerBoxRender.orientation();
            if (orientation != ShulkerBoxSpecialRender.DEFAULT_ORIENTATION) {
                writer.name("orientation").value(orientation.name().toLowerCase());
            }
        } else if (render instanceof NoFieldSpecialRender) {
            final NoFieldSpecialRender noFieldRender = (NoFieldSpecialRender) render;
            writer.name("type").value(KeySerializer.toString(noFieldRender.key()));
        } else {
            throw new IllegalArgumentException("Unknown special render type: " + render.getClass());
        }
        writer.endObject();
        writer.name("base").value(KeySerializer.toString(model.base()));
    }

    private @NotNull SpecialItemModel readSpecial(final @NotNull JsonObject node) {
        final JsonObject modelNode = node.getAsJsonObject("model");
        final SpecialRender render;
        final Key type = Key.key(modelNode.get("type").getAsString());
        if (!type.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            throw new IllegalArgumentException("Unknown special render type: " + type);
        }
        switch (type.value()) {
            case "banner":
                render = SpecialRender.banner(DyeColor.valueOf(modelNode.get("color").getAsString().toUpperCase()));
                break;
            case "bed":
                render = SpecialRender.bed(Key.key(modelNode.get("texture").getAsString()));
                break;
            case "chest":
                final Key chestTexture = Key.key(modelNode.get("texture").getAsString());
                final float openness = modelNode.has("openness")
                        ? modelNode.get("openness").getAsFloat()
                        : ChestSpecialRender.DEFAULT_OPENNESS;
                render = SpecialRender.chest(chestTexture, openness);
                break;
            case "hanging_sign":
            case "standing_sign":
                final boolean hanging = type.equals("hanging_sign");
                final WoodType woodType = WoodType.valueOf(modelNode.get("wood_type").getAsString().toUpperCase());
                final Key signTexture = modelNode.has("texture")
                        ? Key.key(modelNode.get("texture").getAsString())
                        : null;
                render = hanging ? SpecialRender.hangingSign(woodType, signTexture) : SpecialRender.standingSign(woodType, signTexture);
                break;
            case "head":
                final HeadType kind = HeadType.valueOf(modelNode.get("kind").getAsString().toUpperCase());
                final Key headTexture = modelNode.has("texture")
                        ? Key.key(modelNode.get("texture").getAsString())
                        : null;
                final float animation = modelNode.has("animation")
                        ? modelNode.get("animation").getAsFloat()
                        : HeadSpecialRender.DEFAULT_ANIMATION;
                render = SpecialRender.head(kind, headTexture, animation);
                break;
            case "shulker_box":
                final Key shulkerBoxTexture = Key.key(modelNode.get("texture").getAsString());
                final float shulkerBoxOpenness = modelNode.has("openness")
                        ? modelNode.get("openness").getAsFloat()
                        : ShulkerBoxSpecialRender.DEFAULT_OPENNESS;
                final CubeFace orientation = modelNode.has("orientation")
                        ? CubeFace.valueOf(modelNode.get("orientation").getAsString().toUpperCase())
                        : ShulkerBoxSpecialRender.DEFAULT_ORIENTATION;
                render = SpecialRender.shulkerBox(shulkerBoxTexture, shulkerBoxOpenness, orientation);
                break;
            case "conduit":
                render = SpecialRender.conduit();
                break;
            case "decorated_pot":
                render = SpecialRender.decoratedPot();
                break;
            case "shield":
                render = SpecialRender.shield();
                break;
            case "trident":
                render = SpecialRender.trident();
                break;
            default:
                throw new IllegalArgumentException("Unknown special render type: " + type);
        }
        return ItemModel.special(render, Key.key(node.get("base").getAsString()));
    }

    private void writeComposite(final @NotNull JsonWriter writer, final @NotNull CompositeItemModel model, final int targetPackFormat) throws IOException {
        writer.name("type").value("composite");
        writer.name("models").beginArray();
        for (ItemModel child : model.models()) {
            serializeItemModel(child, writer, targetPackFormat);
        }
        writer.endArray();
    }

    private @NotNull CompositeItemModel readComposite(final @NotNull JsonObject node) throws IOException {
        final List<ItemModel> models = new ArrayList<>();
        for (JsonElement childElement : node.getAsJsonArray("models")) {
            models.add(deserializeItemModel(childElement));
        }
        return ItemModel.composite(models);
    }

    private void writeCondition(final @NotNull JsonWriter writer, final @NotNull ConditionItemModel model, final int targetPackFormat) throws IOException {
        writer.name("type").value("condition");
        final ItemBooleanProperty condition = model.condition();
        if (condition instanceof CustomModelDataItemBooleanProperty) {
            writer.name("property").value("custom_model_data");
            final int index = ((CustomModelDataItemBooleanProperty) condition).index();
            if (index != CustomModelDataItemBooleanProperty.DEFAULT_INDEX) {
                writer.name("index").value(index);
            }
        } else if (condition instanceof HasComponentItemBooleanProperty) {
            final HasComponentItemBooleanProperty hasComponent = (HasComponentItemBooleanProperty) condition;
            writer.name("property").value("has_component");
            writer.name("component").value(hasComponent.component());

            final boolean ignoreDefault = hasComponent.ignoreDefault();
            if (ignoreDefault != HasComponentItemBooleanProperty.DEFAULT_IGNORE_DEFAULT) {
                writer.name("ignore_default").value(ignoreDefault);
            }
        } else if (condition instanceof KeybindDownItemBooleanProperty) {
            writer.name("property").value("keybind_down");
            writer.name("keybind").value(((KeybindDownItemBooleanProperty) condition).key());
        } else if (condition instanceof NoFieldItemBooleanProperty) {
            writer.name("property").value(KeySerializer.toString(((NoFieldItemBooleanProperty) condition).key()));
        } else {
            throw new IllegalArgumentException("Unknown condition type: " + condition.getClass());
        }
        writer.name("on_true");
        serializeItemModel(model.onTrue(), writer, targetPackFormat);
        writer.name("on_false");
        serializeItemModel(model.onFalse(), writer, targetPackFormat);
    }

    private @NotNull ConditionItemModel readCondition(final @NotNull JsonObject node) throws IOException {
        final ItemBooleanProperty condition;
        final Key property = Key.key(node.get("property").getAsString());
        if (!property.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            throw new IllegalArgumentException("Unknown condition property: " + property);
        }
        switch (property.value()) {
            case "custom_model_data":
                final int index = node.has("index")
                        ? node.get("index").getAsInt()
                        : CustomModelDataItemBooleanProperty.DEFAULT_INDEX;
                condition = ItemBooleanProperty.customModelData(index);
                break;
            case "has_component":
                final String component = node.get("component").getAsString();
                final boolean ignoreDefault = node.has("ignore_default")
                        ? node.get("ignore_default").getAsBoolean()
                        : HasComponentItemBooleanProperty.DEFAULT_IGNORE_DEFAULT;
                condition = ItemBooleanProperty.hasComponent(component, ignoreDefault);
                break;
            case "keybind_down":
                condition = ItemBooleanProperty.keybindDown(node.get("keybind").getAsString());
                break;
            case "broken":
                condition = ItemBooleanProperty.broken();
                break;
            case "bundle/has_selected_item":
                condition = ItemBooleanProperty.bundleHasSelectedItem();
                break;
            case "carried":
                condition = ItemBooleanProperty.carried();
                break;
            case "damaged":
                condition = ItemBooleanProperty.damaged();
                break;
            case "extended_view":
                condition = ItemBooleanProperty.extendedView();
                break;
            case "fishing_rod/cast":
                condition = ItemBooleanProperty.fishingRodCast();
                break;
            case "selected":
                condition = ItemBooleanProperty.selected();
                break;
            case "using_item":
                condition = ItemBooleanProperty.usingItem();
                break;
            case "view_entity":
                condition = ItemBooleanProperty.viewEntity();
                break;
            default:
                throw new IllegalArgumentException("Unknown condition property: " + property);
        }
        return ItemModel.conditional(
                condition,
                deserializeItemModel(node.get("on_true")),
                deserializeItemModel(node.get("on_false"))
        );
    }

    private void writeSelect(final @NotNull JsonWriter writer, final @NotNull SelectItemModel model, final int targetPackFormat) throws IOException {
        writer.name("type").value("select");

        final ItemStringProperty property = model.property();
        if (property instanceof BlockStateItemStringProperty) {
            writer.name("property").value("block_state");
            writer.name("block_state_property").value(((BlockStateItemStringProperty) property).property());
        } else if (property instanceof CustomModelDataItemStringProperty) {
            writer.name("property").value("custom_model_data");
            final int index = ((CustomModelDataItemStringProperty) property).index();
            if (index != CustomModelDataItemStringProperty.DEFAULT_INDEX) {
                writer.name("index").value(index);
            }
        } else if (property instanceof LocalTimeItemStringProperty) {
            final LocalTimeItemStringProperty localTimeProperty = (LocalTimeItemStringProperty) property;
            writer.name("property").value("local_time");
            writer.name("pattern").value(localTimeProperty.pattern());

            final String locale = localTimeProperty.locale();
            if (!locale.equals(LocalTimeItemStringProperty.DEFAULT_LOCALE)) {
                writer.name("locale").value(locale);
            }

            final String timezone = localTimeProperty.timezone();
            if (timezone != null) {
                writer.name("timezone").value(timezone);
            }
        } else if (property instanceof NoFieldItemStringProperty) {
            writer.name("property").value(KeySerializer.toString(((NoFieldItemStringProperty) property).key()));
        } else {
            throw new IllegalArgumentException("Unknown select property type: " + property.getClass());
        }

        writer.name("cases").beginArray();
        for (final SelectItemModel.Case _case : model.cases()) {
            writer.beginObject();
            writer.name("when");
            final List<String> when = _case.when();
            if (when.size() == 1) {
                writer.value(when.get(0));
            } else {
                writer.beginArray();
                for (String value : when) {
                    writer.value(value);
                }
                writer.endArray();
            }
            writer.name("model");
            serializeItemModel(_case.model(), writer, targetPackFormat);
            writer.endObject();
        }
        writer.endArray();

        final ItemModel fallback = model.fallback();
        if (fallback != null) {
            writer.name("fallback");
            serializeItemModel(fallback, writer, targetPackFormat);
        }
    }

    private @NotNull SelectItemModel readSelect(final @NotNull JsonObject node) throws IOException {
        final ItemStringProperty property;
        final Key propertyType = Key.key(node.get("property").getAsString());
        if (!propertyType.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            throw new IllegalArgumentException("Unknown select property type: " + propertyType);
        }
        switch (propertyType.value()) {
            case "block_state":
                property = ItemStringProperty.blockState(node.get("block_state_property").getAsString());
                break;
            case "custom_model_data":
                final int index = node.has("index")
                        ? node.get("index").getAsInt()
                        : CustomModelDataItemStringProperty.DEFAULT_INDEX;
                property = ItemStringProperty.customModelData(index);
                break;
            case "local_time":
                final String pattern = node.get("pattern").getAsString();
                final String locale = node.has("locale")
                        ? node.get("locale").getAsString()
                        : LocalTimeItemStringProperty.DEFAULT_LOCALE;
                final String timezone = node.has("timezone")
                        ? node.get("timezone").getAsString()
                        : null;
                property = ItemStringProperty.localTime(locale, timezone, pattern);
                break;
            case "charge_type":
                property = ItemStringProperty.chargeType();
                break;
            case "context_dimension":
                property = ItemStringProperty.contextDimension();
                break;
            case "context_entity_type":
                property = ItemStringProperty.contextEntityType();
                break;
            case "display_context":
                property = ItemStringProperty.displayContext();
                break;
            case "main_hand":
                property = ItemStringProperty.mainHand();
                break;
            case "trim_material":
                property = ItemStringProperty.trimMaterial();
                break;
            default:
                throw new IllegalArgumentException("Unknown select property type: " + propertyType);
        }

        final List<SelectItemModel.Case> cases = new ArrayList<>();
        for (JsonElement caseElement : node.getAsJsonArray("cases")) {
            final JsonObject caseObject = caseElement.getAsJsonObject();
            final List<String> when = new ArrayList<>();

            JsonElement whenNode = caseObject.get("when");
            if (whenNode.isJsonArray()) {
                for (JsonElement whenElement : whenNode.getAsJsonArray()) {
                    when.add(whenElement.getAsString());
                }
            } else {
                when.add(whenNode.getAsString());
            }
            cases.add(SelectItemModel.Case._case(
                    deserializeItemModel(caseObject.get("model")),
                    when
            ));
        }

        final ItemModel fallback = node.has("fallback")
                ? deserializeItemModel(node.get("fallback"))
                : null;

        return ItemModel.select(property, cases, fallback);
    }

    private void writeRangeDispatch(final @NotNull JsonWriter writer, final @NotNull RangeDispatchItemModel model, final int targetPackFormat) throws IOException {
        writer.name("type").value("range_dispatch");

        final ItemNumericProperty property = model.property();
        if (property instanceof CompassItemNumericProperty) {
            final CompassItemNumericProperty compassProperty = (CompassItemNumericProperty) property;
            writer.name("property").value("compass");
            writer.name("target").value(compassProperty.target().name().toLowerCase());

            final boolean wobble = compassProperty.wobble();
            if (wobble != CompassItemNumericProperty.DEFAULT_WOBBLE) {
                writer.name("wobble").value(wobble);
            }
        } else if (property instanceof CountItemNumericProperty) {
            writer.name("property").value("count");
            final boolean normalize = ((CountItemNumericProperty) property).normalize();
            if (normalize != CountItemNumericProperty.DEFAULT_NORMALIZE) {
                writer.name("normalize").value(normalize);
            }
        } else if (property instanceof CustomModelDataItemNumericProperty) {
            writer.name("property").value("custom_model_data");
            final int index = ((CustomModelDataItemNumericProperty) property).index();
            if (index != CustomModelDataItemNumericProperty.DEFAULT_INDEX) {
                writer.name("index").value(index);
            }
        } else if (property instanceof DamageItemNumericProperty) {
            writer.name("property").value("damage");
            final boolean normalize = ((DamageItemNumericProperty) property).normalize();
            if (normalize != DamageItemNumericProperty.DEFAULT_NORMALIZE) {
                writer.name("normalize").value(normalize);
            }
        } else if (property instanceof TimeItemNumericProperty) {
            final TimeItemNumericProperty timeProperty = (TimeItemNumericProperty) property;
            writer.name("property").value("time");
            final boolean wobble = timeProperty.wobble();
            if (wobble != TimeItemNumericProperty.DEFAULT_WOBBLE) {
                writer.name("wobble").value(wobble);
            }
            writer.name("source").value(timeProperty.source().name().toLowerCase());
        } else if (property instanceof UseCycleItemNumericProperty) {
            writer.name("property").value("use_cycle");
            final float period = ((UseCycleItemNumericProperty) property).period();
            if (period != UseCycleItemNumericProperty.DEFAULT_PERIOD) {
                writer.name("period").value(period);
            }
        } else if (property instanceof UseDurationItemNumericProperty) {
            writer.name("property").value("use_duration");
            final boolean remaining = ((UseDurationItemNumericProperty) property).remaining();
            if (remaining != UseDurationItemNumericProperty.DEFAULT_REMAINING) {
                writer.name("remaining").value(remaining);
            }
        } else if (property instanceof NoFieldItemNumericProperty) {
            writer.name("property").value(KeySerializer.toString(((NoFieldItemNumericProperty) property).key()));
        } else {
            throw new IllegalArgumentException("Unknown range dispatch property type: " + property.getClass());
        }

        final float scale = model.scale();
        if (scale != RangeDispatchItemModel.DEFAULT_SCALE) {
            writer.name("scale").value(scale);
        }

        writer.name("entries").beginArray();
        for (final RangeDispatchItemModel.Entry entry : model.entries()) {
            writer.beginObject();
            writer.name("threshold").value(entry.threshold());
            writer.name("model");
            serializeItemModel(entry.model(), writer, targetPackFormat);
            writer.endObject();
        }
        writer.endArray();

        final ItemModel fallback = model.fallback();
        if (fallback != null) {
            writer.name("fallback");
            serializeItemModel(fallback, writer, targetPackFormat);
        }
    }

    private @NotNull RangeDispatchItemModel readRangeDispatch(final @NotNull JsonObject node) throws IOException {
        final ItemNumericProperty property;
        final Key propertyType = Key.key(node.get("property").getAsString());
        if (!propertyType.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            throw new IllegalArgumentException("Unknown range dispatch property type: " + propertyType);
        }
        switch (propertyType.value()) {
            case "compass":
                final CompassItemNumericProperty.Target target = CompassItemNumericProperty.Target.valueOf(node.get("target").getAsString().toUpperCase());
                final boolean wobble = node.has("wobble")
                        ? node.get("wobble").getAsBoolean()
                        : CompassItemNumericProperty.DEFAULT_WOBBLE;
                property = ItemNumericProperty.compass(target, wobble);
                break;
            case "count":
                final boolean normalize = node.has("normalize")
                        ? node.get("normalize").getAsBoolean()
                        : CountItemNumericProperty.DEFAULT_NORMALIZE;
                property = ItemNumericProperty.count(normalize);
                break;
            case "custom_model_data":
                final int index = node.has("index")
                        ? node.get("index").getAsInt()
                        : CustomModelDataItemNumericProperty.DEFAULT_INDEX;
                property = ItemNumericProperty.customModelData(index);
                break;
            case "damage":
                final boolean damageNormalize = node.has("normalize")
                        ? node.get("normalize").getAsBoolean()
                        : DamageItemNumericProperty.DEFAULT_NORMALIZE;
                property = ItemNumericProperty.damage(damageNormalize);
                break;
            case "time":
                final boolean timeWobble = node.has("wobble")
                        ? node.get("wobble").getAsBoolean()
                        : TimeItemNumericProperty.DEFAULT_WOBBLE;
                final TimeItemNumericProperty.Source source = TimeItemNumericProperty.Source.valueOf(node.get("source").getAsString().toUpperCase());
                property = ItemNumericProperty.time(timeWobble, source);
                break;
            case "use_cycle":
                final float period = node.has("period")
                        ? node.get("period").getAsFloat()
                        : UseCycleItemNumericProperty.DEFAULT_PERIOD;
                property = ItemNumericProperty.useCycle(period);
                break;
            case "use_duration":
                final boolean remaining = node.has("remaining")
                        ? node.get("remaining").getAsBoolean()
                        : UseDurationItemNumericProperty.DEFAULT_REMAINING;
                property = ItemNumericProperty.useDuration(remaining);
                break;
            case "bundle/fullness":
                property = ItemNumericProperty.bundleFullness();
                break;
            case "cooldown":
                property = ItemNumericProperty.cooldown();
                break;
            case "crossbow/pull":
                property = ItemNumericProperty.crossbowPull();
                break;
            default:
                throw new IllegalArgumentException("Unknown range dispatch property type: " + propertyType);
        }

        final float scale = node.has("scale")
                ? node.get("scale").getAsFloat()
                : RangeDispatchItemModel.DEFAULT_SCALE;

        final List<RangeDispatchItemModel.Entry> entries = new ArrayList<>();
        for (JsonElement entryElement : node.getAsJsonArray("entries")) {
            final JsonObject entryObject = entryElement.getAsJsonObject();
            entries.add(RangeDispatchItemModel.Entry.entry(
                    entryObject.get("threshold").getAsFloat(),
                    deserializeItemModel(entryObject.get("model"))
            ));
        }

        final ItemModel fallback = node.has("fallback")
                ? deserializeItemModel(node.get("fallback"))
                : null;

        return ItemModel.rangeDispatch(property, scale, entries, fallback);
    }
}
