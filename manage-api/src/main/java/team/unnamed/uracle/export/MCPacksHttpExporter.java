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
package team.unnamed.uracle.export;

import org.jetbrains.annotations.NotNull;
import team.unnamed.uracle.serialize.TreeOutputStream;
import team.unnamed.uracle.pack.ResourcePackLocation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

/**
 * Fluent-style class for exporting resource
 * packs and upload it using HTTP servers like
 * <a href="https://mc-packs.net">MCPacks</a>,
 * that requires us to compute the SHA-1 hash and
 * upload the file
 */
public class MCPacksHttpExporter implements ResourceExporter {

    private static final String UPLOAD_URL = "https://mc-packs.net/";
    private static final String DOWNLOAD_URL_TEMPLATE = "https://download.mc-packs.net/pack/" +
            "%HASH%.zip";

    private static final String BOUNDARY = "UracleBoundary~~~~---" + UUID.randomUUID();
    private static final String LINE_FEED = "\r\n";

    private final URL url;

    public MCPacksHttpExporter() throws MalformedURLException {
        this.url = new URL(UPLOAD_URL);
    }

    @Override
    @NotNull
    public ResourcePackLocation export(ResourcePackWriter writer) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("User-Agent", "Uracle");
        connection.setRequestProperty("Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        String hashString;

        // write http request body
        try (OutputStream output = connection.getOutputStream()) {
            Streams.writeUTF(
                    output,
                    "--" + BOUNDARY + LINE_FEED
                            + "Content-Disposition: form-data; name=\"file\"; filename=\"resource-pack.zip\""
                            + LINE_FEED + "Content-Type: application/zip" + LINE_FEED + LINE_FEED
            );

            MessageDigest digest;

            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("Cannot find SHA-1 algorithm");
            }

            TreeOutputStream treeOutput = TreeOutputStream.forZip(
                    new ZipOutputStream(new DigestOutputStream(output, digest))
            );
            try {
                writer.write(treeOutput);
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
                        .append(getHexChar(part1))
                        .append(getHexChar(part2));
            }

            hashString = hashBuilder.toString();

            Streams.writeUTF(
                    output,
                    LINE_FEED + "--" + BOUNDARY + "--" + LINE_FEED
            );
        }

        // execute request and close, no response expected
        connection.getInputStream().close();

        return ResourcePackLocation.of(
                DOWNLOAD_URL_TEMPLATE.replace("%HASH%", hashString),
                hashString
        );
    }

    private char getHexChar(int c) {
        return "0123456789abcdef".charAt(c);
    }

}