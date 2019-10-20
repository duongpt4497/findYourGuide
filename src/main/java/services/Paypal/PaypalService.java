package services.Paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import entities.TransactionRecord;

public interface PaypalService {
    public Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    public long createTransactionRecord(String payment_id, String payer_id, String description, boolean success);

    public double getTransactionPrice(long order_id);

    public String getTransactionDescription(long order_id);
}
