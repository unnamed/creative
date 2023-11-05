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
package team.unnamed.creative.model;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Minecraft block/item model.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface Model extends ResourcePackPart, Keyed, Examinable {
    @Contract("-> new")
    static @NotNull Builder model() {
        return new ModelImpl.BuilderImpl();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("-> new")
    static @NotNull Builder builder() {
        return model();
    }

    /**
     * A {@link Model} can be set to extend this key to use
     * a model that is created out of the specified icon
     *
     * @since 1.0.0
     */
    Key ITEM_GENERATED = Key.key("item/generated");

    Key ITEM_HANDHELD = Key.key("item/handheld");

    /**
     * A {@link Model} can be set to extend this key to load
     * a model from an entity file. As you cannot specify the entity,
     * this does not work for all items (only for chests, ender chests,
     * mob heads, shields, banners and tridents)
     *
     * @since 1.0.0
     */
    Key BUILT_IN_ENTITY = Key.key("builtin/entity");

    Key BUILT_IN_GENERATED = Key.key("builtin/generated");

    boolean DEFAULT_AMBIENT_OCCLUSION = true;

    @Override
    @NotNull Key key();

    /**
     * Returns the parent model of this
     * model object
     *
     * @return The parent model location
     * @since 1.0.0
     */
    @Nullable Key parent();

    /**
     * Returns the boolean that determines whether
     * to use ambient occlusion or not for this block
     *
     * @return True to use ambient occlusion
     * @since 1.0.0
     */
    boolean ambientOcclusion();

    /**
     * Returns a map of the different places
     * where the model can be displayed
     *
     * @return An unmodifiable map of displays
     * @since 1.0.0
     */
    @NotNull @Unmodifiable Map<ItemTransform.Type, ItemTransform> display();

    /**
     * Returns this model textures
     *
     * @return This model textures
     * @since 1.0.0
     */
    @NotNull ModelTextures textures();

    /**
     * Returns the way how the item is rendered, can be "side"
     * or "front"
     *
     * <p>If set to "side", the model is rendered like a block.
     * If set to "front", model is shaded like a flat item</p>
     *
     * @return Value that determines how to render this item
     * @since 1.0.0
     */
    @Nullable GuiLight guiLight();

    /**
     * Returns an unmodifiable list containing all
     * the model elements, which can only have cubic
     * forms
     *
     * @return The model elements
     * @since 1.0.0
     */
    @NotNull @Unmodifiable List<Element> elements();

    /**
     * Returns a list of item overrides, item overrides determine cases
     * in which a different model should be used based on item tags
     *
     * <p>All cases are evaluated in order from top to bottom and
     * last predicate that matches overrides. However, overrides are ignored
     * if it has been already overridden once, for example this avoids recursion
     * on overriding to the same model</p>
     *
     * @return This item model overrides
     * @since 1.0.0
     */
    @NotNull List<ItemOverride> overrides();

    /**
     * Converts this model object to a {@link Builder}
     * with all the model-properties in this object.
     *
     * @return The builder
     * @since 1.3.0
     */
    @Contract("-> new")
    @NotNull default Builder toBuilder() {
        return Model.model().key(key())
                .parent(parent())
                .ambientOcclusion(ambientOcclusion())
                .display(display())
                .textures(textures())
                .guiLight(guiLight())
                .elements(elements())
                .overrides(overrides());
    }

    /**
     * Adds this model to the given resource container.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.model(this);
    }

    /**
     * Enum of possible "gui_light" property
     * values
     *
     * @since 1.0.0
     */
    enum GuiLight {
        FRONT,
        SIDE
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        @Contract("_ -> this")
        @NotNull Builder parent(final @Nullable Key parent);

        @Contract("_ -> this")
        @NotNull Builder ambientOcclusion(final boolean ambientOcclusion);

        @Contract("_ -> this")
        @NotNull Builder display(final @NotNull Map<ItemTransform.Type, ItemTransform> display);

        @Contract("_ -> this")
        @NotNull Builder textures(final @NotNull ModelTextures textures);

        @Contract("_ -> this")
        @NotNull Builder guiLight(final @Nullable GuiLight guiLight);

        @Contract("_ -> this")
        @NotNull Builder elements(final @NotNull List<Element> elements);

        @Contract("_ -> this")
        default @NotNull Builder elements(final @NotNull Element @NotNull ... elements) {
            requireNonNull(elements, "elements");
            return elements(Arrays.asList(elements));
        }

        @Contract("_ -> this")
        @NotNull Builder addElement(final @NotNull Element element);

        @Contract("_ -> this")
        @NotNull Builder overrides(final @NotNull List<ItemOverride> overrides);

        @Contract("_ -> this")
        default @NotNull Builder overrides(final @NotNull ItemOverride @NotNull ... overrides) {
            requireNonNull(overrides, "overrides");
            return overrides(Arrays.asList(overrides));
        }

        @Contract("_ -> this")
        @NotNull Builder addOverride(final @NotNull ItemOverride override);

        @Contract("-> new")
        @NotNull Model build();
    }
}
