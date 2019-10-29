package winter.findGuider.web.api;

import entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ordertrip.OrderTripService;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/Order", produces = "application/json")
@CrossOrigin(origins = "*")
public class OrderTripController {
    private OrderTripService orderTripService;

    public OrderTripController(OrderTripService os) {
        this.orderTripService = os;
    }

    @RequestMapping("/Create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
        try {
            orderTripService.getOrderGuiderId_Price_EndDate(newOrder);
            // Check for availability of order
            int count = orderTripService.checkAvailabilityOfOrder(newOrder);
            if (count != 0) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            // Create order
            int insertedId = orderTripService.createOrder(newOrder);
            return new ResponseEntity<>(orderTripService.findOrder(insertedId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetAvailableHours")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> getAvailableBookingHour(@RequestBody Order newOrder) {
        try {
            ArrayList<String> availableHour = orderTripService.getGuiderAvailableHours(newOrder.getBegin_date().toLocalDate(),
                    newOrder.getPost_id(), newOrder.getGuider_id());
            return new ResponseEntity<>(availableHour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
