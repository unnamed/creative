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
package team.unnamed.creative.util;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyCompactionTest {

    @Test
    @DisplayName("Test that keys with default namespace are compacted")
    public void test_default_namespace() {
        assertEquals("test", Keys.toString(Key.key("minecraft:test")));
        assertEquals("test_no_namespace", Keys.toString(Key.key("test_no_namespace")));
        assertEquals("test_separate", Keys.toString(Key.key("minecraft", "test_separate")));
        assertEquals("test_constant", Keys.toString(Key.key(Key.MINECRAFT_NAMESPACE, "test_constant")));
    }

    @Test
    @DisplayName("Test that keys withot default namespace are not compacted")
    public void test_custom_namespace() {
        assertEquals("creative:test", Keys.toString(Key.key("creative:test")));
        assertEquals("creative:test_separate", Keys.toString(Key.key("creative", "test_separate")));
    }

}
