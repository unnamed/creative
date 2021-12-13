package team.unnamed.uracle;

/**
 * Represents one or more resource pack elements,
 * most resource pack components like languages,
 * fonts, sounds, textures and models are considered
 * elements
 */
public interface Element {

    /**
     * Writes this element information (that may contain
     * other elements) using a {@link TreeWriter}
     *
     * @param writer The target tree writer
     */
    void write(TreeWriter writer);

    /**
     * Represents a single resource pack element that
     * depends on another element, it requires an already
     * initialized {@link TreeWriter.Context} to be written
     */
    interface Part {

        /**
         * Writes this element information into the given
         * {@link TreeWriter.Context}
         *
         * @param context The target context, implementations
         *               MUST NOT close it
         */
        void write(TreeWriter.Context context);

    }

}
