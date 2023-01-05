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
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.blockstate.Condition;
import team.unnamed.creative.blockstate.MultiVariant;
import team.unnamed.creative.blockstate.Selector;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.serialize.minecraft.io.FileTreeWriter;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final class SerializerBlockState {

    static final SerializerBlockState INSTANCE = new SerializerBlockState();

    public void write(BlockState state, FileTreeWriter tree) throws IOException {
        try (JsonWriter writer = tree.openJsonWriter(MinecraftResourcePackStructure.pathOf(state))) {
            writer.beginObject();

            // write "variants" part if not empty
            Map<String, MultiVariant> variants = state.variants();
            if (!variants.isEmpty()) {
                writer.name("variants").beginObject();
                for (Map.Entry<String, MultiVariant> entry : variants.entrySet()) {
                    writer.name(entry.getKey());
                    writeMultiVariant(writer, entry.getValue());
                }
                writer.endObject();
            }

            // write "multipart" part if not empty
            List<Selector> multipart = state.multipart();
            if (!multipart.isEmpty()) {
                writer.name("multipart").beginArray();
                for (Selector selector : multipart) {
                    writeSelector(writer, selector);
                }
                writer.endArray();
            }

            writer.endObject();
        }
    }

    private static void writeMultiVariant(JsonWriter writer, MultiVariant multiVariant) throws IOException {
        List<Variant> variants = multiVariant.variants();
        if (variants.size() == 1) {
            // single variant, write as an object
            writeVariant(writer, variants.get(0));
        } else {
            // multiple variants, write as an array
            writer.beginArray();
            for (Variant variant : variants) {
                writeVariant(writer, variant);
            }
            writer.endArray();
        }
    }

    private static void writeVariant(JsonWriter writer, Variant variant) throws IOException {
        writer
                .beginObject()
                .name("model").value(Keys.toString(variant.model()));
        int x = variant.x();
        if (x != Variant.DEFAULT_X_ROTATION) {
            writer.name("x").value(x);
        }
        int y = variant.y();
        if (y != Variant.DEFAULT_Y_ROTATION) {
            writer.name("y").value(y);
        }
        boolean uvLock = variant.uvLock();
        if (uvLock != Variant.DEFAULT_UV_LOCK) {
            writer.name("uvlock").value(uvLock);
        }
        int weight = variant.weight();
        if (weight != Variant.DEFAULT_WEIGHT) {
            writer.name("weight").value(weight);
        }
        writer.endObject();
    }

    private static void writeSelector(JsonWriter writer, Selector selector) throws IOException {
        writer.beginObject();

        Condition condition = selector.condition();
        if (condition != Condition.NONE) {
            writer.name("when").beginObject();
            writeCondition(writer, condition, true);
            writer.endObject();
        }

        writer.name("apply");
        writeMultiVariant(writer, selector.variant());
        writer.endObject();
    }

    private static void writeCondition(JsonWriter writer, Condition condition) throws IOException {
        writeCondition(writer, condition, false);
    }

    private static void writeCondition(JsonWriter writer, Condition condition, boolean topLevel) throws IOException {
        if (condition instanceof Condition.Match) {
            Condition.Match match = (Condition.Match) condition;
            writer.name(match.key()).value(match.value().toString());// TODO: don't toString()
        } else if (condition instanceof Condition.Or) {
            List<Condition> conditions = ((Condition.Or) condition).conditions();
            if (conditions.size() == 1) {
                // single condition, just write it
                writeCondition(writer, conditions.get(0));
                return;
            }

            writer.name("OR").beginArray();
            for (Condition orCondition : conditions) {
                writer.beginObject();
                writeCondition(writer, orCondition);
                writer.endObject();
            }
            writer.endArray();
        } else if (condition instanceof Condition.And) {
            List<Condition> conditions = ((Condition.And) condition).conditions();
            if (topLevel || conditions.size() == 1) {
                // top level or single condition, direct write
                for (Condition child : conditions) {
                    writeCondition(writer, child);
                }
                return;
            }

            writer.name("AND").beginArray();
            for (Condition child : conditions) {
                writer.beginArray();
                writeCondition(writer, child);
                writer.endObject();
            }
            writer.endArray();
        }
    }

}