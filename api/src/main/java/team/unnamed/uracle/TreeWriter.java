package team.unnamed.uracle;

public interface TreeWriter {

    Context enter(ResourceLocation location, String prefix, String suffix);

    Context enter(String location);

    interface Context extends AutoCloseable {

        void startObject();

        void writeKey(String key);

        void writeStringField(String key, String value);

        void writeBooleanField(String key, boolean value);

        void writeSeparator();

        void endObject();

        default void writePart(Element.Part value) {
            value.write(this);
        }

        @Override
        void close();

    }

}
