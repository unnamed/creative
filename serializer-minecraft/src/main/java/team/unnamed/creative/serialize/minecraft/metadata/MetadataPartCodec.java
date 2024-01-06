/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
package team.unnamed.creative.serialize.minecraft.metadata;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.serialize.minecraft.GsonUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

interface MetadataPartCodec<T extends MetadataPart> {

    @NotNull Class<T> type();

    @NotNull String name();

    @NotNull T read(final @NotNull JsonObject node);

    default @NotNull T fromJson(final @NotNull String json) {
        return read(GsonUtil.parseString(json).getAsJsonObject());
    }

    void write(final @NotNull JsonWriter writer, final @NotNull T part) throws IOException;

    default @NotNull String toJson(final @NotNull T part) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        try {
            write(jsonWriter, part);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write", e);
        }
        return writer.toString();
    }

}
