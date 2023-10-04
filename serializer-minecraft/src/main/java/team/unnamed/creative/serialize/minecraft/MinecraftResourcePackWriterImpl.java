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

import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Keyed;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;
import team.unnamed.creative.serialize.minecraft.metadata.MetadataSerializer;
import team.unnamed.creative.serialize.minecraft.sound.SoundRegistrySerializer;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Map;

import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.PACK_ICON_FILE;
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.PACK_METADATA_FILE;

final class MinecraftResourcePackWriterImpl implements MinecraftResourcePackWriter {

    static final MinecraftResourcePackWriterImpl INSTANCE = new MinecraftResourcePackWriterImpl();

    private MinecraftResourcePackWriterImpl() {
    }

    public <T extends Keyed> void writeFullCategory(ResourcePack resourcePack, FileTreeWriter target, ResourceCategory<T> category) {
        for (T resource : category.lister().apply(resourcePack)) {
            String path = category.pathOf(resource);
            try (OutputStream output = target.openStream(path)) {
                category.serializer().serialize(resource, output);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public void write(FileTreeWriter target, ResourcePack resourcePack) {
        // write icon
        {
            Writable icon = resourcePack.icon();
            if (icon != null) {
                target.write(PACK_ICON_FILE, icon);
            }
        }

        // write metadata
        {
            Metadata metadata = resourcePack.metadata();
            // TODO: Should we check for pack meta?
            writeToJson(target, MetadataSerializer.INSTANCE, metadata, PACK_METADATA_FILE);
        }

        // write atlases
        for (ResourceCategory<?> category : ResourceCategories.categories()) {
            writeFullCategory(resourcePack, target, category);
        }

        // write sound registries
        for (SoundRegistry soundRegistry : resourcePack.soundRegistries()) {
            writeToJson(target, SoundRegistrySerializer.INSTANCE, soundRegistry, MinecraftResourcePackStructure.pathOf(soundRegistry));
        }

        // write textures
        for (Texture texture : resourcePack.textures()) {
            target.write(
                    MinecraftResourcePackStructure.pathOf(texture),
                    texture.data()
            );

            Metadata metadata = texture.meta();
            if (!metadata.parts().isEmpty()) {
                writeToJson(target, MetadataSerializer.INSTANCE, metadata, MinecraftResourcePackStructure.pathOfMeta(texture));
            }
        }

        // write unknown files
        for (Map.Entry<String, Writable> entry : resourcePack.unknownFiles().entrySet()) {
            target.write(entry.getKey(), entry.getValue());
        }
    }

    private <T> void writeToJson(FileTreeWriter writer, JsonResourceSerializer<T> serializer, T object, String path) {
        try (JsonWriter jsonWriter = new JsonWriter(writer.openWriter(path))) {
            serializer.serializeToJson(object, jsonWriter);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to " + path, e);
        }
    }

}
