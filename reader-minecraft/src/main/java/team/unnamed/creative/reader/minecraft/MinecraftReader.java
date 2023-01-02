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
package team.unnamed.creative.reader.minecraft;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.ResourcePackBuilder;
import team.unnamed.creative.base.Writable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public final class MinecraftReader {

    private static final String PACK_META = "pack.mcmeta";
    private static final String PACK_ICON = "pack.png";

    private static final String ASSETS_FOLDER = "assets";

    private static final String MODELS_CATEGORY = "models";
    private static final String FONTS_CATEGORY = "font";
    private static final String TEXTURES_CATEGORY = "textures";
    private static final String LANGUAGES_CATEGORY = "lang";
    private static final String BLOCK_STATES_CATEGORY = "blockstates";
    private static final String SOUNDS_CATEGORY = "sounds";

    private MinecraftReader() {
    }

    public void read(FileTreeWalker tree, ResourcePackBuilder builder) throws IOException {

        while (tree.hasNext()) {
            String path = tree.next();
            InputStream inputStream = tree.input();

            Queue<String> tokens = new LinkedList<>(Arrays.asList(path.split("/")));

            if (tokens.isEmpty()) {
                // INVALID!
                continue;
            }

            if (tokens.size() == 1) {
                switch (tokens.poll()) {
                    case PACK_META: {
                        System.out.println("Found pack metadata");
                        break;
                    }
                    case PACK_ICON: {
                        System.out.println("Found pack icon");
                        break;
                    }
                    default: {
                        System.out.println("Unknown file: " + path);
                        // unknown top-level file, maybe some
                        // credits.txt file?
                        builder.file(path, Writable.bytes(toBytes(inputStream)));
                        break;
                    }
                }
                continue;
            }

            // has folders
            String folder = tokens.poll();
            if (folder.equals(ASSETS_FOLDER)) {
                if (tokens.isEmpty()) {
                    // assets was a file?
                    System.err.println("Is 'assets' a file?");
                    continue;
                }

                // then the next token is a namespace
                String namespace = tokens.poll();
                if (tokens.isEmpty()) {
                    // found a file like assets/<file>
                    // when it should be assets/<namespace>/...
                    System.err.println("Found a file in the namespaces folder: " + path);
                    continue;
                }

                String category = tokens.poll();
                if (tokens.isEmpty()) {
                    if (category.equals("sounds.json")) {
                        System.out.println("Found sounds.json file for namespace " + namespace);
                    } else {
                        System.err.println("Found a file in the namespace root folder");
                    }
                    continue;
                }

                switch (category) {
                    case MODELS_CATEGORY: {
                        String keyValue = String.join("/", tokens);
                        Key key = Key.key(namespace, keyValue);
                        System.out.println("Found a model with key: " + key);
                        break;
                    }
                    case TEXTURES_CATEGORY: {
                        // TODO: Read texture metadata
                        String keyValue = String.join("/", tokens);
                        Key key = Key.key(namespace, keyValue);
                        System.out.println("Found a texture with key: " + key);
                        break;
                    }
                    case SOUNDS_CATEGORY: {
                        break;
                    }
                    case FONTS_CATEGORY: {
                        break;
                    }
                    case LANGUAGES_CATEGORY: {
                        break;
                    }
                    case BLOCK_STATES_CATEGORY: {
                        break;
                    }
                    default: {
                        System.out.println("Unknown category: " + category);
                    }
                }
            } else {
                // unknown folder, just add it as an unknown file
                System.out.println("Unknown complex file: " + path);
                builder.file(path, Writable.bytes(toBytes(inputStream)));
            }
        }
    }

    public static MinecraftReader minecraft() {
        return new MinecraftReader();
    }

    private static byte[] toBytes(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = input.read(buf)) != -1) {
            output.write(buf, 0, len);
        }
        return output.toByteArray();
    }

}
