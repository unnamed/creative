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
package team.unnamed.creative.model;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.file.FileResource;
import team.unnamed.creative.model.block.BlockTexture;
import team.unnamed.creative.model.item.ItemOverride;

import java.util.List;
import java.util.Map;

/**
 * Base interface for representing block and
 * item models
 *
 * @since 1.0.0
 */
public interface Model extends Keyed, FileResource {

    /**
     * Returns the parent model of this
     * model object
     *
     * @return The parent model location
     */
    @Nullable Key parent();

    /**
     * Returns a map of the different places
     * where the model can be displayed
     *
     * @return An unmodifiable map of displays
     */
    @Unmodifiable Map<ModelDisplay.Type, ModelDisplay> display();

    /**
     * Returns an unmodifiable list containing all
     * the model elements, which can only have cubic
     * forms
     *
     * @return The model elements
     */
    @Unmodifiable List<Element> elements();

    static BlockModel block(
            Key key,
            @Nullable Key parent,
            boolean ambientOcclusion,
            Map<ModelDisplay.Type, ModelDisplay> display,
            BlockTexture textures,
            BlockModel.GuiLight guiLight,
            List<Element> elements,
            List<ItemOverride> overrides
    ) {
        return new BlockModel(
                key, parent, ambientOcclusion, display,
                textures, guiLight, elements, overrides
        );
    }

    static BlockModel.Builder block() {
        return new BlockModel.Builder();
    }

}
