package team.unnamed.uracle.texture;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.ResourceLocation;
import team.unnamed.uracle.base.Writable;

public class Texture {

    private final ResourceLocation location;
    private final Writable data;

    @Nullable private final TextureMeta meta;
    @Nullable private final AnimationMeta animation;

    public Texture(
            ResourceLocation location,
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation
    ) {
        this.location = location;
        this.data = data;
        this.meta = meta;
        this.animation = animation;
    }

}