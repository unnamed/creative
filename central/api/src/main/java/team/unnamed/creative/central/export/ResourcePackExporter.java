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
package team.unnamed.creative.central.export;

import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;

import java.io.IOException;

/**
 * Interface for exporting resource packs to different
 * targets like files, external servers (like MCPacks,
 * Polymath, Dropbox, etc.) or a local server
 *
 * @since 1.0.0
 */
public interface ResourcePackExporter {

    /**
     * Exports the given {@code resourcePack} to the
     * target of this exporter, returning the location
     * of the exported resource pack, or {@code null}
     * if the exporting method is not hosted
     *
     * @param resourcePack The resource pack to export
     * @return The location of the exported resource pack,
     * null if the exporting method is not hosted
     * @throws IOException If the exporting process fails
     * @since 1.0.0
     */
    @Nullable ResourcePackLocation export(ResourcePack resourcePack) throws IOException;

}
