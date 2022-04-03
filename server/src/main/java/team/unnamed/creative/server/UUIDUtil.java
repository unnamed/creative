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
package team.unnamed.creative.server;

import java.util.UUID;

final class UUIDUtil {

    private UUIDUtil() {
    }

    public static UUID fromUndashedString(String string) {
        if (string.length() != 32) {
            throw new IllegalArgumentException("Undashed string must be 32 characters long");
        }
        long msb = parseUnsignedLong(string.substring(0, 16), 16);
        long lsb = parseUnsignedLong(string.substring(16), 16);
        return new UUID(msb, lsb);
    }

    private static long parseUnsignedLong(String str, int radix) {

        // from Long.parseUnsignedLong in Java 9+
        int len = str.length();

        if (str.length() < 1) {
            throw new IllegalArgumentException("String with invalid length");
        }

        if (str.charAt(0) == '-') {
            throw new IllegalArgumentException("Illegal leading minus sign on unsigned string " + str);
        }

        if (len <= 12 || (radix == 10 && len <= 18)) {
            return Long.parseLong(str, radix);
        }

        long first = Long.parseLong(str.substring(0, len - 1), radix);
        int second = Character.digit(str.charAt(len - 1), radix);
        if (second < 0) {
            throw new IllegalArgumentException("Bad digit at end of " + str);
        }
        long result = first * radix + second;

        int guard = radix * (int) (first >>> 57);
        if (guard >= 128 || (result >= 0 && guard >= 128 - Character.MAX_RADIX)) {
            throw new IllegalArgumentException("String value " + str
                    + " exceeds range of unsigned long");
        }
        return result;
    }

}
