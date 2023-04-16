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
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.PACK_ICON_FILE;
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.PACK_METADATA_FILE;

final class MinecraftResourcePackWriterImpl implements MinecraftResourcePackWriter {

    static final MinecraftResourcePackWriterImpl INSTANCE = new MinecraftResourcePackWriterImpl();

    private MinecraftResourcePackWriterImpl() {
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
            writeToJson(target, SerializerMetadata.INSTANCE, metadata, PACK_METADATA_FILE);
        }

        // write block states
        for (BlockState blockState : resourcePack.blockStates()) {
            writeToJson(target, SerializerBlockState.INSTANCE, blockState, MinecraftResourcePackStructure.pathOf(blockState));
        }

        // write fonts
        for (Font font : resourcePack.fonts()) {
            writeToJson(target, SerializerFont.INSTANCE, font, MinecraftResourcePackStructure.pathOf(font));
        }

        // write languages
        for (Language language : resourcePack.languages()) {
            writeToJson(target, SerializerLanguage.INSTANCE, language, MinecraftResourcePackStructure.pathOf(language));
        }

        // write models
        for (Model model : resourcePack.models()) {
            writeToJson(target, SerializerModel.INSTANCE, model, MinecraftResourcePackStructure.pathOf(model));
        }

        // write sound registries
        for (SoundRegistry soundRegistry : resourcePack.soundRegistries()) {
            writeToJson(target, SerializerSoundRegistry.INSTANCE, soundRegistry, MinecraftResourcePackStructure.pathOf(soundRegistry));
        }

        // write sounds
        for (Sound.File sound : resourcePack.sounds()) {
            target.write(MinecraftResourcePackStructure.pathOf(sound), sound.data());
        }

        // write textures
        for (Texture texture : resourcePack.textures()) {
            target.write(
                    MinecraftResourcePackStructure.pathOf(texture),
                    texture.data()
            );

            Metadata metadata = texture.meta();
            if (!metadata.parts().isEmpty()) {
                writeToJson(target, SerializerMetadata.INSTANCE, metadata, MinecraftResourcePackStructure.pathOfMeta(texture));
            }
        }

        // write unknown files
        for (Map.Entry<String, Writable> entry : resourcePack.unknownFiles().entrySet()) {
            target.write(entry.getKey(), entry.getValue());
        }
    }

    private <T> void writeToJson(FileTreeWriter writer, JsonFileStreamWriter<T> serializer, T object, String path) {
        try (JsonWriter jsonWriter = writer.openJsonWriter(path)) {
            serializer.serialize(object, jsonWriter);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to " + path, e);
        }
    }

}
