package winter.findGuider.web.api;

import entities.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketChatController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketChatController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Principal principal, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor ) {
        /*MessageHeaders msgHeaders = headerAccessor.getMessageHeaders();
        Principal princ = (Principal) msgHeaders.get("receiver");

        if(princ != null){
            logger.info(princ.toString());
        }

        SimpMessageHeaderAccessor ha = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        ha.setSessionId(headerAccessor.getSessionId());

        ha.setLeaveMutable(true);*/
//logger.info(headerAccessor.getFirstNativeHeader(chatMessage.getReceiver()));
        logger.info(chatMessage.getReceiver() +"@@");
       this.simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver(),"/queue/reply", chatMessage);
        this.simpMessagingTemplate.convertAndSendToUser(chatMessage.getUser(),"/queue/reply", chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("user", chatMessage.getUser());
        return chatMessage;
    }
}