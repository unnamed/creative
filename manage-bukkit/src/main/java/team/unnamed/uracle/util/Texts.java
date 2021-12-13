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
     * Adds a backslash before every special character JSON
     * doesn't accept for a string found in the given {@code text}
     * string
     */
    public static String escapeForJson(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
                result.append('\\').append(c);
                continue;
            } else if (c == '\n') {
                // append '\n' (literal, not the character)
                result.append("\\n");
                continue;
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