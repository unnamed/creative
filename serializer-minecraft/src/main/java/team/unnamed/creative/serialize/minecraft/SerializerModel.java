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
package team.unnamed.creative.serialize.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.base.Axis3D;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.base.Vector4Float;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.ElementRotation;
import team.unnamed.creative.model.ItemOverride;
import team.unnamed.creative.model.ItemPredicate;
import team.unnamed.creative.model.ItemTransform;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.serialize.minecraft.io.GsonUtil;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class SerializerModel implements JsonFileStreamWriter<Model>, JsonFileTreeReader.Keyed<Model> {

    static final SerializerModel INSTANCE = new SerializerModel();

    @Override
    public void serialize(Model model, JsonWriter writer) throws IOException {
        writer.beginObject();

        // parent
        Key parent = model.parent();
        if (parent != null) {
            writer.name("parent").value(Keys.toString(parent));
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

        writer.name("textures");
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
    public Model readFromTree(JsonElement node, Key key) {

        JsonObject objectNode = node.getAsJsonObject();

        // parent
        Key parent = null;
        if (objectNode.has("parent")) {
            parent = Key.key(objectNode.get("parent").getAsString());
        }

        // display
        Map<ItemTransform.Type, ItemTransform> display = new HashMap<>();
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

        return Model.builder()
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
        writeVector3Float(writer, element.from());
        writer.name("to");
        writeVector3Float(writer, element.to());

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

        // faces
        writer.name("faces").beginObject();
        for (Map.Entry<CubeFace, ElementFace> entry : element.faces().entrySet()) {
            CubeFace type = entry.getKey();
            ElementFace face = entry.getValue();

            writer.name(type.name().toLowerCase(Locale.ROOT))
                    .beginObject();
            if (face.uv() != null) {
                Vector4Float uv = face.uv();
                Vector4Float defaultUv = ElementFace.getDefaultUvForFace(type, element.from(), element.to());
                if (uv != null && !uv.equals(defaultUv)) {
                    writer.name("uv");
                    writeVector4Float(writer, uv.multiply(ElementFace.MINECRAFT_UV_UNIT));
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

    private static Element readElement(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();
        ElementRotation rotation = null;

        if (objectNode.has("rotation")) {
            rotation = readElementRotation(objectNode.get("rotation"));
        }

        Map<CubeFace, ElementFace> faces = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : objectNode.getAsJsonObject("faces").entrySet()) {
            CubeFace face = CubeFace.valueOf(entry.getKey().toUpperCase(Locale.ROOT));
            JsonObject elementFaceNode = entry.getValue().getAsJsonObject();
            Vector4Float uv = null;
            if (elementFaceNode.has("uv")) {
                uv = readVector4Float(elementFaceNode.get("uv")).multiply(1F / ElementFace.MINECRAFT_UV_UNIT);
            }

            CubeFace cullFace = null;
            if (elementFaceNode.has("cullface")) {
                cullFace = CubeFace.valueOf(elementFaceNode.get("cullface").getAsString().toUpperCase(Locale.ROOT));
            }

            faces.put(
                    face,
                    ElementFace.builder()
                            .uv(uv)
                            .texture(elementFaceNode.get("texture").getAsString())
                            .cullFace(cullFace)
                            .rotation(GsonUtil.getInt(elementFaceNode, "rotation", ElementFace.DEFAULT_ROTATION))
                            .tintIndex(GsonUtil.getInt(elementFaceNode, "tintindex", ElementFace.DEFAULT_TINT_INDEX))
                            .build()
            );
        }

        return Element.builder()
                .from(readVector3Float(objectNode.get("from")))
                .to(readVector3Float(objectNode.get("to")))
                .rotation(rotation)
                .shade(GsonUtil.getBoolean(objectNode, "shade", Element.DEFAULT_SHADE))
                .faces(faces)
                .build();
    }

    private static void writeElementRotation(JsonWriter writer, ElementRotation rotation) throws IOException {
        writer.beginObject()
                .name("origin");
        writeVector3Float(writer, rotation.origin());
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
                .origin(readVector3Float(objectNode.get("origin")))
                .axis(Axis3D.valueOf(objectNode.get("axis").getAsString().toUpperCase(Locale.ROOT)))
                .angle(objectNode.get("angle").getAsFloat())
                .rescale(GsonUtil.getBoolean(objectNode, "rescale", ElementRotation.DEFAULT_RESCALE))
                .build();
    }

    private static void writeItemOverride(JsonWriter writer, ItemOverride override) throws IOException {
        writer.beginObject()
                .name("predicate").beginObject();
        for (ItemPredicate predicate : override.predicate()) {
            writer.name(predicate.name()).value(predicate.value().toString()); // TODO:!
        }
        writer.endObject()
                .name("model").value(Keys.toString(override.model()))
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
            writeVector3Float(writer, rotation);
        }
        Vector3Float translation = transform.translation();
        if (!translation.equals(ItemTransform.DEFAULT_TRANSLATION)) {
            writer.name("translation");
            writeVector3Float(writer, translation);
        }
        Vector3Float scale = transform.scale();
        if (!scale.equals(ItemTransform.DEFAULT_SCALE)) {
            writer.name("scale");
            writeVector3Float(writer, scale);
        }
        writer.endObject();
    }

    private static ItemTransform readItemTransform(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();
        Vector3Float rotation = ItemTransform.DEFAULT_ROTATION;
        Vector3Float translation = ItemTransform.DEFAULT_TRANSLATION;
        Vector3Float scale = ItemTransform.DEFAULT_SCALE;
        if (objectNode.has("rotation")) {
            rotation = readVector3Float(objectNode.get("rotation"));
        }
        if (objectNode.has("translation")) {
            translation = readVector3Float(objectNode.get("translation"));
        }
        if (objectNode.has("scale")) {
            scale = readVector3Float(objectNode.get("scale"));
        }
        return ItemTransform.of(rotation, translation, scale);
    }

    private static void writeTextures(JsonWriter writer, ModelTextures texture) throws IOException {
        writer.beginObject();
        ModelTexture particle = texture.particle();
        if (particle != null) {
            writer.name("particle");
            writeModelTexture(writer, particle);
        }
        List<ModelTexture> layers = texture.layers();
        for (int i = 0; i < layers.size(); i++) {
            writer.name("layer" + i);
            writeModelTexture(writer, layers.get(i));
        }
        for (Map.Entry<String, ModelTexture> variable : texture.variables().entrySet()) {
            writer.name(variable.getKey());
            writeModelTexture(writer, variable.getValue());
        }
        writer.endObject();
    }

    private static void writeModelTexture(JsonWriter writer, ModelTexture texture) throws IOException {
        if (texture.reference() != null) {
            writer.value("#" + texture.reference());
        } else {
            writer.value(Keys.toString(texture.key()));
        }
    }

    private static ModelTextures readTextures(JsonElement node) {

        JsonObject objectNode = node.getAsJsonObject();
        ModelTexture particle = null;
        List<ModelTexture> layers = new ArrayList<>(objectNode.entrySet().size());
        Map<String, ModelTexture> variables = new HashMap<>();

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

    private static void writeVector3Float(JsonWriter writer, Vector3Float vector) throws IOException {
        writer.beginArray();
        writer.value(vector.x());
        writer.value(vector.y());
        writer.value(vector.z());
        writer.endArray();
    }

    private static void writeVector2Float(JsonWriter writer, Vector2Float vector) throws IOException {
        writer.beginArray();
        writer.value(vector.x());
        writer.value(vector.y());
        writer.endArray();
    }

    private static void writeVector4Float(JsonWriter writer, Vector4Float vector) throws IOException {
        writer.beginArray();
        writer.value(vector.x());
        writer.value(vector.y());
        writer.value(vector.x2());
        writer.value(vector.y2());
        writer.endArray();
    }

   private static Vector3Float readVector3Float(JsonElement element) {
       JsonArray array = element.getAsJsonArray();
       return new Vector3Float(
               (float) array.get(0).getAsDouble(),
               (float) array.get(1).getAsDouble(),
               (float) array.get(2).getAsDouble()
       );
   }

    private static Vector4Float readVector4Float(JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        return new Vector4Float(
                (float) array.get(0).getAsDouble(),
                (float) array.get(1).getAsDouble(),
                (float) array.get(2).getAsDouble(),
                (float) array.get(3).getAsDouble()
        );
    }

}