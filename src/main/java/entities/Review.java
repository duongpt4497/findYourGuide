/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author dgdbp
 */
@Component
public class Review {

    private long order_id;
    private long traveler_id;
    private long guider_id;
    private long post_id;
    private float rated;
    private Date post_date;
    private String review;

    public Review() {
    }

    public Review(long order_id, long traveler_id, long guider_id, long post_id, float rated, Date post_date, String review) {
        this.order_id = order_id;
        this.traveler_id = traveler_id;
        this.guider_id = guider_id;
        this.post_id = post_id;
        this.rated = rated;
        this.post_date = post_date;
        this.review = review;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getTraveler_id() {
        return traveler_id;
    }


    public Review(long guider_id, long rated_star) {
        this.guider_id = guider_id;
        this.rated = rated_star;
    }

    public long getPost_id() {
        return post_id;

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

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public float getRated() {
        return rated;
    }

    public void setRated(float rated) {
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
