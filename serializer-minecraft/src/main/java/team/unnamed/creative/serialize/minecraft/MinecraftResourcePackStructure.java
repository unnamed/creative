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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringJoiner;

final class MinecraftResourcePackStructure {

    // file extensions
    public static final String TEXTURE_EXTENSION = ".png";
    public static final String METADATA_EXTENSION = ".mcmeta";
    public static final String SOUND_EXTENSION = ".ogg";
    public static final String OBJECT_EXTENSION = ".json";

    public static final String FILE_SEPARATOR = "/";

    // special root files
    public static final String PACK_METADATA_FILE = "pack" + METADATA_EXTENSION;
    public static final String PACK_ICON_FILE = "pack" + TEXTURE_EXTENSION;

    // assets folder (assets/<namespace>)
    public static final String ASSETS_FOLDER = "assets";

    // known special folders
    public static final String OPTIFINE_FOLDER = "optifine";

    // sounds.json file (assets/<namespace>/sounds.json)
    public static final String SOUNDS_FILE = "sounds" + OBJECT_EXTENSION;

    // resource categories (assets/<namespace>/<category>/...)
    public static final String TEXTURES_FOLDER = "textures";
    public static final String SOUNDS_FOLDER = "sounds";
    public static final String BLOCKSTATES_FOLDER = "blockstates";
    public static final String FONTS_FOLDER = "font";
    public static final String LANGUAGES_FOLDER = "lang";
    public static final String MODELS_FOLDER = "models";
    public static final String TEXTS_FOLDER = "texts";

    private MinecraftResourcePackStructure() {
    }

    // 1. Metadata
    //2. BlockState
    //3. Font
    //4. Language
    //5. Model
    //6. Sound Registry
    //7. Sound Files
    //8. Texture

    public static String pathOf(BlockState blockState) {
        // assets/<namespace>/blockstates/<path>.json
        return withCategory(BLOCKSTATES_FOLDER, blockState, OBJECT_EXTENSION);
    }

    public static String pathOf(Font font) {
        // assets/<namespace>/font/<path>.json
        return withCategory(ASSETS_FOLDER, font, OBJECT_EXTENSION);
    }

    public static String pathOf(Language language) {
        // assets/<namespace>/lang/<path>.json
        return withCategory(LANGUAGES_FOLDER, language, OBJECT_EXTENSION);
    }

    public static String pathOf(Model model) {
        // assets/<namespace>/models/<path>.json
        return withCategory(MODELS_FOLDER, model, OBJECT_EXTENSION);
    }

    public static String pathOf(SoundRegistry registry) {
        // assets/<namespace>/sounds.json
        return path(ASSETS_FOLDER, registry.namespace(), SOUNDS_FILE);
    }

    public static String pathOf(Sound.File sound) {
        // assets/<namespace>/sounds/<path>.ogg
        return withCategory(SOUNDS_FOLDER, sound, SOUND_EXTENSION);
    }

    public static String pathOf(Texture texture) {
        // assets/<namespace>/textures/<path>.png
        return withCategory(TEXTURES_FOLDER, texture, TEXTURE_EXTENSION);
    }

    public static String pathOfMeta(Texture texture) {
        // assets/<namespace>/textures/<path>.mcmeta
        return withCategory(TEXTURES_FOLDER, texture, METADATA_EXTENSION);
    }

    public static Queue<String> tokenize(String path) {
        return new LinkedList<>(Arrays.asList(path.split(FILE_SEPARATOR)));
    }

    public static String path(String... path) {
        StringJoiner joiner = new StringJoiner(FILE_SEPARATOR);
        for (String part : path) {
            joiner.add(part);
        }
        return joiner.toString();
    }

    public static String path(Iterable<String> path) {
        StringJoiner joiner = new StringJoiner(FILE_SEPARATOR);
        for (String part : path) {
            joiner.add(part);
        }
        return joiner.toString();
    }

    // helper methods
    private static String withCategory(String categoryFolder, Keyed resource, String extension) {
        Key key = resource.key();
        // assets/<namespace>/<category>/<path>.<extension>
        return path(ASSETS_FOLDER, key.namespace(), categoryFolder, key.value() + extension);
    }

}
