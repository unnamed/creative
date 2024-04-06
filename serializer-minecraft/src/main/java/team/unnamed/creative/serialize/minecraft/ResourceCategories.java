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
package team.unnamed.creative.serialize.minecraft;

import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.serialize.minecraft.atlas.AtlasSerializer;
import team.unnamed.creative.serialize.minecraft.blockstate.BlockStateSerializer;
import team.unnamed.creative.serialize.minecraft.font.FontSerializer;
import team.unnamed.creative.serialize.minecraft.language.LanguageSerializer;
import team.unnamed.creative.serialize.minecraft.model.ModelSerializer;
import team.unnamed.creative.serialize.minecraft.sound.SoundSerializer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceCategories {

    private static final Map<String, ResourceCategory<?>> CATEGORIES;

    static {
        CATEGORIES = new LinkedHashMap<>();
        registerCategory(AtlasSerializer.CATEGORY);
        registerCategory(SoundSerializer.CATEGORY);
        registerCategory(ModelSerializer.CATEGORY);
        registerCategory(LanguageSerializer.CATEGORY);
        registerCategory(BlockStateSerializer.CATEGORY);
        registerCategory(FontSerializer.CATEGORY);
    }

    private ResourceCategories() {
    }

    private static void registerCategory(ResourceCategory<?> category) {
        CATEGORIES.put(category.folder(), category);
    }

    public static Collection<ResourceCategory<?>> categories() {
        return CATEGORIES.values();
    }

    public static @Nullable ResourceCategory<?> getByFolder(String folder) {
        return CATEGORIES.get(folder);
    }

}
