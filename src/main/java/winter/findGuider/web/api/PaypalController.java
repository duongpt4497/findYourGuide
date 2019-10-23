package winter.findGuider.web.api;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.Paypal.PaypalService;

@RestController
@RequestMapping(path = "/Payment", produces = "application/json")
@CrossOrigin(origins = "*")
public class PaypalController {

    private static final String URL_PAYPAL_SUCCESS = "/Pay/Success";
    private static final String URL_PAYPAL_CANCEL = "/Pay/Cancel";
    private static final String URL_ROOT = "http://localhost:8080/Payment";

    private PaypalService paypalService;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public PaypalController(PaypalService ps) {
        this.paypalService = ps;
    }

    @RequestMapping("/Pay/{order_id}")
    @ResponseStatus(HttpStatus.OK)
    public String payment(@PathVariable("order_id") int order_id) {
        String cancelUrl = URL_ROOT + URL_PAYPAL_CANCEL;
        String successUrl = URL_ROOT + URL_PAYPAL_SUCCESS + "?order_id=" + order_id;
        try {
            double price = paypalService.getTransactionPrice(order_id);
            String description = paypalService.getTransactionDescription(order_id);
            Payment payment = paypalService.createPayment(price, "USD", description, cancelUrl, successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    // if transfer to paypal succeed
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        // if transfer to paypal fail
        return "return to post page";
    }

    @RequestMapping(URL_PAYPAL_CANCEL)
    @ResponseStatus(HttpStatus.OK)
    public String cancelPay() {
        return "return to post page";
    }

    @RequestMapping(URL_PAYPAL_SUCCESS)
    @ResponseStatus(HttpStatus.OK)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                             @RequestParam("order_id") long order_id) {
        String description = paypalService.getTransactionDescription(order_id);
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String transaction_id = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
            if (payment.getState().equals("approved")) {
                // payment succeed
                paypalService.createTransactionRecord(transaction_id, paymentId, payerId, description, true, order_id);
                return "url to success page";
            } else {
                paypalService.createTransactionRecord(transaction_id, paymentId, payerId, description, false, order_id);
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        // payment fail
        return "url to fail page";
    }

    @RequestMapping("/Refund/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public String refund(@PathVariable("transaction_id") String transaction_id) {
        String message = "success";
        try {
            Refund refund = paypalService.refundPayment(transaction_id);
            if (refund.getState().equals("completed")) {
                paypalService.createRefundRecord(transaction_id, message);
                return "url to success page?message=" + message;
            }
        } catch (PayPalRESTException e) {
            message = e.getDetails().getMessage();
            paypalService.createRefundRecord(transaction_id, message);
        }
        return "url to fail page?message=" + message;
    }
}
