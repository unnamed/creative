/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
package team.unnamed.creative.serialize.minecraft;

import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.overlays.OverlayEntry;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.overlay.Overlay;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;
import team.unnamed.creative.serialize.minecraft.fs.ZipEntryLifecycleHandler;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;
import team.unnamed.creative.serialize.minecraft.io.ResourceSerializer;
import team.unnamed.creative.serialize.minecraft.metadata.MetadataSerializer;
import team.unnamed.creative.serialize.minecraft.sound.SoundRegistrySerializer;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.*;

final class MinecraftResourcePackWriterImpl implements MinecraftResourcePackWriter {
    static final MinecraftResourcePackWriter INSTANCE = MinecraftResourcePackWriter.builder()
            .prettyPrinting(false)
            .build();

    private final ZipEntryLifecycleHandler zipEntryLifecycleHandler;
    private final boolean prettyPrinting;
    private final int targetPackFormat;

    private MinecraftResourcePackWriterImpl(
            final @NotNull ZipEntryLifecycleHandler zipEntryLifecycleHandler,
            final boolean prettyPrinting,
            final int targetPackFormat
    ) {
        this.zipEntryLifecycleHandler = zipEntryLifecycleHandler; // trust the caller (builder)
        this.prettyPrinting = prettyPrinting;
        this.targetPackFormat = targetPackFormat;
    }

    @Override
    public @NotNull ZipEntryLifecycleHandler zipEntryLifecycleHandler() {
        return zipEntryLifecycleHandler;
    }

    @Override
    public int targetPackFormat() {
        return targetPackFormat;
    }

    public <T extends Keyed & ResourcePackPart> void writeFullCategory(
            final @NotNull String basePath,
            final @NotNull ResourceContainer resourceContainer,
            final @NotNull FileTreeWriter target,
            final @NotNull ResourceCategory<T> category,
            final int localTargetPackFormat
    ) {
        for (T resource : category.lister().apply(resourceContainer)) {
            String path = basePath + category.pathOf(resource, localTargetPackFormat);
            final ResourceSerializer<T> serializer = category.serializer();

            if (serializer instanceof JsonResourceSerializer) {
                // if it's a JSON serializer, we can use our own method, that will
                // do some extra configuration
                writeToJson(target, (JsonResourceSerializer<T>) serializer, resource, path, localTargetPackFormat);
            } else {
                try (OutputStream output = target.openStream(path)) {
                    category.serializer().serialize(resource, output, localTargetPackFormat);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
    }

    private void writeWithBasePathAndTargetPackFormat(FileTreeWriter target, ResourceContainer container, String basePath, final int localTargetPackFormat) {
        // write resources from most categories
        for (ResourceCategory<?> category : ResourceCategories.categories()) {
            writeFullCategory(basePath, container, target, category, localTargetPackFormat);
        }

        // write sound registries
        for (SoundRegistry soundRegistry : container.soundRegistries()) {
            writeToJson(target, SoundRegistrySerializer.INSTANCE, soundRegistry, basePath + MinecraftResourcePackStructure.pathOf(soundRegistry), localTargetPackFormat);
        }

        // write textures
        for (Texture texture : container.textures()) {
            target.write(
                    basePath + MinecraftResourcePackStructure.pathOf(texture),
                    texture.data()
            );

            Metadata metadata = texture.meta();
            if (!metadata.parts().isEmpty()) {
                writeToJson(target, MetadataSerializer.INSTANCE, metadata, basePath + MinecraftResourcePackStructure.pathOfMeta(texture), localTargetPackFormat);
            }
        }

        // write unknown files
        for (Map.Entry<String, Writable> entry : container.unknownFiles().entrySet()) {
            target.write(basePath + entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void write(final @NotNull FileTreeWriter target, final @NotNull ResourcePack resourcePack) {
        // write icon
        {
            Writable icon = resourcePack.icon();
            if (icon != null) {
                target.write(PACK_ICON_FILE, icon);
            }
        }

        // write metadata
        {
            Metadata metadata = resourcePack.metadata();
            PackMeta packMeta = metadata.meta(PackMeta.class);
            // todo: find a better way to log warnings
            if (packMeta == null) {
                System.err.println("Resource pack does not contain PackMeta, won't be recognized by Minecraft");
            } else if (targetPackFormat != -1 && !packMeta.formats().isInRange(targetPackFormat)) {
                System.err.println("Resource pack format mismatch, the resource pack specifies formats "
                        + packMeta.formats() + " but the target format specified to the writer is " + targetPackFormat);
            }
            writeToJson(target, MetadataSerializer.INSTANCE, metadata, PACK_METADATA_FILE, targetPackFormat);
        }

        writeWithBasePathAndTargetPackFormat(target, resourcePack, "", targetPackFormat);

        // write from overlays
        Map<String, PackFormat> overlayFormats = new HashMap<>();
        {
            OverlaysMeta overlaysMeta = resourcePack.metadata().meta(OverlaysMeta.class);
            if (overlaysMeta != null) {
                for (OverlayEntry entry : overlaysMeta.entries()) {
                    overlayFormats.put(entry.directory(), entry.formats());
                }
            }
        }

        for (Overlay overlay : resourcePack.overlays()) {
            String dir = overlay.directory();
            PackFormat packFormat = overlayFormats.get(dir);
            int overlayTargetPackFormat = packFormat == null ? -1 : packFormat.min(); // todo: consider max pack format
            writeWithBasePathAndTargetPackFormat(target, overlay, OVERLAYS_FOLDER + '/' + dir + '/', overlayTargetPackFormat);
        }
    }

    private <T> void writeToJson(FileTreeWriter writer, JsonResourceSerializer<T> serializer, T object, String path, final int localTargetPackFormat) {
        try (JsonWriter jsonWriter = new JsonWriter(writer.openWriter(path))) {
            if (prettyPrinting) {
                jsonWriter.setIndent("  ");
            }
            serializer.serializeToJson(object, jsonWriter, localTargetPackFormat);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to " + path, e);
        }
    }

    static final class BuilderImpl implements Builder {
        private ZipEntryLifecycleHandler zipEntryLifecycleHandler = ZipEntryLifecycleHandler.DEFAULT;
        private boolean prettyPrinting;
        private int targetPackFormat = -1;

        @Override
        public @NotNull Builder zipEntryLifecycleHandler(final @NotNull ZipEntryLifecycleHandler zipEntryLifecycleHandler) {
            this.zipEntryLifecycleHandler = requireNonNull(zipEntryLifecycleHandler, "zipEntryLifecycleHandler");
            return this;
        }

        @Override
        public @NotNull Builder prettyPrinting(final boolean prettyPrinting) {
            this.prettyPrinting = prettyPrinting;
            return this;
        }

        @Override
        public @NotNull Builder targetPackFormat(final int targetPackFormat) {
            this.targetPackFormat = targetPackFormat;
            return this;
        }

        @Override
        public @NotNull MinecraftResourcePackWriter build() {
            return new MinecraftResourcePackWriterImpl(zipEntryLifecycleHandler, prettyPrinting, targetPackFormat);
        }
    }
}
