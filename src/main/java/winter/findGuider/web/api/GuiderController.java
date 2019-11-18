package winter.findGuider.web.api;

import entities.Guider;
import entities.Guider_Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.guider.GuiderService;

import java.util.List;

@RestController
@RequestMapping(path = "/Guider", produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderController {

    private GuiderService guiderService;

    @Autowired
    public GuiderController(GuiderService gs) {
        this.guiderService = gs;
    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> createGuider(@RequestBody Guider newGuider, @RequestBody Guider_Contract newGuiderContract) {
        try {
            long insertedId = guiderService.createGuider(newGuider);
            guiderService.createGuiderContract(insertedId, newGuiderContract);
            return new ResponseEntity<>(guiderService.findGuiderWithID(insertedId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/Edit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> editGuider(@RequestBody Guider guiderNeedUpdate) {
        try {
            guiderService.updateGuiderWithId(guiderNeedUpdate);
            return new ResponseEntity<>(guiderService.findGuiderWithID(guiderNeedUpdate.getGuider_id()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/Activate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> activateGuider(@PathVariable("id") long id) {
        try {
            long activatedId = guiderService.activateGuider(id);
            return new ResponseEntity<>(guiderService.findGuiderWithID(activatedId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping("/Deactivate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> deactivateGuider(@PathVariable("id") long id) {
        try {
            long deactivatedId = guiderService.deactivateGuider(id);
            return new ResponseEntity<>(guiderService.findGuiderWithID(deactivatedId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/Search/{key}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Guider>> searchGuider(@PathVariable("key") String key) {
        try {
            return new ResponseEntity<>(guiderService.searchGuider(key), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
}
