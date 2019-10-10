package winter.findGuider.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import entities.Guider;
import services.guider.GuiderService;
import services.guider.GuiderServiceImpl;

@RestController
@RequestMapping(path = "/EditGuider", produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderEditApi {

    private GuiderService guiderService;

    @Autowired
    public GuiderEditApi(GuiderService gs) {
        this.guiderService = gs;
    }

    @RequestMapping("/{id}")
    public Guider editGuider(@PathVariable("id") long id, @RequestParam("first_name") String first_name,
                             @RequestParam("last_name") String last_name, @RequestParam("age") int age,
                             @RequestParam("about_me") String about_me, @RequestParam("city") String city) {
        Guider guiderNeedUpdate = guiderService.findGuiderWithID(id);
        if (guiderNeedUpdate == null) {
            // TODO raise error ?
        }

        guiderNeedUpdate.setGuider_id(id);
        guiderNeedUpdate.setFirst_name(first_name);
        guiderNeedUpdate.setLast_name(last_name);
        guiderNeedUpdate.setAge(age);
        guiderNeedUpdate.setAbout_me(about_me);
        guiderNeedUpdate.setCity(city);

        guiderService.updateGuiderWithId(guiderNeedUpdate);
        return guiderService.findGuiderWithID(id);
    }
}
