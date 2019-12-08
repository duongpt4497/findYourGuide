package services.Review;

import entities.Review;
import entities.TravelerReview;

import java.util.List;

public interface ReviewService {
    List<Review> findReviewByOrderId(long trip_id) throws Exception;

    List<Review> findReviewsByGuiderId(long guider_id) throws Exception;

    List<Review> findReviewsByPostId(long post_id) throws Exception;

    List<Review> findReviewsByPostIdAdmin(long post_id) throws Exception;

    boolean createReview(Review newReview) throws Exception;

    boolean checkReviewExist(long trip_id) throws Exception;

    void showHideReview(long trip_id) throws Exception;

    void createTravelerReview(TravelerReview review) throws Exception;

    List<TravelerReview> findReviewOfATraveler(long traveler_id) throws Exception;
}
