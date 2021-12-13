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
package team.unnamed.uracle.sound;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoundEvent {

    /**
     * The sound event location, usually separated in categories
     * (such as entity.enderman.stare), the file is always at
     * assets/&lt;namespace&gt;/sounds.json. This property specifies
     * the namespace and name of this file
     */
    private Key location;

    /**
     * True if the sounds listed in {@link SoundEvent#sounds}
     * should replace the sounds listed in the default
     * sounds.json for this sound event. False if the
     * sounds listed should be added to the list of
     * default sounds
     */
    private boolean replace;

    /**
     * Translated as the subtitle of the sound if
     * "Show Subtitles" is enabled in-game, optional
     */
    @Nullable private String subtitle;

    /**
     * The sounds this sound event uses, one of the
     * listed sounds is randomly selected to play when
     * this sound event is triggered, optional
     */
    @Nullable private List<Sound> sounds;

}
