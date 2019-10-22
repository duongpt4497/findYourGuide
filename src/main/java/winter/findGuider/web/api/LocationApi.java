package winter.findGuider.web.api;

import entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.guider.LocationServiceImpl;

@RestController
@RequestMapping(path = "/location", produces = "application/json")
@CrossOrigin(origins = "*")
public class LocationApi {

    private LocationServiceImpl locationService;

    @Autowired
    public LocationApi(LocationServiceImpl locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<Location> findSpecificPost() {
        try{

            return new ResponseEntity(locationService.showAllLocation(), HttpStatus.OK);
        }catch(Exception e ){
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        return null;
    }
}