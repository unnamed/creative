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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import team.unnamed.creative.base.Axis3D;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.ElementRotation;
import team.unnamed.creative.model.ItemOverride;
import team.unnamed.creative.model.ItemPredicate;
import team.unnamed.creative.model.ItemTransform;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.GsonUtil;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.base.KeySerializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;
import team.unnamed.creative.texture.TextureUV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ApiStatus.Internal
public final class ModelSerializer implements JsonResourceSerializer<Model>, JsonResourceDeserializer<Model> {

    private static final float MINECRAFT_UV_UNIT = 16F;

    public static final ModelSerializer INSTANCE;
    public static final ResourceCategory<Model> CATEGORY;

    static {
        INSTANCE = new ModelSerializer();
        CATEGORY = new ResourceCategory<>(
                "models",
                ".json",
                ResourceContainer::model,
                ResourceContainer::models,
                ModelSerializer.INSTANCE
        );
    }

    @Override
    public void serializeToJson(Model model, JsonWriter writer) throws IOException {
        writer.beginObject();

        // parent
        Key parent = model.parent();
        if (parent != null) {
            writer.name("parent").value(KeySerializer.toString(parent));
        }

        // display
        Map<ItemTransform.Type, ItemTransform> display = model.display();
        if (!display.isEmpty()) {
            writer.name("display").beginObject();
            for (Map.Entry<ItemTransform.Type, ItemTransform> entry : display.entrySet()) {
                writer.name(entry.getKey().name().toLowerCase(Locale.ROOT));
                writeItemTransform(writer, entry.getValue());
            }
            writer.endObject();
        }

        // elements
        List<Element> elements = model.elements();
        if (!elements.isEmpty()) {
            writer.name("elements").beginArray();
            for (Element element : elements) {
                writeElement(writer, element);
            }
            writer.endArray();
        }

        boolean ambientOcclusion = model.ambientOcclusion();
        if (ambientOcclusion != Model.DEFAULT_AMBIENT_OCCLUSION) {
            // only write if not default value
            writer.name("ambientocclusion").value(ambientOcclusion);
        }

        writeTextures(writer, model.textures());

        Model.GuiLight guiLight = model.guiLight();
        if (guiLight != null) {
            // only write if not default
            writer.name("gui_light").value(guiLight.name().toLowerCase(Locale.ROOT));
        }

        List<ItemOverride> overrides = model.overrides();
        if (!overrides.isEmpty()) {
            writer.name("overrides").beginArray();
            for (ItemOverride override : overrides) {
                writeItemOverride(writer, override);
            }
            writer.endArray();
        }
        writer.endObject();
    }

    @Override
    public Model deserializeFromJson(JsonElement node, Key key) {

        JsonObject objectNode = node.getAsJsonObject();

        // parent
        Key parent = null;
        if (objectNode.has("parent")) {
            parent = Key.key(objectNode.get("parent").getAsString());
        }

        // display
        Map<ItemTransform.Type, ItemTransform> display = new LinkedHashMap<>();
        if (objectNode.has("display")) {
            JsonObject displayNode = objectNode.getAsJsonObject("display");
            for (Map.Entry<String, JsonElement> entry : displayNode.entrySet()) {
                ItemTransform.Type type = ItemTransform.Type.valueOf(entry.getKey().toUpperCase(Locale.ROOT));
                display.put(type, readItemTransform(entry.getValue()));
            }
        }

        // elements
        List<Element> elements = new ArrayList<>();
        if (objectNode.has("elements")) {
            for (JsonElement elementNode : objectNode.getAsJsonArray("elements")) {
                elements.add(readElement(elementNode));
            }
        }

        ModelTextures texture = ModelTextures.builder().build();

        if (objectNode.has("textures")) {
            texture = readTextures(objectNode.get("textures"));
        }

        Model.GuiLight guiLight = null;
        if (objectNode.has("gui_light")) {
            // only write if not default
            guiLight = Model.GuiLight.valueOf(objectNode.get("gui_light").getAsString().toUpperCase(Locale.ROOT));
        }

        List<ItemOverride> overrides = new ArrayList<>();
        if (objectNode.has("overrides")) {
            for (JsonElement overrideNode : objectNode.getAsJsonArray("overrides")) {
                overrides.add(readItemOverride(overrideNode));
            }
        }

        return Model.model()
                .key(key)
                .parent(parent)
                .display(display)
                .elements(elements)
                .ambientOcclusion(GsonUtil.getBoolean(objectNode, "ambientocclusion", Model.DEFAULT_AMBIENT_OCCLUSION))
                .textures(texture)
                .guiLight(guiLight)
                .overrides(overrides)
                .build();
    }

