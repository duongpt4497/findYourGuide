package winter.findGuider.web.api;

import entities.Contract;
import entities.Guider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Mail.MailService;
import services.guider.GuiderService;

import java.util.List;

@RestController
@RequestMapping(path = "/Guider", produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private GuiderService guiderService;
    private MailService mailService;

    @Autowired
    public GuiderController(GuiderService gs, MailService ms) {
        this.guiderService = gs;
        this.mailService = ms;
    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> createGuider(@RequestBody Guider newGuider, @RequestBody Contract newGuiderContract) {
        try {
            long guider_id = guiderService.createGuider(newGuider);
            long contract_id = guiderService.createGuiderContract(newGuiderContract);
            return new ResponseEntity<>(guiderService.findGuiderWithID(guider_id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/Search/{key}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Guider>> searchGuider(@PathVariable("key") String key) {
        try {
            return new ResponseEntity<>(guiderService.searchGuiderByName(key), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> findGuider(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(guiderService.findGuiderWithID(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/guiderByPostId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Guider> findGuiderByPostId(@RequestParam("post_id") long post_id) {
        try {
            return new ResponseEntity<>(guiderService.findGuiderWithPostId(post_id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/getTopGuiderByRate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Guider>> getTop5guiderByRate() {
        try {
            return new ResponseEntity<>(guiderService.getTopGuiderByRate(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/getTopGuiderByContribute")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Guider>> getTop5guiderByContribute() {
        try {
            return new ResponseEntity<>(guiderService.getTopGuiderByContribute(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/getAllContract")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Contract>> getAllContract() {
        try {
            return new ResponseEntity<>(guiderService.getAllContract(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/AcceptContract")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> acceptContract(@RequestParam("guider_id") long guider_id, @RequestParam("contract_id") long contract_id) {
        try {
            guiderService.linkGuiderWithContract(guider_id, contract_id);

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}
