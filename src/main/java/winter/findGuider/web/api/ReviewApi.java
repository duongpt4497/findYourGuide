package winter.findGuider.web.api;

import entities.Guider;
import entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.guider.GuiderServiceImpl;
import services.guider.ReviewServiceImpl;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<Review> findReviewByGuiderId(@RequestParam("guider_id") long guider_id,@RequestParam("pageCursor") int pgCursor,@RequestParam("size") int pageSize){
        try{
            List<Review>  reviews= new ArrayList<>();
            reviews = reviewServiceImpl.findReviewsByGuiderId(guider_id);
            int count = reviews.size();

            Map<String,Integer> pagingCount = generalServiceImpl.countSizeForPaging(pgCursor,pageSize);
            if(count < pagingCount.get("lastElement")){
                reviews = reviews.subList(pagingCount.get("startElement"),count);
            }else{
                reviews = reviews.subList(pagingCount.get("startElement"),pagingCount.get("lastElement"));
            }
            return reviews;
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + guider_id);
        }
        return null;
    }

    @GetMapping("/reviewByPostId")
    public List<Review> findReviewByPostId(@RequestParam("post_id") long post_id,@RequestParam("pageCursor") int pgCursor,@RequestParam("size") int pageSize){
        try{

            List<Review>  reviews= new ArrayList<>();
            reviews = reviewServiceImpl.findReviewsByPostId(post_id);
            int count = reviews.size();

            Map<String,Integer> pagingCount = generalServiceImpl.countSizeForPaging(pgCursor,pageSize);
            if(count < pagingCount.get("lastElement")){
                reviews = reviews.subList(pagingCount.get("startElement"),count);
            }else{
                reviews = reviews.subList(pagingCount.get("startElement"),pagingCount.get("lastElement"));
            }
            return reviews;
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + post_id);
        }
        return null;
    }
}
