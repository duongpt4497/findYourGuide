package services.trip;

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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class TripServiceImplTest {

    @InjectMocks
    TripServiceImpl tripService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        ReflectionTestUtils.setField(tripService, "bufferPercent", "30");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createOrder() {
        Order order = new Order();
        order.setPost_id(1);
        tripService.createTrip(order);
    }

    @Test
    public void findOrderById() {
        Assert.assertEquals(null, tripService.findTripById(1));
    }

    @Test
    public void findOrderByStatusAsGuider1() {
        Assert.assertTrue(tripService.findTripByStatus("guider", 1, "active").isEmpty());
    }

    @Test
    public void findOrderByStatusAsGuider2() {
        Assert.assertTrue(tripService.findTripByStatus("traveler", 1, "active").isEmpty());
    }

    @Test
    public void findOrderByStatusAsGuider3() {
        Assert.assertTrue(tripService.findTripByStatus("none", 1, "active").isEmpty());
    }

    @Test
    public void acceptOrder() {
        Assert.assertEquals(false, tripService.acceptTrip(1));
    }

    @Test
    public void cancelOrder() {
        Assert.assertEquals(false, tripService.cancelTrip(1));
    }

    @Test
    public void finishOrder() {
        Assert.assertEquals(false, tripService.finishTrip(1));
    }

    @Test
    public void getOrderGuiderId_FinishDate() {
        Order order = new Order();
        tripService.getTripGuiderId_FinishDate(order);
    }

    @Test
    public void checkOrderExist() {
        Assert.assertEquals(0, tripService.checkTripExist(1));
    }

    @Test
    public void checkAvailabilityOfOrder() {
        Order order = new Order();
        order.setGuider_id(1);
        order.setBegin_date(LocalDateTime.now());
        order.setFinish_date(LocalDateTime.now());
        Assert.assertEquals(0, tripService.checkAvailabilityOfTrip(order));
    }

    @Test(expected = AssertionError.class)
    public void getGuiderAvailableHours() {
        Assert.assertTrue(tripService.getGuiderAvailableHours(LocalDate.now(), 1, 1).isEmpty());
    }

    @Test
    public void getClosestTourFinishDate() {
        Assert.assertEquals("", tripService.getClosestTripFinishDate(LocalDate.now(), 1));
    }

    @Test
    public void checkOrderReach48Hours() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-29T00:00"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T00:30");
        Assert.assertEquals(false, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void checkOrderReach48Hours2() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-27T00:00"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T00:30");
        Assert.assertEquals(true, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void checkOrderReach48Hours3() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-28T01:00"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T00:00");
        Assert.assertEquals(false, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void checkOrderReach48Hours4() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-28T01:00"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T02:00");
        Assert.assertEquals(true, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void checkOrderReach48Hours5() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-28T01:30"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T01:00");
        Assert.assertEquals(false, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void checkOrderReach48Hours6() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-28T01:30"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T01:50");
        Assert.assertEquals(true, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void checkOrderReach48Hours7() {
        Order order = new Order();
        order.setBegin_date(LocalDateTime.parse("2019-11-28T01:30"));
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T01:30");
        Assert.assertEquals(true, tripService.checkTripReach48Hours(order, rightNow));
    }

    @Test
    public void getExpectedEndTourTime() {
        LocalDateTime rightNow = LocalDateTime.parse("2019-11-26T01:30");
        Assert.assertEquals("11/26/2019 01:00", tripService.getExpectedEndTripTime(1, rightNow));
    }

    @Test
    public void getOrderByWeek() {
        Assert.assertTrue(tripService.getTripByWeek(1, Date.from(Instant.now()), Date.from(Instant.now())).isEmpty());
    }

    @Test
    public void orderFilter() throws PayPalRESTException {
        tripService.orderFilter();
    }
}