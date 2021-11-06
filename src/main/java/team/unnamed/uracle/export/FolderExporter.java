package team.unnamed.uracle.export;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.io.ResourcePackWriter;
import team.unnamed.uracle.io.TreeOutputStream;
import team.unnamed.uracle.resourcepack.UrlAndHash;

import java.io.File;
import java.io.IOException;

public class FolderExporter implements ResourceExporter {

    public static final String NAME = "folder";

    private final File root;

    public FolderExporter(File root) {
        this.root = root;
    }

    @Override
    public @Nullable UrlAndHash export(ResourcePackWriter writer) throws IOException {
        // write resource pack
        try (TreeOutputStream output = TreeOutputStream.forFolder(root)) {
            writer.write(output);
        }
        return null;
    }

}
