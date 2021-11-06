package team.unnamed.uracle.resourcepack;

import org.jetbrains.annotations.Nullable;

public class ResourcePack extends UrlAndHash {

    private final boolean required;
    @Nullable private final String prompt;

    public ResourcePack(
            String url,
            String hash,
            boolean required,
            @Nullable String prompt
    ) {
        super(url, hash);
        this.required = required;
        this.prompt = prompt;
    }

    public boolean isRequired() {
        return required;
    }

    public @Nullable String getPrompt() {
        return prompt;
    }

    public ResourcePack withLocation(String url, String hash) {
        return new ResourcePack(url, hash, required, prompt);
    }

}