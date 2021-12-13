package team.unnamed.uracle.model.item;

import team.unnamed.uracle.Vector3Float;

/**
 * Place where an item model is displayed. Holds its rotation,
 * translation and scale for the specified situation
 */
public class ItemDisplay {

    /**
     * Specifies the rotation of the model
     */
    private final Vector3Float rotation;

    /**
     * Specifies the position of the model.
     * If the value is greater than 80, it
     * is displayed as 80. If the value is
     * less than -80, it is displayed as -80.
     */
    private final Vector3Float translation;

    /**
     * Specifies the scale of the mode. If
     * the value is greater than 4, it is
     * displayed as 4.
     */
    private final Vector3Float scale;

    public ItemDisplay(
            Vector3Float rotation,
            Vector3Float translation,
            Vector3Float scale
    ) {
        this.rotation = rotation;
        this.translation = translation;
        this.scale = scale;
    }

    public Vector3Float getRotation() {
        return rotation;
    }

    public Vector3Float getTranslation() {
        return translation;
    }

    public Vector3Float getScale() {
        return scale;
    }

    public enum Type {
        THIRDPERSON_RIGHTHAND,
        THIRDPERSON_LEFTHAND,
        FIRSTPERSON_RIGHTHAND,
        FIRSTPERSON_LEFTHAND,
        GUI,
        HEAD,
        GROUND,
        FIXED
    }

}
