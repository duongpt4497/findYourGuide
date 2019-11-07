package services.guider;

import entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findReviewsById(long guider_id);
    List<Review> findReviewsByPostId(long post_id);
}