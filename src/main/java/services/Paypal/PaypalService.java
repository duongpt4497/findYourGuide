package services.Paypal;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;

public interface PaypalService {
    public Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    public void createTransactionRecord(String transaction_id, String payment_id, String payer_id, String description, boolean success, long order_id);

    public double getTransactionPrice(long post_id);

    public String getTransactionDescription(long post_id);

    public Refund refundPayment(String transaction_id) throws PayPalRESTException;

    public void createRefundRecord(String transaction_id, String message);
}
