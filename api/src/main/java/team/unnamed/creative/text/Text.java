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
package team.unnamed.creative.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

/**
 * Represents a text.
 *
 * @sinceMinecraft 1.6.1
 * @sincePackFormat 1
 * @since 1.3.0
 */
public interface Text extends Keyed, ResourcePackPart, Examinable {
    /**
     * The key for the <b>legacy</b> credits file, which was just
     * a text file, formatted with legacy section characters (§).
     *
     * <p>The legacy credits file exists from the first resource-pack
     * implementation (1.6.1, pack format: 1) to Minecraft 1.17 (pack
     * format: 7), where it was replaced by the new credits file,
     * represented by the {@link Credits} interface.</p>
     *
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     * @deprecated Since Minecraft 1.17, use {@link Credits} instead.
     */
    @Deprecated
    Key LEGACY_CREDITS = Key.key("credits.txt");

    /**
     * The key for the splashes file, which is just a list of splash
     * messages, shown in the main menu, separated by new lines.
     *
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    Key SPLASHES = Key.key("splashes.txt");

    /**
     * The key for the end poem file. Supports formatting with legacy
     * section characters (§) and replacing the "PLAYERNAME" literal string
     * by the player's name.
     *
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    Key END = Key.key("end.txt");

    /**
     * The key for the post credits file. Supports formatting with legacy
     * section characters (§).
     *
     * @sincePackFormat 8
     * @sinceMinecraft 1.18
     * @since 1.3.0
     */
    Key POSTCREDITS = Key.key("postcredits.txt");

    /**
     * Creates a new text with the given {@code key} and {@code content}.
     *
     * @param key     The text key
     * @param content The text content
     * @return The created text instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract("_, _ -> new")
    static @NotNull Text text(final @NotNull Key key, final @NotNull Writable content) {
        return new TextImpl(key, content);
    }

    /**
     * Creates a new text with the given {@code key} and {@code content}.
     *
     * @param key     The text key
     * @param content The text content
     * @return The created text instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract("_, _ -> new")
    static @NotNull Text text(final @NotNull Key key, final @NotNull String content) {
        return text(key, Writable.stringUtf8(content));
    }

    /**
     * Returns the text key.
     *
     * @return The text key
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Override
    @NotNull Key key();

    /**
     * Creates a new text with the given {@code key},
     * but with the same content as this text.
     *
     * @param key The new text key
     * @return The created text instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull Text key(final @NotNull Key key);

    /**
     * Returns the text content as a {@link Writable}
     * instance.
     *
     * @return The text content
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @NotNull Writable content();

    /**
     * Creates a new text with the given {@code content},
     * but with the same key as this text.
     *
     * @param content The new text content
     * @return The created text instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull Text content(final @NotNull Writable content);

    /**
     * Adds this text to the given {@code resourceContainer}.
     *
     * @param resourceContainer The resource container
     * @since 1.3.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.text(this);
    }
}
