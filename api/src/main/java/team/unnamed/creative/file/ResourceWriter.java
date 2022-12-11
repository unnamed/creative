/*
 * This file is part of creative, licensed under the MIT license
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
package team.unnamed.creative.file;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.util.Keys;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * Helper class to write JavaScript-Notated Objects to a stream, and
 * easing the usage of some {@link OutputStream} methods by converting
 * its checked exceptions to unchecked exceptions
 *
 * <strong>This is a modified version of Google's GSON JsonWriter that
 * can be reused to write multiple objects to a stream and allowing to
 * write raw bytes to the underlying stream</strong>
 *
 * @author Jesse Wilson
 * @since 1.0.0
 */
public class ResourceWriter
        extends FilterOutputStream {

    private static final int EMPTY_ARRAY = 1;
    private static final int NONEMPTY_ARRAY = 2;

    private static final int EMPTY_OBJECT = 3;
    private static final int NONEMPTY_OBJECT = 4;

    private static final int DANGLING_KEY = 5;
    private static final int DOCUMENT = 6;

    /*
     * From RFC 7159, "All Unicode characters may be placed within the
     * quotation marks except for the characters that must be escaped:
     * quotation mark, reverse solidus, and the control characters
     * (U+0000 through U+001F)."
     *
     * We also escape '\u2028' and '\u2029', which JavaScript interprets as
     * newline characters. This prevents eval() from failing with a syntax
     * error. http://code.google.com/p/google-gson/issues/detail?id=341
     */
    private static final String[] REPLACEMENT_CHARS;

    static {
        REPLACEMENT_CHARS = new String[128];
        REPLACEMENT_CHARS['"'] = "\\\"";
        REPLACEMENT_CHARS['\\'] = "\\\\";
        REPLACEMENT_CHARS['\t'] = "\\t";
        REPLACEMENT_CHARS['\b'] = "\\b";
        REPLACEMENT_CHARS['\n'] = "\\n";
        REPLACEMENT_CHARS['\r'] = "\\r";
        REPLACEMENT_CHARS['\f'] = "\\f";
        for (int i = 0; i <= 0x1F; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
    }

    /**
     * Internal {@link Writer} instance, to deal with
     * characters and encoding, we can't directly extend
     * Writer since we need compatibility with it as we
     * handle binary data like textures
     */
    private final Writer writer;

    /**
     * A string containing a full set of spaces
     * for a single level of indentation, or null
     * for no pretty-printing
     */
    @Nullable private String indent;

    /**
     * The name/value separator; either ":"
     * or ": "
     */
    private String separator = ":";

    private int[] stack = new int[16];
    private int stackSize = 0;

    @Nullable private String deferredName;

    public ResourceWriter(OutputStream out, Charset charset) {
        super(out);
        this.writer = new OutputStreamWriter(out, charset);
        // initialize stack
        push(DOCUMENT);
    }

    public ResourceWriter(OutputStream out) {
        this(out, StandardCharsets.UTF_8);
    }

    /**
     * Sets the indentation string to be repeated for each level
     * of indentation in the written JSON document. If the given
     * string is empty, the encoded document will be compact.
     * Otherwise, the encoded document will be more human-readable
     *
     * @param indent a string containing only whitespace.
     */
    public void indent(String indent) {
        if (indent.isEmpty()) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = indent;
            this.separator = ": ";
        }
    }

    public ResourceWriter startObject() {
        return start(EMPTY_OBJECT, '{');
    }

    public ResourceWriter endObject() {
        return end(EMPTY_OBJECT, NONEMPTY_OBJECT, '}');
    }

    public ResourceWriter startArray() {
        return start(EMPTY_ARRAY, '[');
    }

    public ResourceWriter endArray() {
        return end(EMPTY_ARRAY, NONEMPTY_ARRAY, ']');
    }

    public ResourceWriter key(String key) {
        requireNonNull(key, "key");
        if (deferredName != null) {
            throw new IllegalStateException("There is already a deferred name");
        }
        deferredName = key;
        return this;
    }

    public ResourceWriter value(Object object) {
        if (object instanceof Key) {
            // transform key to string, keys
            // can be optimized by removing
            // "minecraft" namespace
            object = Keys.toString((Key) object);
        }

        if (object instanceof String) {
            writeDeferredName();
            beforeValue();
            // strings must be quoted and escaped
            string(object.toString());
        } else if (object instanceof Iterable) {
            // array-like value
            startArray();
            for (Object e : ((Iterable<?>) object)) {
                value(e);
            }
            return endArray();
        } else if (object instanceof SerializableResource) {
            ((SerializableResource) object).serialize(this);
            return this;
        } else {
            if (object instanceof Double || object instanceof Float) {
                Number number = (Number) object;
                double value = number.doubleValue();
                int intValue = number.intValue();

                // optimization: removes ".0" suffix
                // and negation sign on zeros (-0.0)
                if (value == intValue) {
                    object = intValue;
                }
            }
            writeDeferredName();
            beforeValue();
            // other objects (numbers, booleans) are
            // written literally as its toString() value
            try {
                writer.write(String.valueOf(object));
                writer.flush();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return this;
    }

    private void writeDeferredName() {
        if (deferredName != null) {
            beforeKey();
            string(deferredName);
            deferredName = null;
        }
    }

    private void string(String value) {
        try {
            // write start quote
            writer.write('\"');

            int last = 0;
            int length = value.length();

            for (int i = 0; i < length; i++) {
                char c = value.charAt(i);
                String replacement;
                if (c < 128) {
                    replacement = REPLACEMENT_CHARS[c];
                    if (replacement == null) {
                        continue;
                    }
                } else if (c == '\u2028') {
                    replacement = "\\u2028";
                } else if (c == '\u2029') {
                    replacement = "\\u2029";
                } else {
                    continue;
                }
                if (last < i) {
                    writer.write(value, last, i - last);
                }
                writer.write(replacement);
                last = i + 1;
            }
            if (last < length) {
                writer.write(value, last, length - last);
            }

            // write end quote
            writer.write('\"');
            writer.flush();
        } catch (IOException e) {
             throw new UncheckedIOException(e);
        }
    }

    //#region Stack manipulation
    private void push(int newTop) {
        if (stackSize == stack.length) {
            // create a new, bigger stack array
            stack = Arrays.copyOf(stack, stackSize * 2);
        }
        stack[stackSize++] = newTop;
    }

    private int peek() {
        return stack[stackSize - 1];
    }

    private void replaceTop(int topOfStack) {
        stack[stackSize - 1] = topOfStack;
    }
    //#endregion

    //#region Starting and ending objects or arrays
    private ResourceWriter start(int empty, char openBracket) {
        writeDeferredName();
        beforeValue();
        push(empty);
        try {
            writer.write(openBracket);
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    private ResourceWriter end(int empty, int nonempty, char closeBracket) {
        int context = peek();
        if (context != nonempty && context != empty) {
            throw new IllegalStateException("Nesting problem");
        }
        if (deferredName != null) {
            throw new IllegalStateException("Dangling name: " + deferredName);
        }

        stackSize--;
        if (context == nonempty) {
            newLine();
        }
        write(closeBracket);
        return this;
    }
    //#endregion

    //#region Key and value preparation
    /**
     * Inserts any necessary separators and whitespace
     * before a key. Also adjusts the stack to expect
     * the key's value
     */
    private void beforeKey() {
        int context = peek();
        if (context == NONEMPTY_OBJECT) {
            try {
                // not the first entry in an
                // object, write a comma
                writer.write(',');
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else if (context != EMPTY_OBJECT) {
            throw new IllegalStateException(
                    "Keys can only be written on objects!"
            );
        }

        newLine();
        replaceTop(DANGLING_KEY);
    }

    /**
     * Inserts any necessary separators and whitespace
     * before a literal value, inline array, or inline
     * object. Also adjusts the stack to expect either
     * a closing bracket or another element
     */
    private void beforeValue() {
        try {
            switch (peek()) {
                case DOCUMENT: {
                    break;
                }
                case EMPTY_ARRAY: {
                    // first element in array
                    replaceTop(NONEMPTY_ARRAY);
                    newLine();
                    break;
                }
                case NONEMPTY_ARRAY: {
                    // another element in array
                    writer.write(',');
                    newLine();
                    break;
                }
                case DANGLING_KEY: {
                    // value for name
                    writer.write(separator);
                    writer.flush();
                    replaceTop(NONEMPTY_OBJECT);
                    break;
                }
                default:
                    throw new IllegalStateException("Nesting problem.");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    //#endregion

    //#region Pretty printing
    private void newLine() {
        try {
            if (indent != null) {
                writer.write('\n');
                for (int i = 1, size = stackSize; i < size; i++) {
                    writer.write(indent);
                }
            }
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    //#endregion

    //#region (unchecked) OutputStream methods
    @Override
    public void write(int b) {
        try {
            out.write(b);
        } catch (IOException e) {
            // just let it be unchecked
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(byte @NotNull [] b) {
        try {
            out.write(b);
        } catch (IOException e) {
            // just let it be unchecked
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) {
        try {
            out.write(b, off, len);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void write(Writable writable) {
        try {
            writable.write(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    //#endregion

}
