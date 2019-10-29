package winter.findGuider.web.api;

import entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.Review.ReviewServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/review",produces = "application/json")
@CrossOrigin(origins = "*")
public class ReviewApi {
    private GeneralServiceImpl generalServiceImpl;
    private ReviewServiceImpl reviewServiceImpl;
    @Autowired
    public ReviewApi (GeneralServiceImpl gs,ReviewServiceImpl guS){
        this.generalServiceImpl = gs;
        this.reviewServiceImpl = guS;
    }

    @GetMapping("/reviewByGuiderId")
    public List<Review> findReviewByGuiderId(@RequestParam("guider_id") long guider_id){
        try{

            return reviewServiceImpl.findReviewsByGuiderId(guider_id);
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + guider_id);
        }
        return null;
    }

    @GetMapping("/reviewByPostId")
    public List<Review> findReviewByPostId(@RequestParam("post_id") long post_id){
        try{

            return reviewServiceImpl.findReviewsByPostId(post_id);
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + post_id);
        }
        return null;
    }
}
