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
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.file.FileTree;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.filter.FilterPattern;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.List;

public interface ResourcePackBuilder {

    //#region Metadata methods
    // |-----------------------------------|
    // |------- METADATA OPERATIONS -------|
    // |-----------------------------------|
    ResourcePackBuilder meta(PackMeta meta);

    default ResourcePackBuilder meta(int format, String description) {
        return meta(PackMeta.of(format, description));
    }

    ResourcePackBuilder filter(FilterMeta filter);

    default ResourcePackBuilder filter(FilterPattern... patterns) {
        return filter(FilterMeta.of(patterns));
    }

    default ResourcePackBuilder filter(List<FilterPattern> patterns) {
        return filter(FilterMeta.of(patterns));
    }

    ResourcePackBuilder customMetaPart(MetadataPart part);
    //#endregion

    ResourcePackBuilder blockState(BlockState state);

    ResourcePackBuilder font(Font font);

    ResourcePackBuilder language(Language language);

    ResourcePackBuilder model(Model model);

    ResourcePackBuilder sounds(SoundRegistry soundRegistry);

    ResourcePackBuilder sound(Sound.File soundFile);

    //#region Texture methods
    // |------------------------------------|
    // |-------- TEXTURE OPERATIONS --------|
    // |------------------------------------|
    ResourcePackBuilder texture(Texture texture);

    default ResourcePackBuilder texture(Key key, Writable data) {
        return texture(Texture.of(key, data));
    }

    default ResourcePackBuilder texture(Key key, Writable data, Metadata meta) {
        return texture(Texture.of(key, data, meta));
    }
    //#endregion

    void writeTo(FileTree tree);

    default ResourcePack build() {
        return ResourcePack.build(this::writeTo);
    }

    static ResourcePackBuilder create() {
        return new ResourcePackBuilderImpl();
    }

}
