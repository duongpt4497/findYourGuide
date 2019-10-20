package entities;

import lombok.Data;

import java.util.Date;

@Data
public class Review  {
    private long review_id;
    private long post_id;
    private long guider_id;
    private String review;
    private long rated_star;
    private Date date;

    public Review(long review_id, long guider_id,long post_id, String review, long rated_star, Date date) {
        this.review_id = review_id;
        this.guider_id = guider_id;
        this.post_id = post_id;
        this.review = review;
        this.rated_star = rated_star;
        this.date = date;
    }

    public long getPost_id() {
        return post_id;
    }

    public long getReview_id() {
        return review_id;
    }

    public long getGuider_id() {
        return guider_id;
    }

    public String getReview() {
        return review;
    }

    public long getRated_star() {
        return rated_star;
    }

    public Date getDate() {
        return date;
    }
}
