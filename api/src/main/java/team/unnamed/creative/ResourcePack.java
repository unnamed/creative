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
package team.unnamed.creative;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.metadata.overlays.OverlayEntry;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.overlay.Overlay;
import team.unnamed.creative.overlay.ResourceContainer;

import java.util.Collection;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Minecraft: Java Edition resource-pack,
 * it is only a memory representation and needs to be
 * serialized/compiled to a {@link BuiltResourcePack}
 * if you want to send it
 *
 * <p>This interface is very useful during the stage of
 * resource collection, where you need to add all the
 * resources inside the resource-pack</p>
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ResourcePack extends ResourceContainer {
    /**
     * Creates a new, empty resource-pack instance.
     *
     * @return A new resource-pack instance
     * @since 1.1.0
     */
    static @NotNull ResourcePack resourcePack() {
        return new ResourcePackImpl();
    }

    /**
     * Returns the resource-pack icon
     * in <bold>PNG</bold> format
     *
     * @return The resource-pack icon
     * @since 1.0.0
     */
    @Nullable Writable icon();

    /**
     * Sets the resource-pack icon
     * in <bold>PNG</bold> format
     *
     * @param icon The resource-pack icon
     * @since 1.0.0
     */
    void icon(@Nullable Writable icon);

    /**
     * Returns the metadata of this resource
     * pack, may be empty, but it is never null
     *
     * @return The resource-pack metadata
     * @since 1.0.0
     */
    @NotNull Metadata metadata();

    /**
     * Sets the metadata for this resource pack,
     * may be empty, but not null
     *
     * @param metadata The resource-pack metadata
     * @since 1.0.0
     */
    void metadata(final @NotNull Metadata metadata);

    //#region helpers
    default void editMetadata(final @NotNull Consumer<Metadata.Builder> editFunction) {
        requireNonNull(editFunction, "editFunction");
        Metadata.Builder builder = metadata().toBuilder();
        editFunction.accept(builder);
        metadata(builder.build());
    }

    default @Nullable PackMeta packMeta() {
        return metadata().meta(PackMeta.class);
    }

    default void packMeta(final @NotNull PackMeta packMeta) {
        requireNonNull(packMeta, "packMeta");
        editMetadata(metadata -> metadata.addPart(packMeta));
    }

    default void packMeta(final int format, final @NotNull String description) {
        packMeta(PackMeta.of(format, description));
    }

    /**
     * Sets the resource-pack metadata, which specifies the
     * pack supported format(s) and its description.
     *
     * @param format      The supported pack format(s).
     * @param description The pack description.
     * @since 1.1.0
     */
    default void packMeta(final @NotNull PackFormat format, final @NotNull Component description) {
        packMeta(PackMeta.of(format, description));
    }

    /**
     * Sets the resource-pack metadata, which specifies the
     * pack supported format and its description.
     *
     * @param format      The supported pack format.
     * @param description The pack description.
     * @since 1.1.0
     */
    default void packMeta(final int format, final @NotNull Component description) {
        packMeta(PackMeta.of(format, description));
    }

    /**
     * Returns the pack format range that this resource-pack
     * supports.
     *
     * @return The pack format range, null if not specified
     * @since 1.1.0
     */
    default @Nullable PackFormat formats() {
        PackMeta meta = packMeta();
        if (meta == null) {
            return null;
        } else {
            return meta.formats();
        }
    }

    default @Nullable Component description() {
        PackMeta meta = packMeta();
        if (meta == null) {
            return null;
        } else {
            return meta.description();
        }
    }

    default @Nullable LanguageMeta languageMeta() {
        return metadata().meta(LanguageMeta.class);
    }

    default void languageMeta(final @NotNull LanguageMeta languageMeta) {
        requireNonNull(languageMeta, "languageMeta");
        editMetadata(metadata -> metadata.addPart(languageMeta));
    }

    default @Nullable FilterMeta filterMeta() {
        return metadata().meta(FilterMeta.class);
    }

    default void filterMeta(final @NotNull FilterMeta filterMeta) {
        requireNonNull(filterMeta, "filterMeta");
        editMetadata(metadata -> metadata.addPart(filterMeta));
    }

    /**
     * Gets the overlay meta specified in the resource-pack
     * metadata.
     *
     * @return The overlays meta, null if not set.
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     * @since 1.1.0
     */
    default @Nullable OverlaysMeta overlaysMeta() {
        return metadata().meta(OverlaysMeta.class);
    }

    /**
     * Sets the overlay meta specified in the resource-pack
     * metadata.
     *
     * @param overlaysMeta The overlays meta.
     * @sincePackFormat 18
     * @since 1.1.0
     * @since 1.20.2
     */
    default void overlaysMeta(final @NotNull OverlaysMeta overlaysMeta) {
        requireNonNull(overlaysMeta, "overlaysMeta");
        editMetadata(metadata -> metadata.addPart(overlaysMeta));
    }
    //#endregion

    /**
     * Adds or sets an overlay to this resource-pack.
     *
     * @param overlay The overlay to add or set.
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     * @since 1.1.0
     */
    void overlay(final @NotNull Overlay overlay);

    /**
     * Gets the overlay with the given directory name.
     *
     * @param directory The overlay directory name.
     * @return The overlay, null if not found.
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     * @since 1.1.0
     */
    @Nullable Overlay overlay(final @NotNull @OverlayEntry.Directory String directory);

    /**
     * Gets all the overlays in this resource-pack.
     *
     * @return The overlays.
     * @sincePackFormat 18
     * @sinceMinecraft 1.20.2
     * @since 1.1.0
     */
    @NotNull Collection<Overlay> overlays();
}
