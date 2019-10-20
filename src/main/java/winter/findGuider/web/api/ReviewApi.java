package winter.findGuider.web.api;

import entities.Guider;
import entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.guider.GuiderServiceImpl;
import services.guider.ReviewServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/view",produces = "application/json")
@CrossOrigin(origins = "*")
public class ReviewApi {
    private GeneralServiceImpl generalServiceImpl;
    private ReviewServiceImpl reviewServiceImpl;
    @Autowired
    public ReviewApi (GeneralServiceImpl gs,ReviewServiceImpl guS){
        this.generalServiceImpl = gs;
        this.reviewServiceImpl = guS;
    }

    @GetMapping("/{id}"+".json")
    public List<Review> findReview(@PathVariable("id") long id){
        try{

            return reviewServiceImpl.findReviewsById(id);
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + id);
        }
        return null;
    }

}
