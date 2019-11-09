package winter.findGuider.web.api;

import entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Post>> findAllPostOfOneGuider(@RequestParam("guider_id") long guider_id) {
        try {
            return new ResponseEntity<>(postServiceImpl.findAllPost(guider_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/allPostOfOneCategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Post>> findAllPostOfOneCategory(@RequestParam("category_id") long category_id) {
        try {
            return new ResponseEntity<>(postServiceImpl.findAllPost(category_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/findSpecificPost")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Post> findSpecificPost(@RequestParam("post_id") long id) {
        try {
            return new ResponseEntity(postServiceImpl.findSpecificPost(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(consumes = "application/json", value = "/update/post")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updatePost(@RequestBody Post post) {
        try {
            postServiceImpl.updatePost(post.getPost_id(), post);
            return new ResponseEntity(post.getPost_id(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(consumes = "application/json", value = "/add/post")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> insertNewPost(@RequestParam("guider_id") long guider_id, @RequestBody Post post) {
        try {
            return new ResponseEntity(postServiceImpl.insertNewPost(guider_id, post), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
