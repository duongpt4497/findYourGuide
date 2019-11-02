package winter.findGuider.web.api;

import entities.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.traveler.TravelerService;

@RestController
@RequestMapping(path = "/Traveler", produces = "application/json")
@CrossOrigin(origins = "*")
public class TravelerController {
    private TravelerService travelerService;

    @Autowired
    public TravelerController(TravelerService ts) {
        this.travelerService = ts;
    }

    @RequestMapping("/Get")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> getTravelerWithId(@RequestBody Traveler traveler) {
        try {
            return new ResponseEntity<>(travelerService.findTravelerWithId(traveler.getTraveler_id()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> createTraveler(@RequestBody Traveler newTraveler) {
        long createdId;
        try {
            createdId = travelerService.createTraveler(newTraveler);
            return new ResponseEntity<>(travelerService.findTravelerWithId(createdId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/Update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> updateTraveler(@RequestBody Traveler travelerNeedUpdate) {
        try {
            travelerService.updateTraveler(travelerNeedUpdate);
            return new ResponseEntity<>(travelerService.findTravelerWithId(travelerNeedUpdate.getTraveler_id()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
