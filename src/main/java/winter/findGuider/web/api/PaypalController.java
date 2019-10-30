package winter.findGuider.web.api;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.Paypal.PaypalService;
import services.ordertrip.OrderTripService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(path = "/Payment", produces = "application/json")
@CrossOrigin(origins = "*")
public class PaypalController {

    private static final String URL_PAYPAL_SUCCESS = "/Pay/Success";
    private static final String URL_PAYPAL_CANCEL = "/Pay/Cancel";
    private static final String URL_ROOT = "http://localhost:8080/Payment";

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
        String cancelUrl = URL_ROOT + URL_PAYPAL_CANCEL;
        String successUrl = URL_ROOT + URL_PAYPAL_SUCCESS;
        try {
            orderTripService.getOrderGuiderId_FinishDate(order);
            order.setFee_paid(paypalService.getTransactionFee(order));
            // Check for availability of order
            int count = orderTripService.checkAvailabilityOfOrder(order);
            if (count != 0) {
                return "return to chat page url?message=booking time not available";
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
        return "return to chat page url?message=paypal server error";
    }

    @RequestMapping(URL_PAYPAL_CANCEL)
    @ResponseStatus(HttpStatus.OK)
    public String cancelPay() {
        return "return to chat page url";
    }

    @RequestMapping(URL_PAYPAL_SUCCESS)
    @ResponseStatus(HttpStatus.OK)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        // TODO Mock data
        Order order = new Order();
        order.setTraveler_id(1);
        order.setPost_id(1);
        order.setAdult_quantity(2);
        order.setChildren_quantity(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        order.setBegin_date(LocalDateTime.parse("10/02/2019 09:00", formatter));
        order.setFee_paid(125.00);
        orderTripService.getOrderGuiderId_FinishDate(order);

        String description = paypalService.getTransactionDescription(order);
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String transaction_id = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
            order.setTransaction_id(transaction_id);
            if (payment.getState().equals("approved")) {
                paypalService.createTransactionRecord(transaction_id, paymentId, payerId, description, true, order.getPost_id());
                // Create order
                int insertedId = orderTripService.createOrder(order);
                return "url to success page";
            }
            paypalService.createTransactionRecord(transaction_id, paymentId, payerId, description, false, order.getPost_id());
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "url to fail page";
    }

    @RequestMapping("/Refund")
    @ResponseStatus(HttpStatus.OK)
    public String refund(@RequestBody String transaction_id) {
        String message = "success";
        try {
            Refund refund = paypalService.refundPayment(transaction_id);
            if (refund.getState().equals("completed")) {
                paypalService.createRefundRecord(transaction_id, message);
                return "url to success page?message=" + message;
            }
        } catch (PayPalRESTException paypalException) {
            message = paypalException.getDetails().getMessage();
            paypalService.createRefundRecord(transaction_id, message);
        }
        return "url to fail page?message=" + message;
    }
}
