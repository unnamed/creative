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
import team.unnamed.creative.serialize.FileTree;

import java.io.OutputStream;
import java.io.Writer;

import static java.util.Objects.requireNonNull;

public class MinecraftFileTree implements FileTree {

    static final String ASSETS_FOLDER = "assets";
    static final String TEXTURES_FOLDER = "textures";
    static final String SOUNDS_FOLDER = "sounds";
    static final String BLOCKSTATES_FOLDER = "blockstates";
    static final String FONTS_FOLDER = "font";
    static final String LANGUAGES_FOLDER = "lang";
    static final String MODELS_FOLDER = "models";

    static final String TEXTURE_EXTENSION = ".png";
    static final String METADATA_EXTENSION = ".mcmeta";
    static final String SOUND_EXTENSION = ".ogg";
    static final String OBJECT_EXTENSION = ".json";

    private final FileTree delegate;

    public MinecraftFileTree(FileTree delegate) {
        this.delegate = requireNonNull(delegate, "delegate");
    }

    @Override
    public boolean exists(String path) {
        return delegate.exists(path);
    }

    @Override
    public OutputStream openStream(String path) {
        return delegate.openStream(path);
    }

    @Override
    public Writer openWriter(String path) {
        return delegate.openWriter(path);
    }

    public JsonWriter openJsonWriter(String... path) {
        return new JsonWriter(openWriter(String.join("/", path)));
    }

    @Override
    public void write(String path, Writable data) {
        delegate.write(path, data);
    }

    @Override
    public void close() {
        delegate.close();
    }

}