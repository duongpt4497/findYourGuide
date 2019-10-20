package winter.findGuider.web.api;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.Paypal.PaypalPaymentIntent;
import services.Paypal.PaypalPaymentMethod;
import services.Paypal.PaypalService;

@RestController
@RequestMapping(path = "/Payment", produces = "application/json")
@CrossOrigin(origins = "*")
public class PaypalController {

    public static final String URL_PAYPAL_SUCCESS = "/Create/Success";
    public static final String URL_PAYPAL_CANCEL = "/Create/Cancel";

    private PaypalService paypalService;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public PaypalController(PaypalService ps) {
        this.paypalService = ps;
    }

    @RequestMapping("/Create/{price}")
    @ResponseStatus(HttpStatus.OK)
    public String payment(@PathVariable("price") double price) {
        String cancelUrl = "http://localhost:8080/Payment" + URL_PAYPAL_CANCEL;
        String successUrl = "http://localhost:8080/Payment" + URL_PAYPAL_SUCCESS;
        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(URL_PAYPAL_CANCEL)
    @ResponseStatus(HttpStatus.OK)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(URL_PAYPAL_SUCCESS)
    @ResponseStatus(HttpStatus.OK)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
