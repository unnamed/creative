package team.unnamed.uracle.font;

import net.kyori.adventure.key.Key;

public class LegacyUnicodeFont implements Font {

    /**
     * The resource location inside assets/&lt;namespace&gt;/font describing a
     * binary file describing the horizontal start and end positions for
     * each character from 0 to 15. The file extension of the target file
     * should be .bin
     */
    private final Key sizes;

    /**
     * The resource location inside assets/&lt;namespace&gt;/textures that leads to
     * the texture files that should be used for this provider. The game replaces
     * %s from the value of this tag with the first two characters of the hex code
     * of the replaced characters, so a single provider of this type can point
     * into multiple texture files.
     */
    private final Key template;

    public LegacyUnicodeFont(Key sizes, Key template) {
        this.sizes = sizes;
        this.template = template;
    }

    public Key getSizes() {
        return sizes;
    }

    public Key getTemplate() {
        return template;
    }

}
