package team.unnamed.uracle.font;

import team.unnamed.uracle.ResourceLocation;
import team.unnamed.uracle.Vector2Float;

import java.util.List;

public class TrueTypeFont implements Font {

    /**
     * The resource location of the TrueType font file within
     * assets/&lt;namespace&gt;/font
     */
    private final ResourceLocation file;

    /**
     * The distance by which the characters of this provider are
     * shifted. X: Left shift, Y: Downwards shift
     */
    private final Vector2Float shift;

    /**
     * Font size to render at
     */
    private final float size;

    /**
     * Resolution to render at
     */
    private final float oversample;

    /**
     * List of strings to exclude
     */
    private final List<String> skip;

    public TrueTypeFont(
            ResourceLocation file,
            Vector2Float shift,
            float size,
            float oversample,
            List<String> skip
    ) {
        this.file = file;
        this.shift = shift;
        this.size = size;
        this.oversample = oversample;
        this.skip = skip;
    }

}
