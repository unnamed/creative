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
package team.unnamed.creative.overlay;

import org.junit.jupiter.api.Test;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourcePackMergeTest {
    private static final Writable ICON_0 = Writable.stringUtf8("icon 0");
    private static final Writable ICON_1 = Writable.stringUtf8("icon 1");

    private static final Writable TE = Writable.stringUtf8("te");
    private static final Writable AB = Writable.stringUtf8("ab");

    @Test
    void test_override() {
        final ResourcePack base = ResourcePack.resourcePack();
        final ResourcePack added = ResourcePack.resourcePack();

        // icons
        base.icon(ICON_0);
        added.icon(ICON_1);

        // unknown files
        base.unknownFile("te", TE);
        base.unknownFile("ab", AB);
        added.unknownFile("added", AB);
        added.unknownFile("te", AB);

        // fonts
        base.font(Font.font()
                .key(Font.MINECRAFT_DEFAULT)
                .addProvider(FontProvider.space()
                        .advance("a", 5)
                        .advance(" ", 4)
                        .build())
                .build());
        added.font(Font.font()
                .key(Font.MINECRAFT_DEFAULT)
                .addProvider(FontProvider.space()
                        .advance("b", 6)
                        .advance("\t", 7)
                        .build())
                .build());

        // merge!
        base.merge(added, MergeMode.OVERRIDE);

        assertEquals(ICON_1, base.icon());
        assertEquals(AB, base.unknownFile("te"));
        assertEquals(AB, base.unknownFile("added"));
        assertEquals(AB, base.unknownFile("ab"));
        assertEquals(
                Font.font()
                        .key(Font.MINECRAFT_DEFAULT)
                        .addProvider(FontProvider.space()
                                .advance("b", 6)
                                .advance("\t", 7)
                                .build())
                        .build(),
                base.font(Font.MINECRAFT_DEFAULT)
        );
    }

    @Test
    void test_merge_keeping_first_on_error() {
        final ResourcePack base = ResourcePack.resourcePack();
        final ResourcePack added = ResourcePack.resourcePack();

        // icons
        base.icon(ICON_0);
        added.icon(ICON_1);

        // unknown files
        base.unknownFile("te", TE);
        base.unknownFile("ab", AB);
        added.unknownFile("added", AB);
        added.unknownFile("te", AB);

        // fonts
        base.font(Font.font()
                .key(Font.MINECRAFT_DEFAULT)
                .addProvider(FontProvider.space()
                        .advance("a", 5)
                        .advance(" ", 4)
                        .build())
                .build());
        added.font(Font.font()
                .key(Font.MINECRAFT_DEFAULT)
                .addProvider(FontProvider.space()
                        .advance("b", 6)
                        .advance("\t", 7)
                        .build())
                .build());

        // merge!
        base.merge(added, MergeMode.MERGE_AND_KEEP_FIRST_ON_ERROR);

        assertEquals(ICON_0, base.icon()); // keeps first
        assertEquals(TE, base.unknownFile("te")); // keeps first
        assertEquals(AB, base.unknownFile("added")); // added
        assertEquals(AB, base.unknownFile("ab")); // keeps first
        assertEquals(
                Font.font()
                        .key(Font.MINECRAFT_DEFAULT)
                        // todo: merging should be smarter than this
                        .addProvider(
                                FontProvider.space()
                                        .advance("a", 5)
                                        .advance(" ", 4)
                                        .build()
                        )
                        .addProvider(
                                FontProvider.space()
                                        .advance("b", 6)
                                        .advance("\t", 7)
                                        .build()
                        )
                        .build(),
                base.font(Font.MINECRAFT_DEFAULT)
        );
    }
}
