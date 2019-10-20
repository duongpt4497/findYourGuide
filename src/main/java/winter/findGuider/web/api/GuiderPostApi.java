package winter.findGuider.web.api;

import entities.Guider;
import entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.guider.GuiderServiceImpl;
import services.guider.PostServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/guiderpost",produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderPostApi {

    private GeneralServiceImpl generalServiceImpl;
    private PostServiceImpl postServiceImpl;

    @Autowired
    public GuiderPostApi (GeneralServiceImpl gs,PostServiceImpl postServiceImpl){
        this.generalServiceImpl = gs;
        this.postServiceImpl = postServiceImpl;
    }

    @GetMapping("/{id}")
    public List<Post> findGuider(@PathVariable("id") long id){
        try{

            return postServiceImpl.findAllPost(id);
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + id);
        }
        return null;
    }

}
