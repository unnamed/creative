/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle;

import net.kyori.adventure.key.Key;

import java.io.OutputStream;

/**
 * Represents the object responsible for creating and
 * writing the resource-pack tree
 *
 * @since 1.0.0
 */
public interface TreeWriter {

    /**
     * Joins a file context at a {@link Key} location,
     * using a suffix, only one context instance must
     * exist.
     *
     * @param location The file location
     * @param suffix The file location suffix (post-namespace)
     * @return The file context
     */
    Context join(Key location, String suffix);

    /**
     * Joins a file context at a {@link Key} location,
     * using a suffix (path prefix, namespace suffix)
     * and an extension (path suffix), only one context
     * instance must exist.
     *
     * @param location The file location
     * @param suffix The file locations suffix (post-namespace)
     * @param extension The file extension
     * @return The file context
     */
    Context join(Key location, String suffix, String extension);

    /**
     * Joins a file context at a {@link String} location,
     * only one context instance must exist
     *
     * @param location The file location
     * @return The file context
     */
    Context join(String location);

    /**
     * Represents a file in a tree, contains some utility methods
     * to write properties with a format similar to JSON
     *
     * @since 1.0.0
     */
    abstract class Context extends OutputStream implements AutoCloseable {

        /**
         * Writes the start of an object, '{' in case
         * of JSON format
         */
        public abstract void startObject();

        /**
         * Writes the end of an object, '}' in case
         * of JSON format
         */
        public abstract void endObject();

        /**
         * Writes the start of an array, '[' in case
         * of JSON format
         */
        public abstract void startArray();

        /**
         * Writes the end of an array, ']' in case of
         * JSON format
         */
        public abstract void endArray();

        /**
         * Writes an object key, 'key:' in case of
         * JSON format, this method lets the cursor
         * prepared for a value
         *
         * @param key The object key
         */
        public abstract void writeKey(String key);

        /**
         * Writes a string field (key and value), 'key: "value"'
         * in case of JSON format, this method is a shortcut for
         * {@link Context#writeKey} and {@link Context#writeStringValue}
         *
         * @param key The string field key
         * @param value The string field value
         */
        public void writeStringField(String key, String value) {
            writeKey(key);
            writeStringValue(value);
        }

        /**
         * Writes a boolean field (key and value), 'key: value'
         * in case of JSON format
         *
         * @param key The boolean field key
         * @param value The boolean field value
         */
        public abstract void writeBooleanField(String key, boolean value);

        /**
         * Writes an integer field (key and value), 'key: value'
         * in case of JSON format
         *
         * @param key The int field key
         * @param value The int field value
         */
        public abstract void writeIntField(String key, int value);

        /**
         * Writes a float field (key and value), 'key: value'
         * in case of JSON format
         *
         * @param key The float field key
         * @param value The float field value
         */
        public abstract void writeFloatField(String key, float value);

        /**
         * Writes a string value (without key) into this
         * file. In case of JSON, it just quotes, escapes
         * and writes the given value
         *
         * @param value The written string
         */
        public abstract void writeStringValue(String value);

        /**
         * Writes an integer value (without key) into this
         * file. In case of JSON, it just writes its decimal
         * representation into this file.
         *
         * @param value The written integer
         */
        public abstract void writeIntValue(int value);

        /**
         * Writes an {@link Element.Part} into this
         * file
         *
         * @param value The element part
         */
        public void writePart(Element.Part value) {
            value.write(this);
        }

        /**
         * Closes this file context and lets {@link TreeWriter}
         * create another one
         */
        @Override
        public abstract void close();

    }

}
