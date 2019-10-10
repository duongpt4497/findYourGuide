package winter.findGuider.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/{id}/{first_name}/{last_name}/{age}/{about_me}/{city}")
    public Guider editGuider(@PathVariable("id") long id, @PathVariable("first_name") String first_name,
                             @PathVariable("last_name") String last_name, @PathVariable("age") int age,
                             @PathVariable("about_me") String about_me, @PathVariable("city") String city) {
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
