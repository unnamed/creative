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
     * Adds a backslash before every double quote found
     * in the given {@code text} string
     */
    public static String escapeDoubleQuotes(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
                result.append('\\');
            }
            result.append(c);
        }
        return result.toString();
    }

    /**
     * Replaces {@code &} characters by {@link ChatColor#COLOR_CHAR}
     * using {@link ChatColor#translateAlternateColorCodes(char, String)}
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}