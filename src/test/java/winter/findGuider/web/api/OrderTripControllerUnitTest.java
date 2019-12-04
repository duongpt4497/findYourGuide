package winter.findGuider.web.api;

import com.paypal.api.payments.Error;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import entities.Guider;
import entities.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import services.Mail.MailService;
import services.Paypal.PaypalService;
import services.account.AccountRepository;
import services.contributionPoint.ContributionPointService;
import services.guider.GuiderService;
import services.trip.TripService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class OrderTripControllerUnitTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    TripController orderTripController;
    @Mock
    TripService orderTripService;
    @Mock
    PaypalService paypalService;
    @Mock
    AccountRepository accountRepository;
    @Mock
    MailService mailService;

    @Mock
    GuiderService guiderService;

    @Mock
    ContributionPointService contributionPointService;
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAvailableBookingHour() {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        //when()
        ResponseEntity<ArrayList<String>> result = orderTripController.getAvailableBookingHour(order);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testtestGetAvailableBookingHourWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");

        when(orderTripService.getGuiderAvailableHours(LocalDate.from(order.getBegin_date()), order.getPost_id(), order.getGuider_id())).thenThrow(Exception.class);
        ResponseEntity<ArrayList<String>> result = orderTripController.getAvailableBookingHour(order);

        Assert.assertEquals(404, result.getStatusCodeValue());

    }

    @Test
    public void testOrderByStatus() {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        //when()
        ResponseEntity<List<Order>> result = orderTripController.getOrderByStatus("GUIDER", 1, "available");
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testOrderByStatusWithException() throws Exception {
        thrown.expect(AssertionError.class);
        //Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        when(orderTripService.findTripByStatus("GUIDER", 1, "available")).thenThrow(Exception.class);
        ResponseEntity<List<Order>> result = orderTripController.getOrderByStatus("GUIDER", 1, "available");
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testGetCloseFinishDate() {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        //when()
        ResponseEntity<String> result = orderTripController.getClosestFinishDate(order);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testGetCloseFinishDateWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        when(orderTripService.getClosestTripFinishDate(LocalDate.from(order.getBegin_date()), 1)).thenThrow(Exception.class);
        ResponseEntity<String> result = orderTripController.getClosestFinishDate(order);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");

        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.cancelTrip(order.gettrip_id())).thenReturn(true);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler2() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");

        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.cancelTrip(order.gettrip_id())).thenReturn(false);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testCancelOrderAsTravelerWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        //ocalDateTime rightNow = LocalDateTime.now();
        when(orderTripService.findTripById(1)).thenThrow(Exception.class);

        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler3() throws Exception {

        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Refund refund = new Refund();
        refund.setState("completed");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.checkTripReach48Hours(order, rightNow2)).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler4() throws Exception {

        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Refund refund = new Refund();
        refund.setState("completed");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.checkTripReach48Hours(order, rightNow2)).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        when(orderTripService.cancelTrip(order.gettrip_id())).thenReturn(true);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler5() throws Exception {

        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Refund refund = new Refund();
        refund.setState("processing");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.checkTripReach48Hours(order, rightNow2)).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTravelerWithPayPalRESRException() throws Exception {

        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Refund refund = new Refund();
        refund.setState("processing");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        PayPalRESTException payPalRESTException = Mockito.mock(PayPalRESTException.class);
        Error error = new Error();
        error.setMessage("payment fail");
        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.checkTripReach48Hours(order, rightNow2)).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenThrow(payPalRESTException);
        when(payPalRESTException.getDetails()).thenReturn(error);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTravelerWithExceptionNestedInPayPalRESRException() throws Exception {

        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Refund refund = new Refund();
        refund.setState("processing");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        PayPalRESTException payPalRESTException = Mockito.mock(PayPalRESTException.class);
        Error error = new Error();
        error.setMessage("payment fail");
        when(orderTripService.findTripById(1)).thenReturn(order);
        when(orderTripService.checkTripReach48Hours(order, rightNow2)).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenThrow(payPalRESTException);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Refund refund = new Refund();
        refund.setState("completed");
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider2() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Refund refund = new Refund();
        refund.setState("processing");
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider3() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Refund refund = new Refund();
        refund.setState("completed");
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        when(orderTripService.cancelTrip(1)).thenReturn(true);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider4() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Guider guider = Mockito.mock(Guider.class);
        Refund refund = new Refund();
        refund.setState("completed");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        when(orderTripService.checkTripReach48Hours(order,rightNow2)).thenReturn(true);
        when(guiderService.findGuiderWithPostId(order.getPost_id())).thenReturn(guider);
        when(orderTripService.cancelTrip(order.gettrip_id())).thenReturn(true);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider5() throws Exception {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Guider guider = Mockito.mock(Guider.class);
        Refund refund = new Refund();
        refund.setState("completed");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime rightNow = LocalDateTime.now();
        rightNow.format(formatter);
        String si = rightNow.format(formatter);
        LocalDateTime rightNow2 = LocalDateTime.parse(si);
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        when(orderTripService.checkTripReach48Hours(order,rightNow2)).thenReturn(true);
        when(guiderService.findGuiderWithPostId(order.getPost_id())).thenReturn(guider);
        when(orderTripService.cancelTrip(order.gettrip_id())).thenReturn(false);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuiderWithPaypalRESTExceptionException() throws Exception, PayPalRESTException {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Refund refund = new Refund();
        refund.setState("completed");
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        PayPalRESTException payPalRESTException = Mockito.mock(PayPalRESTException.class);
        Error error = new Error();
        error.setMessage("payment fail");
        when(paypalService.refundPayment(order.getTransaction_id())).thenThrow(payPalRESTException);
        when(payPalRESTException.getDetails()).thenReturn(error);
        when(orderTripService.cancelTrip(1)).thenReturn(true);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuiderWithExceptionNestedInPaypalRESTException() throws Exception, PayPalRESTException {
        Order order = Mockito.mock(Order.class);
        Refund refund = new Refund();
        refund.setState("completed");
        when(orderTripService.findTripById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        PayPalRESTException payPalRESTException = Mockito.mock(PayPalRESTException.class);
        Error error = new Error();
        error.setMessage("payment fail");

        when(paypalService.refundPayment(order.getTransaction_id())).thenThrow(payPalRESTException);

        when(orderTripService.cancelTrip(1)).thenReturn(true);

        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuiderWithException() throws Exception, PayPalRESTException {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        Refund refund = new Refund();
        refund.setState("completed");
        when(orderTripService.findTripById(1)).thenThrow(Exception.class);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenThrow(PayPalRESTException.class);
        when(orderTripService.cancelTrip(1)).thenReturn(true);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testAcceptOrder() throws Exception {
        when(orderTripService.checkTripExist(1)).thenReturn(1);

        ResponseEntity<Boolean> result = orderTripController.acceptOrder(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testAcceptOrder2() throws Exception {
        Order order = new Order();
        order.setTraveler_id(1);
        when(orderTripService.checkTripExist(1)).thenReturn(1);
        when(orderTripService.acceptTrip(0)).thenReturn(true);
        when(orderTripService.findTripById(0)).thenReturn(order);

        ResponseEntity<Boolean> result = orderTripController.acceptOrder(0);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testAcceptOrderWithException() throws Exception {
        thrown.expect(AssertionError.class);
        when(orderTripService.checkTripExist(1)).thenThrow(Exception.class);
        ResponseEntity<Boolean> result = orderTripController.acceptOrder(1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testGetOrderByWeek() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH");
        Date date = formatter.parse("12/12/2019 12");
        ResponseEntity<List<Order>> result = orderTripController.getOrderByWeek(1, date);
    }

    @Test
    public void testGetOrderByWeekWithException() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH");
        Date date = formatter.parse("12/12/2019 12");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date start = cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date end = cal.getTime();
        when(orderTripService.getTripByWeek(1, start, end)).thenThrow(Exception.class);
        ResponseEntity<List<Order>> result = orderTripController.getOrderByWeek(1, date);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testGetExpectedTourEnd() {
        Order order = Mockito.mock(Order.class);
        ResponseEntity<String> result = orderTripController.getExpectedTourEnd(order);
    }

    @Test
    public void testGetExpectedTourEndWithException() throws Exception {
        Order order = Mockito.mock(Order.class);
        when(orderTripService.getExpectedEndTripTime(order.getPost_id(), order.getBegin_date())).thenThrow(Exception.class);
        ResponseEntity<String> result = orderTripController.getExpectedTourEnd(order);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }
}
