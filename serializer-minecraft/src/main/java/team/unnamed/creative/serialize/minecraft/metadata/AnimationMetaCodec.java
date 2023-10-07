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
package team.unnamed.creative.serialize.minecraft.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.animation.AnimationFrame;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.serialize.minecraft.GsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class AnimationMetaCodec implements MetadataPartCodec<AnimationMeta> {

    @Override
    public Class<AnimationMeta> type() {
        return AnimationMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "animation";
    }

    @Override
    public @NotNull AnimationMeta read(final @NotNull JsonObject node) {
        AnimationMeta.Builder animation = AnimationMeta.builder()
                .interpolate(GsonUtil.getBoolean(node, "interpolate", AnimationMeta.DEFAULT_INTERPOLATE))
                .width(GsonUtil.getInt(node, "width", AnimationMeta.DEFAULT_WIDTH))
                .height(GsonUtil.getInt(node, "height", AnimationMeta.DEFAULT_HEIGHT))
                .frameTime(GsonUtil.getInt(node, "frametime", AnimationMeta.DEFAULT_FRAMETIME));

        if (node.has("frames")) {
            List<AnimationFrame> frames = new ArrayList<>();
            for (JsonElement frameNode : node.get("frames").getAsJsonArray()) {
                if (frameNode.isJsonObject()) {
                    JsonObject frameObject = frameNode.getAsJsonObject();
                    // represents complete frame (index and frame time)
                    int time = GsonUtil.getInt(frameObject, "time", AnimationFrame.DELEGATE_FRAME_TIME);
                    int index = frameObject.get("index").getAsInt(); // required
                    frames.add(AnimationFrame.of(index, time));
                } else {
                    // represents the index only
                    int index = frameNode.getAsInt();
                    frames.add(AnimationFrame.of(index));
                }
            }
            animation.frames(frames);
        }

        return animation.build();
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull AnimationMeta animation) throws IOException {
        writer.beginObject();
        boolean interpolate = animation.interpolate();
        if (interpolate != AnimationMeta.DEFAULT_INTERPOLATE) {
            writer.name("interpolate").value(interpolate);
        }
        int width = animation.width();
        if (width != AnimationMeta.DEFAULT_WIDTH) {
            writer.name("width").value(width);
        }
        int height = animation.height();
        if (height != AnimationMeta.DEFAULT_HEIGHT) {
            writer.name("height").value(height);
        }
        int frameTime = animation.frameTime();
        if (frameTime != AnimationMeta.DEFAULT_FRAMETIME) {
            writer.name("frametime").value(frameTime);
        }
        List<AnimationFrame> frames = animation.frames();
        if (!frames.isEmpty()) {
            writer.name("frames").beginArray();
            for (AnimationFrame frame : frames) {
                int index = frame.index();
                int time = frame.frameTime();
                if (frameTime == time || frameTime == AnimationFrame.DELEGATE_FRAME_TIME) {
                    // same as default frameTime, we can skip it
                    writer.value(index);
                } else {
                    // specific frameTime, write as an object
                    writer.beginObject()
                            .name("index").value(index)
                            .name("time").value(time)
                            .endObject();
                }
            }
            writer.endArray();
        }

        writer.endObject();
    }

}
