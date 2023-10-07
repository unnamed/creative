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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.ApiStatus;
import team.unnamed.creative.metadata.pack.PackFormat;

import java.io.IOException;

@ApiStatus.Internal
public final class PackFormatSerializer {

    private PackFormatSerializer() {
        throw new UnsupportedOperationException("Can't instantiate utility class");
    }

    // this class serializes and deserializes PackFormat
    // instances from/to JSON format (readable by Minecraft
    // clients)
    //
    // it has multiple formats:
    // - number
    // - [ number, number ]
    // - { "min_inclusive": number, "max_inclusive": number }
    //
    // Some valid examples:
    //  - 16
    //  - [ 16, 17 ]
    //  - { "min_inclusive": 16, "max_inclusive": 17 }

    public static void serialize(PackFormat format, JsonWriter writer) throws IOException {
        // serialize using the shortest possible version
        if (format.isSingle()) {
            writer.value(format.format());
        } else {
            writer.beginArray();
            writer.value(format.min());
            writer.value(format.max());
            writer.endArray();
        }
    }

    public static PackFormat deserialize(JsonElement el, int format) {
        PackFormat f = deserialize(el);
        return PackFormat.format(format, f.min(), f.max());
    }

    public static PackFormat deserialize(JsonElement el) {
        int min;
        int max;
        if (el.isJsonPrimitive()) {
            // single value
            min = max = el.getAsInt();
        } else if (el.isJsonArray()) {
            JsonArray arr = el.getAsJsonArray();
            // [min, max]
            min = arr.get(0).getAsInt();
            max = arr.get(1).getAsInt();
        } else if (el.isJsonObject()) {
            JsonObject obj = el.getAsJsonObject();
            // {"min_inclusive": min, "max_inclusive": max}
            min = obj.get("min_inclusive").getAsInt();
            max = obj.get("max_inclusive").getAsInt();
        } else {
            throw new IllegalStateException("Unsupported supported_formats type: " + el.getClass());
        }
        return PackFormat.format(min, min, max);
    }

}
