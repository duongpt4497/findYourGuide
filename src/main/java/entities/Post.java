package entities;

import java.util.List;

public class Post {
    private long post_id;
    private String title;
    private String video_link;
    private String[] picture_link;
    private int total_hour;
    private String description;
    private String[] including_service;
    private boolean active;
    private String location;
    private long price;
    private long rated;
    private String reasons;


    public Post(String title, String video_link, String[] picture_link, int total_hour, String description, String[] including_service, boolean active, String location, long price, long rated, String reasons) {

        this.title = title;
        this.video_link = video_link;
        this.picture_link = picture_link;
        this.total_hour = total_hour;
        this.description = description;
        this.including_service = including_service;
        this.active = active;
        this.location = location;
        this.price = price;
        this.rated = rated;
        this.reasons = reasons;
    }

    public Post(long post_id, String title, String video_link, String[] picture_link, int total_hour, String description, String[] including_service, boolean active, String location, long price, long rated, String reasons) {
        this.post_id = post_id;
        this.title = title;
        this.video_link = video_link;
        this.picture_link = picture_link;
        this.total_hour = total_hour;
        this.description = description;
        this.including_service = including_service;
        this.active = active;
        this.location = location;
        this.price = price;
        this.rated = rated;
        this.reasons = reasons;
    }

    public Post(long post_id,String title, String[] picture_link, String description, boolean active) {
        this.post_id  = post_id;
        this.title = title;
        this.picture_link = picture_link;
        this.description = description;
        this.active = active;
    }

    public Post() {

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

    public String[] getIncluding_service() {
        return including_service;
    }

    public boolean isActive() {
        return active;
    }

    public String getLocation() {
        return location;
    }


    public long getPost_id() {
        return post_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public void setPicture_link(String[] picture_link) {
        this.picture_link = picture_link;
    }

    public void setTotal_hour(int total_hour) {
        this.total_hour = total_hour;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIncluding_service(String[] including_service) {
        this.including_service = including_service;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getRated() {
        return rated;
    }

    public void setRated(long rated) {
        this.rated = rated;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
}