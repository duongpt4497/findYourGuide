package winter.findGuider.web.api;

import entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Location.LocationService;

@RestController
@RequestMapping(path = "/location", produces = "application/json")
@CrossOrigin(origins = "*")
public class LocationController {

    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @RequestMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Location> findAllLocation() {
        try {
            return new ResponseEntity(locationService.showAllLocation(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> createLocation(@RequestParam String country, @RequestParam String city,
                                                  @RequestParam String place) {
        try {
            locationService.createLocation(country, city, place);
            return new ResponseEntity(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(false, HttpStatus.NOT_FOUND);
        }
    }
}
