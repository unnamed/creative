package team.unnamed.uracle.texture;

public class TextureMeta {

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

}
