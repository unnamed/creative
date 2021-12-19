/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle;

import net.kyori.adventure.key.Key;
import team.unnamed.uracle.font.Font;
import team.unnamed.uracle.lang.Language;
import team.unnamed.uracle.sound.SoundRegistry;
import team.unnamed.uracle.texture.Texture;

/**
 * Represents the object responsible for providing
 * methods to build a Minecraft: Java Edition resource
 * pack, there are methods for every independent file
 * in resource-packs such as textures, fonts, languages,
 * metadata and extra files
 *
 * @since 1.0.0
 */
public interface ResourcePackBuilder {

    /**
     * Writes the given {@code font} to the
     * resource-pack
     *
     * @param location The font location
     * @param font The written font
     * @return This builder, for chaining
     */
    ResourcePackBuilder font(Key location, Font font);

    /**
     * Writes the given {@code language} to
     * the resource-pack
     *
     * @param location The language location
     * @param language The written language
     * @return This builder, for chaining
     */
    ResourcePackBuilder language(Key location, Language language);

    /**
     * Writes a new texture into the specified
     * namespaced location
     *
     * @param location The texture location
     * @param texture The written texture
     * @return This builder, for chaining
     */
    ResourcePackBuilder texture(Key location, Texture texture);

    /**
     * Sets the sound registry for a specific
     * namespace
     *
     * @param namespace The sound registry namespace
     * @param registry The sound registry
     * @return This builder, for chaining
     */
    ResourcePackBuilder sounds(String namespace, SoundRegistry registry);

    /**
     * Writes the resource-pack meta into
     * this resource-pack
     *
     * @param meta The resource-pack metadata
     * @return This builder, for chaining
     */
    ResourcePackBuilder meta(PackMeta meta);

    /**
     * Writes a set of bytes to an arbitrary
     * file, use it only when an implementation
     * of a resource-pack part doesn't exist
     *
     * <p>In case a part doesn't exist and it
     * is from Minecraft: Java Edition (Vanilla,
     * no modifications), please open an issue
     * on GitHub</p>
     *
     * @param path The file path
     * @param data The written data
     * @return This builder, for chaining
     */
    ResourcePackBuilder file(String path, Writable data);

}
