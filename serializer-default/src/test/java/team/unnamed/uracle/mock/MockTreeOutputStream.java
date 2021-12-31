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
package team.unnamed.uracle.mock;

import team.unnamed.uracle.AssetWriter;
import team.unnamed.uracle.TreeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class MockTreeOutputStream extends TreeOutputStream {

    private static final byte NEW_LINE = '\n';

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Override
    public AssetWriter useEntry(String name) {
        try {
            output.write(name.getBytes(StandardCharsets.UTF_8));
            output.write(":\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return super.assetWriter;
    }

    @Override
    protected void closeEntry() {
        output.write(NEW_LINE);
    }

    @Override
    public void finish() {
        // no-op
    }

    @Override
    public void write(int i) {
        output.write(i);
    }

    public String output() {
        return output.toString();
    }

    @Override
    public boolean has(String path) {
        return false;
    }
}
