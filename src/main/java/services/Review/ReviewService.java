package services.Review;

import entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findReviewsByGuiderId(long guider_id);
    List<Review> findReviewsByPostId(long post_id);
}
