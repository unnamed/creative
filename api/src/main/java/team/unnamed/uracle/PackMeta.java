package team.unnamed.uracle;

import java.util.Map;

public class PackMeta {

    private final PackInfo pack;
    private final Map<String, Language> languages;

    public PackMeta(PackInfo pack, Map<String, Language> languages) {
        this.pack = pack;
        this.languages = languages;
    }

}
