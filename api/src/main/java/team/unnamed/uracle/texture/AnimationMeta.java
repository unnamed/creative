package team.unnamed.uracle.texture;

import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Block and item textures support animation by placing each
 * additional frame below the past. The animation is then controlled
 * using this {@link AnimationMeta} type
 *
 * @since 1.0.0
 */
public class AnimationMeta implements Element.Part {

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

    private AnimationMeta(
            boolean interpolate,
            int width,
            int height,
            int frameTime,
            List<Frame> frames
    ) {
        requireNonNull(frames, "frames");
        this.interpolate = interpolate;
        this.width = width;
        this.height = height;
        this.frameTime = frameTime;
        // create a copy to avoid modifications
        this.frames = new ArrayList<>(frames);
    }

    /**
     * Determines if the animation is interpolated,
     * in that case, Minecraft will generate frames
     * between our frames
     *
     * @return True if animation is interpolated
     */
    public boolean isInterpolate() {
        return interpolate;
    }

    /**
     * Returns the width of the tile, as a ratio
     *
     * @return The tile width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the tile, as a ratio
     *
     * @return The tile height
     */
    public int getHeight() {
        return height;
    }

    /**
     * The global frame time for all frames, it can
     * be overwritten by specific {@link Frame} elements
     *
     * @return The fallback frame-time for frames that
     * don't specify one
     */
    public int getFrameTime() {
        return frameTime;
    }

    /**
     * Returns an unmodifiable list of the animation
     * {@link Frame} frames
     *
     * @return The animation frames
     */
    public List<Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }

    @Override
    public void write(TreeWriter.Context context) {
        context.writeBooleanField("interpolate", interpolate);
        context.writeIntField("width", width);
        context.writeIntField("height", height);
        context.writeIntField("frameTime", frameTime);
        context.writeKey("frames");
        context.startArray();
        {
            Iterator<Frame> iterator = frames.iterator();
            while (iterator.hasNext()) {
                Frame frame = iterator.next();
                int index = frame.getIndex();
                int time = frame.getFrameTime();

                if (frameTime == time) {
                    // same as default frameTime, we can
                    // skip it
                    context.writeIntValue(index);
                } else {
                    // specific frameTime, write as
                    // an object
                    context.startObject();
                    context.writeIntField("index", index);
                    context.writeIntField("time", time);
                    context.endObject();
                }

                if (iterator.hasNext()) {
                    // separate from next frame
                    context.writeSeparator();
                }
            }
        }
        context.endArray();
    }

    @Override
    public String toString() {
        return "AnimationMeta{" +
                "interpolate=" + interpolate +
                ", width=" + width +
                ", height=" + height +
                ", frameTime=" + frameTime +
                ", frames=" + frames +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationMeta that = (AnimationMeta) o;
        return interpolate == that.interpolate
                && width == that.width
                && height == that.height
                && frameTime == that.frameTime
                && frames.equals(that.frames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interpolate, width, height, frameTime, frames);
    }

    /**
     * Creates a new {@link AnimationMeta} instance
     * using the provided values
     *
     * @param interpolate If frames must be interpolated
     * @param width Frame-texture width ratio
     * @param height Frame-texture height ratio
     * @param frameTime Default frame time
     * @param frames Animation frames
     * @return A new {@link AnimationMeta} instance
     */
    public static AnimationMeta of(
            boolean interpolate,
            int width,
            int height,
            int frameTime,
            List<Frame> frames
    ) {
        return new AnimationMeta(
                interpolate,
                width,
                height,
                frameTime,
                frames
        );
    }

    /**
     * Returns a new instance of our builder implementation
     * used to build {@link AnimationMeta} instances
     *
     * @return A new fresh builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Represents a texture animation frame, includes the
     * index of the frame and its specific but optional
     * frame-time, it will use {@link AnimationMeta#getFrameTime()}
     * if it is not specified
     *
     * @since 1.0.0
     */
    public static class Frame {

        /**
         * A number corresponding to position of a frame from the
         * top, with the top frame being 0.
         */
        private final int index;

        /**
         * The time in ticks to show this frame, uses
         * {@link AnimationMeta#getFrameTime()} when it is
         * not specified
         */
        private final int frameTime;

        /**
         * Instantiated via {@link AnimationMeta.Builder#frame},
         * not by the end-user
         */
        private Frame(int index, int frameTime) {
            this.index = index;
            this.frameTime = frameTime;
        }

        /**
         * Returns the animation frame index
         *
         * @return The frame index
         */
        public int getIndex() {
            return index;
        }

        /**
         * Returns the animation frame time
         *
         * @return The frame time in ticks
         */
        public int getFrameTime() {
            return frameTime;
        }

        @Override
        public String toString() {
            return "Frame{" +
                    "index=" + index +
                    ", frameTime=" + frameTime +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Frame frame = (Frame) o;
            return index == frame.index && frameTime == frame.frameTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, frameTime);
        }

        /**
         * Creates a new frame using the given index and
         * frameTime
         */
        public static Frame of(int index, int frameTime) {
            return new Frame(index, frameTime);
        }

    }

    /**
     * Mutable and fluent-style builder for {@link AnimationMeta}
     * and {@link Frame} instances, since animation meta has a lot
     * of parameters and frame depends on meta, we must use this
     *
     * @since 1.0.0
     */
    public static class Builder {

        private boolean interpolate;
        private int width;
        private int height;
        private int frameTime;
        private List<Frame> frames;

        private Builder() {
        }

        public Builder interpolate(boolean interpolate) {
            this.interpolate = interpolate;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder frameTime(int frameTime) {
            this.frameTime = frameTime;
            return this;
        }

        public Builder frames(List<Frame> frames) {
            requireNonNull(frames, "frames");
            // copy the list in case we need to modify
            // it on frame(int) or frame(int, int)
            this.frames = new ArrayList<>(frames);
            return this;
        }

        public Builder frame(int index, int frameTime) {
            if (frames == null) {
                // create frames if null
                frames = new ArrayList<>();
            }
            frames.add(new Frame(index, frameTime));
            return this;
        }

        public Builder frame(int index) {
            // use default frame time
            return frame(index, this.frameTime);
        }

        /**
         * Finishes building the {@link AnimationMeta} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created animation meta
         */
        public AnimationMeta build() {
            // frames can be initially null, in that
            // case we use an empty list instead
            if (frames == null) {
                frames = Collections.emptyList();
            }
            return new AnimationMeta(
                    interpolate,
                    width,
                    height,
                    frameTime,
                    frames
            );
        }
    }

}