package uom.edu.smnaggr;

public class TwitterEntry {

    private String name;
    private String url;
    private String query;
    private String tweet_volume;

    public TwitterEntry(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTweet_volume() {
        return tweet_volume;
    }

    public void setTweet_volume(String tweet_volume) {
        this.tweet_volume = tweet_volume;
    }

    @Override
    public String toString() {
        return "TwitterEntry{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", query='" + query + '\'' +
                ", tweet_volume='" + tweet_volume + '\'' +
                '}';
    }
}
