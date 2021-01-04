package uom.edu.smnaggr;

public class PostEntry {
    private String post_id;

    public PostEntry(){};

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return "PostEntry{" +
                "id='" + post_id + '\'' +
                '}';
    }
}
