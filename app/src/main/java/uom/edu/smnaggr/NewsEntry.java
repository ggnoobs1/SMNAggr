package uom.edu.smnaggr;

public class NewsEntry {
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;

    public NewsEntry(){};

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + ( description.length() > 20 ? description.substring(0,20) : description) + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }
}
