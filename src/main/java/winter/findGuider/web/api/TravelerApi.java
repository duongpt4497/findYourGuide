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
public class TravelerApi {
    private TravelerService travelerService;

    @Autowired
    public TravelerApi(TravelerService ts) {
        this.travelerService = ts;
    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> createTraveler(@RequestBody Traveler newTraveler) {
        long createdId;
        try {
            createdId = travelerService.createTraveler(newTraveler);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(travelerService.findTravelerWithId(createdId), HttpStatus.OK);
    }

    @RequestMapping("/Edit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> editTraveler(@RequestBody Traveler travelerNeedUpdate) {
        try {
            travelerService.updateTraveler(travelerNeedUpdate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(travelerService.findTravelerWithId(travelerNeedUpdate.getTraveler_id()), HttpStatus.OK);
    }
}
