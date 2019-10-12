package winter.findGuider.web.api;

import entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.guider.ActivityServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/activity",produces = "application/json")
@CrossOrigin(origins = "*")
public class ActivityApi {

    private GeneralServiceImpl generalServiceImpl;
    private ActivityServiceImpl activityService;
    @Autowired
    public ActivityApi (GeneralServiceImpl gs,ActivityServiceImpl activityService){
        this.generalServiceImpl = gs;
        this.activityService = activityService;
    }

    @GetMapping("/post/{id}")
    public List<Activity> findActivity(@PathVariable("id") long post_id){
        try {
            return activityService.findActivityOfAPost(post_id);
        }catch(Exception e ){

        }
        return null;
    }

    @PostMapping(consumes="application/json",value = "/update/post")
    @ResponseStatus(HttpStatus.OK)
    public void updateActivity( @RequestParam("id") long post_id,@RequestBody List<Activity> activity) {


        activityService.updateActivity(post_id,activity);

    }
}
