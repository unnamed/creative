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
package team.unnamed.creative.serialize.minecraft;

import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.serialize.FileTree;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class MinecraftResourcePackWriter implements ResourcePackWriter {

    private final MinecraftFileTree tree;

    private MinecraftResourcePackWriter(FileTree tree) {
        this.tree = new MinecraftFileTree(tree);
    }

    @Override
    public ResourcePackWriter meta(Metadata meta) {
        //writeMetadata("pack" + METADATA_EXTENSION, meta);
        return this;
    }

    @Override
    public ResourcePackWriter texture(Texture texture) {
        /*String path = at(texture.key().namespace(), TEXTURES_FOLDER, texture.key().value() + TEXTURE_EXTENSION);
        tree.write(path, texture.data());

        Metadata meta = texture.meta();
        if (!meta.parts().isEmpty()) {
            writeMetadata(path + METADATA_EXTENSION, meta);
        }*/
        return this;
    }

    @Override
    public ResourcePackWriter sounds(SoundRegistry registry) {
        try {
            SoundSerializer.INSTANCE.write(registry, tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackWriter sound(Key key, Writable data) {
        /*tree.write(
                at(key.namespace(), SOUNDS_FOLDER, key.value() + SOUND_EXTENSION),
                data
        );*/
        return this;
    }

    @Override
    public ResourcePackWriter blockState(BlockState blockState) {
        try {
            BlockStateSerializer.INSTANCE.write(blockState, tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackWriter font(Font font) {
        try {
            FontSerializer.INSTANCE.write(font, tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackWriter language(Language language) {
        try {
            LanguageSerializer.INSTANCE.write(language, tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ResourcePackWriter model(Model model) {
        try {
            ModelSerializer.INSTANCE.write(model, tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    private JsonWriter openJsonWriter(String path) throws IOException {
        return new JsonWriter(tree.openWriter(path));
    }

    private void writeMetadata(String path, Metadata metadata) {
        try {
            MetadataSerializer.INSTANCE.write(
                    openJsonWriter(path),
                    metadata
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