    private static void writeElement(JsonWriter writer, Element element) throws IOException {
        writer
                .beginObject()
                .name("from");
        GsonUtil.writeVector3Float(writer, element.from());
        writer.name("to");
        GsonUtil.writeVector3Float(writer, element.to());

        ElementRotation rotation = element.rotation();
        if (rotation != null) {
            writer.name("rotation");
            writeElementRotation(writer, rotation);
        }

        boolean shade = element.shade();
        if (shade != Element.DEFAULT_SHADE) {
            // only write if not equal to default value
            writer.name("shade").value(shade);
        }

        int lightEmission = element.lightEmission();
        if (lightEmission != 0) {
            writer.name("light_emission").value(lightEmission);
        }

        // faces
        writer.name("faces").beginObject();
        for (Map.Entry<CubeFace, ElementFace> entry : element.faces().entrySet()) {
            CubeFace type = entry.getKey();
            ElementFace face = entry.getValue();

            writer.name(type.name().toLowerCase(Locale.ROOT))
                    .beginObject();
            if (face.uv() != null) {
                TextureUV uv = face.uv0();
                TextureUV defaultUv = getDefaultUvForFace(type, element.from(), element.to());
                if (uv != null && !uv.equals(defaultUv)) {
                    writer.name("uv");
                    writer.beginArray();
                    writer.value(uv.from().x() * MINECRAFT_UV_UNIT);
                    writer.value(uv.from().y() * MINECRAFT_UV_UNIT);
                    writer.value(uv.to().x() * MINECRAFT_UV_UNIT);
                    writer.value(uv.to().y() * MINECRAFT_UV_UNIT);
                    writer.endArray();
                }
            }
            writer.name("texture").value(face.texture());
            if (face.cullFace() != null) {
                writer.name("cullface").value(face.cullFace().name().toLowerCase(Locale.ROOT));
            }
            if (face.rotation() != ElementFace.DEFAULT_ROTATION) {
                writer.name("rotation").value(face.rotation());
            }
            if (face.tintIndex() != ElementFace.DEFAULT_TINT_INDEX) {
                writer.name("tintindex").value(face.tintIndex());
            }
            writer.endObject();
        }
        writer.endObject().endObject();
    }

    private static TextureUV getDefaultUvForFace(CubeFace face, Vector3Float from, Vector3Float to) {
        from = from.divide(MINECRAFT_UV_UNIT);
        to = to.divide(MINECRAFT_UV_UNIT);
        switch (face) {
            case WEST:
                return TextureUV.uv(from.z(), 1F - to.y(), to.z(), 1F - from.y());
            case EAST:
                return TextureUV.uv(1F - to.z(), 1F - to.y(), 1F - from.z(), 1F - from.y());
            case DOWN:
                return TextureUV.uv(from.x(), 1F - to.z(), to.x(), 1F - from.z());
            case UP:
                return TextureUV.uv(from.x(), from.z(), to.x(), to.z());
            case NORTH:
                return TextureUV.uv(1F - to.x(), 1F - to.y(), 1F - from.x(), 1F - from.y());
            case SOUTH:
                return TextureUV.uv(from.x(), 1F - to.y(), to.x(), 1F - from.y());
            default:
                throw new IllegalArgumentException("Unknown face: " + face);
        }
    }

