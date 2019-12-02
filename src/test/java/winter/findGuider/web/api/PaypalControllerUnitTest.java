package winter.findGuider.web.api;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import services.Mail.MailService;
import services.Paypal.PaypalService;

import services.account.AccountRepository;
import services.trip.TripService;
import sun.awt.image.ImageWatched;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class PaypalControllerUnitTest {
    @InjectMocks
    PaypalController paypalController;

    @Mock
    TripService orderTripService;

    @Mock
    PaypalService paypalService;

    @Mock
    MailService mailService;

    @Mock
    AccountRepository accountRepository;
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private static final String URL_PAYPAL_SUCCESS = "/Pay/Success";
    private static final String URL_PAYPAL_CANCEL = "/Pay/Cancel";
    private  String CHATBOX_PATH = "/chatbox/";
    private String URL_ROOT_SERVER="null";
    private String URL_ROOT_CLIENT="null";

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPayment() throws Exception {
        Order order = new Order(1,1,1,1, LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,0.0,"1","false");
        Payment payment = new Payment();
        List<Links> listLinks = new ArrayList<>();
        Links links = new Links();
        links.setRel("approval_url");
        listLinks.add(links);
        payment.setLinks(listLinks);
        String cancelUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_CANCEL + "?post_id=" + order.getPost_id();
        String successUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_SUCCESS + "?traveler_id=" + order.getTraveler_id()
                + "&post_id=" + order.getPost_id() + "&adult=" + order.getAdult_quantity()
                + "&children=" + order.getChildren_quantity() + "&begin_date=" + order.getBegin_date()+"&fee=" + order.getFee_paid();
        when(paypalService.getTransactionDescription(order)).thenReturn("successful");
        when(paypalService.createPayment(order.getFee_paid(), "USD", "successful", cancelUrl, successUrl)).thenReturn(payment);
        String hihi = paypalController.payment(order);
    }

    @Test
    public void testPayment2() throws Exception {
        Order order = new Order(1,1,1,1, LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,0.0,"1","false");
        Payment payment = new Payment();
        List<Links> listLinks = new ArrayList<>();
        Links links = new Links();
        links.setRel("approval_url");
        listLinks.add(links);
        payment.setLinks(listLinks);
        String cancelUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_CANCEL + "?post_id=" + order.getPost_id();
        String successUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_SUCCESS + "?traveler_id=" + order.getTraveler_id()
                + "&post_id=" + order.getPost_id() + "&adult=" + order.getAdult_quantity()
                + "&children=" + order.getChildren_quantity() + "&begin_date=" + order.getBegin_date()+"&fee=" + order.getFee_paid();
        when(paypalService.getTransactionDescription(order)).thenReturn("successful");
        when(orderTripService.checkAvailabilityOfTrip(order)).thenReturn(1);
        when(paypalService.createPayment(order.getFee_paid(), "USD", "successful", cancelUrl, successUrl)).thenReturn(payment);
        String hihi = paypalController.payment(order);
    }

    @Test(expected = AssertionError.class)
    public void testPaymentWithPaypalRESTException() throws Exception {
        thrown.expect(AssertionError.class);
        Order order = new Order(1,1,1,1, LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,0.0,"1","false");
        Payment payment = new Payment();
        List<Links> listLinks = new ArrayList<>();
        Links links = new Links();
        links.setRel("approval_url");
        listLinks.add(links);
        payment.setLinks(listLinks);
        //ReflectionTestUtils.setField(paypalController, "cancelUrl", "hihi");
        String cancelUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_CANCEL + "?post_id=" + order.getPost_id();
        String successUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_SUCCESS + "?traveler_id=" + order.getTraveler_id()
                + "&post_id=" + order.getPost_id() + "&adult=" + order.getAdult_quantity()
                + "&children=" + order.getChildren_quantity() + "&begin_date=" + order.getBegin_date()+"&fee=" + order.getFee_paid();
        when(paypalService.getTransactionDescription(order)).thenThrow(PayPalRESTException.class);
        String hihi = paypalController.payment(order);
    }

    @Test(expected = AssertionError.class)
    public void testPaymentWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Order order = new Order(1,1,1,1, LocalDateTime.parse("2019-01-01T01:01:01"),LocalDateTime.parse("2019-01-01T10:01:01"),1,1,0.0,"1","false");
        Payment payment = new Payment();
        List<Links> listLinks = new ArrayList<>();
        Links links = new Links();
        links.setRel("approval_url");
        listLinks.add(links);
        payment.setLinks(listLinks);
        //ReflectionTestUtils.setField(paypalController, "cancelUrl", "hihi");
        String cancelUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_CANCEL + "?post_id=" + order.getPost_id();
        String successUrl = URL_ROOT_SERVER + "/Payment" + URL_PAYPAL_SUCCESS + "?traveler_id=" + order.getTraveler_id()
                + "&post_id=" + order.getPost_id() + "&adult=" + order.getAdult_quantity()
                + "&children=" + order.getChildren_quantity() + "&begin_date=" + order.getBegin_date()+"&fee=" + order.getFee_paid();
        //when(orderTripService.getTripGuiderId_FinishDate(order)).thenThrow(PayPalRESTException.class);
        ReflectionTestUtils.setField(paypalController, "tripService", null);

        String hihi = paypalController.payment(order);
    }

    @Test()
    public void testCancelPay(){
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        //ReflectionTestUtils.setField(paypalController, "httpHeaders", httpHeaders);
        ResponseEntity<Object> result = paypalController.cancelPay(1);
    }

    @Test()
    public void testCancelPayWithException(){
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        ReflectionTestUtils.setField(paypalController, "URL_ROOT_CLIENT", "35456$%$%$#$");
        //ReflectionTestUtils.setField(paypalController, "CHATBOX_PATH", "#$%#");
        //ReflectionTestUtils.setField(paypalController, "httpHeaders", httpHeaders);
        ResponseEntity<Object> result = paypalController.cancelPay(1);
    }

    @Test
    public void testSuccessPay() throws Exception{
        Payment payment= new Payment();
        Transaction transaction = new Transaction();
        RelatedResources relatedResources = new RelatedResources();
        Sale sale = new Sale();
        sale.setId("asdhf123cjsd");
        relatedResources.setSale(sale);
        transaction.setRelatedResources(Collections.singletonList(relatedResources));
        payment.setTransactions(Collections.singletonList(transaction));
        payment.setState("approved");
        Order order = new Order();
        order.setTraveler_id(1);
        when(orderTripService.checkTripExist(1)).thenReturn(1);
        when(orderTripService.acceptTrip(0)).thenReturn(true);
        when(orderTripService.findTripById(0)).thenReturn(order);
        when(paypalService.executePayment("1","1")).thenReturn(payment);
        ResponseEntity<Object> result = paypalController.successPay("1","1",1,1,1,1,"2019-01-01T01:01:01",1.2);
    }

    @Test
    public void testSuccessPay2() throws Exception{
        Payment payment= new Payment();
        Transaction transaction = new Transaction();
        RelatedResources relatedResources = new RelatedResources();
        Sale sale = new Sale();
        sale.setId("asdhf123cjsd");
        relatedResources.setSale(sale);
        transaction.setRelatedResources(Collections.singletonList(relatedResources));
        payment.setTransactions(Collections.singletonList(transaction));
        payment.setState("denied");
        when(paypalService.executePayment("1","1")).thenReturn(payment);
        ResponseEntity<Object> result = paypalController.successPay("1","1",1,1,1,1,"2019-01-01T01:01:01",1.2);

    }

    @Test(expected = AssertionError.class)
    public void testSuccessPayWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Payment payment= new Payment();
        Transaction transaction = new Transaction();
        RelatedResources relatedResources = new RelatedResources();
        Sale sale = new Sale();
        sale.setId("asdhf123cjsd");
        relatedResources.setSale(sale);
        transaction.setRelatedResources(Collections.singletonList(relatedResources));
        payment.setTransactions(Collections.singletonList(transaction));
        payment.setState("denied");
        when(paypalService.executePayment("1","1")).thenThrow(PayPalRESTException.class);
        ResponseEntity<Object> result = paypalController.successPay("1","1",1,1,1,1,"2019-01-01T01:01:01",1.2);
        Assert.assertEquals(303,result.getStatusCodeValue());
    }
}
