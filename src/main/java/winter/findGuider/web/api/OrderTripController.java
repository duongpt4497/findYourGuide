package winter.findGuider.web.api;

import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public ResponseEntity<List<Order>> getOrderByStatus(@RequestParam("role") String role, @RequestParam("id") int id, @RequestParam("status") String status) {
        try {
            return new ResponseEntity<>(orderTripService.findOrderByStatusAsGuider(role, id, status), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetClosestFinishDate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getClosestFinishDate(@RequestBody Order newOrder) {
        try {
            String finishDate = orderTripService.getClosestTourFinishDate(newOrder.getBegin_date().toLocalDate(),
                    newOrder.getGuider_id());
            return new ResponseEntity<>(finishDate, HttpStatus.OK);
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
            boolean isRefund = orderTripService.checkOrderReach48Hours(cancelOrder, rightNow);
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

    @RequestMapping("/CancelOrderAsGuider")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelOrderAsGuider(@RequestBody Order order) {
        LocalDateTime rightNow = LocalDateTime.now();
        Order cancelOrder = new Order();
        try {
            cancelOrder = orderTripService.findOrderById(order.getOrder_id());

            // check if penalty is needed
            boolean isPenalty = orderTripService.checkOrderReach48Hours(cancelOrder, rightNow);

            // start cancel order
            boolean cancelSuccess;

            // refund traveler
            Refund refund = paypalService.refundPayment(cancelOrder.getTransaction_id());
            if (refund.getState().equals("completed")) {
                paypalService.createRefundRecord(cancelOrder.getTransaction_id(), "success");
            } else {
                return new ResponseEntity<>("Refund fail", HttpStatus.OK);
            }

            // penalty guider contribution point
            if (isPenalty) {
                // TODO penalty guider code here
                cancelSuccess = orderTripService.cancelOrder(cancelOrder.getOrder_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
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

    @RequestMapping("/AcceptOrder/{order_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> acceptOrder(@RequestParam int orderId) {
        try {
            // Check for availability of order
            int count = orderTripService.checkOrderExist(orderId);
            if (count != 0) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            return new ResponseEntity<>(orderTripService.acceptOrder(orderId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
