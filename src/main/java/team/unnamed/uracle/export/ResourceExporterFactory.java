package team.unnamed.uracle.export;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.UraclePlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Factory for creating {@link ResourceExporter}
 * instances
 */
public final class ResourceExporterFactory {

    private ResourceExporterFactory() {
    }

    /**
     * Gets a {@link ResourceExporter} from the given {@code string},
     * it can be a name or a name with parameters (name:param1:param2:...)
     * @return The created resource exporter, or null, if not found
     * @throws IOException If exporter instantiation fails
     */
    @Nullable
    public static ResourceExporter of(String string) throws IOException {

        //#region non-parameterized exporters
        switch (string.toLowerCase()) {

            case MCPacksHttpExporter.NAME:
                return new MCPacksHttpExporter();

            case ZipExporter.NAME:
                return new ZipExporter(new File(
                        getFolder(),
                        "resource-pack.zip"
                ));

            case FolderExporter.NAME:
                return new FolderExporter(new File(
                        getFolder(),
                        "resource-pack"
                ));
        }
        //#endregion

        //#region Parameterized exporters
        // polymath
        if (string.startsWith(PolymathHttpExporter.NAME)) {
            String[] args = string.substring(PolymathHttpExporter.NAME.length())
                    .split(":");

            if (args.length != 2) {
                // two arguments are required
                throw new IOException("Invalid polymath parameters: '" + string + "'");
            }

            return new PolymathHttpExporter(
                    new URL(args[0]),
                    args[1]
            );
        }
        //#endregion

        // not found
        return null;
    }

    private static File getFolder() {
        return UraclePlugin.getPlugin(UraclePlugin.class).getDataFolder();
    }

}
