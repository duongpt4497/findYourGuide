package services.Review;

import entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findReviewByOrderId(long trip_id) throws Exception;

    List<Review> findReviewsByGuiderId(long guider_id) throws Exception;

    List<Review> findReviewsByPostId(long post_id) throws Exception;

    boolean createReview(Review newReview) throws Exception;

    boolean checkReviewExist(long trip_id) throws Exception;

    void showHideReview(long trip_id) throws Exception;
}
