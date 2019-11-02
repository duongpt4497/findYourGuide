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

    @PostMapping("/create")
    public ResponseEntity<Integer> createPlan(@RequestBody Plan plan) {
        try {
            

            int createdId = planService.createPlan(plan);
            return new ResponseEntity<>(createdId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/update")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) {
        try {
            planService.updatePlan(plan.getPost_id(), plan.getMeeting_point(), plan.getDetail());
            return new ResponseEntity<>(planService.searchPlanByPostId(plan.getPost_id()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{post_id}")
    public ResponseEntity<Plan> findPlan(@PathVariable("post_id") int post_id) {
        try {
            return new ResponseEntity<>(planService.searchPlanByPostId(post_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
