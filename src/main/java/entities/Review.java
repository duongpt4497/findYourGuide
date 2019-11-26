package entities;

import lombok.Data;

import java.util.Date;

@Data
public class Review {
    private long trip_id;
    private long traveler_id;
    private long guider_id;
    private long post_id;
    private long rated;
    private Date post_date;
    private String review;

    public Review() {
    }

    public Review(long trip_id, long traveler_id, long guider_id, long post_id, long rated, Date post_date, String review) {
        this.trip_id = trip_id;
        this.traveler_id = traveler_id;
        this.guider_id = guider_id;
        this.post_id = post_id;
        this.rated = rated;
        this.post_date = post_date;
        this.review = review;
    }

    public long gettrip_id() {
        return trip_id;
    }

    public void settrip_id(long trip_id) {
        this.trip_id = trip_id;
    }

    public long getTraveler_id() {
        return traveler_id;
    }

    public void setTraveler_id(long traveler_id) {
        this.traveler_id = traveler_id;
    }

    public long getGuider_id() {
        return guider_id;
    }

    public void setGuider_id(long guider_id) {
        this.guider_id = guider_id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public long getRated() {
        return rated;
    }

    public void setRated(long rated) {
        this.rated = rated;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
