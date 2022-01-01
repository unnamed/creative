/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.model;

import team.unnamed.uracle.CubeFace;
import team.unnamed.uracle.serialize.AssetWriter;

import java.util.Locale;
import java.util.Map;

abstract class AbstractModel
        implements Model {

    protected abstract void serializeOwnProperties(AssetWriter writer);

    @Override
    public void serialize(AssetWriter writer) {

        writer.startObject();

        // parent
        writer.key("parent").value(parent());

        // display
        writer.key("display").startObject();
        for (Map.Entry<ModelDisplay.Type, ModelDisplay> entry : display().entrySet()) {
            ModelDisplay.Type type = entry.getKey();
            ModelDisplay display = entry.getValue();

            writer.key(type.name().toLowerCase(Locale.ROOT)).startObject()
                    .key("rotation").value(display.rotation())
                    .key("translation").value(display.translation())
                    .key("scale").value(display.scale())
                    .endObject();
        }
        writer.endObject();

        // elements
        writer.key("elements").startArray();
        for (Element element : elements()) {
            ElementRotation rotation = element.rotation();

            writer
                    .key("from").value(element.from())
                    .key("to").value(element.to())
                    .key("rotation").startObject()
                    .key("origin").value(rotation.origin())
                    .key("axis").value(rotation.axis().name().toLowerCase(Locale.ROOT))
                    .key("angle").value(rotation.angle());

            if (rotation.rescale()) {
                // only write if not equal to default value
                writer.key("rescale").value(rotation.rescale());
            }
            writer.endObject();

            if (!element.shade()) {
                // only write if not equal to default value
                writer.key("shade").value(element.shade());
            }

            // faces
            writer.key("faces").startObject();
            for (Map.Entry<CubeFace, ElementFace> entry : element.faces().entrySet()) {
                CubeFace type = entry.getKey();
                ElementFace face = entry.getValue();

                writer.key(type.name().toLowerCase(Locale.ROOT)).startObject();
                if (face.uv() != null) {
                    writer.key("uv").value(face.uv());
                }
                writer.key("texture").value(face.texture());
                if (face.cullFace() != null) {
                    writer.key("cullface").value(face.cullFace().name().toLowerCase(Locale.ROOT));
                }
                if (face.rotation() != 0) {
                    writer.key("rotation").value(face.rotation());
                }
                if (face.tintIndex() != null) {
                    writer.key("tintindex").value(face.tintIndex());
                }
                writer.endObject();
            }
            writer.endObject();
        }
        writer.endArray();

        serializeOwnProperties(writer);
        writer.endObject();
    }

}
