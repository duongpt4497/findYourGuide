package winter.findGuider.web.api;

import entities.ChatMessage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import services.chatMessage.ChatMessageRepositoryImpl;

import java.security.Principal;

public class WebSocketChatControllerUnitTest {
    @InjectMocks
    WebSocketChatController webSocketChatController;

    @Mock
    ChatMessageRepositoryImpl chatMessageRepository;

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendMessage(){
        Principal principal= Mockito.mock(Principal.class);
        ChatMessage chatMessage = Mockito.mock(ChatMessage.class);
        SimpMessageHeaderAccessor headerAccessor = Mockito.mock(SimpMessageHeaderAccessor.class);

        webSocketChatController.sendMessage(principal,chatMessage,headerAccessor,"ha");
    }
    @Test
    public void testSendMessageWithException(){
        Principal principal= Mockito.mock(Principal.class);
        ChatMessage chatMessage = Mockito.mock(ChatMessage.class);
        SimpMessageHeaderAccessor headerAccessor = Mockito.mock(SimpMessageHeaderAccessor.class);
        ReflectionTestUtils.setField(webSocketChatController, "chatMessageRepository", null);

        webSocketChatController.sendMessage(principal,chatMessage,headerAccessor,"ha");
    }
    @Test
    public void testGetMessage(){
        /*Principal principal= Mockito.mock(Principal.class);
        ChatMessage chatMessage = Mockito.mock(ChatMessage.class);
        SimpMessageHeaderAccessor headerAccessor = Mockito.mock(SimpMessageHeaderAccessor.class);
*/
        webSocketChatController.getMessage("dung","ha",1,10);
    }
}
