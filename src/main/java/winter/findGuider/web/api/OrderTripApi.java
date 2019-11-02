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
public class OrderTripApi {
    private OrderTripService orderTripService;
    private PaypalService paypalService;

    @Autowired
    public OrderTripApi(OrderTripService os, PaypalService ps) {
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

    @RequestMapping("/GetOrderByStatus")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Order>> getOrderByStatus(@RequestBody Order order) {
        try {
            return new ResponseEntity<>(orderTripService.findOrderByStatus(order.getGuider_id(), order.getStatus()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/CancelOrderAsTraveler")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelOrderAsTraveler(@RequestBody Order order) {
        try {
            if (order.getBegin_date().getYear() == LocalDateTime.now().getYear()
                    && order.getBegin_date().getMonth().equals(LocalDateTime.now().getMonth())) {
                int timespan = order.getFinish_date().getDayOfMonth() - LocalDateTime.now().getDayOfMonth();
                boolean cancelSuccess;
                if (timespan <= 2) {
                    cancelSuccess = orderTripService.cancelOrder(order.getOrder_id());
                    if (!cancelSuccess) {
                        return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                    }
                } else {
                    Refund refund = paypalService.refundPayment(order.getTransaction_id());
                    if (refund.getState().equals("completed")) {
                        paypalService.createRefundRecord(order.getTransaction_id(), "success");
                        cancelSuccess = orderTripService.cancelOrder(order.getOrder_id());
                        if (!cancelSuccess) {
                            return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                        }
                    } else {
                        return new ResponseEntity<>("Refund fail", HttpStatus.OK);
                    }
                }
            }
            return new ResponseEntity<>("Cancel Success", HttpStatus.OK);
        } catch (PayPalRESTException e) {
            String message = e.getDetails().getMessage();
            paypalService.createRefundRecord(order.getTransaction_id(), message);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
