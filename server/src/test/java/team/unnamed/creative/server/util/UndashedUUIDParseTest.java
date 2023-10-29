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
package team.unnamed.creative.server.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UndashedUUIDParseTest {
    @Test
    @DisplayName("Test the correct functionality of undashed uuid parsing")
    void test_success() {
        assertEquals(
                UUID.fromString("845ea5cc-6823-40b7-b022-ab57ed54c7af"),
                UndashedUUID.fromUndashedString("845ea5cc682340b7b022ab57ed54c7af")
        );

        assertEquals(
                UUID.fromString("54be616e-deda-428c-92a5-154fce4ca8f9"),
                UndashedUUID.fromUndashedString("54be616ededa428c92a5154fce4ca8f9")
        );

        for (int i = 0; i < 50; i++) {
            testRandom();
        }
    }

    @Test
    @DisplayName("Test that invalid undashed uuid parsing fails correctly")
    void test_fail() {
        // invalid string length, it is not 32
        assertThrows(IllegalArgumentException.class, () ->
                UndashedUUID.fromUndashedString("0123456789"));

        // invalid/illegal characters
        assertThrows(IllegalArgumentException.class, () ->
                UndashedUUID.fromUndashedString("012345678\nabcdef/123456789abcde\t"));
        assertThrows(IllegalArgumentException.class, () ->
                UndashedUUID.fromUndashedString("-123456789abcdef0123456789abcdef"));
    }

    private static void testRandom() {
        final UUID uuid = UUID.randomUUID();
        final String undashedUuid = uuid.toString().replace("-", "");
        UUID result = null;

        try {
            result = UndashedUUID.fromUndashedString(undashedUuid);
        } catch (final Exception e) {
            Assertions.fail("Failed to parse UUID '"
                    + uuid + "' (undashed: '" + undashedUuid + "')", e);
        }

        assertNotNull(result, "UUIDUtil#fromUndashedString should not return null");
        assertEquals(uuid, result);
    }
}
