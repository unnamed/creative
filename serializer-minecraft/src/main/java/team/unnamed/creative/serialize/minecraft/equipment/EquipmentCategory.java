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
package team.unnamed.creative.serialize.minecraft.equipment;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure;
import team.unnamed.creative.serialize.minecraft.io.ResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.ResourceSerializer;

import java.util.Collection;
import java.util.function.Function;

@ApiStatus.Internal
public final class EquipmentCategory implements ResourceCategory<Equipment> {
    public static final ResourceCategory<Equipment> INSTANCE = new EquipmentCategory();

    private EquipmentCategory() {
    }

    @Override
    public @NotNull String folder(int packFormat) {
        // In 1.21.4 (pack format 43), the equipment stuff was
        // moved from models/equipment to just equipment
        if (packFormat >= 43 || packFormat < 0) {
            // <0 means that the pack format is unknown, use latest
            return "equipment";
        } else {
            return "models/equipment";
        }
    }

    @Override
    public @NotNull String extension(int packFormat) {
        return MinecraftResourcePackStructure.OBJECT_EXTENSION;
    }

    @Override
    public @NotNull ResourceDeserializer<Equipment> deserializer() {
        return EquipmentSerializer.INSTANCE;
    }

    @Override
    public @NotNull Function<ResourceContainer, Collection<Equipment>> lister() {
        return ResourceContainer::equipment;
    }

    @Override
    public @NotNull ResourceSerializer<Equipment> serializer() {
        return EquipmentSerializer.INSTANCE;
    }
}
