package team.unnamed.uracle.resourcepack;

import java.util.Objects;

class ResourcePackLocationImpl
        implements ResourcePackLocation {

    private final String url;
    private final String hash;

    ResourcePackLocationImpl(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String hash() {
        return hash;
    }

    @Override
    public String toString() {
        return "ResourcePackLocationImpl{" +
                "url='" + url + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourcePackLocationImpl that = (ResourcePackLocationImpl) o;
        return url.equals(that.url)
                && hash.equals(that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, hash);
    }

}