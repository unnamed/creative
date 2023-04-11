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
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.List;

public interface ResourcePackWriter<R extends ResourcePackWriter<R>> {

    /**
     * Sets the resource pack icon image
     * (must be a PNG file)
     *
     * @param icon The icon (PNG image)
     */
    R icon(Writable icon);

    /**
     * Sets the resource pack metadata
     *
     * @param metadata The metadata
     */
    R metadata(Metadata metadata);

    R blockState(BlockState state);

    R font(Font font);

    //#region Font helpers
    default R font(Key key, FontProvider... providers) {
        return font(Font.of(key, providers));
    }

    default R font(Key key, List<FontProvider> providers) {
        return font(Font.of(key, providers));
    }
    //#endregion

    R language(Language language);

    R model(Model model);

    R soundRegistry(SoundRegistry soundRegistry);

    R sound(Sound.File soundFile);

    //#region Sound helpers
    default R sound(Key key, Writable data) {
        return sound(Sound.File.of(key, data));
    }
    //#endregion

    R texture(Texture texture);

    //#region Texture helpers
    default R texture(Key key, Writable data) {
        return texture(Texture.of(key, data));
    }

    default R texture(Key key, Writable data, Metadata meta) {
        return texture(Texture.of(key, data, meta));
    }
    //#endregion

    R file(String path, Writable data);

}
