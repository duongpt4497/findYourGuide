package winter.findGuider.web.api;

import entities.Guider;
import entities.Guider_Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.guider.GuiderService;

@RestController
@RequestMapping(path = "/Guider", produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderController {

    private GuiderService guiderService;

    @Autowired
    public GuiderController(GuiderService gs) {
        this.guiderService = gs;
    }

//    @RequestMapping("/Create/{first_name}/{last_name}/{age}/{about_me}/{city}/{language}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Guider> createGuider(@PathVariable("first_name") String first_name,
//                                               @PathVariable("last_name") String last_name,
//                                               @PathVariable("age") int age,
//                                               @PathVariable("about_me") String about_me,
//                                               @PathVariable("city") String city,
//                                               @PathVariable("language") String[] language) {
//        Guider newGuider = new Guider();
//        long insertedId;
//        try {
//            newGuider.setFirst_name(first_name);
//            newGuider.setLast_name(last_name);
//            newGuider.setAge(age);
//            newGuider.setAbout_me(about_me);
//            newGuider.setCity(city);
//            newGuider.setLanguage(language);
//            insertedId = guiderService.createGuider(newGuider);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(guiderService.findGuiderWithID(insertedId), HttpStatus.OK);
//    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> createGuider(@RequestBody Guider newGuider, @RequestBody Guider_Contract newGuiderContract) {
        long insertedId;
        try {
            insertedId = guiderService.createGuider(newGuider);
            guiderService.createGuiderContract(insertedId, newGuiderContract);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guiderService.findGuiderWithID(insertedId), HttpStatus.OK);
    }

    @RequestMapping("/Edit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> editGuider(@RequestBody Guider guiderNeedUpdate) {
        try {
            guiderService.updateGuiderWithId(guiderNeedUpdate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guiderService.findGuiderWithID(guiderNeedUpdate.getGuider_id()), HttpStatus.OK);
    }

    @RequestMapping("/Activate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> activateGuider(@PathVariable("id") long id) {
        long activatedId;
        try {
            activatedId = guiderService.activateGuider(id);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guiderService.findGuiderWithID(activatedId), HttpStatus.OK);
    }

    @RequestMapping("/Deactivate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> deactivateGuider(@PathVariable("id") long id) {
        long deactivatedId;
        try {
            deactivatedId = guiderService.deactivateGuider(id);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guiderService.findGuiderWithID(deactivatedId), HttpStatus.OK);
    }
}
