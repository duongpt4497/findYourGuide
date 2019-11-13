package winter.findGuider.web.api;

import entities.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.chatMessage.ChatMessageRepositoryImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocketChatController {

    @Autowired
    private ChatMessageRepositoryImpl chatMessageRepository;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Principal principal, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor , @PathVariable("receiver") String receiver) {
        chatMessageRepository.save(chatMessage);
        this.simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver(),"/queue/reply", chatMessage);
        this.simpMessagingTemplate.convertAndSendToUser(chatMessage.getUser(),"/queue/reply", chatMessage);

    }

    @RequestMapping(value = "/messages/{user}/{receiver}/{firstElement}/{lastElement}", method = RequestMethod.POST)
    public HttpEntity getMessage(@PathVariable("user") String user,@PathVariable("receiver") String receiver,@PathVariable("firstElement") int firstElement,@PathVariable("lastElement") int lastElement){
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages = chatMessageRepository.get(user,receiver,firstElement,lastElement);
        return new ResponseEntity(chatMessages, HttpStatus.OK);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("user", chatMessage.getUser());
        return chatMessage;
    }
}