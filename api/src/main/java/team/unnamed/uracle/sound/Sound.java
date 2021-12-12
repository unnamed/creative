package team.unnamed.uracle.sound;

import team.unnamed.uracle.ResourceLocation;
import team.unnamed.uracle.base.Writable;

public class Sound {

    private ResourceLocation location;

    /**
     * The OGG sound data, located at the specified {@code location}
     * inside assets/&lt;namespace&gt;/sounds folder
     */
    private Writable data;

    private float volume;
    private float pitch;
    private int weight;
    private boolean stream;
    private int attenuationDistance;
    private boolean preload;
    private Type type;

    enum Type {
        SOUND,
        EVENT
    }

}
