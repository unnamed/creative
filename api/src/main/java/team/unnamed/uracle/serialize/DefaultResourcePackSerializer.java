/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.serialize;

import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import team.unnamed.uracle.CubeFace;
import team.unnamed.uracle.ResourcePack;
import team.unnamed.uracle.Writable;
import team.unnamed.uracle.font.BitMapFont;
import team.unnamed.uracle.font.Font;
import team.unnamed.uracle.font.FontRegistry;
import team.unnamed.uracle.font.LegacyUnicodeFont;
import team.unnamed.uracle.font.TrueTypeFont;
import team.unnamed.uracle.lang.Language;
import team.unnamed.uracle.model.BlockModel;
import team.unnamed.uracle.model.BlockState;
import team.unnamed.uracle.model.Element;
import team.unnamed.uracle.model.ElementFace;
import team.unnamed.uracle.model.ElementRotation;
import team.unnamed.uracle.model.ItemModel;
import team.unnamed.uracle.model.Model;
import team.unnamed.uracle.model.ModelDisplay;
import team.unnamed.uracle.model.block.BlockTexture;
import team.unnamed.uracle.model.blockstate.StateCase;
import team.unnamed.uracle.model.blockstate.StateVariant;
import team.unnamed.uracle.model.item.ItemOverride;
import team.unnamed.uracle.model.item.ItemPredicate;
import team.unnamed.uracle.model.item.ItemTexture;
import team.unnamed.uracle.sound.Sound;
import team.unnamed.uracle.sound.SoundEvent;
import team.unnamed.uracle.sound.SoundRegistry;
import team.unnamed.uracle.metadata.texture.TextureMeta;
import team.unnamed.uracle.metadata.VillagerMeta;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DefaultResourcePackSerializer
        implements ResourcePackSerializer {

    private static final String ASSETS = "assets/";
    private static final String JSON_EXT = ".json";
    private static final String PNG_EXT = ".png";
    private static final String MCMETA_EXT = ".mcmeta";

    @Override
    public void serialize(
            ResourcePack resourcePack,
            FileTree tree
    ) {
        // write metadata

        // write icon
        Writable icon = resourcePack.icon();
        if (icon != null) {
            tree.write("pack.png", icon);
        }

        // write fonts
        for (Map.Entry<Key, FontRegistry> entry : resourcePack.fonts().entrySet()) {
            font(tree, entry.getKey(), entry.getValue());
        }

        // write languages
        for (Map.Entry<Key, Language> entry : resourcePack.languages().entrySet()) {
            language(tree, entry.getKey(), entry.getValue());
        }

        // write models
        for (Map.Entry<Key, Model> entry : resourcePack.models().entrySet()) {
            model(tree, entry.getKey(), entry.getValue());
        }
    }

    //#region Font Serialization Region
    private static void bitMapFont(AssetWriter writer, BitMapFont font) {
        writer
            .key("type").value("bitmap")
            .key("file").value(font.file());

        if (font.height() != BitMapFont.DEFAULT_HEIGHT) {
            // only write if height is not equal to the default height
            writer.key("height").value(font.height());
        }

        writer
            .key("ascent").value(font.ascent())
            .key("chars").startArray();

        for (String character : font.characters()) {
            writer.value(character);
        }

        writer.endArray();
    }

    private static void legacyUnicodeFont(AssetWriter writer, LegacyUnicodeFont font) {
        writer
            .key("sizes").value(font.sizes())
            .key("template").value(font.template());
    }

    private static void ttfFont(AssetWriter writer, TrueTypeFont font) {
        writer
            .key("file").value(font.file())
            .key("shift").startArray()
                .value(font.shift().x())
                .value(font.shift().y())
            .endArray()
            .key("size").value(font.size())
            .key("oversample").value(font.oversample())
            .key("skip").startArray();

        for (String toSkip : font.skip()) {
            writer.value(toSkip);
        }
        writer.endArray();
    }

    private static void font(FileTree tree, Key location, FontRegistry font) {

        // e.g.: assets/minecraft/font/default
        String path = ASSETS + location.namespace() + "/font/" + location.value();

        try (AssetWriter writer = tree.open(path)) {
            writer.startObject()
                .key("providers")
                .startArray();

            // write providers
            for (Font provider : font.providers()) {
                writer.startObject();
                if (provider instanceof BitMapFont) {
                    bitMapFont(writer, (BitMapFont) provider);
                } else if (provider instanceof LegacyUnicodeFont) {
                    legacyUnicodeFont(writer, (LegacyUnicodeFont) provider);
                } else {
                    ttfFont(writer, (TrueTypeFont) provider);
                }
                writer.endObject();
            }

            writer
                .endArray()
                .endObject();
        }
    }
    //#endregion

    //#region Language Serialization Region
    private static void language(FileTree tree, Key location, Language language) {
        // create the JSON file path (assets/<namespace>/lang/file.json)
        String path = ASSETS + location.namespace() + "/lang/" + location.value() + JSON_EXT;
        try (AssetWriter writer = tree.open(path)) {
            writer.startObject();
            for (Map.Entry<String, String> entry : language.translations().entrySet()) {
                writer.key(entry.getKey()).value(entry.getValue());
            }
            writer.endObject();
        }
    }
    //#endregion

    //#region Model Serialization Region
    private static void writeModelProperties(Model model, AssetWriter writer) {
        // parent
        writer.key("parent").value(model.parent());

        // display
        writer.key("display").startObject();
        for (Map.Entry<ModelDisplay.Type, ModelDisplay> entry : model.display().entrySet()) {
            ModelDisplay.Type type = entry.getKey();
            ModelDisplay display = entry.getValue();

            writer.key(type.name().toLowerCase(Locale.ROOT)).startObject()
                    .key("rotation").value(display.rotation())
                    .key("translation").value(display.translation())
                    .key("scale").value(display.scale())
                .endObject();
        }
        writer.endObject();

        // elements
        writer.key("elements").startArray();
        for (Element element : model.elements()) {
            ElementRotation rotation = element.rotation();

            writer
                .key("from").value(element.from())
                .key("to").value(element.to())
                .key("rotation").startObject()
                    .key("origin").value(rotation.origin())
                    .key("axis").value(rotation.axis().name().toLowerCase(Locale.ROOT))
                    .key("angle").value(rotation.angle());

            if (rotation.rescale()) {
                // only write if not equal to default value
                writer.key("rescale").value(rotation.rescale());
            }
            writer.endObject();

            if (!element.shade()) {
                // only write if not equal to default value
                writer.key("shade").value(element.shade());
            }

            // faces
            writer.key("faces").startObject();
            for (Map.Entry<CubeFace, ElementFace> entry : element.faces().entrySet()) {
                CubeFace type = entry.getKey();
                ElementFace face = entry.getValue();

                writer.key(type.name().toLowerCase(Locale.ROOT)).startObject();
                if (face.uv() != null) {
                    // this is a pure function but IDE still warns me, I have already checked it!!!!!!!!!!!!!!!!!!!!!!!!
                    writer.key("uv").value(face.uv());
                }
                writer.key("texture").value(face.texture());
                if (face.cullFace() != null) {
                    writer.key("cullface").value(face.cullFace().name().toLowerCase(Locale.ROOT));
                }
                if (face.rotation() != 0) {
                    writer.key("rotation").value(face.rotation());
                }
                if (face.tintIndex() != null) {
                    writer.key("tintindex").value(face.tintIndex());
                }
                writer.endObject();
            }
            writer.endObject();
        }
        writer.endArray();
    }

    private static void writeItemModel(ItemModel model, AssetWriter writer) {
        writer.startObject();
        writeModelProperties(model, writer);

        // textures
        ItemTexture textures = model.textures();
        writer.key("textures").startObject();
        // ah yes, don't repeat yourself
        if (textures.particle() != null) {
            writer.key("particle").value(textures.particle());
        }
        for (int i = 0; i < textures.layers().size(); i++) {
            writer.key("layer" + i).value(textures.layers().get(i));
        }
        for (Map.Entry<String, Key> variable : textures.variables().entrySet()) {
            writer.key(variable.getKey()).value(variable.getValue());
        }
        writer.endObject();

        if (model.guiLight() != ItemModel.GuiLight.SIDE) {
            // only write if not default
            writer.key("gui_light").value(model.guiLight().name().toLowerCase(Locale.ROOT));
        }

        // overrides
        writer.key("overrides").startArray();
        for (ItemOverride override : model.overrides()) {
            writer.startObject()
                .key("predicate").startObject();
            for (ItemPredicate predicate : override.predicate()) {
                writer.key(predicate.name()).value(predicate.value());
            }
            writer.endObject()
                .key("model").value(override.model())
                .endObject();
        }
        writer.endArray().endObject();
    }

    private static void writeBlockModel(BlockModel model, AssetWriter writer) {
        writer.startObject();
        writeModelProperties(model, writer);
        if (!model.ambientOcclusion()) {
            // only write if not default value
            writer.key("ambientocclusion").value(model.ambientOcclusion());
        }

        // textures
        BlockTexture textures = model.textures();
        writer.key("textures").startObject();
        if (textures.particle() != null) {
            writer.key("particle").value(textures.particle());
        }
        for (Map.Entry<String, Key> variable : textures.variables().entrySet()) {
            writer.key(variable.getKey()).value(variable.getValue());
        }
        writer.endObject().endObject();
    }

    private static void model(FileTree tree, Key location, Model model) {
        String path = ASSETS + location.namespace() + "/models" + location.value() + JSON_EXT;
        try (AssetWriter writer = tree.open(path)) {
            if (model instanceof ItemModel) {
                writeItemModel((ItemModel) model, writer);
            } else if (model instanceof BlockModel) {
                writeBlockModel((BlockModel) model, writer);
            } else {
                throw new IllegalArgumentException("Invalid model type");
            }
        }
    }
    //#endregion

    //#region Block State Serialization Region
    private static void writeVariant(StateVariant variant, AssetWriter writer, boolean writeWeight) {
        writer
            .startObject()
            .key("model").value(variant.model())
            .key("x").value(variant.x())
            .key("y").value(variant.y())
            .key("uvlock").value(variant.uvLock());
        if (writeWeight) {
            writer.key("weight").value(variant.weight());
        }
        writer.endObject();
    }

    private static void writeVariant(List<StateVariant> variant, AssetWriter writer) {
        if (variant.size() == 1) {
            // single variant, write as an object
            // without the weight
            writeVariant(variant.get(0), writer, false);
        } else {
            // multiple variants, write everything
            writer.startArray();
            for (StateVariant v : variant) {
                writeVariant(v, writer, true);
            }
            writer.endArray();
        }
    }

    private static void blockState(FileTree tree, Key location, BlockState state) {
        String path = ASSETS + location.namespace() + "/blockstates/" + location.value() + JSON_EXT;
        try (AssetWriter writer = tree.open(path)) {
            writer.startObject();
            Map<String, List<StateVariant>> variants = state.variants();
            List<StateCase> multipart = state.multipart();

            // write "variants" part if not empty
            if (!variants.isEmpty()) {
                writer.key("variants").startObject();
                for (Map.Entry<String, List<StateVariant>> entry : variants.entrySet()) {
                    writer.key(entry.getKey());
                    writeVariant(entry.getValue(), writer);
                }
                writer.endObject();
            }

            // write "multipart" part if not empty
            if (!multipart.isEmpty()) {
                writer.key("multipart").startArray();
                for (StateCase stateCase : multipart) {
                    writer.startObject()
                        .key("when")
                        .startObject();

                    StateCase.When when = stateCase.when();
                    List<StateCase.Filter> filters = when.or();

                    if (!filters.isEmpty()) {
                        // write "OR" cases if not empty
                        writer.key("or").startArray();
                        for (StateCase.Filter filter : filters) {
                            writer.startObject();
                            for (Map.Entry<String, String> condition : filter.state().entrySet()) {
                                writer.key(condition.getKey()).value(condition.getValue());
                            }
                            writer.endObject();
                        }
                        writer.endArray();
                    }

                    for (Map.Entry<String, String> condition : when.state().entrySet()) {
                        writer.key(condition.getKey()).value(condition.getValue());
                    }
                    writer.endObject();

                    writer.key("apply");
                    writeVariant(stateCase.apply(), writer);
                    writer.endObject();
                }
                writer.endArray();
            }

            writer.endObject();
        }
    }
    //#endregion

    //#region Sound Serialization Region
    private static void sounds(
            FileTree tree,
            @Subst(Key.MINECRAFT_NAMESPACE) String namespace,
            SoundRegistry registry
    ) {
        // let Key validate the namespace
        Key.key(namespace, "dummy");

        String path = ASSETS + namespace + "/sounds" + JSON_EXT;

        try (AssetWriter writer = tree.open(path)) {
            writer.startObject();
            for (Map.Entry<String, SoundEvent> entry : registry.sounds().entrySet()) {
                SoundEvent event = entry.getValue();

                writer.key(entry.getKey()).startObject();

                if (event.replace()) {
                    // only write if not default (false)
                    writer.key("replace").value(event.replace());
                }

                if (event.subtitle() != null) {
                    writer.key("subtitle").value(event.subtitle());
                }
                if (!event.sounds().isEmpty()) {
                    writer.key("sounds").startArray();
                    for (Sound sound : event.sounds()) {
                        // in order to make some optimizations, we
                        // have to do this
                        if (sound.allDefault()) {
                            // everything is default, just write the name
                            writer.value(sound.name());
                        } else {
                            writer.startObject()
                                .key("name").value(sound.name());
                            if (sound.volume() != Sound.DEFAULT_VOLUME) {
                                writer.key("volume").value(sound.volume());
                            }
                            if (sound.pitch() != Sound.DEFAULT_PITCH) {
                                writer.key("pitch").value(sound.pitch());
                            }
                            if (sound.weight() != Sound.DEFAULT_WEIGHT) {
                                writer.key("weight").value(sound.weight());
                            }
                            if (sound.stream() != Sound.DEFAULT_STREAM) {
                                writer.key("stream").value(sound.stream());
                            }
                            if (sound.attenuationDistance() != Sound.DEFAULT_ATTENUATION_DISTANCE) {
                                writer.key("attenuation_distance").value(sound.attenuationDistance());
                            }
                            if (sound.preload() != Sound.DEFAULT_PRELOAD) {
                                writer.key("preload").value(sound.preload());
                            }
                            if (sound.type() != Sound.DEFAULT_TYPE) {
                                writer.key("type").value(sound.type().name().toLowerCase(Locale.ROOT));
                            }
                            writer.endObject();
                        }
                    }
                    writer.endArray();
                }
                writer.endObject();
            }
            writer.endObject();
        }
    }
    //#endregion

    //#region Texture Serialization Region
    private static void texture(FileTree tree, Key location, Texture texture) {

        String path = ASSETS + location.namespace() + "/textures/" + location.value() + PNG_EXT;

        // write the actual texture PNG image
        try (AssetWriter writer = tree.open(path)) {
            texture.data().write(writer);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write texture", e);
        }

        TextureMeta meta = texture.meta();
        VillagerMeta villager = texture.villager();

        boolean hasMeta = meta != null;
        boolean hasVillager = villager != null;

        if (!hasMeta && !hasVillager) {
            // no metadata to write
            return;
        }

        try (AssetWriter writer = tree.open(path + MCMETA_EXT)) {
            writer.startObject();

            if (hasMeta) {
                writer.key("texture").startObject()
                    .key("blur").value(meta.blur())
                    .key("clamp").value(meta.clamp())
                    .key("mipmaps").startArray();
                for (int mipmap : meta.mipmaps()) {
                    writer.value(mipmap);
                }
                writer.endArray().endObject();
            }

            if (hasVillager) {
                VillagerMeta.Hat hat = villager.hat();
                writer.key("villager").startObject();
                if (hat != null) {
                    writer.key("hat").value(hat.name().toLowerCase(Locale.ROOT));
                }
                writer.endObject();
            }

            writer.endObject();
        }
    }
    //#endregion

}
