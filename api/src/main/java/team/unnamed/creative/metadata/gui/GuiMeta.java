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

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;

/**
 * Represents meta-data applicable to gui-elements
 *
 * @since 1.2.0
 * @sinceMinecraft 1.20.2
 * @sincePackFormat 18
 */
public interface GuiMeta extends MetadataPart {

    GuiScaling scaling();

    /**
     * Creates a new {@link GuiMeta} instance
     * from the given values
     *
     * @param scaling Scaling instance
     * @return A new instance of {@link GuiMeta}
     * @since 1.0.0
     */
    static @NotNull GuiMeta of(GuiScaling scaling) {
        return new GuiMetaImpl(scaling);
    }

    static @NotNull GuiMeta.Builder builder() {
        return new GuiMeta.Builder();
    }

    class Builder {
        private GuiScaling scaling;

        private Builder() {
        }

        public Builder scaling(GuiScaling scaling) {
            this.scaling = scaling;
            return this;
        }

        public GuiMeta build() {
            return new GuiMetaImpl(scaling);
        }
    }
}
