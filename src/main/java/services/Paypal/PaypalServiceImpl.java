package services.Paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaypalServiceImpl implements PaypalService {

    private static final Logger logger = LoggerFactory.getLogger(PaypalServiceImpl.class);
    private JdbcTemplate jdbcTemplate;
    private APIContext apiContext;

    @Autowired
    public PaypalServiceImpl(JdbcTemplate jt, APIContext context) {
        this.jdbcTemplate = jt;
        this.apiContext = context;
    }

    @Override
    public Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.paypal.toString());

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
    public long createTransactionRecord(String payment_id, String payer_id, String description, boolean success) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String insertQuery = "insert into transaction (payment_id, payer_id, description, date_of_transaction, success) " +
                    "values (?,?,?,?,?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery, new String[]{"transaction_id"});
                ps.setString(1, payment_id);
                ps.setString(2, payer_id);
                ps.setString(3, description);
                ps.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setBoolean(5, success);
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return (long) keyHolder.getKey();
    }

    @Override
    public double getTransactionPrice(long order_id) {
        String query = "select fee_paid from ordertrip where order_id = ?";
        double fee = jdbcTemplate.queryForObject(query, new Object[] {order_id}, double.class);
        return fee;
    }

    @Override
    public String getTransactionDescription(long order_id) {
        String description = "";
        try {
            String query = "SELECT traveler.first_name as tra_first_name, traveler.last_name as tra_last_name, \n" +
                    "guider.first_name as gu_first_name, guider.last_name as gu_last_name, \n" +
                    "title, begin_date, adult_quantity as adult, " +
                    "children_quantity as children, fee_paid " +
                    "FROM public.ordertrip " +
                    "join public.post " +
                    "on ordertrip.post_id = post.post_id " +
                    "join public.guider " +
                    "on post.guider_id = guider.guider_id " +
                    "join public.traveler " +
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
            logger.info(e.getMessage());
        }
        return description;
    }
}
