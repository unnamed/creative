package team.unnamed.uracle.model.item;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.ResourceLocation;

import java.util.List;
import java.util.Map;

public class ItemTexture {

    private final List<ResourceLocation> layers;

    @Nullable private final ResourceLocation particle;

    private final Map<String, ResourceLocation> variables;

}