    private static Element readElement(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();
        ElementRotation rotation = null;

        if (objectNode.has("rotation")) {
            rotation = readElementRotation(objectNode.get("rotation"));
        }

        Map<CubeFace, ElementFace> faces = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : objectNode.getAsJsonObject("faces").entrySet()) {
            CubeFace face = CubeFace.valueOf(entry.getKey().toUpperCase(Locale.ROOT));
            JsonObject elementFaceNode = entry.getValue().getAsJsonObject();
            TextureUV uv = null;
            if (elementFaceNode.has("uv")) {
                JsonArray array = elementFaceNode.getAsJsonArray("uv");
                Vector2Float from = new Vector2Float(array.get(0).getAsFloat(), array.get(1).getAsFloat());
                Vector2Float to = new Vector2Float(array.get(2).getAsFloat(), array.get(3).getAsFloat());
                uv = TextureUV.uv(
                        from.divide(MINECRAFT_UV_UNIT),
                        to.divide(MINECRAFT_UV_UNIT)
                );
            }

            CubeFace cullFace = null;
            if (elementFaceNode.has("cullface")) {
                try {
                    cullFace = CubeFace.valueOf(elementFaceNode.get("cullface").getAsString().toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }

            faces.put(
                    face,
                    ElementFace.face()
                            .uv(uv)
                            .texture(elementFaceNode.get("texture").getAsString())
                            .cullFace(cullFace)
                            .rotation(GsonUtil.getInt(elementFaceNode, "rotation", ElementFace.DEFAULT_ROTATION))
                            .tintIndex(GsonUtil.getInt(elementFaceNode, "tintindex", ElementFace.DEFAULT_TINT_INDEX))
                            .build()
            );
        }

        return Element.element()
                .from(GsonUtil.readVector3Float(objectNode.get("from")))
                .to(GsonUtil.readVector3Float(objectNode.get("to")))
                .rotation(rotation)
                .shade(GsonUtil.getBoolean(objectNode, "shade", Element.DEFAULT_SHADE))
                .lightEmission(GsonUtil.getInt(objectNode, "light_emission", 0))
                .faces(faces)
                .build();
    }

    private static void writeElementRotation(JsonWriter writer, ElementRotation rotation) throws IOException {
        writer.beginObject()
                .name("origin");
        GsonUtil.writeVector3Float(writer, rotation.origin());
        writer.name("axis").value(rotation.axis().name().toLowerCase(Locale.ROOT))
                .name("angle").value(rotation.angle());

        boolean rescale = rotation.rescale();
        if (rescale != ElementRotation.DEFAULT_RESCALE) {
            // only write if not equal to default value
            writer.name("rescale").value(rescale);
        }
        writer.endObject();
    }

    private static ElementRotation readElementRotation(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();
        return ElementRotation.builder()
                .origin(GsonUtil.readVector3Float(objectNode.get("origin")))
                .axis(Axis3D.valueOf(objectNode.get("axis").getAsString().toUpperCase(Locale.ROOT)))
                .angle(objectNode.get("angle").getAsFloat())
                .rescale(GsonUtil.getBoolean(objectNode, "rescale", ElementRotation.DEFAULT_RESCALE))
                .build();
    }

    private static void writeItemOverride(JsonWriter writer, ItemOverride override) throws IOException {
        writer.beginObject()
                .name("predicate").beginObject();
        for (ItemPredicate predicate : override.predicate()) {
            writer.name(predicate.name());
            Object value = predicate.value();

            // match the type of the value and write it
            if (value instanceof Long) {
                writer.value((long) value);
            } else if (value instanceof Float) {
                writer.value((float) value);
            } else if (value instanceof Double) {
                writer.value((double) value);
            } else if (value instanceof Number) {
                writer.value((Number) value);
            } else if (value instanceof String) {
                writer.value((String) value);
            } else if (value instanceof Boolean) {
                writer.value((boolean) value);
            } else {
                throw new IOException("Unknown predicate value type: " + value.getClass().getName());
            }
        }
        writer.endObject()
                .name("model").value(KeySerializer.toString(override.model()))
                .endObject();
    }

    private static ItemOverride readItemOverride(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();
        Key key = Key.key(objectNode.get("model").getAsString());
        List<ItemPredicate> predicates = new ArrayList<>();
        for (Map.Entry<String, JsonElement> predicateEntry : objectNode.getAsJsonObject("predicate").entrySet()) {
            JsonElement value = predicateEntry.getValue();
            // TODO: better transformation
            Object object;
            if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    object = primitive.getAsNumber();
                } else if (primitive.isBoolean()) {
                    object = primitive.getAsBoolean();
                } else {
                    object = primitive.getAsString();
                }
            } else {
                object = value.getAsString();
            }
            predicates.add(ItemPredicate.custom(predicateEntry.getKey(), object));
        }
        return ItemOverride.of(key, predicates);
    }

    private static void writeItemTransform(JsonWriter writer, ItemTransform transform) throws IOException {
        writer.beginObject();
        Vector3Float rotation = transform.rotation();
        if (!rotation.equals(ItemTransform.DEFAULT_ROTATION)) {
            writer.name("rotation");
            GsonUtil.writeVector3Float(writer, rotation);
        }
        Vector3Float translation = transform.translation();
        if (!translation.equals(ItemTransform.DEFAULT_TRANSLATION)) {
            writer.name("translation");
            GsonUtil.writeVector3Float(writer, translation);
        }
        Vector3Float scale = transform.scale();
        if (!scale.equals(ItemTransform.DEFAULT_SCALE)) {
            writer.name("scale");
            GsonUtil.writeVector3Float(writer, scale);
        }
        writer.endObject();
    }

    private static ItemTransform readItemTransform(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();
        Vector3Float rotation = ItemTransform.DEFAULT_ROTATION;
        Vector3Float translation = ItemTransform.DEFAULT_TRANSLATION;
        Vector3Float scale = ItemTransform.DEFAULT_SCALE;
        if (objectNode.has("rotation")) {
            rotation = GsonUtil.readVector3Float(objectNode.get("rotation"));
        }
        if (objectNode.has("translation")) {
            translation = GsonUtil.readVector3Float(objectNode.get("translation"));
            // clamp translations between -80 and 80 (what Minecraft does)
            translation = new Vector3Float(
                    Math.max(-80F, Math.min(80F, translation.x())),
                    Math.max(-80F, Math.min(80F, translation.y())),
                    Math.max(-80F, Math.min(80F, translation.z()))
            );
        }
        if (objectNode.has("scale")) {
            scale = GsonUtil.readVector3Float(objectNode.get("scale"));
            // set max to 4 (what Minecraft does)
            scale = new Vector3Float(
                    Math.min(4F, scale.x()),
                    Math.min(4F, scale.y()),
                    Math.min(4F, scale.z())
            );
        }
        return ItemTransform.transform(rotation, translation, scale);
    }

    private static void writeTextures(JsonWriter writer, ModelTextures texture) throws IOException {
        final ModelTexture particle = texture.particle();
        final List<ModelTexture> layers = texture.layers();
        final Map<String, ModelTexture> variables = texture.variables();

        if (particle == null && layers.isEmpty() && variables.isEmpty()) {
            // do not write if completely empty
            return;
        }

        writer.name("textures");
        writer.beginObject();
        if (particle != null) {
            writer.name("particle");
            writeModelTexture(writer, particle);
        }
        for (int i = 0; i < layers.size(); i++) {
            writer.name("layer" + i);
            writeModelTexture(writer, layers.get(i));
        }
        for (Map.Entry<String, ModelTexture> variable : variables.entrySet()) {
            writer.name(variable.getKey());
            writeModelTexture(writer, variable.getValue());
        }
        writer.endObject();
    }

    private static void writeModelTexture(JsonWriter writer, ModelTexture texture) throws IOException {
        if (texture.reference() != null) {
            writer.value("#" + texture.reference());
        } else {
            writer.value(KeySerializer.toString(texture.key()));
        }
    }

    private static ModelTextures readTextures(JsonElement node) {

        JsonObject objectNode = node.getAsJsonObject();
        ModelTexture particle = null;
        List<ModelTexture> layers = new ArrayList<>(objectNode.entrySet().size());
        Map<String, ModelTexture> variables = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : objectNode.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            ModelTexture texture = value.charAt(0) == '#'
                    ? ModelTexture.ofReference(value.substring(1))
                    : ModelTexture.ofKey(Key.key(value));

            if ("particle".equals(key)) {
                particle = texture;
            } else if (key.startsWith("layer")) {
                int layer = Integer.parseInt(key.substring("layer".length()));
                // TODO: Fix
                layers.add(texture);
            } else {
                variables.put(key, texture);
            }
        }

        return ModelTextures.builder()
                .particle(particle)
                .layers(layers)
                .variables(variables)
                .build();
    }

}