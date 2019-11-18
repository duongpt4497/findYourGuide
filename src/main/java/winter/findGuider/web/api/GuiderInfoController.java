package winter.findGuider.web.api;

import entities.Guider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.guider.GuiderService;

@RestController
@RequestMapping(path = "/guider", produces = "application/json")
@CrossOrigin(origins = "*")
public class GuiderInfoController {
    private GuiderService guiderService;

    @Autowired
    public GuiderInfoController(GuiderService gs) {
        this.guiderService = gs;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Guider> findGuider(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(guiderService.findGuiderWithID(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/guiderByPostId")
    public ResponseEntity<Guider> findGuiderByPostId(@RequestParam("post_id") long post_id) {
        try {
            return new ResponseEntity<>(guiderService.findGuiderWithPostId(post_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
