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
package team.unnamed.creative.metadata.gui;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

final class GuiScalingImpl implements GuiScaling {

    public static final ScalingType DEFAULT_TYPE = ScalingType.STRETCH;

    private final ScalingType type;
    private final int width;
    private final int height;
    private final GuiBorder border;

    GuiScalingImpl(ScalingType type, int width, int height, GuiBorder border) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.border = border;
    }

    public ScalingType type() {
        return type;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public GuiBorder border() {
        return border;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("type", type),
                ExaminableProperty.of("width", width),
                ExaminableProperty.of("height", height),
                ExaminableProperty.of("border", border)
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
        GuiScalingImpl scaling = (GuiScalingImpl) o;
        return width == scaling.width &&
                height == scaling.height &&
                type == scaling.type &&
                border == scaling.border;
    }

    @Override
    public int hashCode() {
        return type.hashCode() + width + height + border.hashCode();
    }
}
