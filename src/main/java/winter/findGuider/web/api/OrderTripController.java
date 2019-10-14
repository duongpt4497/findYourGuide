package winter.findGuider.web.api;

import entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ordertrip.OrderTripService;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(path = "/Order", produces = "application/json")
@CrossOrigin(origins = "*")
public class OrderTripController {
    private OrderTripService orderTripService;

    public OrderTripController(OrderTripService os) {
        this.orderTripService = os;
    }

    @RequestMapping("/Create/{traveler_id}/{guider_id}/{post_id}/{begin_date}/{end_date}/{adult_quan}/{child_quan}/{fee}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> createGuider(@PathVariable("traveler_id") long traveler_id,
                                              @PathVariable("guider_id") long guider_id,
                                              @PathVariable("post_id") long post_id,
                                              @PathVariable("begin_date") String begin_date,
                                              @PathVariable("end_date") String end_date,
                                              @PathVariable("adult_quan") int adult_quan,
                                              @PathVariable("child_quan") int children_quan,
                                              @PathVariable("fee") long fee) {
        Order newOrder = new Order();
        long insertedId;
        try {
            newOrder.setTraveler_id(traveler_id);
            newOrder.setGuider_id(guider_id);
            newOrder.setPost_id(post_id);
            newOrder.setBegin_date(new SimpleDateFormat("dd-MM-yyyy").parse(begin_date));
            newOrder.setFinish_date(new SimpleDateFormat("dd-MM-yyyy").parse(end_date));
            newOrder.setAdult_quantity(adult_quan);
            newOrder.setChildren_quantity(children_quan);
            newOrder.setFee_paid(fee);
            insertedId = orderTripService.createOrder(newOrder);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderTripService.findOrder(insertedId), HttpStatus.OK);
    }
}
