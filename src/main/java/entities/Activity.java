package entities;

public class Activity {
    private long post_id;
    private String brief;
    private String detail;

    public Activity() {
    }

    public Activity(String brief, String detail) {
        this.brief = brief;
        this.detail = detail;
    }

    public Activity(long post_id, String brief, String detail) {
        this.post_id = post_id;
        this.brief = brief;
        this.detail = detail;
    }

    public long getPost_id() {
        return post_id;
    }

    public String getBrief() {
        return brief;
    }

    public String getDetail() {
        return detail;
    }
}
