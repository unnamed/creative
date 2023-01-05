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

import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
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
import team.unnamed.creative.serialize.minecraft.io.FileTree;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class SerializerModel {

    static final SerializerModel INSTANCE = new SerializerModel();

    public void write(Model model, FileTree tree) throws IOException {
        try (JsonWriter writer = tree.openJsonWriter(MinecraftResourcePackStructure.pathOf(model))) {
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
            writeTexture(writer, model.textures());

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

    private static void writeTexture(JsonWriter writer, ModelTexture texture) throws IOException {
        writer.beginObject();
        Key particle = texture.particle();
        if (particle != null) {
            writer.name("particle").value(Keys.toString(particle));
        }
        List<Key> layers = texture.layers();
        for (int i = 0; i < layers.size(); i++) {
            writer.name("layer" + i).value(Keys.toString(layers.get(i)));
        }
        for (Map.Entry<String, Key> variable : texture.variables().entrySet()) {
            writer.name(variable.getKey()).value(Keys.toString(variable.getValue()));
        }
        writer.endObject();
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

}