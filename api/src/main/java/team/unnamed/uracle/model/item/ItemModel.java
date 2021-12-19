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
package team.unnamed.uracle.model.item;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.model.ModelDisplay;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ItemModel {

    public static final Key ITEM_GENERATED = Key.key("item/generated");
    public static final Key BUILT_IN_ENTITY = Key.key("builtin/entity");

    private Key location;

    /**
     * Loads a different model from the given path, in form
     * of a resource location. If both "parent" and "elements"
     * are set, the "elements" tag overrides the "elements" tag
     * from the previous model.
     *
     * <p>Can be set to "item/generated" to use a model that is
     * created out of the specified icon</p>
     *
     * <p>Can be set to "builtin/entity" to load a model from
     * an entity file. As you cannot specify the entity, this
     * doesn't work for all items (only for chests, ender chests,
     * mob heads, shields, banners and tridents</p>
     */
    private Key parent;

    /**
     * Holds the different places where item models are displayed
     */
    private Map<ModelDisplay.Type, ModelDisplay> display;

    private ItemTexture textures;

    /**
     * Can be {@link GuiLight#FRONT} or {@link GuiLight#SIDE}. If set
     * to "side", the model is rendered like a block. If set to "front",
     * model is shaded like a flat item. Defaults to "side"
     */
    @Nullable private GuiLight guiLight;

    /**
     * Determines cases in which a different model should be used based on
     * item tags. All cases are evaluated in order from top to bottom and
     * last predicate that matches overrides. However, overrides are ignored
     * if it has been already overridden once, for example this avoids recursion
     * on overriding to the same model.
     */
    private List<ItemOverride> overrides;


    public enum GuiLight {
        FRONT,
        SIDE
    }

}
