package team.unnamed.uracle.font;

import team.unnamed.uracle.Element;

/**
 * Represents a Minecraft Resource Pack Font
 * provider
 */
public interface Font extends Element, Element.Part {

    /**
     * Returns the type of this font, the font
     * contents depend on it
     *
     * @return The font type
     */
    Type getType();

    enum Type {
        BITMAP,
        LEGACY_UNICODE,
        TTF
    }

}
