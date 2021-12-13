package team.unnamed.uracle;

import team.unnamed.uracle.lang.Language;

import java.util.Iterator;
import java.util.Map;

public class PackMeta implements Element {

    private final PackInfo pack;
    private final Map<String, Language> languages;

    private PackMeta(
            PackInfo pack,
            Map<String, Language> languages
    ) {
        this.pack = pack;
        this.languages = languages;
    }

    @Override
    public void write(TreeWriter writer) {
        // {
        //   pack: {
        //      pack_format: 0,
        //      description: "desc"
        //   },
        //   language: {
        //      es_MX: {
        //          name: "Spanish"
        //          region: "Mexico"
        //          bidirectional: false
        //      }
        //   }
        // }
        try (TreeWriter.Context context = writer.enter("pack.mcmeta")) {
            context.startObject();
            context.writeKey("pack");
            // todo: write "pack"

            if (!languages.isEmpty()) {
                context.writeSeparator();
                context.writeKey("language");
                context.startObject();

                Iterator<Map.Entry<String, Language>> iterator
                        = languages.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, Language> entry = iterator.next();

                    context.writeKey(entry.getKey());
                    context.writePart(entry.getValue());

                    if (iterator.hasNext()) {
                        // separator for the next
                        // language entry
                        context.writeSeparator();
                    }
                }

                context.endObject();
            }
            context.endObject();
        }
    }

}
