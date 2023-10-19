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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.metadata.overlays.OverlayEntry;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OverlaysMetaTest {

    @Test
    @DisplayName("Test Overlays meta serialization")
    void test_simple_serialization() {
        final OverlaysMeta overlaysMeta = OverlaysMeta.of(
                OverlayEntry.of(PackFormat.format(18), "v18"),
                OverlayEntry.of(PackFormat.format(19), "v19")
        );
        assertEquals(
                "{\"entries\":[{\"formats\":18,\"directory\":\"v18\"},{\"formats\":19,\"directory\":\"v19\"}]}",
                OverlaysMetaCodec.INSTANCE.toJson(overlaysMeta)
        );
    }

    @Test
    @DisplayName("Test overlays meta serialization with custom pack format range")
    void test_range_serialization() {
        final OverlaysMeta overlaysMeta = OverlaysMeta.of(
                OverlayEntry.of(PackFormat.format(18, 18, 20), "v18-20"),
                OverlayEntry.of(PackFormat.format(21, 21, 24), "v21-24")
        );
        assertEquals(
                "{\"entries\":[{\"formats\":[18,20],\"directory\":\"v18-20\"},{\"formats\":[21,24],\"directory\":\"v21-24\"}]}",
                OverlaysMetaCodec.INSTANCE.toJson(overlaysMeta)
        );
    }

    @Test
    @DisplayName("Test simple overlays meta deserialization")
    void test_simple_deserialization() {
        final OverlaysMeta overlaysMeta = OverlaysMetaCodec.INSTANCE.fromJson("{\"entries\":[{\"formats\":18,\"directory\":\"v18\"},{\"formats\":19,\"directory\":\"v19\"}]}");
        assertEquals(
                OverlaysMeta.of(
                        OverlayEntry.of(PackFormat.format(18), "v18"),
                        OverlayEntry.of(PackFormat.format(19), "v19")
                ),
                overlaysMeta
        );
    }

    @Test
    @DisplayName("Test overlays meta deserialization with custom pack format range")
    void test_range_deserialization() {
        final OverlaysMeta overlaysMeta = OverlaysMetaCodec.INSTANCE.fromJson("{\"entries\":[{\"formats\":[18,20],\"directory\":\"v18-20\"},{\"formats\":[21,24],\"directory\":\"v21-24\"}]}");
        assertEquals(
                OverlaysMeta.of(
                        OverlayEntry.of(PackFormat.format(18, 18, 20), "v18-20"),
                        OverlayEntry.of(PackFormat.format(21, 21, 24), "v21-24")
                ),
                overlaysMeta
        );
    }

}
