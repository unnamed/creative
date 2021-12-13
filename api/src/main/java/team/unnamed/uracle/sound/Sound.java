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
import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;
import team.unnamed.uracle.Writable;

import java.util.Locale;

public class Sound implements Element.Part {

    private static final float DEFAULT_VOLUME = 1.0F;
    private static final float DEFAULT_PITCH = 1.0F;
    private static final int DEFAULT_WEIGHT = 1;
    private static final boolean DEFAULT_STREAM = false;
    private static final int DEFAULT_ATTENUATION_DISTANCE = 16;
    private static final boolean DEFAULT_PRELOAD = false;
    private static final Type DEFAULT_TYPE = Type.SOUND;

    private Key key;

    /**
     * The OGG sound data, located at the specified {@code location}
     * inside assets/&lt;namespace&gt;/sounds folder
     */
    private Writable data;

    private float volume;
    private float pitch;
    private int weight;
    private boolean stream;
    private int attenuationDistance;
    private boolean preload;
    private Type type;

    enum Type {
        SOUND,
        EVENT
    }

    @Override
    public void write(TreeWriter.Context context) {
        // in order to make some optimizations, we
        // have to do this
        if (
                volume == DEFAULT_VOLUME
                        && pitch == DEFAULT_PITCH
                        && weight == DEFAULT_WEIGHT
                        && stream == DEFAULT_STREAM
                        && attenuationDistance == DEFAULT_ATTENUATION_DISTANCE
                        && preload == DEFAULT_PRELOAD
                        && type == DEFAULT_TYPE
        ) {
            // everything is default, just write the path
            context.writeStringValue(key.toString());
        } else {
            context.startObject();
            context.writeStringField("name", key.toString());
            if (volume != DEFAULT_VOLUME) {
                context.writeFloatField("volume", volume);
            }
            if (pitch != DEFAULT_PITCH) {
                context.writeFloatField("pitch", pitch);
            }
            if (weight != DEFAULT_WEIGHT) {
                context.writeIntField("weight", weight);
            }
            if (stream != DEFAULT_STREAM) {
                context.writeBooleanField("stream", stream);
            }
            if (attenuationDistance != DEFAULT_ATTENUATION_DISTANCE) {
                context.writeIntField("attenuation_distance", attenuationDistance);
            }
            if (preload != DEFAULT_PRELOAD) {
                context.writeBooleanField("preload", preload);
            }
            if (type != DEFAULT_TYPE) {
                context.writeStringField("type", type.name().toLowerCase(Locale.ROOT));
            }
            context.endObject();
        }
    }

}
