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
package team.unnamed.creative.serialize;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.Collection;
import java.util.Map;

public interface ResourcePackInput {

    // |--------------------------------------|
    // |--------- RESOURCE PACK ICON ---------|
    // |--------------------------------------|
    /**
     * Returns the resource pack icon image (must
     * be a PNG file), it may be null
     *
     * @return The resource pack icon image, null if
     * not set
     */
    @Nullable Writable icon();



    // |--------------------------------------|
    // |-------------- METADATA --------------|
    // |--------------------------------------|

    // ----- Pack Meta -----
    @Nullable PackMeta meta();

    default int format() {
        PackMeta meta = meta();
        if (meta == null) {
            return -1;
        } else {
            return meta.format();
        }
    }

    default @Nullable String description() {
        PackMeta meta = meta();
        if (meta == null) {
            return null;
        } else {
            return meta.description();
        }
    }


    // ----- Language Meta -----
    @Nullable LanguageMeta languageRegistry();

    @Nullable LanguageEntry languageEntry(Key key);

    Collection<LanguageEntry> languageEntries();


    // ----- Filter Meta -----
    @Nullable FilterMeta filter();



    // |-------------------------------------|
    // |------------ BLOCK STATE ------------|
    // |-------------------------------------|
    @Nullable BlockState blockState(Key key);

    Collection<BlockState> blockStates();



    // |-------------------------------------|
    // |--------------- FONTS ---------------|
    // |-------------------------------------|
    @Nullable Font font(Key key);

    Collection<Font> fonts();



    // |-------------------------------------|
    // |------------- LANGUAGES -------------|
    // |-------------------------------------|
    Collection<Language> languages();



    // |--------------------------------------|
    // |--------------- MODELS ---------------|
    // |--------------------------------------|
    Collection<Model> models();



    // |--------------------------------------|
    // |--------------- SOUNDS ---------------|
    // |--------------------------------------|
    //SoundRegistry soundRegistry();

    //Collection<Sound.File> sounds();



    // |--------------------------------------|
    // |-------------- TEXTURES --------------|
    // |--------------------------------------|
    Collection<Texture> textures();



    // |--------------------------------------|
    // |---------------- OTHER ---------------|
    // |--------------------------------------|
    Map<String, Writable> unknownFiles();

}
