package entities;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Post {
    private long post_id;
    private long guider_id;
    private long location_id;
    private long category_id;
    private String title;
    private String video_link;
    @NotNull
    private String[] picture_link;
    private int total_hour;
    private String description;
    private String[] including_service;
    private boolean active;
    private String location;
    private String category;
    private long price;
    private long rated;
    private String reasons;

    public Post(String title, String video_link, String[] picture_link, int total_hour, String description, String[] including_service, boolean active, String location, long price, long rated, String reasons, String category) {

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
        this.category = category;
    }

    public Post(long post_id, long guider_id, long location_id, long category_id, String title, String video_link, @NotNull String[] picture_link, int total_hour, String description, String[] including_service, boolean active, long price, long rated, String reasons) {
        this.post_id = post_id;
        this.guider_id = guider_id;
        this.location_id = location_id;
        this.category_id = category_id;
        this.title = title;
        this.video_link = video_link;
        this.picture_link = picture_link;
        this.total_hour = total_hour;
        this.description = description;
        this.including_service = including_service;
        this.active = active;
        this.price = price;
        this.rated = rated;
        this.reasons = reasons;
    }

    public Post(long post_id, String title, String video_link, String[] picture_link, int total_hour, String description, String[] including_service, boolean active, String location, long price, long rated, String reasons, String category) {
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
        this.category = category;
    }

    public Post(long post_id, String title, String[] picture_link, String description, boolean active) {
        this.post_id = post_id;
        this.title = title;
        this.picture_link = picture_link;
        this.description = description;
        this.active = active;
    }

    public Post(long location_id, long category_id, String title, String video_link, @NotNull String[] picture_link, int total_hour, String description, String[] including_service, boolean active, long price, long rated, String reasons) {
        this.location_id = location_id;
        this.category_id = category_id;
        this.title = title;
        this.video_link = video_link;
        this.picture_link = picture_link;
        this.total_hour = total_hour;
        this.description = description;
        this.including_service = including_service;
        this.active = active;
        this.price = price;
        this.rated = rated;
        this.reasons = reasons;
    }

    public Post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String[] getPicture_link() {
        return picture_link;
    }

    public void setPicture_link(String[] picture_link) {
        this.picture_link = picture_link;
    }

    public int getTotal_hour() {
        return total_hour;
    }

    public void setTotal_hour(int total_hour) {
        this.total_hour = total_hour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getIncluding_service() {
        return including_service;
    }

    public void setIncluding_service(String[] including_service) {
        this.including_service = including_service;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getGuider_id() {
        return guider_id;
    }

    public void setGuider_id(long guider_id) {
        this.guider_id = guider_id;
    }

    public long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(long location_id) {
        this.location_id = location_id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }
}
