package team.unnamed.uracle.resourcepack;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.io.Writeable;

import java.util.Objects;

/**
 * Resource pack information, contains extra information
 * that can be omitted by a resource pack writer
 */
public class PackMeta {

    private final int format;
    private final String description;
    @Nullable
    private final Writeable icon;

    /**
     * Constructs a new resource pack info object
     * @param format The pack format, depends on minecraft version
     * @param description The resource-pack description
     * @param icon The resource-pack icon, it won't be written if
     *             it's null
     */
    public PackMeta(
            int format,
            String description,
            @Nullable Writeable icon
    ) {
        this.format = format;
        this.description = description;
        this.icon = icon;
    }

    public int getFormat() {
        return format;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public Writeable getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "ResourcePackInfo{" +
                "format=" + format +
                ", description='" + description + '\'' +
                ", icon=" + icon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackMeta that = (PackMeta) o;
        return format == that.format
                && description.equals(that.description)
                && Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, description, icon);
    }

}