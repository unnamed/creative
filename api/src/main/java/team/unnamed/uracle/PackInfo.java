package team.unnamed.uracle;

import java.util.Objects;

public class PackInfo implements Element.Part {

    private final int format;
    private final String description;

    public PackInfo(int format, String description) {
        this.format = format;
        this.description = description;
    }

    public int getFormat() {
        return format;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void write(TreeWriter.Context context) {
        context.writeIntField("format", format);
        context.writeStringField("description", description);
    }

    @Override
    public String toString() {
        return "PackInfo{" +
                "format=" + format +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackInfo packInfo = (PackInfo) o;
        return format == packInfo.format && description.equals(packInfo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, description);
    }

}
