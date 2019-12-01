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

import java.util.ArrayList;
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
        ChatMessage msg1 = new ChatMessage();

        when(mongoTemplate.find(new Query(Criteria.where("user").is("abc")
                .andOperator(Criteria.where("receiver").is("def"))).
                with(new Sort(Sort.Direction.DESC, "dateReceived")),ChatMessage.class)).thenReturn();
        Assert.assertEquals(null, chatMessageRepository.get("abc", "def", 1, 1));
    }

    @Test
    public void putDataFromMongoToPostgres() {
    }
}