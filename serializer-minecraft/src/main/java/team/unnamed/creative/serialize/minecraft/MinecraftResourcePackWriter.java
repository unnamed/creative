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
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.serialize.minecraft.io.FileTreeWriter;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.UncheckedIOException;

final class MinecraftResourcePackWriter implements ResourcePackWriter<MinecraftResourcePackWriter> {

    private final FileTreeWriter target;

    MinecraftResourcePackWriter(FileTreeWriter target) {
        this.target = target;
    }

    @Override
    public MinecraftResourcePackWriter icon(Writable icon) {
        target.write(MinecraftResourcePackStructure.PACK_ICON_FILE, icon);
        return this;
    }

    @Override
    public MinecraftResourcePackWriter metadata(Metadata metadata) {
        writeToJson(SerializerMetadata.INSTANCE, metadata, MinecraftResourcePackStructure.PACK_METADATA_FILE);
        return this;
    }

    @Override
    public MinecraftResourcePackWriter blockState(BlockState state) {
        writeToJson(SerializerBlockState.INSTANCE, state, MinecraftResourcePackStructure.pathOf(state));
        return this;
    }

    @Override
    public MinecraftResourcePackWriter font(Font font) {
        writeToJson(SerializerFont.INSTANCE, font, MinecraftResourcePackStructure.pathOf(font));
        return this;
    }

    @Override
    public MinecraftResourcePackWriter language(Language language) {
        writeToJson(SerializerLanguage.INSTANCE, language, MinecraftResourcePackStructure.pathOf(language));
        return this;
    }

    @Override
    public MinecraftResourcePackWriter model(Model model) {
        writeToJson(SerializerModel.INSTANCE, model, MinecraftResourcePackStructure.pathOf(model));
        return this;
    }

    @Override
    public MinecraftResourcePackWriter soundRegistry(SoundRegistry soundRegistry) {
        writeToJson(SerializerSoundRegistry.INSTANCE, soundRegistry, MinecraftResourcePackStructure.pathOf(soundRegistry));
        return this;
    }

    @Override
    public MinecraftResourcePackWriter sound(Sound.File soundFile) {
        target.write(MinecraftResourcePackStructure.pathOf(soundFile), soundFile.data());
        return this;
    }

    @Override
    public MinecraftResourcePackWriter texture(Texture texture) {
        target.write(
                MinecraftResourcePackStructure.pathOf(texture),
                texture.data()
        );

        Metadata metadata = texture.meta();
        if (!metadata.parts().isEmpty()) {
            writeToJson(SerializerMetadata.INSTANCE, metadata, MinecraftResourcePackStructure.pathOfMeta(texture));
        }
        return this;
    }

    @Override
    public MinecraftResourcePackWriter file(String path, Writable data) {
        target.write(path, data);
        return this;
    }

    private <T> void writeToJson(JsonFileStreamWriter<T> serializer, T object, String path) {
        try (JsonWriter jsonWriter = target.openJsonWriter(path)) {
            serializer.serialize(object, jsonWriter);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to " + path, e);
        }
    }

}
