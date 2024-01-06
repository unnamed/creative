/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.io.ResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.ResourceSerializer;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@ApiStatus.Internal
public class ResourceCategory<T extends Keyed> {

    private final String folder;
    private final String extension;
    private final BiConsumer<ResourceContainer, T> setter;
    private final Function<ResourceContainer, Collection<T>> lister;

    private final ResourceDeserializer<T> deserializer;
    private final ResourceSerializer<T> serializer;

    public ResourceCategory(
            String folder,
            String extension,
            BiConsumer<ResourceContainer, T> setter,
            Function<ResourceContainer, Collection<T>> lister,
            ResourceDeserializer<T> deserializer,
            ResourceSerializer<T> serializer
    ) {
        this.folder = requireNonNull(folder, "folder");
        this.extension = requireNonNull(extension, "extension");
        this.setter = requireNonNull(setter, "setter");
        this.lister = requireNonNull(lister, "lister");
        this.deserializer = requireNonNull(deserializer, "deserializer");
        this.serializer = requireNonNull(serializer, "serializer");
    }

    public <TCodec extends ResourceSerializer<T> & ResourceDeserializer<T>> ResourceCategory(
            String folder,
            String extension,
            BiConsumer<ResourceContainer, T> setter,
            Function<ResourceContainer, Collection<T>> lister,
            TCodec codec
    ) {
        this(
                folder,
                extension,
                setter,
                lister,
                codec,
                codec
        );
    }

    public String folder() {
        return folder;
    }

    public String extension() {
        return extension;
    }

    public BiConsumer<ResourceContainer, T> setter() {
        return setter;
    }

    public ResourceDeserializer<T> deserializer() {
        return deserializer;
    }

    public Function<ResourceContainer, Collection<T>> lister() {
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

}
