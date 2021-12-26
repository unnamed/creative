package team.unnamed.uracle.util;

import org.bukkit.ChatColor;

/**
 * Utility class containing some static
 * utility methods for handling {@link String}
 * instances and related
 */
public final class Texts {

    private Texts() {
    }

    /**
     * Replaces {@code &} characters by {@link ChatColor#COLOR_CHAR}
     * using {@link ChatColor#translateAlternateColorCodes(char, String)}
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}