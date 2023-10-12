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

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public final class Validate {

    private Validate() {
    }

    /**
     * Validates that the provided object isn't null,
     * if it is, the method throws an {@link NullPointerException}
     * with the specified message.
     *
     * @param object     The checked object
     * @param message    The exception message
     * @param parameters Parameters for the message,
     *                   message is formatted using
     *                   {@link String#format}.
     * @param <T>        The type of the checked object
     * @return The object, never null
     * @throws NullPointerException if object is null
     */
    public static <T> T isNotNull(T object, String message, Object... parameters) {
        if (object == null) {
            throw new NullPointerException(String.format(message, parameters));
        } else {
            return object;
        }
    }

    /**
     * Validates that the provided object isn't null,
     * if it is, the method throws an {@link NullPointerException}
     *
     * @param object The checked object
     * @param <T>    The type of the checked object
     * @return The object, never null
     * @throws NullPointerException if object is null
     */
    public static <T> T isNotNull(T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    /**
     * Validates that the specified expression is true,
     * if it is false, the method throws a {@link IllegalStateException}
     * with the specified message.
     *
     * @param expression The checked expression
     * @param message    The message for the thrown exception
     * @param parameters The parameters for the exception
     *                   message. The message is formatted
     *                   using {@link String#format}.
     * @throws IllegalStateException If expression is false
     */
    public static void isState(boolean expression, String message, Object... parameters) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, parameters));
        }
    }

    /**
     * Validates that the specified expression is true,
     * if it is false, the method throws a {@link IllegalStateException}
     *
     * @param expression The checked expression
     * @throws IllegalStateException If expression is false
     */
    public static void isState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * Validates that the specified expression is true,
     * if it is false, the method throws a {@link IllegalArgumentException}
     * with the specified message
     *
     * @param expression The checked expression
     * @param message    The message for the thrown exception
     * @param parameters The parameters for the exception
     *                   message. The message is formatted
     *                   using {@link String#format}.
     * @throws IllegalArgumentException If expression is false
     */
    public static void isTrue(boolean expression, String message, Object... parameters) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, parameters));
        }
    }

    /**
     * Validates that the specified expression is true,
     * if it is false, the method throws a {@link IllegalArgumentException}
     *
     * @param expression The checked expression
     * @throws IllegalArgumentException If expression is false
     */
    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Validates that the specified string is
     * not null and not empty. If the string is
     * null, throws a {@link NullPointerException},
     * if the string is empty, throws a
     * {@link IllegalArgumentException}.
     *
     * @param string     The checked string
     * @param message    The message used for the messages
     * @param parameters The parameters for the exception
     *                   message. The message is formatted
     *                   using {@link String#format}.
     * @return The string, not null and not empty
     * @throws NullPointerException     if the string is null
     * @throws IllegalArgumentException If the string is empty
     */
    public static String isNotEmpty(String string, String message, Object... parameters) {
        if (string == null) {
            throw new NullPointerException(String.format(message, parameters));
        } else if (string.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, parameters));
        }
        return string;
    }

    /**
     * Validates that the specified string is
     * not null and not empty. If the string is
     * null, throws a {@link NullPointerException},
     * if the string is empty, throws a
     * {@link IllegalArgumentException}.
     *
     * @param string The checked string
     * @return The string, not null and not empty
     * @throws NullPointerException     if the string is null
     * @throws IllegalArgumentException If the string is empty
     */
    public static String isNotEmpty(String string) {
        if (string == null) {
            throw new NullPointerException("Null string");
        } else if (string.isEmpty()) {
            throw new IllegalArgumentException("Empty string");
        } else {
            return string;
        }
    }

}
