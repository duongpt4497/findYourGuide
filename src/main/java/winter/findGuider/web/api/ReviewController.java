package winter.findGuider.web.api;

import entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Review.ReviewService;

import java.util.List;

@RestController
@RequestMapping(path = "/review", produces = "application/json")
@CrossOrigin(origins = "*")
public class ReviewController {
    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService rs) {
        this.reviewService = rs;
    }

    @RequestMapping("/reviewByGuiderId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Review>> findReviewByGuiderId(@RequestParam("guider_id") long guider_id) {
        try {
            return new ResponseEntity<>(reviewService.findReviewsByGuiderId(guider_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/reviewByPostId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Review>> findReviewByPostId(@RequestParam("post_id") long post_id) {
        try {
            return new ResponseEntity<>(reviewService.findReviewsByPostId(post_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> createReview(@RequestBody Review newReview) {
        try {
            return new ResponseEntity<>(reviewService.createReview(newReview), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/checkExist")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Review>> checkReviewExist(@RequestParam("order_id") long order_id) {
        try {
            boolean isExist = reviewService.checkReviewExist(order_id);
            if (isExist) {
                return new ResponseEntity<>(reviewService.findReviewByOrderId(order_id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}