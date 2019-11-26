package services.Mail;

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
public class MailServiceImplTest {

    @InjectMocks
    MailServiceImpl mailService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendMail() {
        Assert.assertEquals(false, mailService.sendMail("travelwithlocalsysadm@gmail.com", "test method", "test"));
    }

    @Test
    public void getMailContent() {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 2, 1, 150, "ABCD", null);
        Assert.assertEquals(null, mailService.getMailContent(order, "UNCONFIRMED"));
    }

    @Test
    public void getMailContentError() {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 2, 1, 150, "ABCD", null);
        Assert.assertEquals(null, mailService.getMailContent(order, "UNCONFIRMED"));
    }
}