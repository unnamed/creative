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
import team.unnamed.creative.ResourcePackBuilder;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.minecraft.io.FileTreeWriter;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Map;

final class MinecraftResourcePackWriter {

    private MinecraftResourcePackWriter() {
    }

    public static void write(ResourcePackBuilder resourcePack, FileTreeWriter writer) {
        try {
            _write(resourcePack, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void _write(ResourcePackBuilder resourcePack, FileTreeWriter writer) throws IOException {

        // write metadata
        {
            Metadata.Builder metadata = Metadata.builder();
            PackMeta packMeta = resourcePack.meta();
            if (packMeta != null) {
                metadata.add(packMeta);
            }
            LanguageMeta languageMeta = resourcePack.languageRegistry();
            if (languageMeta != null) {
                metadata.add(languageMeta);
            }
            FilterMeta filterMeta = resourcePack.filter();
            if (filterMeta != null) {
                metadata.add(filterMeta);
            }

            try (JsonWriter jsonWriter = writer.openJsonWriter(MinecraftResourcePackStructure.PACK_METADATA_FILE)) {
                SerializerMetadata.INSTANCE.write(jsonWriter, metadata.build());
            }
            System.out.println(" [INFO] Written resource-pack metadata");
        }

        // write icon
        {
            Writable icon = resourcePack.icon();
            if (icon != null) {
                try (OutputStream output = writer.openStream(MinecraftResourcePackStructure.PACK_ICON_FILE)) {
                    icon.write(output);
                }
            }
            System.out.println(" [INFO] Written resource-pack icon");
        }

        // write languages
        {
            for (Language language : resourcePack.languages()) {
                try (JsonWriter jsonWriter = writer.openJsonWriter(MinecraftResourcePackStructure.pathOf(language))) {
                    SerializerLanguage.INSTANCE.write(jsonWriter, language);
                }
            }
            System.out.println(" [INFO] Written " + resourcePack.languages().size() + " languages");
        }

        // write block states
        {
            for (BlockState blockState : resourcePack.blockStates()) {
                try (JsonWriter jsonWriter = writer.openJsonWriter(MinecraftResourcePackStructure.pathOf(blockState))) {
                    SerializerBlockState.INSTANCE.write(jsonWriter, blockState);
                }
            }
            System.out.println(" [INFO] Written " + resourcePack.blockStates().size() + " block states");
        }

        // write fonts
        {
            for (Font font : resourcePack.fonts()) {
                try (JsonWriter jsonWriter = writer.openJsonWriter(MinecraftResourcePackStructure.pathOf(font))) {
                    SerializerFont.INSTANCE.write(jsonWriter, font);
                }
            }
            System.out.println(" [INFO] Written " + resourcePack.fonts().size() + " fonts");
        }

        // write models
        {
            for (Model model : resourcePack.models()) {
                try (JsonWriter jsonWriter = writer.openJsonWriter(MinecraftResourcePackStructure.pathOf(model))) {
                    SerializerModel.INSTANCE.write(jsonWriter, model);
                }
            }
            System.out.println(" [INFO] Written " + resourcePack.models().size() + " models");
        }

        // write textures
        {
            for (Texture texture : resourcePack.textures()) {
                try (OutputStream stream = writer.openStream(MinecraftResourcePackStructure.pathOf(texture))) {
                    texture.data().write(stream);
                }

                Metadata metadata = texture.meta();
                if (!metadata.parts().isEmpty()) {
                    try (JsonWriter jsonWriter = writer.openJsonWriter(MinecraftResourcePackStructure.pathOfMeta(texture))) {
                        SerializerMetadata.INSTANCE.write(jsonWriter, metadata);
                    }
                }
            }
            System.out.println(" [INFO] Written " + resourcePack.textures().size() + " textures");
        }

        // write extra files
        {
            for (Map.Entry<String, Writable> entry : resourcePack.extraFiles().entrySet()) {
                // System.out.println(" [INFO] Extra file --> " + entry.getKey());
                try (OutputStream stream = writer.openStream(entry.getKey())) {
                    entry.getValue().write(stream);
                }
            }
            System.out.println(" [INFO] Written " + resourcePack.extraFiles().size() + " extra files (unknown)");
        }
    }

}
