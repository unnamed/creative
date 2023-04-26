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

import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.central.export.ResourcePackExporter;
import team.unnamed.creative.central.export.ResourcePackLocation;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

/**
 * Fluent-style class for exporting resource
 * packs and upload it using HTTP servers like
 * <a href="https://mc-packs.net">MCPacks</a>,
 * that requires us to compute the SHA-1 hash and
 * upload the file
 */
public class MCPacksHttpExporter implements ResourcePackExporter {

    private static final String USER_AGENT = "creative-central";
    private static final String FILE_NAME = "creative-resource-pack.zip";

    private static final String UPLOAD_URL = "https://mc-packs.net/";
    private static final String DOWNLOAD_URL_TEMPLATE = "https://download.mc-packs.net/pack/%HASH%.zip";

    private static final String BOUNDARY = "UnnamedBoundary";
    private static final String LINE_FEED = "\r\n";

    private final Logger logger;
    private final URL url;

    public MCPacksHttpExporter(Logger logger) {
        this.logger = logger;
        try {
            this.url = new URL(UPLOAD_URL);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Malformed url constant: " + UPLOAD_URL, e);
        }
    }

    @Override
    public ResourcePackLocation export(ResourcePack resourcePack) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(10000);

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        String hashString;

        // write http request body
        try (OutputStream output = connection.getOutputStream()) {
            output.write((
                    "--" + BOUNDARY + LINE_FEED
                    + "Content-Disposition: form-data; name=\"file\"; filename=\"" + FILE_NAME + "\"" + LINE_FEED
                    + "Content-Type: application/zip" + LINE_FEED + LINE_FEED
            ).getBytes(StandardCharsets.UTF_8));

            MessageDigest digest;

            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("Cannot find SHA-1 algorithm");
            }


            FileTreeWriter treeOutput = FileTreeWriter.zip(new ZipOutputStream(new DigestOutputStream(output, digest)));
            try {
                MinecraftResourcePackWriter.minecraft().write(treeOutput, resourcePack);
            } finally {
                treeOutput.finish();
            }

            byte[] hash = digest.digest();
            int len = hash.length;
            StringBuilder hashBuilder = new StringBuilder(len * 2);
            for (byte b : hash) {
                int part1 = (b >> 4) & 0xF;
                int part2 = b & 0xF;
                hashBuilder
                        .append(hex(part1))
                        .append(hex(part2));
            }

            hashString = hashBuilder.toString();

            output.write((LINE_FEED + "--" + BOUNDARY + "--" + LINE_FEED).getBytes(StandardCharsets.UTF_8));
        }

        // execute request and close, no response expected
        connection.getInputStream().close();

        String url = DOWNLOAD_URL_TEMPLATE.replace("%HASH%", hashString);
        logger.info("Uploaded resource-pack to: " + url);

        return ResourcePackLocation.of(url, hashString);
    }

    private char hex(int c) {
        return "0123456789abcdef".charAt(c);
    }

}