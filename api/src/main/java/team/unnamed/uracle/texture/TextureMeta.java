package team.unnamed.uracle.texture;

import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;

public class TextureMeta implements Element.Part {

    /**
     * Causes the texture to blur when viewed
     * from close up
     */
    private final boolean blur;

    /**
     * Causes the texture to stretch instead of
     * tiling in cases where it otherwise would,
     * such as on the shadow
     */
    private final boolean clamp;

    /**
     * Custom mipmap values for the texture
     */
    private final int[] mipmaps;

    public TextureMeta(
            boolean blur,
            boolean clamp,
            int[] mipmaps
    ) {
        this.blur = blur;
        this.clamp = clamp;
        this.mipmaps = mipmaps;
    }

    public boolean isBlur() {
        return blur;
    }

    public boolean isClamp() {
        return clamp;
    }

    public int[] getMipmaps() {
        return mipmaps;
    }

    @Override
    public void write(TreeWriter.Context context) {
        context.writeBooleanField("blur", blur);
        context.writeBooleanField("clamp", clamp);
        context.writeKey("mipmaps");
        context.startArray();
        for (int i = 0; i < mipmaps.length; i++) {
            if (i != 0) {
                // write separator from previous
                // value and current value
                context.writeSeparator();
            }
            context.writeIntValue(mipmaps[i]);
        }
        context.endArray();
    }

}
