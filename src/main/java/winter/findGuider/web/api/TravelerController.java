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

    @RequestMapping("/GetTraveler")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> getTravelerWithId(@RequestParam int traveler_id) {
        try {
            Traveler searchTraveler = travelerService.findTravelerWithId(traveler_id);
            if (searchTraveler == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(searchTraveler, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> createTraveler(@RequestBody Traveler newTraveler) {
        try {
            boolean success = travelerService.createTraveler(newTraveler);
            return new ResponseEntity<>(success, HttpStatus.OK);
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
