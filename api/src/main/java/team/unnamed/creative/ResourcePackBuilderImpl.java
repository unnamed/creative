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
package team.unnamed.creative;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.file.FileTree;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class ResourcePackBuilderImpl implements ResourcePackBuilder {

    private @Nullable PackMeta packMeta;
    private @Nullable FilterMeta filterMeta;
    private @Nullable Map<String, MetadataPart> customMetadataParts;

    private @Nullable Map<Key, Texture> textures;


    //#region Metadata methods
    // |-----------------------------------|
    // |------- METADATA OPERATIONS -------|
    // |-----------------------------------|
    @Override
    public ResourcePackBuilder meta(PackMeta meta) {
        this.packMeta = requireNonNull(meta, "meta");
        return this;
    }

    @Override
    public ResourcePackBuilder filter(FilterMeta filter) {
        this.filterMeta = requireNonNull(filter, "filter");
        return this;
    }

    @Override
    public ResourcePackBuilder customMetaPart(MetadataPart part) {
        if (customMetadataParts == null) {
            customMetadataParts = new HashMap<>();
        }
        customMetadataParts.put(part.name(), part);
        return this;
    }
    //#endregion

    @Override
    public ResourcePackBuilder blockState(BlockState state) {
        return null;
    }

    @Override
    public ResourcePackBuilder font(Font font) {
        return null;
    }

    @Override
    public ResourcePackBuilder language(Language language) {
        return null;
    }

    @Override
    public ResourcePackBuilder model(Model model) {
        return null;
    }

    @Override
    public ResourcePackBuilder sounds(SoundRegistry soundRegistry) {
        return null;
    }

    @Override
    public ResourcePackBuilder sound(Sound.File soundFile) {
        return null;
    }

    @Override
    public ResourcePackBuilder texture(Texture texture) {
        if (textures == null) {
            textures = new HashMap<>();
        }
        textures.put(texture.key(), texture);
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writeTo(FileTree tree) {
        if (packMeta == null) {
            throw new IllegalStateException("You must set the resource pack meta (ResourcePackBuilder#meta)");
        }

        // write our pack.mcmeta file
        {
            Metadata.Builder metaBuilder = Metadata.builder()
                    .add(packMeta);

            if (filterMeta != null) {
                metaBuilder.add(filterMeta);
            }

            if (customMetadataParts != null) {
                for (MetadataPart part : customMetadataParts.values()) {
                    metaBuilder.add((Class) part.getClass(), part);
                }
            }

            tree.write(metaBuilder.build());
        }

        // write textures
        if (textures != null) {
            for (Texture texture : textures.values()) {
                tree.write(texture);
            }
        }
    }

}
