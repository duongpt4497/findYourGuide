package winter.findGuider.web.api;

import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Mail.MailService;
import services.Paypal.PaypalService;
import services.trip.TripService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/Order", produces = "application/json")
@CrossOrigin(origins = "*")
public class TripController {

    private TripService tripService;
    private PaypalService paypalService;
    private MailService mailService;

    @Autowired
    public TripController(TripService os, PaypalService ps, MailService ms) {
        this.tripService = os;
        this.paypalService = ps;
        this.mailService = ms;
    }

    @RequestMapping("/GetAvailableHours")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> getAvailableBookingHour(@RequestBody Order newOrder) {
        try {
            ArrayList<String> availableHour = tripService.getGuiderAvailableHours(newOrder.getBegin_date().toLocalDate(),
                    newOrder.getPost_id(), newOrder.getGuider_id());
            return new ResponseEntity<>(availableHour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetOrderByStatus")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Order>> getOrderByStatus(@RequestParam("role") String role, @RequestParam("id") int id,
                                                        @RequestParam("status") String status) {
        try {
            return new ResponseEntity<>(tripService.findOrderByStatusAsGuider(role, id, status), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetClosestFinishDate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getClosestFinishDate(@RequestBody Order newOrder) {
        try {
            String finishDate = tripService.getClosestTourFinishDate(newOrder.getBegin_date().toLocalDate(),
                    newOrder.getGuider_id());
            return new ResponseEntity<>(finishDate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/CancelOrderAsTraveler")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelOrderAsTraveler(@RequestParam("trip_id") int trip_id) {
        LocalDateTime rightNow = LocalDateTime.now();
        Order cancelOrder = new Order();
        try {
            cancelOrder = tripService.findOrderById(trip_id);
            // check if refund is needed
            boolean isRefund = tripService.checkOrderReach48Hours(cancelOrder, rightNow);
            // start cancel order
            boolean cancelSuccess;
            if (isRefund) {
                Refund refund = paypalService.refundPayment(cancelOrder.getTransaction_id());
                if (refund.getState().equals("completed")) {
                    paypalService.createRefundRecord(cancelOrder.getTransaction_id(), "success");
                    cancelSuccess = tripService.cancelOrder(cancelOrder.gettrip_id());
                    if (!cancelSuccess) {
                        return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>("Refund fail", HttpStatus.OK);
                }
            } else {
                cancelSuccess = tripService.cancelOrder(cancelOrder.gettrip_id());
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
    public ResponseEntity<String> cancelOrderAsGuider(@RequestParam("trip_id") int trip_id) {
        LocalDateTime rightNow = LocalDateTime.now();
        Order cancelOrder = new Order();
        try {
            cancelOrder = tripService.findOrderById(trip_id);

            // check if penalty is needed
            boolean isPenalty = tripService.checkOrderReach48Hours(cancelOrder, rightNow);

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
                cancelSuccess = tripService.cancelOrder(cancelOrder.gettrip_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                }
            } else {
                cancelSuccess = tripService.cancelOrder(cancelOrder.gettrip_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                }
            }
            // TODO get email
            Order order = tripService.findOrderById(trip_id);
            String content = mailService.getMailContent(order, "CANCELLED");
            mailService.sendMail("travelwithlocalsysadm@gmail.com", "TravelWLocal Tour Cancelled", content);
            return new ResponseEntity<>("Cancel Success", HttpStatus.OK);
        } catch (PayPalRESTException e) {
            String message = e.getDetails().getMessage();
            paypalService.createRefundRecord(cancelOrder.getTransaction_id(), message);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @RequestMapping("/AcceptOrder/{trip_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> acceptOrder(@PathVariable("trip_id") int orderId) {
        try {
            // Check for availability of order
            int count = tripService.checkOrderExist(orderId);
            if (count != 0) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            boolean result = tripService.acceptOrder(orderId);
            if (result) {
                // TODO get email
                Order order = tripService.findOrderById(orderId);
                String content = mailService.getMailContent(order, "ONGOING");
                mailService.sendMail("travelwithlocalsysadm@gmail.com", "TravelWLocal Tour Accepted", content);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/getOrderByWeek/{guider_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Order>> getOrderByWeek(@PathVariable("guider_id") int id, @RequestBody Date order) {
        try {

            Calendar cal = Calendar.getInstance();

            cal.setTime(order);

            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date start = cal.getTime();
            System.out.println("Start of this week:       " + start);

            cal.add(Calendar.WEEK_OF_YEAR, 1);
            Date end = cal.getTime();
            System.out.println("Start of the next week:   " + end);

            return new ResponseEntity<>(tripService.getOrderByWeek(id, start, end
            ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetExpectedTourEnd")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getExpectedTourEnd(@RequestBody Order order) {
        try {
            return new ResponseEntity<>(tripService.getExpectedEndTourTime(order.getPost_id(), order.getBegin_date()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
