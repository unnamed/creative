package team.unnamed.uracle.generate;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.generate.exporter.ResourceExporter;
import team.unnamed.uracle.resourcepack.ResourcePack;
import team.unnamed.uracle.resourcepack.ResourcePackProvider;
import team.unnamed.uracle.resourcepack.UrlAndHash;

import java.io.IOException;

public class StandardExportManager implements ExportManager {

    private final ResourcePackProvider packInfoSupplier;
    private final TreeWriteable contentWriter;
    private final ResourceExporter exporter;

    private ResourcePack resourcePack;

    public StandardExportManager(
            ResourcePackProvider packInfoSupplier,
            TreeWriteable contentWriter,
            ResourceExporter exporter
    ) {
        this.packInfoSupplier = packInfoSupplier;
        this.contentWriter = contentWriter;
        this.exporter = exporter;
    }

    @Override
    public ResourceExporter getExporter() {
        return exporter;
    }

    @Override
    public @Nullable ResourcePack getResourcePack() {
        return resourcePack;
    }

    @Override
    public void exportAndUpdate() throws IOException {
        // export
        UrlAndHash location = exporter.export(contentWriter);

        if (location == null) {
            // not exported to a remote location, make
            // resourcePack invalid (null)
            resourcePack = null;
            return;
        }

        resourcePack = packInfoSupplier.of(location);
    }

}
