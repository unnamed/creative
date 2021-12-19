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
package team.unnamed.uracle.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A legacy unicode font. This format is deprecated and only
 * prioritized when the "Force Unicode Font" option is turned on.
 *
 * @since 1.0.0
 */
public class LegacyUnicodeFont implements Font {

    /**
     * The resource location inside assets/&lt;namespace&gt;/font
     * describing a binary file describing the horizontal start and
     * end positions for each character from 0 to 15. The file extension
     * of the target file should be .bin
     */
    private final Key sizes;

    /**
     * The resource location inside assets/&lt;namespace&gt;/textures
     * that leads to the texture files that should be used for this
     * provider. The game replaces %s from the value of this tag with
     * the first two characters of the hex code of the replaced characters,
     * so a single provider of this type can point into multiple texture files.
     */
    private final Key template;

    protected LegacyUnicodeFont(Key sizes, Key template) {
        this.sizes = requireNonNull(sizes, "sizes");
        this.template = requireNonNull(template, "template");
    }

    @Override
    public Type type() {
        return Type.LEGACY_UNICODE;
    }

    /**
     * Returns the location of the file that describes
     * the horizontal start and end positions for each
     * character
     *
     * @return The sizes file location
     */
    public Key sizes() {
        return sizes;
    }

    /**
     * Returns the location of the file that describes
     * the character textures
     *
     * @return The template file location
     */
    public Key template() {
        return template;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("sizes", sizes),
                ExaminableProperty.of("template", template)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegacyUnicodeFont that = (LegacyUnicodeFont) o;
        return sizes.equals(that.sizes) && template.equals(that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizes, template);
    }

}
