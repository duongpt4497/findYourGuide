/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.LocationRepo;
import entity.Location;
import org.springframework.http.HttpStatus;
/**
 *
 * @author dgdbp
 */
@RestController
@RequestMapping(path = "/location", produces = "application/json")
@CrossOrigin(origins = "*")
public class LocationController {

    private LocationRepo locationService;

    @Autowired
    public LocationController(LocationRepo locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<Location> findSpecificPost() {
        try {

            return new ResponseEntity(locationService.showAllLocation(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        return null;
    }
}
