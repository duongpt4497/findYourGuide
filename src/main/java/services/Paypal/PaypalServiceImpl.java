package services.Paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaypalServiceImpl implements PaypalService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private APIContext apiContext;

    @Autowired
    public PaypalServiceImpl(JdbcTemplate jt, APIContext context) {
        this.jdbcTemplate = jt;
        this.apiContext = context;
    }

    @Override
    public Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        // Create amount
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));
        //Create transaction
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        // Create payer
        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.paypal.toString());
        // Create payment
        Payment payment = new Payment();
        payment.setIntent(PaypalPaymentIntent.sale.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);
        apiContext.setMaskRequestId(true);
        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    @Override
    public void createTransactionRecord(String transaction_id, String payment_id, String payer_id, String description, boolean success, long order_id) {
        try {
            String insertQuery = "insert into transaction (transaction_id, payment_id, payer_id, description, date_of_transaction, success, order_id) " +
                    "values (?,?,?,?,?,?,?)";
            jdbcTemplate.update(insertQuery, transaction_id, payment_id, payer_id, description,
                    new java.sql.Timestamp(System.currentTimeMillis()), success, order_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public double getTransactionPrice(long order_id) {
        double fee = 0;
        try {
            String query = "select fee_paid from ordertrip where order_id = ?";
            fee = jdbcTemplate.queryForObject(query, new Object[]{order_id}, double.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return fee;
    }

    @Override
    public String getTransactionDescription(long order_id) {
        String description = "";
        try {
            String query = "SELECT traveler.first_name as tra_first_name, traveler.last_name as tra_last_name, " +
                    "guider.first_name as gu_first_name, guider.last_name as gu_last_name, " +
                    "title, begin_date, adult_quantity as adult, " +
                    "children_quantity as children, fee_paid " +
                    "FROM ordertrip " +
                    "join post " +
                    "on ordertrip.post_id = post.post_id " +
                    "join guider " +
                    "on post.guider_id = guider.guider_id " +
                    "join traveler " +
                    "on ordertrip.traveler_id = traveler.traveler_id " +
                    "where order_id = ?";
            description = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
                    return rs.getString("title") + " on " + format.format(rs.getTimestamp("begin_date"))
                            + " of " + rs.getString("tra_first_name") + " " + rs.getString("tra_last_name")
                            + " with " + rs.getString("gu_first_name") + " " + rs.getString("gu_last_name")
                            + ". Include adult: " + rs.getInt("adult") + ", children: " + rs.getInt("children")
                            + ". Fee: " + rs.getDouble("fee_paid");
                }
            }, order_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return description;
    }

    @Override
    public Refund refundPayment(String transaction_id) throws PayPalRESTException {
        String query = "SELECT fee_paid " +
                "FROM ordertrip, transaction " +
                "where transaction_id = ? and ordertrip.order_id = transaction.order_id";
        double fee = jdbcTemplate.queryForObject(query, new Object[]{transaction_id}, double.class);
        // Create new refund
        Refund refund = new Refund();
        // Create amount
        Amount amount = new Amount();
        amount.setTotal(String.format("%.2f", fee));
        amount.setCurrency("USD");
        refund.setAmount(amount);
        Sale sale = new Sale();
        sale.setId(transaction_id);
        return sale.refund(apiContext, refund);
    }

    @Override
    public void createRefundRecord(String transaction_id, String message) {
        try {
            String query = "insert into refund (transaction_id, date_of_refund, message) values (?,?,?)";
            jdbcTemplate.update(query, transaction_id, new java.sql.Timestamp(System.currentTimeMillis()), message);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
