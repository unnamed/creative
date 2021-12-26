package team.unnamed.uracle.resourcepack;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class ResourcePackApplicationImpl
        implements ResourcePackApplication {

    private final boolean required;
    @Nullable private final String prompt;

    ResourcePackApplicationImpl(
            boolean required,
            @Nullable String prompt
    ) {
        this.required = required;
        this.prompt = prompt;
    }

    @Override
    public boolean required() {
        return required;
    }

    @Override
    public @Nullable String prompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "ResourcePackApplicationImpl{" +
                "required=" + required +
                ", prompt='" + prompt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourcePackApplicationImpl that = (ResourcePackApplicationImpl) o;
        return required == that.required
                && Objects.equals(prompt, that.prompt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(required, prompt);
    }

}
