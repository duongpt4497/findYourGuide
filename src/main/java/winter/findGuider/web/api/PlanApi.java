package winter.findGuider.web.api;

import entities.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.plan.PlanService;

@RestController
@RequestMapping(path = "/plan", produces = "application/json")
@CrossOrigin(origins = "*")
public class PlanApi {
    private PlanService planService;

    @Autowired
    public PlanApi(PlanService ps) {
        this.planService = ps;
    }

    @GetMapping("/create/{meeting_point}/{detail}/{post_id}/{order_id}")
    public ResponseEntity<Plan> createPlan(@PathVariable("meeting_point") String meeting_point,
                                           @PathVariable("detail") String detail,
                                           @PathVariable("post_id") int post_id,
                                           @PathVariable("order_id") int order_id) {
        try {
            Plan newPlan = new Plan(meeting_point, detail, post_id, order_id);

            int createdId = planService.createPlan(newPlan);
            return new ResponseEntity<>(planService.searchPlanByPlanId(createdId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/update/{post_id}/{meeting_point}/{detail}")
    public ResponseEntity<Plan> updatePlan(@PathVariable("post_id") int post_id,
                                           @PathVariable("meeting_point") String meeting_point,
                                           @PathVariable("detail") String detail) {
        try {
            planService.updatePlan(post_id, meeting_point, detail);
            return new ResponseEntity<>(planService.searchPlanByPostId(post_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
