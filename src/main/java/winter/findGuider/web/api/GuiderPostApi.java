package winter.findGuider.web.api;

import entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.guider.ActivityServiceImpl;
import services.guider.PostServiceImpl;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/guiderpost",produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderPostApi {

    private GeneralServiceImpl generalServiceImpl;
    private PostServiceImpl postServiceImpl;
    private ActivityServiceImpl activityService;

    @Autowired
    public GuiderPostApi (GeneralServiceImpl gs,PostServiceImpl postServiceImpl,ActivityServiceImpl activityService){
        this.generalServiceImpl = gs;
        this.postServiceImpl = postServiceImpl;
        this.activityService = activityService;
    }

    @GetMapping("/postOfOneGuider")
    public List<Post> findAllPostOfOneGuider(@RequestParam("guider_id") long guider_id,@RequestParam("pageCursor") int pgCursor,@RequestParam("size") int pageSize){
        try{
            List<Post>  post= postServiceImpl.findAllPost(guider_id);
            int count = post.size();

            Map<String,Integer> pagingCount = generalServiceImpl.countSizeForPaging(pgCursor,pageSize);
            if(count < pagingCount.get("lastElement")){
                post = post.subList(pagingCount.get("startElement"),count);
            }else{
                post = post.subList(pagingCount.get("startElement"),pagingCount.get("lastElement"));
            }

            return post;
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + guider_id);
        }
        return null;
    }

    @GetMapping("/allPostOfOneCategory")
    public List<Post> findAllPostOfOneCategory(@RequestParam("category_id") long category_id,@RequestParam("pageCursor") int pgCursor,@RequestParam("size") int pageSize){
        try{

            List<Post>  post=postServiceImpl.findAllPost(category_id);
            int count = post.size();

            Map<String,Integer> pagingCount = generalServiceImpl.countSizeForPaging(pgCursor,pageSize);
            if(count < pagingCount.get("lastElement")){
                post = post.subList(pagingCount.get("startElement"),count);
            }else{
                post = post.subList(pagingCount.get("startElement"),pagingCount.get("lastElement"));
            }

            return post;
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + category_id);
        }
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<Post> findSpecificPost(@RequestParam("post_id") long id) {
        try{

            return new ResponseEntity(postServiceImpl.findSpecificPost(id), HttpStatus.OK);
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        return null;
    }



    @PostMapping(consumes="application/json",value = "/update/post")
    @ResponseStatus(HttpStatus.OK)
    public Long updatePost( @RequestBody Post post) {
        postServiceImpl.updatePost(post.getPost_id(),post);
        return post.getPost_id();
    }

    @PostMapping(consumes="application/json",value = "/add/post")
    @ResponseStatus(HttpStatus.OK)
    public int insertNewPost(@RequestParam("guider_id") long guider_id ,@RequestBody Post post) {
        int post_id = postServiceImpl.insertNewPost(guider_id,post);
        System.out.println(post_id);
        return post_id;
    }
}
