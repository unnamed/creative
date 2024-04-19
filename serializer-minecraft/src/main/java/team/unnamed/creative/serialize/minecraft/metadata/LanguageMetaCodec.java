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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.serialize.minecraft.GsonUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

final class LanguageMetaCodec implements MetadataPartCodec<LanguageMeta> {

    @Override
    public @NotNull Class<LanguageMeta> type() {
        return LanguageMeta.class;
    }

    @Override
    public @NotNull String name() {
        return "language";
    }

    @Override
    public @NotNull LanguageMeta read(final @NotNull JsonObject node) {
        Map<String, LanguageEntry> languages = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : node.entrySet()) {
            String code = entry.getKey();
            JsonObject languageEntryNode = entry.getValue().getAsJsonObject();

            LanguageEntry languageEntry = LanguageEntry.languageEntry()
                    .name(languageEntryNode.get("name").getAsString())
                    .region(languageEntryNode.get("region").getAsString())
                    .bidirectional(GsonUtil.getBoolean(languageEntryNode, "bidirectional", LanguageEntry.DEFAULT_BIDIRECTIONAL))
                    .build();
            languages.put(code, languageEntry);
        }
        return LanguageMeta.language(languages);
    }

    @Override
    public void write(final @NotNull JsonWriter writer, final @NotNull LanguageMeta language) throws IOException {
        writer.beginObject();
        for (Map.Entry<String, LanguageEntry> entry : language.languages().entrySet()) {
            LanguageEntry value = entry.getValue();
            writer.name(entry.getKey())
                    .beginObject()
                    .name("name").value(value.name())
                    .name("region").value(value.region());
            boolean bidirectional = value.bidirectional();
            if (bidirectional != LanguageEntry.DEFAULT_BIDIRECTIONAL) {
                // only write if not default
                writer.name("bidirectional").value(bidirectional);
            }
            writer.endObject();
        }
        writer.endObject();
    }

}
