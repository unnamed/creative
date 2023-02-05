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

import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

public class MinecraftReaderTest {

    //@Test
    public void test() throws IOException {
        String pack = "URBAN_128x_1.19+";

        System.out.println(" [INFO] ------------------------------");
        System.out.println(" [INFO]    Starting deserialization   ");
        System.out.println(" [INFO] ------------------------------");

        try (FileTreeReader treeReader = FileTreeReader.zip(new ZipInputStream(new FileInputStream("/home/nd/Desktop/" + pack + ".zip")))) {
            MinecraftResourcePackSerializer.minecraft().pipe(
                    MinecraftResourcePackSerializer.minecraft().reader(treeReader),
                    new ResourcePackWriter<MinecraftResourcePackWriter>() {
                        @Override
                        public MinecraftResourcePackWriter icon(Writable icon) {
                            System.out.println("ICON!");
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter metadata(Metadata metadata) {
                            System.out.println("METADATA!");
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter blockState(BlockState state) {
                            System.out.println("BLOCK STATE! " + state.key());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter font(Font font) {
                            System.out.println("FONT! " + font.key());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter language(Language language) {
                            System.out.println("LANGUAGE! " + language.key());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter model(Model model) {
                            System.out.println("MODEL! " + model.key());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter soundRegistry(SoundRegistry soundRegistry) {
                            System.out.println("SOUND REGISTRY! " + soundRegistry.namespace());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter sound(Sound.File soundFile) {
                            System.out.println("SOUND! " + soundFile.key());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter texture(Texture texture) {
                            System.out.println("TEXTURE! " + texture.key() + " WITH META " + texture.meta().parts().size());
                            return null;
                        }

                        @Override
                        public MinecraftResourcePackWriter file(String path, Writable data) {
                            System.out.println("UNKNOWN " + path);
                            return null;
                        }
                    }
            );
        }
    }

}
