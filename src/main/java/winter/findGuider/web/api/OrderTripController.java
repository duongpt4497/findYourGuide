package winter.findGuider.web.api;

import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Paypal.PaypalService;
import services.ordertrip.OrderTripService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/Order", produces = "application/json")
@CrossOrigin(origins = "*")
public class OrderTripController {
    private OrderTripService orderTripService;
    private PaypalService paypalService;

    public OrderTripController(OrderTripService os, PaypalService ps) {
        this.orderTripService = os;
        this.paypalService = ps;
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

    @RequestMapping("/GetOrderByStatus")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Order>> getOrderByStatus(@RequestParam String role, @RequestParam int id, @RequestParam String status) {
        try {
            return new ResponseEntity<>(orderTripService.findOrderByStatusAsGuider(role, id, status), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/CancelOrderAsTraveler")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelOrderAsTraveler(@RequestBody Order order) {
        LocalDateTime rightNow = LocalDateTime.now();
        Order cancelOrder = new Order();
        try {
            cancelOrder = orderTripService.findOrderById(order.getOrder_id());
            // check if refund is needed
            boolean isRefund = orderTripService.checkOrderCanRefund(cancelOrder, rightNow);
            // start cancel order
            boolean cancelSuccess;
            if (isRefund) {
                Refund refund = paypalService.refundPayment(cancelOrder.getTransaction_id());
                if (refund.getState().equals("completed")) {
                    paypalService.createRefundRecord(cancelOrder.getTransaction_id(), "success");
                    cancelSuccess = orderTripService.cancelOrder(cancelOrder.getOrder_id());
                    if (!cancelSuccess) {
                        return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>("Refund fail", HttpStatus.OK);
                }
            } else {
                cancelSuccess = orderTripService.cancelOrder(cancelOrder.getOrder_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Cancel Success", HttpStatus.OK);
        } catch (PayPalRESTException e) {
            String message = e.getDetails().getMessage();
            paypalService.createRefundRecord(cancelOrder.getTransaction_id(), message);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
