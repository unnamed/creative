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
package team.unnamed.creative.blockstate;

import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.file.SerializableResource;

/**
 * Represents a {@link Selector} condition, may be used
 * to create from simple to very complex conditions to
 * match block state properties ({@link Condition#match}
 *
 * @see Selector
 * @since 1.0.0
 */
public interface Condition extends SerializableResource {

    Condition NONE = (tree, topLevel) -> {};

    void serialize(ResourceWriter writer, boolean topLevel);

    @Override
    default void serialize(ResourceWriter writer) {
        serialize(writer, false);
    }

    static Condition and(Condition... conditions) {
        return (writer, topLevel) -> {
            if (topLevel || conditions.length == 1) {
                // single condition, just delegate serialization
                for (Condition condition : conditions) {
                    condition.serialize(writer);
                }
                return;
            }

            writer.key("AND").startArray();
            for (Condition condition : conditions) {
                writer.startObject();
                condition.serialize(writer);
                writer.endObject();
            }
            writer.endArray();
        };
    }

    static Condition or(Condition... conditions) {
        return (writer, topLevel) -> {
            if (conditions.length == 1) {
                // single condition, just delegate serialization
                conditions[0].serialize(writer);
                return;
            }

            writer.key("OR").startArray();
            for (Condition condition : conditions) {
                writer.startObject();
                condition.serialize(writer);
                writer.endObject();
            }
            writer.endArray();
        };
    }

    static Condition match(String key, Object value) {
        return (writer, topLevel) -> writer.key(key).value(value);
    }

}
