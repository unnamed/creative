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

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.serialize.minecraft.io.ResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.ResourceSerializer;

import java.util.Collection;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@ApiStatus.Internal
public final class ResourceCategoryImpl<T extends Keyed & ResourcePackPart> implements ResourceCategory<T> {

    private final String folder;
    private final String extension;
    private final Function<ResourceContainer, Collection<T>> lister;

    private final ResourceDeserializer<T> deserializer;
    private final ResourceSerializer<T> serializer;

    public ResourceCategoryImpl(
            String folder,
            String extension,
            Function<ResourceContainer, Collection<T>> lister,
            ResourceDeserializer<T> deserializer,
            ResourceSerializer<T> serializer
    ) {
        this.folder = requireNonNull(folder, "folder");
        this.extension = requireNonNull(extension, "extension");
        this.lister = requireNonNull(lister, "lister");
        this.deserializer = requireNonNull(deserializer, "deserializer");
        this.serializer = requireNonNull(serializer, "serializer");
    }

    public <TCodec extends ResourceSerializer<T> & ResourceDeserializer<T>> ResourceCategoryImpl(
            String folder,
            String extension,
            Function<ResourceContainer, Collection<T>> lister,
            TCodec codec
    ) {
        this(
                folder,
                extension,
                lister,
                codec,
                codec
        );
    }

    @Override
    public @NotNull String folder(final int packFormat) {
        return folder;
    }

    @Override
    public @NotNull String extension(final int packFormat) {
        return extension;
    }

    @Override
    public @NotNull ResourceDeserializer<T> deserializer() {
        return deserializer;
    }

    @Override
    public @NotNull Function<ResourceContainer, Collection<T>> lister() {
        return lister;
    }

    @Override
    public @NotNull ResourceSerializer<T> serializer() {
        return serializer;
    }
}
