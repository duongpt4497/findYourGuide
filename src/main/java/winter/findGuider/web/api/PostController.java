package winter.findGuider.web.api;

import entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Activity.ActivityServiceImpl;
import services.GeneralServiceImpl;
import services.Post.PostServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/guiderpost", produces = "application/json")
@CrossOrigin(origins = "*")
public class PostController {

    private GeneralServiceImpl generalServiceImpl;
    private PostServiceImpl postServiceImpl;

    @Autowired
    public PostController(GeneralServiceImpl gs, PostServiceImpl postServiceImpl) {
        this.generalServiceImpl = gs;
        this.postServiceImpl = postServiceImpl;
    }

    @RequestMapping("/postOfOneGuider")
    public List<Post> findAllPostOfOneGuider(@RequestParam("guider_id") long guider_id) {
        try {
            return postServiceImpl.findAllPost(guider_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + guider_id);
        }
        return null;
    }

    @RequestMapping("/allPostOfOneCategory")
    public List<Post> findAllPostOfOneCategory(@RequestParam("category_id") long category_id) {
        try {
            return postServiceImpl.findAllPost(category_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + category_id);
        }
        return null;
    }

    @RequestMapping("/findSpecificPost")
    public ResponseEntity<Post> findSpecificPost(@RequestParam("post_id") long id) {
        try {
            return new ResponseEntity(postServiceImpl.findSpecificPost(id), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        return null;
    }

    @RequestMapping(consumes = "application/json", value = "/update/post")
    @ResponseStatus(HttpStatus.OK)
    public Long updatePost(@RequestBody Post post) {
        postServiceImpl.updatePost(post.getPost_id(), post);
        return post.getPost_id();
    }

    @RequestMapping(consumes = "application/json", value = "/add/post")
    @ResponseStatus(HttpStatus.OK)
    public int insertNewPost(@RequestParam("guider_id") long guider_id, @RequestBody Post post) {
        System.out.println(post.getReasons());
        int post_id = postServiceImpl.insertNewPost(guider_id, post);
        System.out.println(post_id);
        return post_id;
    }
}
