package winter.findGuider.web.api;

import entities.Guider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.GeneralServiceImpl;
import services.guider.GuiderServiceImpl;

@RestController
@RequestMapping(path = "/guider", produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderInfoApi {
    private GeneralServiceImpl generalServiceImpl;
    private GuiderServiceImpl guiderServiceImpl;

    @Autowired
    public GuiderInfoApi(GeneralServiceImpl gs, GuiderServiceImpl guS) {
        this.generalServiceImpl = gs;
        this.guiderServiceImpl = guS;
    }

    @GetMapping("/{id}")
    public Guider findGuider(@PathVariable("id") long id) {
        try {
            return guiderServiceImpl.findGuiderWithID(id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + id);
        }
        return null;
    }

    @GetMapping("/guiderByPostId")
    public Guider findGuiderByPostId(@RequestParam("post_id") long post_id) {
        try {
            return guiderServiceImpl.findGuiderWithPostId(post_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage() + post_id);
        }
        return null;
    }
}
