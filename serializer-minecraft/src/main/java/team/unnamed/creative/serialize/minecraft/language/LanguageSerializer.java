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
package team.unnamed.creative.serialize.minecraft.language;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LanguageSerializer implements JsonResourceSerializer<Language>, JsonResourceDeserializer<Language> {

    public static final LanguageSerializer INSTANCE;
    public static final ResourceCategory<Language> CATEGORY;


    static {
        INSTANCE = new LanguageSerializer();
        CATEGORY = new ResourceCategory<>(
                "lang",
                ".json",
                ResourceContainer::language,
                ResourceContainer::languages,
                INSTANCE
        );
    }

    @Override
    public void serializeToJson(Language language, JsonWriter writer) throws IOException {
        // {
        //   "key.1": "value 1",
        //   "key.2": "value 2"
        // }
        writer.beginObject();
        for (Map.Entry<String, String> entry : language.translations().entrySet()) {
            writer.name(entry.getKey()).value(entry.getValue());
        }
        writer.endObject();
    }

    @Override
    public Language deserializeFromJson(JsonElement node, Key key) {
        JsonObject objectNode = node.getAsJsonObject();
        Map<String, String> translations = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> translationEntry : objectNode.entrySet()) {
            String translationKey = translationEntry.getKey();
            String translationValue = translationEntry.getValue().getAsString();

            translations.put(translationKey, translationValue);
        }

        return Language.language(key, translations);
    }

}