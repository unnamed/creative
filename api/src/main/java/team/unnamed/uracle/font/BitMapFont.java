package team.unnamed.uracle.font;

import team.unnamed.uracle.ResourceLocation;

import java.util.List;

public class BitMapFont implements Font {

    /**
     * Resource location of the bitmap file, starting from
     * assets/&lt;namespace&gt;/textures
     */
    private final ResourceLocation file;

    /**
     * Optional. The height of the character, measured in pixels.
     * Can be negative. This tag is separate from the area used
     * in the source texture and just rescales the displayed
     * result. Default is 8
     */
    private final int height = 8;

    /**
     * The ascent of the character, measured in pixels. This value
     * adds a vertical shift to the displayed result.
     */
    private final int ascent = 0;

    /**
     * A list of strings containing the characters replaced by this
     * provider, as well as their order within the texture. All elements
     * must describe the same number of characters. The texture is split
     * into one equally sized row for each element of this list. Each
     * row is split into one equally sized character for each character
     * within one list element.
     */
    private final List<String> characters;

    public BitMapFont(ResourceLocation file, List<String> characters) {
        this.file = file;
        this.characters = characters;
    }

}
