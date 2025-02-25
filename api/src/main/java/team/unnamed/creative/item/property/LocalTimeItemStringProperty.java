/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
package team.unnamed.creative.item.property;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the <strong>local time</strong> item string property, which evaluates to the
 * current time, formatted according to the given locale, timezone and pattern. The value
 * is updated every second.
 *
 * <p>Possible values are any string.</p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface LocalTimeItemStringProperty extends ItemStringProperty {
    @ApiStatus.Internal
    String DEFAULT_LOCALE = ""; // means "root" locale

    /**
     * Returns the value describing the locale, for example:
     * <ul>
     *     <li>{@code en_US} - English language (for like week names),
     *     formatting as in USA</li>
     *     <li>{@code cs_AU@numbers=thai;calendar=japanese} - Czech language,
     *     Australian formatting, Thai numerals and Japanese calendar</li>
     * </ul>
     *
     * <p>Defaults to ""</p>
     *
     * @return the locale value
     */
    @NotNull String locale();

    /**
     * Returns the optional value describing the time zone,
     * if not specified, defaults to the timezone set on the
     * client computer.
     *
     * <p>Examples: {@code Europe/Stockholm}, {@code GMT+0:45}</p>
     *
     * @return the timezone value
     */
    @Nullable String timezone();

    /**
     * Returns the string describing the format to be used for the time formatting.
     * Examples: {@code yyyy-MM-dd}, {@code HH:mm:ss}.
     *
     * @return the pattern value
     */
    @NotNull String pattern();
}
