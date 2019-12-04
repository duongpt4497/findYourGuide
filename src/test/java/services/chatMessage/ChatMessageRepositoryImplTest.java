package services.chatMessage;

import entities.ChatMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatMessageRepositoryImplTest {

    @InjectMocks
    ChatMessageRepositoryImpl chatMessageRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save() {
        chatMessageRepository.save(new ChatMessage());
    }

    @Test
    public void get() {
        Assert.assertEquals(null, chatMessageRepository.get("abc", "def", 1, 1));
    }

    @Test
    public void get2() {
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage msg = new ChatMessage();
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);

        when(mongoTemplate.find(new Query(Criteria.where("user").is("abc")
                .andOperator(Criteria.where("receiver").is("def"))).
                with(new Sort(Sort.Direction.DESC, "dateReceived")), ChatMessage.class,"messageCollection")).thenReturn(list);
        Assert.assertEquals(6, chatMessageRepository.get("abc", "def", 1, 1).size());
    }

    @Test
    public void get3() {
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage msg = new ChatMessage();
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);

        when(mongoTemplate.find(new Query(Criteria.where("user").is("abc")
                .andOperator(Criteria.where("receiver").is("def"))).
                with(new Sort(Sort.Direction.DESC, "dateReceived")), ChatMessage.class)).thenReturn(list);
        Assert.assertEquals(6, chatMessageRepository.get("abc", "def", 1, 9).size());
    }

    @Test
    public void putDataFromMongoToPostgres() throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage msg = new ChatMessage();
        msg.setReceiver("abc");
        msg.setId("1");
        msg.setContent("content");
        msg.setDateReceived(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(LocalDateTime.now())));
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);
        list.add(msg);

        Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(LocalDateTime.now().minusDays(1)));
        Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(LocalDateTime.now()));
        when(mongoTemplate.find(new Query(Criteria.where("dateReceived").gt(startDate).lt(endDate)), ChatMessage.class)).thenReturn(list);
        chatMessageRepository.putDataFromMongoToPostgres();
    }
}