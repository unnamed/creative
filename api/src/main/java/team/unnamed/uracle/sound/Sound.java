package team.unnamed.uracle.sound;

import team.unnamed.uracle.ResourceLocation;

public class Sound {

    private ResourceLocation location;
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
