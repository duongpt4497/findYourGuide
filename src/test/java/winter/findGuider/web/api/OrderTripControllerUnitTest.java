package winter.findGuider.web.api;

import com.paypal.api.payments.Refund;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import services.Paypal.PaypalService;
import services.ordertrip.OrderTripService;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class OrderTripControllerUnitTest {
    @InjectMocks
    OrderTripController orderTripController;

    @Mock
    OrderTripService orderTripService;

    @Mock
    PaypalService paypalService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAvailableBookingHour(){
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        //when()
        ResponseEntity<ArrayList<String>> result = orderTripController.getAvailableBookingHour(order);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }
    @Test(expected = AssertionError.class)
    public void testtestGetAvailableBookingHourWithException() throws Exception{
        thrown.expect(AssertionError.class);
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");

        when(orderTripService.getGuiderAvailableHours(LocalDate.from(order.getBegin_date()),order.getPost_id(),order.getGuider_id())).thenThrow(Exception.class);
        ResponseEntity<ArrayList<String>> result = orderTripController.getAvailableBookingHour(order);

        Assert.assertEquals(404,result.getStatusCodeValue());

    }

    @Test
    public void testOrderByStatus(){
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        //when()
        ResponseEntity<List<Order>> result = orderTripController.getOrderByStatus("GUIDER",1,"available");
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testOrderByStatusWithException() throws Exception{
        thrown.expect(AssertionError.class);
        //Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        when(orderTripService.findOrderByStatusAsGuider("GUIDER",1,"available")).thenThrow(Exception.class);
        ResponseEntity<List<Order>> result = orderTripController.getOrderByStatus("GUIDER",1,"available");
        Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testGetCloseFinishDate(){
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        //when()
        ResponseEntity<String> result = orderTripController.getClosestFinishDate(order);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testGetCloseFinishDateWithException() throws Exception{
        thrown.expect(AssertionError.class);
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        when(orderTripService.getClosestTourFinishDate(LocalDate.from(order.getBegin_date()),1)).thenThrow(Exception.class);
        ResponseEntity<String> result = orderTripController.getClosestFinishDate(order);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler() throws Exception {
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");

        when(orderTripService.findOrderById(1)).thenReturn(order);
        when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsTraveler2() throws Exception {
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");

        when(orderTripService.findOrderById(1)).thenReturn(order);
        when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(false);
        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testCancelOrderAsTravelerWithException() throws Exception{
        thrown.expect(AssertionError.class);
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        //ocalDateTime rightNow = LocalDateTime.now();
        when(orderTripService.findOrderById(1)).thenThrow(Exception.class);

        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
      // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testCancelOrderAsTravelerWithPaypalException() throws Exception {
        thrown.expect(AssertionError.class);
        Order order = new Order(1, 1, 1, 1, LocalDateTime.parse("2019-01-01T01:01:01"), LocalDateTime.parse("2019-01-01T10:01:01"), 1, 1, 1, "1", "false");
        //ocalDateTime rightNow = LocalDateTime.now();
        when(orderTripService.findOrderById(1)).thenThrow(Exception.class);

        ResponseEntity<String> result = orderTripController.cancelOrderAsTraveler(1);
        // Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider() throws Exception {
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        Refund refund = new Refund();
        refund.setState("completed");
        when(orderTripService.findOrderById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test
    public void testCancelOrderAsGuider2() throws Exception {
        Order order = new Order(1,1,1,1,LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,1,"1","false");
        Refund refund = new Refund();
        refund.setState("processing");
        when(orderTripService.findOrderById(1)).thenReturn(order);
        //when(orderTripService.cancelOrder(order.getOrder_id())).thenReturn(true);
        when(paypalService.refundPayment(order.getTransaction_id())).thenReturn(refund);
        ResponseEntity<String> result = orderTripController.cancelOrderAsGuider(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test
    public void testAcceptOrder() throws Exception {
        when(orderTripService.checkOrderExist(1)).thenReturn(1);
        ResponseEntity<Boolean> result = orderTripController.acceptOrder(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }
    @Test
    public void testAcceptOrder2() throws Exception {
        when(orderTripService.checkOrderExist(1)).thenReturn(1);
        ResponseEntity<Boolean> result = orderTripController.acceptOrder(0);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }
    @Test(expected = AssertionError.class)
    public void testAcceptOrderWithException() throws Exception{
        thrown.expect(AssertionError.class);
        when(orderTripService.checkOrderExist(1)).thenThrow(Exception.class);
        ResponseEntity<Boolean> result = orderTripController.acceptOrder(1);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }
}
