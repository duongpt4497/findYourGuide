package winter.findGuider.web.api;

import entities.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.feedback.FeedbackService;

import java.util.List;

@RestController
@RequestMapping(path = "/feedback", produces = "application/json")
@CrossOrigin(origins = "*")
public class FeedbackController {
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService fs) {
        this.feedbackService = fs;
    }

    @RequestMapping("/findAll")
    public ResponseEntity<List<Feedback>> findAllFeedback() {
        try {
            return new ResponseEntity(feedbackService.findAllFeedback(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/create")
    public ResponseEntity<Boolean> createFeedback(@RequestParam int account_id, @RequestParam String message) {
        try {
            feedbackService.createFeedback(account_id, message);
            return new ResponseEntity(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(false, HttpStatus.NOT_FOUND);
        }
    }
}
