package team.unnamed.uracle.model.item;

import team.unnamed.uracle.Axis3D;
import team.unnamed.uracle.Direction;
import team.unnamed.uracle.Vector3Float;
import team.unnamed.uracle.Vector4Int;

import java.util.Map;

public class ItemElement {

    /**
     * Start point of a cuboid. Values must be
     * between -16 and 32.
     */
    private final Vector3Float from;

    /**
     * Stop point of a cuboid. Values must be
     * between -16 and 32.
     */
    private final Vector3Float to;

    private final Rotation rotation;

    private final boolean shade;

    /**
     * Holds all the faces of the cuboid. If a face is
     * left out, it does not render
     */
    private final Map<Direction, Face> faces;

    public static class Rotation {

        private final Vector3Float origin;

        private final Axis3D axis;

        private final float angle;

        private final boolean rescale;

    }

    public static class Face {

        private final Vector4Int uv;

        private final String texture;

        private final Direction cullFace;

        private final int rotation;

        private final boolean tint;

    }

}
