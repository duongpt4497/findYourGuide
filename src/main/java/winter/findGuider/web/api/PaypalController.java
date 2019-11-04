package winter.findGuider.web.api;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.Paypal.PaypalService;
import services.ordertrip.OrderTripService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/Payment", produces = "application/json")
@CrossOrigin(origins = "*")
public class PaypalController {

    private static final String URL_PAYPAL_SUCCESS = "/Pay/Success";
    private static final String URL_PAYPAL_CANCEL = "/Pay/Cancel";
    private static final String CHATBOX_PATH = "/chatbox/";
    @Value("${order.server.root.url}")
    private String URL_ROOT_SERVER;
    @Value("${order.client.root.url}")
    private String URL_ROOT_CLIENT;
    private PaypalService paypalService;
    private OrderTripService orderTripService;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public PaypalController(PaypalService ps, OrderTripService ots) {
        this.paypalService = ps;
        this.orderTripService = ots;
    }

    @RequestMapping("/Pay")
    @ResponseStatus(HttpStatus.OK)
    public String payment(@RequestBody Order order) {
        String cancelUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_CANCEL + "?post_id=" + order.getPost_id();
        String successUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_SUCCESS + "?traveler_id=" + order.getTraveler_id()
                + "&post_id=" + order.getPost_id() + "&adult=" + order.getAdult_quantity()
                + "&children=" + order.getChildren_quantity() + "&begin_date=" + order.getBegin_date();
        try {
            orderTripService.getOrderGuiderId_FinishDate(order);
            order.setFee_paid(paypalService.getTransactionFee(order));
            successUrl += "&fee=" + order.getFee_paid();
            // Check for availability of order
            int count = orderTripService.checkAvailabilityOfOrder(order);
            if (count != 0) {
                return URL_ROOT_CLIENT + CHATBOX_PATH + order.getPost_id() + "/booking_time_not_available";
            }
            String description = paypalService.getTransactionDescription(order);
            Payment payment = paypalService.createPayment(order.getFee_paid(), "USD", description, cancelUrl, successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return URL_ROOT_CLIENT + CHATBOX_PATH + order.getPost_id() + "/paypal_server_error";
    }

    @RequestMapping(URL_PAYPAL_CANCEL)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> cancelPay(@RequestParam("post_id") int post_id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            URI result = new URI(URL_ROOT_CLIENT + CHATBOX_PATH + post_id);
            httpHeaders.setLocation(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @RequestMapping(URL_PAYPAL_SUCCESS)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                                             @RequestParam("traveler_id") int traveler_id, @RequestParam("post_id") int post_id,
                                             @RequestParam("adult") int adult_quantity, @RequestParam("children") int children_quantity,
                                             @RequestParam("begin_date") String begin_date, @RequestParam("fee") double fee_paid) {
        Order order = new Order();
        order.setTraveler_id(traveler_id);
        order.setPost_id(post_id);
        order.setAdult_quantity(adult_quantity);
        order.setChildren_quantity(children_quantity);
        order.setBegin_date(LocalDateTime.parse(begin_date));
        order.setFee_paid(fee_paid);
        orderTripService.getOrderGuiderId_FinishDate(order);
        String description = paypalService.getTransactionDescription(order);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String transaction_id = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
            order.setTransaction_id(transaction_id);
            if (payment.getState().equals("approved")) {
                paypalService.createTransactionRecord(transaction_id, paymentId, payerId, description, true);
                orderTripService.createOrder(order);
                URI result = new URI(URL_ROOT_CLIENT + CHATBOX_PATH + order.getPost_id() + "/booking_success");
                httpHeaders.setLocation(result);
            } else {
                paypalService.createTransactionRecord(transaction_id, paymentId, payerId, description, false);
                URI result = new URI(URL_ROOT_CLIENT + CHATBOX_PATH + order.getPost_id() + "/booking_fail");
                httpHeaders.setLocation(result);
            }
        } catch (PayPalRESTException | URISyntaxException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
