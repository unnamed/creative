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
package team.unnamed.creative.central.common.export;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.central.export.ResourcePackExporter;
import team.unnamed.creative.central.export.ResourcePackLocation;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Fluent-style class for exporting resource
 * packs to {@link File}s
 */
public class FileExporter implements ResourcePackExporter {

    private final File target;
    private final Logger logger;

    public FileExporter(File target, Logger logger) {
        this.target = target;
        this.logger = logger;
    }

    @Override
    @Contract("_ -> null")
    public @Nullable ResourcePackLocation export(ResourcePack resourcePack) throws IOException {
        if (!target.exists() && !target.createNewFile()) {
            throw new IOException("Failed to create target resource pack file");
        }

        MinecraftResourcePackWriter.minecraft().writeToZipFile(target, resourcePack);
        logger.info("Exported resource-pack to file: " + target);
        return null;
    }

}