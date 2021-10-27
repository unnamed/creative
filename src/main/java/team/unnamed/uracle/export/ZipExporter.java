package team.unnamed.uracle.export;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.io.ResourcePackWriter;
import team.unnamed.uracle.io.TreeOutputStream;
import team.unnamed.uracle.resourcepack.RemoteResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public class ZipExporter implements ResourceExporter {

    public static final String NAME = "zip";

    private final File target;

    public ZipExporter(File target) {
        this.target = target;
    }

    @Override
    public @Nullable RemoteResource export(ResourcePackWriter writer)
            throws IOException {

        if (!target.exists()) {
            File parent = target.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IOException("Cannot create parent" +
                        " folder for target ZIP");
            }
            if (!target.createNewFile()) {
                throw new IOException("Cannot create target ZIP file");
            }
        }

        // write resource pack
        try (TreeOutputStream output = TreeOutputStream.forZip(
                new ZipOutputStream(new FileOutputStream(target))
        )) {
            writer.write(output);
        }
        return null;
    }

}
