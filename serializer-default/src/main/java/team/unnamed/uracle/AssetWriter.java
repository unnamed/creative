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
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Helper {@link TreeOutputStream} implementation
 * for writing assets, its main features are the
 * JSON helpers
 */
class AssetWriter extends FilterOutputStream {

    private final TreeOutputStream tree;

    public AssetWriter(TreeOutputStream tree) {
        super(tree);
        this.tree = tree;
    }

    public AssetWriter startObject() {
        write('{');
        return this;
    }

    public AssetWriter endObject() {
        write('}');
        return this;
    }

    public AssetWriter startArray() {
        write('[');
        return this;
    }

    public AssetWriter endArray() {
        write(']');
        return this;
    }

    public AssetWriter key(String key) {
        write('"');
        write(encode(escape(key)));
        write('"');
        write(':');
        return this;
    }

    public AssetWriter value(Object object) {
        if (object instanceof Key) {
            // transform key to string, keys
            // can be optimized by removing
            // "minecraft" namespace
            object = keyToString((Key) object);
        }

        if (object instanceof String) {
            // strings must be quoted and escaped
            write('"');
            write(encode(escape(object.toString())));
            write('"');
        } else {
            // other objects (numbers, booleans) are
            // written literally as its toString() value
            write(encode(String.valueOf(object)));
        }
        return this;
    }

    @Override
    public void write(int b) {
        try {
            tree.write(b);
        } catch (IOException e) {
            // just let it be unchecked
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(byte @NotNull [] b) {
        try {
            tree.write(b);
        } catch (IOException e) {
            // just let it be unchecked
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        // does not close underlying output stream,
        // but closes the file entry
        try {
            tree.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String keyToString(Key key) {
        // very small resource-pack optimization, omits
        // the "minecraft" namespace if key is using it
        if (key.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            return key.value();
        } else {
            return key.asString();
        }
    }

    private byte[] encode(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String str) {
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"') {
                builder.append('\\').append(c);
            } else if (c == '\n') {
                builder.append("\\n");
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

}
