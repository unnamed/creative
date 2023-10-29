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
import team.unnamed.creative.metadata.gui.GuiBorder;
import team.unnamed.creative.metadata.gui.GuiMeta;
import team.unnamed.creative.metadata.gui.GuiScaling;
import team.unnamed.creative.serialize.minecraft.GsonUtil;

import java.io.IOException;

final class GuiMetaCodec implements MetadataPartCodec<GuiMeta> {

    @Override
    public @NotNull Class<GuiMeta> type() {
        return GuiMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "gui";
    }

    @Override
    public @NotNull GuiMeta read(final @NotNull JsonObject node) {
        GuiMeta.Builder gui = GuiMeta.builder();
        if (node.has("scaling")) {
            JsonObject scalingNode = node.getAsJsonObject("scaling");
            JsonElement typeNode = scalingNode.get("type");
            String typeString = typeNode != null ? typeNode.getAsString() : "stretch";
            GuiScaling.ScalingType type = GuiScaling.ScalingType.valueOf(typeString.toUpperCase());
            int width = GsonUtil.getInt(scalingNode, "width", 0);
            int height = GsonUtil.getInt(scalingNode, "height", 0);
            if (GsonUtil.isInt(scalingNode, "border"))
                gui.scaling(GuiScaling.of(type, width, height, GsonUtil.getInt(scalingNode, "border", 0)));
            else {
                JsonObject borderNode = scalingNode.getAsJsonObject("border");
                GuiBorder guiBorder;
                if (borderNode == null) guiBorder = GuiBorder.of(0,0,0,0);
                else {
                    int top = GsonUtil.getInt(borderNode, "top", 0);
                    int bottom = GsonUtil.getInt(borderNode, "bottom", 0);
                    int left = GsonUtil.getInt(borderNode, "left", 0);
                    int right = GsonUtil.getInt(borderNode, "right", 0);
                    guiBorder = GuiBorder.of(top, bottom, left, right);
                }
                gui.scaling(GuiScaling.of(type, width, height, guiBorder));
            }
        }

        return gui.build();
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull GuiMeta value) throws IOException {
        writer.beginObject();
        GuiScaling scaling = value.scaling();
        writer.name("scaling").beginObject();
        writer.name("type").value(scaling.type().name().toLowerCase());
        writer.name("width").value(scaling.width());
        writer.name("height").value(scaling.height());
        writer.name("border");
        writer.beginObject();
        GuiBorder border = scaling.border();
        writer.name("top").value(border.top());
        writer.name("bottom").value(border.bottom());
        writer.name("left").value(border.left());
        writer.name("right").value(border.right());
        writer.endObject();
        writer.endObject();
        writer.endObject();
    }
}
