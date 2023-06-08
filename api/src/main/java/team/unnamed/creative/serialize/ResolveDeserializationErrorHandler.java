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

import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.ElementRotation;
import team.unnamed.creative.model.ItemTransform;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.util.Range;

class ResolveDeserializationErrorHandler implements DeserializationErrorHandler {
    @Override
    public void onInvalidItemTransform(ItemTransform.Builder builder, RuntimeException exception) {
        builder.translation(Range.coerceIn(builder.translation(), ItemTransform.MIN_TRANSLATION, ItemTransform.MAX_TRANSLATION));
        builder.scale(Range.coerceIn(builder.scale(), ItemTransform.MIN_SCALE, ItemTransform.MAX_SCALE));
    }

    @Override
    public void onInvalidBitMapFontProvider(BitMapFontProvider.Builder builder, RuntimeException exception) {
        builder.ascent(Math.min(builder.ascent(), builder.height()));
    }

    @Override
    public void onInvalidElement(Element.Builder builder, RuntimeException exception) {
        builder.from(Range.coerceIn(builder.from(), Element.MIN_EXTENT, Element.MAX_EXTENT));
        builder.to(Range.coerceIn(builder.to(), Element.MIN_EXTENT, Element.MAX_EXTENT));
    }

    @Override
    public void onInvalidElementFace(ElementFace.Builder builder, RuntimeException exception) {
        builder.uv(Range.coerceIn(builder.uv(), 0, 1));
        int rotation = builder.rotation();
        if (rotation % 90 != 0) rotation = rotation / 90 * 90;
        rotation = Range.coerceIn(rotation, 0, 270);
        builder.rotation(rotation);
    }

    @Override
    public void onInvalidElementRotation(ElementRotation.Builder builder, RuntimeException exception) {
        float angle = builder.angle();
        int angle2 = (int) (angle * 2);
        if (angle2 % 45 != 0) {
            angle2 = angle2 / 45 * 45;
            angle = angle2 / 2f;
        }
        angle = Range.coerceIn(angle, -45, 45);
        builder.angle(angle);
    }

    @Override
    public void onInvalidSound(Sound.Builder builder, RuntimeException exception) {
        builder.volume(Math.max(builder.volume(), 0.01f));
        builder.pitch(Math.max(builder.pitch(), 0.01f));
        builder.weight(Math.max(builder.weight(), 1));
    }
}
