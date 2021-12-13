package team.unnamed.uracle.font;

import team.unnamed.uracle.ResourceLocation;
import team.unnamed.uracle.Vector2Float;

import java.util.List;

public interface FontProvider {

    class BitMap implements FontProvider {

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

        public BitMap(ResourceLocation file, List<String> characters) {
            this.file = file;
            this.characters = characters;
        }

    }

    class LegacyUnicodeFontProvider implements FontProvider {

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

        public LegacyUnicodeFontProvider(ResourceLocation sizes, ResourceLocation template) {
            this.sizes = sizes;
            this.template = template;
        }
    }

    class TrueTypeFontProvider implements FontProvider {

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

        public TrueTypeFontProvider(
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

}
