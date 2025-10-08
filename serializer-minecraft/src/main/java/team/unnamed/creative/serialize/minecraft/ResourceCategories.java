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
package team.unnamed.creative.serialize.minecraft;

import team.unnamed.creative.serialize.minecraft.atlas.AtlasSerializer;
import team.unnamed.creative.serialize.minecraft.blockstate.BlockStateSerializer;
import team.unnamed.creative.serialize.minecraft.equipment.EquipmentCategory;
import team.unnamed.creative.serialize.minecraft.font.FontSerializer;
import team.unnamed.creative.serialize.minecraft.item.ItemSerializer;
import team.unnamed.creative.serialize.minecraft.language.LanguageSerializer;
import team.unnamed.creative.serialize.minecraft.model.ModelSerializer;
import team.unnamed.creative.serialize.minecraft.sound.SoundSerializer;
import team.unnamed.creative.serialize.minecraft.waypoint.WaypointStyleSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceCategories {

    private static final List<ResourceCategory<?>> CATEGORIES;

    static {
        CATEGORIES = new ArrayList<>();
        registerCategory(AtlasSerializer.CATEGORY);
        registerCategory(SoundSerializer.CATEGORY);
        registerCategory(ModelSerializer.CATEGORY);
        registerCategory(LanguageSerializer.CATEGORY);
        registerCategory(BlockStateSerializer.CATEGORY);
        registerCategory(FontSerializer.CATEGORY);
        registerCategory(EquipmentCategory.INSTANCE);
        registerCategory(ItemSerializer.CATEGORY);
        registerCategory(WaypointStyleSerializer.CATEGORY);
    }

    private ResourceCategories() {
    }

    private static void registerCategory(ResourceCategory<?> category) {
        CATEGORIES.add(category);
    }

    public static Collection<ResourceCategory<?>> categories() {
        return CATEGORIES;
    }

    public static Map<String, ResourceCategory<?>> buildCategoryMapByFolder(final int packFormat) {
        Map<String, ResourceCategory<?>> map = new HashMap<>(); // note: no need to be linked list
        for (ResourceCategory<?> category : ResourceCategories.categories()) {
            map.put(category.folder(packFormat), category);
        }
        return map;
    }
}
