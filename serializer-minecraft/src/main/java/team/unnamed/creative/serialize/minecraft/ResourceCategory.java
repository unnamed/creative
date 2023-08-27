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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.minecraft.atlas.AtlasSerializer;
import team.unnamed.creative.serialize.minecraft.blockstate.BlockStateSerializer;
import team.unnamed.creative.serialize.minecraft.font.FontSerializer;
import team.unnamed.creative.serialize.minecraft.io.ResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.ResourceSerializer;
import team.unnamed.creative.serialize.minecraft.language.LanguageSerializer;
import team.unnamed.creative.serialize.minecraft.model.ModelSerializer;
import team.unnamed.creative.serialize.minecraft.sound.SoundSerializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@ApiStatus.Internal
public class ResourceCategory<T extends Keyed> {

    private static final Map<String, ResourceCategory<?>> CATEGORIES = new HashMap<>();

    static {
        registerCategory(AtlasSerializer.CATEGORY);
        registerCategory(SoundSerializer.CATEGORY);
        registerCategory(ModelSerializer.CATEGORY);
        registerCategory(LanguageSerializer.CATEGORY);
        registerCategory(BlockStateSerializer.CATEGORY);
        registerCategory(FontSerializer.CATEGORY);
    }

    private final String folder;
    private final String extension;
    private final BiConsumer<ResourcePack, T> setter;
    private final Function<ResourcePack, Collection<T>> lister;

    private final ResourceDeserializer<T> deserializer;
    private final ResourceSerializer<T> serializer;

    public ResourceCategory(
            String folder,
            String extension,
            BiConsumer<ResourcePack, T> setter,
            Function<ResourcePack, Collection<T>> lister,
            ResourceDeserializer<T> deserializer,
            ResourceSerializer<T> serializer
    ) {
        this.folder = folder;
        this.extension = extension;
        this.setter = setter;
        this.deserializer = deserializer;
        this.lister = lister;
        this.serializer = serializer;
    }

    public String folder() {
        return folder;
    }

    public String extension() {
        return extension;
    }

    public BiConsumer<ResourcePack, T> setter() {
        return setter;
    }

    public ResourceDeserializer<T> deserializer() {
        return deserializer;
    }

    public Function<ResourcePack, Collection<T>> lister() {
        return lister;
    }

    public ResourceSerializer<T> serializer() {
        return serializer;
    }

    public String pathOf(T resource) {
        Key key = resource.key();
        // assets/<namespace>/<category>/<path><extension>
        return "assets/" + key.namespace() + "/" + this.folder + "/" + key.value() + extension;
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
