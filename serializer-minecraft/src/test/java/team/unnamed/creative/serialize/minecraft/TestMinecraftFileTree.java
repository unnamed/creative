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
package team.unnamed.creative.serialize.minecraft;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Assertions;
import team.unnamed.creative.serialize.FileTree;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class TestMinecraftFileTree extends MinecraftFileTree {

    private final Map<String, byte[]> data;

    private TestMinecraftFileTree(FileTree delegate, Map<String, byte[]> data) {
        super(delegate);
        this.data = data;
    }

    public ExpectableFile expect(String path) {
        return new ExpectableFile(path);
    }


    public static TestMinecraftFileTree create() {
        Map<String, byte[]> data = new HashMap<>();
        return new TestMinecraftFileTree(new FileTree() {

            private String currentPath;
            private ByteArrayOutputStream current;

            @Override
            public boolean exists(String path) {
                return data.containsKey(path) || path.equals(currentPath);
            }

            @Override
            public OutputStream openStream(String path) {
                _doClose();
                currentPath = path;
                return current = new ByteArrayOutputStream() {
                    @Override
                    public void close() {
                        _doClose();
                    }
                };
            }

            @Override
            public void close() {
                _doClose();
            }

            private void _doClose() {
                if (currentPath != null && current != null) {
                    data.put(currentPath, current.toByteArray());
                }
                // note: no need to call current.close()
                currentPath = null;
                current = null;
            }

        }, data);
    }

    public class ExpectableFile {

        private final String path;

        public ExpectableFile(String path) {
            this.path = requireNonNull(path, "path");
        }

        public void toBeString(String value) {
            byte[] bytes = data.get(path);
            Assertions.assertNotNull(bytes, "No data for file '" + path + "', expected: '" + value + "'");
            Assertions.assertEquals(
                    value,
                    new String(bytes)
            );
        }

        public void toBeJson(@Language("JSON") String value) {
            toBeString(value);
        }

    }

}
