package winter.findGuider.web.api;
import entities.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.notification.NotificationRepositoryCusImpl;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocketNotificationController {
    @Autowired
    private NotificationRepositoryCusImpl notificationRepositoryCus;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/exchange.sendNoti")
    public void sendMessage( @Payload Notification notification) {
        notificationRepositoryCus.save(notification);
        this.simpMessagingTemplate.convertAndSendToUser(notification.getReceiver(),"/queue/reply", notification);
        this.simpMessagingTemplate.convertAndSendToUser(notification.getUser(),"/queue/reply", notification);

    }

    @RequestMapping(value = "/messages/{user}/{receiver}/{firstElement}/{lastElement}", method = RequestMethod.POST)
    public HttpEntity getMessage(@PathVariable("user") String user,@PathVariable("receiver") String receiver,@PathVariable("firstElement") int firstElement,@PathVariable("lastElement") int lastElement){
        List<Notification> notifications = new ArrayList<>();
        notifications = notificationRepositoryCus.get(user,receiver,firstElement,lastElement);
        return new ResponseEntity(notifications, HttpStatus.OK);
    }


}
