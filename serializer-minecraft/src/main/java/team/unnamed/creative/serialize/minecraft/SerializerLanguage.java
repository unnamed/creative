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
package team.unnamed.creative.serialize.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.lang.Language;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class SerializerLanguage extends LazyTypeAdapter<Language> {

    static final SerializerLanguage INSTANCE = new SerializerLanguage();

    @Override
    public void write(JsonWriter writer, Language language) throws IOException {
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

    public Language readFromTree(JsonElement node, Key key) throws IOException {
        JsonObject objectNode = node.getAsJsonObject();
        Map<String, String> translations = new HashMap<>();

        for (Map.Entry<String, JsonElement> translationEntry : objectNode.entrySet()) {
            String translationKey = translationEntry.getKey();
            String translationValue = translationEntry.getValue().getAsString();

            translations.put(translationKey, translationValue);
        }

        return Language.of(key, translations);
    }

    @Override
    public Language readFromTree(JsonElement element) throws IOException {
        throw new UnsupportedOperationException("We require a key!");
    }

}