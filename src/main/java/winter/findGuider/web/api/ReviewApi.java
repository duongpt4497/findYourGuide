package winter.findGuider.web.api;

import entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Review.ReviewServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/review", produces = "application/json")
@CrossOrigin(origins = "*")
public class ReviewApi {
    private ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewApi(ReviewServiceImpl guS) {
        this.reviewServiceImpl = guS;
    }

    @RequestMapping("/reviewByGuiderId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Review>> findReviewByGuiderId(@RequestParam("guider_id") long guider_id) {
        try {
            return new ResponseEntity<>(reviewServiceImpl.findReviewsByGuiderId(guider_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/reviewByPostId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Review>> findReviewByPostId(@RequestParam("post_id") long post_id) {
        try {
            return new ResponseEntity<>(reviewServiceImpl.findReviewsByPostId(post_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
