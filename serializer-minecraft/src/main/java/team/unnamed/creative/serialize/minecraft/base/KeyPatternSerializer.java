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
package team.unnamed.creative.serialize.minecraft.base;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.ApiStatus;
import team.unnamed.creative.base.KeyPattern;

import java.io.IOException;
import java.util.regex.Pattern;

@ApiStatus.Internal
public final class KeyPatternSerializer {

    private KeyPatternSerializer() {
        throw new UnsupportedOperationException("Can't instantiate utility class");
    }

    // this class serializes and deserializes KeyPattern
    // instances from/to JSON format (readable by Minecraft
    // clients)
    //
    // its format is the following:
    // {
    //     "namespace": <optional regex string>,
    //     "path": <optional regex string>
    // }
    //
    // Some valid examples:
    //  - { "namespace": "minecraft" }
    //  - { "namespace": "minecraft", "path": "thing/" }
    //  - { "path": "thing/" }
    //  - { }

    public static void serialize(KeyPattern pattern, JsonWriter writer) throws IOException {
        Pattern namespace = pattern.namespace();
        Pattern value = pattern.value();

        writer.beginObject();
        if (namespace != null) {
            writer.name("namespace").value(namespace.pattern());
        }
        if (value != null) {
            writer.name("path").value(value.pattern());
        }
        writer.endObject();
    }

    public static KeyPattern deserialize(JsonObject node) {
        @RegExp String namespace = null;
        @RegExp String path = null;
        if (node.has("namespace")) {
            namespace = node.get("namespace").getAsString();
        }
        if (node.has("path")) {
            path = node.get("path").getAsString();
        }
        return KeyPattern.of(namespace, path);
    }

}
