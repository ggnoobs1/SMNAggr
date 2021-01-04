package uom.edu.smnaggr;

public class FBEntry {
    private String access_token;
    private String category;
    private String name;
    private String url;
    private String urlToImage;

    public FBEntry(){};

    public String getToken() {
        return access_token;
    }

    public void setToken(String author) {
        this.access_token = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String title) {
        this.category = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String description) {
        this.name = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    @Override
    public String toString() {
        return "NewsEntry{" +
                "token='" + access_token + '\'' +
                ", category='" + category + '\'' +
                ", description='" + name + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }
}
