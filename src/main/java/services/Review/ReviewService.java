package services.Review;

import entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findReviewByOrderId(long order_id);

    List<Review> findReviewsByGuiderId(long guider_id);

    List<Review> findReviewsByPostId(long post_id);

    public boolean createReview(Review newReview);

    public boolean checkReviewExist(long order_id);
}
