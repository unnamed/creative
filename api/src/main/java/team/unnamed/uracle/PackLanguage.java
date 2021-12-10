package team.unnamed.uracle;

import java.util.Map;

public class PackLanguage {

    private final String name;
    private final String region;
    private final boolean bidirectional;

    private final Map<String, String> translations;

    public PackLanguage(
            String name,
            String region,
            boolean bidirectional,
            Map<String, String> translations
    ) {
        this.name = name;
        this.region = region;
        this.bidirectional = bidirectional;
        this.translations = translations;
    }

}
