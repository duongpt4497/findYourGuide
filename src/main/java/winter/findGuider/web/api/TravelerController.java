package winter.findGuider.web.api;

import entities.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.GeneralService;
import services.traveler.TravelerService;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(path = "/Traveler", produces = "application/json")
@CrossOrigin(origins = "*")
public class TravelerController {
    private TravelerService travelerService;

    @Autowired
    public TravelerController(TravelerService ts, GeneralService gs) {
        this.travelerService = ts;
    }

    @RequestMapping("/Create/{first_name}/{last_name}/{phone}/{email}/{gender}/{day}/{month}/{year}/{street}/{home_number}/{postal_code}/{slogan}/{about_me}/{language}/{country}/{city}/{avatar_link}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Traveler> createTraveler(@PathVariable("first_name") String first_name, @PathVariable("last_name") String last_name,
                                                   @PathVariable("phone") String phone, @PathVariable("email") String email,
                                                   @PathVariable("gender") int gender, @PathVariable("day") int day,
                                                   @PathVariable("month") int month, @PathVariable("year") int year,
                                                   @PathVariable("street") String street, @PathVariable("home_number") String home_number,
                                                   @PathVariable("postal_code") String postal_code, @PathVariable("slogan") String slogan,
                                                   @PathVariable("about_me") String about_me, @PathVariable("language") String[] language,
                                                   @PathVariable("country") String country, @PathVariable("city") String city,
                                                   @PathVariable("avatar_link") String avatar_link) {
        Traveler newTraveler = new Traveler();
        newTraveler.setFirst_name(first_name);
        newTraveler.setLast_name(last_name);
        newTraveler.setPhone(phone);
        newTraveler.setEmail(email);
        newTraveler.setGender(gender);
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + month + "-" + year);
            newTraveler.setDate_of_birth(date);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        newTraveler.setStreet(street);
        newTraveler.setHouse_number(home_number);
        newTraveler.setPostal_code(postal_code);
        newTraveler.setSlogan(slogan);
        newTraveler.setAbout_me(about_me);
        newTraveler.setLanguage(language);
        newTraveler.setCountry(country);
        newTraveler.setCity(city);
        newTraveler.setAvatar_link(avatar_link);
        long createdId = travelerService.createTraveler(newTraveler);
        return new ResponseEntity<>(travelerService.findTravelerWithId(createdId), HttpStatus.OK);
    }
}
