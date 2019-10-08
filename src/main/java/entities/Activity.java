package entities;

public class Activity {
    private String brief;
    private String detail;

    public Activity() {
    }

    public Activity(String brief, String detail) {
        this.brief = brief;
        this.detail = detail;
    }

    public String getBrief() {
        return brief;
    }

    public String getDetail() {
        return detail;
    }
}
