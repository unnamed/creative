package team.unnamed.uracle;

import java.util.Map;

public class PackMeta {

    private final PackInfo pack;
    private final Map<String, PackLanguage> languages;

    public PackMeta(PackInfo pack, Map<String, PackLanguage> languages) {
        this.pack = pack;
        this.languages = languages;
    }

}
