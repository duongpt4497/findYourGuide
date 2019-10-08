package entities;

import java.util.List;

public class Post {
    private String title;
    private String video_link;
    private String[] picture_link;
    private int total_hour;
    private String description;
    private String including_service;
    private boolean active;
    private String location;
    private List<List<Activity>> activities;

    public Post(String title, String video_link, String[] picture_link, int total_hour, String description, String including_service, boolean active, String location, List<List<Activity>> activities) {
        this.title = title;
        this.video_link = video_link;
        this.picture_link = picture_link;
        this.total_hour = total_hour;
        this.description = description;
        this.including_service = including_service;
        this.active = active;
        this.location = location;
        this.activities = activities;
    }

    public Post(String title,String[] picture_link,String description,boolean active) {
        this.title = title;
        this.picture_link = picture_link;
        this.description = description;
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public String getVideo_link() {
        return video_link;
    }

    public String[] getPicture_link() {
        return picture_link;
    }

    public int getTotal_hour() {
        return total_hour;
    }

    public String getDescription() {
        return description;
    }

    public String getIncluding_service() {
        return including_service;
    }

    public boolean isActive() {
        return active;
    }

    public String getLocation() {
        return location;
    }

    public List<List<Activity>> getActivities() {
        return activities;
    }


}
