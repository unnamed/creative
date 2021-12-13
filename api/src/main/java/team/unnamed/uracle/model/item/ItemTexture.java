package team.unnamed.uracle.model.item;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemTexture {

    private final List<Key> layers;

    @Nullable private final Key particle;

    private final Map<String, Key> variables;

}
