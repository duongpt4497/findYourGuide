package winter.findGuider.web.api;

import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Mail.MailService;
import services.Paypal.PaypalService;
import services.account.AccountRepository;
import services.contributionPoint.ContributionPointService;
import services.guider.GuiderService;
import services.trip.TripService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/Order", produces = "application/json")
@CrossOrigin(origins = "*")
public class TripController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private TripService tripService;
    private PaypalService paypalService;
    private MailService mailService;
    private ContributionPointService contributionPointService;
    private GuiderService guiderService;
    private AccountRepository accountRepository;

    @Autowired
    public TripController(TripService os, PaypalService ps, MailService ms,
                          ContributionPointService cps, GuiderService gs,
                          AccountRepository ar) {
        this.tripService = os;
        this.paypalService = ps;
        this.mailService = ms;
        this.contributionPointService = cps;
        this.guiderService = gs;
        this.accountRepository = ar;
    }

    @RequestMapping("/GetAvailableHours")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> getAvailableBookingHour(@RequestBody Order newOrder) {
        try {
            ArrayList<String> availableHour = tripService.getGuiderAvailableHours(newOrder.getBegin_date().toLocalDate(),
                    newOrder.getPost_id(), newOrder.getGuider_id());
            return new ResponseEntity<>(availableHour, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetOrderByStatus")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Order>> getOrderByStatus(@RequestParam("role") String role, @RequestParam("id") int id,
                                                        @RequestParam("status") String status) {
        try {
            return new ResponseEntity<>(tripService.findTripByStatus(role, id, status), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetClosestFinishDate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getClosestFinishDate(@RequestBody Order newOrder) {
        try {
            String finishDate = tripService.getClosestTripFinishDate(newOrder.getBegin_date().toLocalDate(),
                    newOrder.getGuider_id());
            return new ResponseEntity<>(finishDate, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/CancelOrderAsTraveler")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelOrderAsTraveler(@RequestParam("trip_id") int trip_id) {
        LocalDateTime rightNow = LocalDateTime.now();
        Order cancelOrder = new Order();
        try {
            cancelOrder = tripService.findTripById(trip_id);
            // check if refund is needed
            boolean isRefund = tripService.checkTripReach48Hours(cancelOrder, rightNow);
            // start cancel order
            boolean cancelSuccess;
            if (isRefund) {
                Refund refund = paypalService.refundPayment(cancelOrder.getTransaction_id());
                if (refund.getState().equals("completed")) {
                    paypalService.createRefundRecord(cancelOrder.getTransaction_id(), "success");
                    cancelSuccess = tripService.cancelTrip(cancelOrder.gettrip_id());
                    if (!cancelSuccess) {
                        return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>("Refund fail", HttpStatus.OK);
                }
            } else {
                cancelSuccess = tripService.cancelTrip(cancelOrder.gettrip_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Cancel Success", HttpStatus.OK);
        } catch (PayPalRESTException e) {
            String message = e.getDetails().getMessage();
            try {
                paypalService.createRefundRecord(cancelOrder.getTransaction_id(), message);
            } catch (Exception exc) {
                logger.error(exc.getMessage());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/CancelOrderAsGuider")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> cancelOrderAsGuider(@RequestParam("trip_id") int trip_id) {
        LocalDateTime rightNow = LocalDateTime.now();
        Order cancelOrder = new Order();
        try {
            cancelOrder = tripService.findTripById(trip_id);
            // check if penalty is needed
            boolean isPenalty = tripService.checkTripReach48Hours(cancelOrder, rightNow);
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
                int guiderId = (int) guiderService.findGuiderWithPostId(cancelOrder.getPost_id()).getGuider_id();
                contributionPointService.penaltyGuiderForCancel(guiderId);
                cancelSuccess = tripService.cancelTrip(cancelOrder.gettrip_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                }
            } else {
                cancelSuccess = tripService.cancelTrip(cancelOrder.gettrip_id());
                if (!cancelSuccess) {
                    return new ResponseEntity<>("Cancel Fail", HttpStatus.OK);
                }
            }
            Order order = tripService.findTripById(trip_id);
            String email = accountRepository.getEmail(order.getTraveler_id());
            String content = mailService.getMailContent(order, "CANCELLED");
            mailService.sendMail(email, "TravelWLocal Tour Cancelled", content);
            return new ResponseEntity<>("Cancel Success", HttpStatus.OK);
        } catch (PayPalRESTException e) {
            String message = e.getDetails().getMessage();
            try {
                paypalService.createRefundRecord(cancelOrder.getTransaction_id(), message);
            } catch (Exception exc) {
                logger.error(exc.getMessage());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/AcceptOrder/{trip_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> acceptOrder(@PathVariable("trip_id") int orderId) {
        try {
            // Check for availability of order
            int count = tripService.checkTripExist(orderId);
            if (count != 0) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            boolean result = tripService.acceptTrip(orderId);
            if (result) {
                Order order = tripService.findTripById(orderId);
                String email = accountRepository.getEmail(order.getTraveler_id());
                String content = mailService.getMailContent(order, "ONGOING");
                mailService.sendMail(email, "TravelWLocal Tour Accepted", content);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
            return new ResponseEntity<>(tripService.getTripByWeek(id, start, end
            ), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/GetExpectedTourEnd")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getExpectedTourEnd(@RequestBody Order order) {
        try {
            return new ResponseEntity<>(tripService.getExpectedEndTripTime(order.getPost_id(), order.getBegin_date()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
