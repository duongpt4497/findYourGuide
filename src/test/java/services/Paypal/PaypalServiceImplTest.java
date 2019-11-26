package services.Paypal;

import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class PaypalServiceImplTest {

    @InjectMocks
    PaypalServiceImpl paypalService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void createPayment() throws PayPalRESTException {
        Assert.assertEquals(null, paypalService.createPayment(10.0, "USD", "a", "a", "a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void executePayment() throws PayPalRESTException {
        Assert.assertEquals(null, paypalService.executePayment("1", "1"));
    }

    @Test
    public void createTransactionRecord() {
        paypalService.createTransactionRecord("1", "1", "1", "a", true);
    }

    @Test
    public void getTransactionFee() {
        Order order = new Order();
        Assert.assertEquals(0, paypalService.getTransactionFee(order), 0D);
    }

    @Test
    public void getTransactionDescription() {
        Order order = new Order();
        order.setAdult_quantity(1);
        order.setChildren_quantity(1);
        order.setBegin_date(LocalDateTime.now());
        order.setFee_paid(10);
        Assert.assertEquals("On Nov 26, 2019\n. Include adult: 1, children: 1. Fee: 10.0.", paypalService.getTransactionDescription(order));
    }

    @Test
    public void getTransactionDescription2() {
        Assert.assertEquals("", paypalService.getTransactionDescription(null));
    }

    @Test(expected = NullPointerException.class)
    public void refundPayment() throws PayPalRESTException {
        Assert.assertEquals(null, paypalService.refundPayment("1"));
    }

    @Test
    public void createRefundRecord() {
        paypalService.createRefundRecord("1", "a");
    }
}