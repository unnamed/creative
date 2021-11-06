package team.unnamed.uracle.resourcepack;

/**
 * Class that represents a remote resource,
 * consists on a URL and the resource hash
 */
public class UrlAndHash {

    private final String url;
    private final String hash;

    public UrlAndHash(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

}