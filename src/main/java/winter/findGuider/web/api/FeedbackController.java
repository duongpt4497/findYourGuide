package winter.findGuider.web.api;

import entities.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Feedback.FeedbackService;

@RestController
@RequestMapping(path = "/Feedback", produces = "application/json")
@CrossOrigin(origins = "*")
public class FeedbackController {
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService fs) {
        this.feedbackService = fs;
    }

    @RequestMapping("/Send")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> sendFeedback(@RequestBody Feedback feedback) {
        try {
            // send feedback
            boolean isMsgSent = feedbackService.sendFeedback(feedback);
            if (!isMsgSent) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            // save feedback to database
            boolean createSuccess = feedbackService.createFeedback(feedback);
            if (!createSuccess) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
