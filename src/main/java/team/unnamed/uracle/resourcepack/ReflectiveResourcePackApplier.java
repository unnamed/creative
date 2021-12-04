package team.unnamed.uracle.resourcepack;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.util.Version;

import java.lang.reflect.Method;

/**
 * Utility for applying resource packs to specific
 * {@link Player} across all supported Bukkit,
 * Spigot and Paper versions
 *
 * @author yusshu (Andre Roldan)
 */
public final class ReflectiveResourcePackApplier {

    private static final Method SET_RESOURCE_PACK_METHOD;
    private static final Method GET_HANDLE_METHOD;
    @Nullable
    private static final Method DESERIALIZE_COMPONENT_METHOD;

    static {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit."
                    + Version.CURRENT + ".entity.CraftPlayer");

            GET_HANDLE_METHOD = craftPlayerClass.getDeclaredMethod("getHandle");

            if (Version.CURRENT.getMinor() < 17) {

                // we use getHandle() and then .setResourcePack(String, String)
                // compatible with both Spigot and Paper
                SET_RESOURCE_PACK_METHOD = Class.forName("net.minecraft.server." + Version.CURRENT + ".EntityPlayer")
                        .getDeclaredMethod("setResourcePack", String.class, String.class);

                DESERIALIZE_COMPONENT_METHOD = null;
            } else {

                // we use getHandle() and then setResourcePack(String, String, boolean, IChatBaseComponent)
                // for 1.17+, compatible with both Spigot and Paper
                SET_RESOURCE_PACK_METHOD = Class.forName("net.minecraft.server.level.EntityPlayer")
                        .getDeclaredMethod(
                                "setResourcePack",
                                String.class,
                                String.class,
                                boolean.class,
                                Class.forName("net.minecraft.network.chat.IChatBaseComponent")
                        );

                DESERIALIZE_COMPONENT_METHOD = Class.forName(
                        "net.minecraft.network.chat.IChatBaseComponent$ChatSerializer"
                ).getDeclaredMethod("a", String.class);
            }
        } catch (ReflectiveOperationException e) {
            // probably found an unsupported version of spigot
            throw new IllegalStateException(
                    "Cannot find setResourcePack method",
                    e
            );
        }
    }

    /**
     * Applies the given {@code resourcePack} to the specified {@code player},
     * if some property of {@code resourcePack} isn't available in current
     * server version, it's silently ignored (e.g. prompts in <1.17)
     *
     * @param player Player to apply resource pack
     * @param resourcePack The applied resource pack
     */
    @SuppressWarnings("all") // ide detects parameter mismatch
    public static void setResourcePack(Player player, ResourcePack resourcePack) {
        try {
            Object handle = GET_HANDLE_METHOD.invoke(player);

            if (Version.CURRENT.getMinor() < 17) {
                // 'required' and 'prompt' fields not supported
                SET_RESOURCE_PACK_METHOD.invoke(
                        handle,
                        resourcePack.getUrl(),
                        resourcePack.getHash()
                );
            } else {
                String prompt = resourcePack.getPrompt();
                SET_RESOURCE_PACK_METHOD.invoke(
                        handle,
                        resourcePack.getUrl(),
                        resourcePack.getHash(),
                        resourcePack.isRequired(),
                        prompt == null ? null : DESERIALIZE_COMPONENT_METHOD.invoke(null, prompt)
                );
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Cannot apply resource pack",
                    e
            );
        }
    }

}