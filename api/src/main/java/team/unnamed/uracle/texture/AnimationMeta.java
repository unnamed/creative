package team.unnamed.uracle.texture;

import java.util.List;

public class AnimationMeta {

    /**
     * If true, Minecraft generates additional frames
     * between frames with a frame time greater than 1
     * between them
     */
    private final boolean interpolate;

    /**
     * The width of the tile, as a direct ratio rather than
     * in pixels. Can be used by resource packs to have frames
     * that are not perfect squares.
     */
    private final int width;

    /**
     * The height of the tile as a ratio rather than in pixels.
     * Can be used by resource packs to have frames that are not
     * perfect squares.
     */
    private final int height;

    /**
     * Sets the default time for each frame in increments of one
     * game tick
     */
    private final int frameTime;

    /**
     * Contains a list of frames. Defaults to displaying all the
     * frames from top to bottom.
     */
    private final List<Frame> frames;

    public AnimationMeta(boolean interpolate, int width, int height, int frameTime, List<Frame> frames) {
        this.interpolate = interpolate;
        this.width = width;
        this.height = height;
        this.frameTime = frameTime;
        this.frames = frames;
    }


    public static class Frame {

        /**
         * A number corresponding to position of a frame from the
         * top, with the top frame being 0.
         */
        private final int index;

        /**
         * The time in ticks to show this frame, overriding
         * animation "frameTime" above.
         */
        private final int frameTime;

        public Frame(int index, int frameTime) {
            this.index = index;
            this.frameTime = frameTime;
        }
    }

}