/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
import team.unnamed.creative.metadata.gui.NineSliceGuiScaling;
import team.unnamed.creative.metadata.gui.StretchGuiScaling;
import team.unnamed.creative.metadata.gui.TileGuiScaling;
import team.unnamed.creative.serialize.minecraft.GsonUtil;

import java.io.IOException;
import java.util.Locale;

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
        if (!node.has("scaling")) { // todo: should this be optional?
            return GuiMeta.of(GuiScaling.stretch());
        }

        JsonObject scalingNode = node.getAsJsonObject("scaling");
        JsonElement typeNode = scalingNode.get("type");
        String typeString = typeNode != null ? typeNode.getAsString() : "stretch";
        switch (typeString.toLowerCase(Locale.ROOT)) {
            case "stretch": {
                return GuiMeta.of(GuiScaling.stretch());
            }
            case "tile": {
                final int width = GsonUtil.getInt(scalingNode, "width", 0);
                final int height = GsonUtil.getInt(scalingNode, "height", 0);
                return GuiMeta.of(GuiScaling.tile(width, height));
            }
            case "nine_slice": {
                final int width = GsonUtil.getInt(scalingNode, "width", 0);
                final int height = GsonUtil.getInt(scalingNode, "height", 0);
                final GuiBorder border;
                if (GsonUtil.isInt(scalingNode, "border")) {
                    border = GuiBorder.border(GsonUtil.getInt(scalingNode, "border", 0));
                } else {
                    final JsonObject borderNode = scalingNode.getAsJsonObject("border");
                    if (borderNode == null) border = GuiBorder.border(0, 0, 0, 0);
                    else {
                        final int top = GsonUtil.getInt(borderNode, "top", 0);
                        final int bottom = GsonUtil.getInt(borderNode, "bottom", 0);
                        final int left = GsonUtil.getInt(borderNode, "left", 0);
                        final int right = GsonUtil.getInt(borderNode, "right", 0);
                        border = GuiBorder.border(top, bottom, left, right);
                    }
                }
                return GuiMeta.of(GuiScaling.nineSlice(width, height, border));
            }
            default:
                throw new IllegalArgumentException("Unknown gui scaling type: " + typeString);
        }
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull GuiMeta value) throws IOException {
        writer.beginObject();
        GuiScaling scaling = value.scaling();
        writer.name("scaling").beginObject();
        if (scaling instanceof StretchGuiScaling) {
            writer.name("type").value("stretch");
        } else if (scaling instanceof TileGuiScaling) {
            final TileGuiScaling tile = (TileGuiScaling) scaling;
            writer.name("type").value("tile");
            writer.name("width").value(tile.width());
            writer.name("height").value(tile.height());
        } else if (scaling instanceof NineSliceGuiScaling) {
            final NineSliceGuiScaling nineSlice = (NineSliceGuiScaling) scaling;
            writer.name("type").value("nine_slice");
            writer.name("width").value(nineSlice.width());
            writer.name("height").value(nineSlice.height());

            final GuiBorder border = nineSlice.border();
            final int top = border.top();
            final int bottom = border.bottom();
            final int left = border.left();
            final int right = border.right();

            writer.name("border");
            if (top == bottom && bottom == left && left == right) {
                // if they are all equal just write as an int
                writer.value(top);
            } else {
                // otherwise write as an object
                writer.beginObject()
                        .name("top").value(top)
                        .name("bottom").value(bottom)
                        .name("left").value(left)
                        .name("right").value(right)
                        .endObject();
            }
        }
        writer.endObject();
        writer.endObject();
    }
}
