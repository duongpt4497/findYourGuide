package winter.findGuider.web.api;

import entities.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.GeneralService;
import services.traveler.TravelerService;

import java.sql.Date;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(path = "/TravelerCreate", produces = "application/json")
@CrossOrigin(origins = "*")
public class TravelerCreateController {
    private TravelerService travelerService;
    private GeneralService generalService;

    @Autowired
    public TravelerCreateController(TravelerService ts, GeneralService gs) {
        this.travelerService = ts;
        this.generalService = gs;
    }

    @RequestMapping("/{first_name}/{last_name}/{phone}/{email}/{gender}/{day}/{month}/{year}/{street}/{home_number}/{postal_code}/{slogan}/{about_me}/{language}/{country}/{city}")
    public Traveler createTraveler(@PathVariable("first_name") String first_name, @PathVariable("last_name") String last_name,
                                   @PathVariable("phone") String phone, @PathVariable("email") String email,
                                   @PathVariable("gender") int gender, @PathVariable("day") int day,
                                   @PathVariable("month") int month, @PathVariable("year") int year,
                                   @PathVariable("street") String street, @PathVariable("home_number") String home_number,
                                   @PathVariable("postal_code") String postal_code, @PathVariable("slogan") String slogan,
                                   @PathVariable("about_me") String about_me, @PathVariable("language") String[] language,
                                   @PathVariable("country") String country, @PathVariable("city") String city) {
        Traveler newTraveler = new Traveler();
        newTraveler.setFirst_name(first_name);
        newTraveler.setLast_name(last_name);
        newTraveler.setPhone(phone);
        newTraveler.setEmail(email);
        newTraveler.setGender(gender);
        try {
            newTraveler.setDate_of_birth((Date) new SimpleDateFormat("dd/MM/yyyy").parse(day + "/" + month + "/" + year));
        } catch (Exception e) {
            // TODO raise error
        }
        newTraveler.setStreet(street);
        newTraveler.setHouse_number(home_number);
        newTraveler.setPostal_code(postal_code);
        newTraveler.setSlogan(slogan);
        newTraveler.setAbout_me(about_me);
        newTraveler.setLanguage(language);
        newTraveler.setCountry(country);
        newTraveler.setCity(city);
        long createdId = travelerService.createTraveler(newTraveler);
        return travelerService.findTravelerWithId(createdId);
    }
}
