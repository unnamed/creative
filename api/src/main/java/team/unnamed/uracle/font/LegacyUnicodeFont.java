package team.unnamed.uracle.font;

import team.unnamed.uracle.ResourceLocation;

public class LegacyUnicodeFont implements Font {

    /**
     * The resource location inside assets/&lt;namespace&gt;/font describing a
     * binary file describing the horizontal start and end positions for
     * each character from 0 to 15. The file extension of the target file
     * should be .bin
     */
    private final ResourceLocation sizes;

    /**
     * The resource location inside assets/&lt;namespace&gt;/textures that leads to
     * the texture files that should be used for this provider. The game replaces
     * %s from the value of this tag with the first two characters of the hex code
     * of the replaced characters, so a single provider of this type can point
     * into multiple texture files.
     */
    private final ResourceLocation template;

    public LegacyUnicodeFont(ResourceLocation sizes, ResourceLocation template) {
        this.sizes = sizes;
        this.template = template;
    }

}
