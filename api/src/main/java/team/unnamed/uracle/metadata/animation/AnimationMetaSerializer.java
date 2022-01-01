/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.metadata.animation;

import team.unnamed.uracle.metadata.MetadataPart;
import team.unnamed.uracle.serialize.AssetWriter;

/**
 * Serializer implementation for {@link AnimationMeta}
 * instances, internal!
 */
final class AnimationMetaSerializer
        implements MetadataPart.Serializer<AnimationMeta> {

    static final MetadataPart.Serializer<AnimationMeta> INSTANCE
            = new AnimationMetaSerializer();

    private AnimationMetaSerializer() {
    }

    @Override
    public void serialize(AssetWriter writer, AnimationMeta part) {
        int frameTime = part.frameTime();

        writer.key("animation").startObject()
                .key("interpolate").value(part.interpolate())
                .key("width").value(part.width())
                .key("height").value(part.height())
                .key("frametime").value(frameTime)
                .key("frames").startArray();

        for (AnimationFrame frame : part.frames()) {
            int index = frame.index();
            int time = frame.frameTime();

            if (frameTime == time || frameTime == AnimationFrame.DELEGATE_FRAME_TIME) {
                // same as default frameTime, we can skip it
                writer.value(index);
            } else {
                // specific frameTime, write as an object
                writer.startObject()
                        .key("index").value(index)
                        .key("time").value(time)
                        .endObject();
            }
        }

        writer.endArray().endObject();
    }

}
