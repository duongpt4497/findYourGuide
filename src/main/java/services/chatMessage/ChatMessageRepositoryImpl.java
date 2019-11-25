package services.chatMessage;

import entities.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCus {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ChatMessage chatMessage) {
        mongoTemplate.save(chatMessage,"messageCollection");
    }

    @Override
    public List<ChatMessage> get(String user, String receiver, int firstElement, int lastElement) {
        List<ChatMessage> allChatMessages = mongoTemplate.find(new Query(Criteria.where("user").is(user)
                .andOperator(Criteria.where("receiver").is(receiver))).
                with(new Sort(Sort.Direction.DESC, "dateReceived")),ChatMessage.class,"messageCollection");

        
        int count = allChatMessages.size();
        if ( count >= lastElement){
            allChatMessages.subList(firstElement,lastElement);
        }
        if ( count <lastElement){
            if ( count<=firstElement){
                return null;
            }else{
                allChatMessages.subList(firstElement,count);
            }
        }
        return allChatMessages;
    }

    @Override
    public List<ChatMessage> getReceiver(String user, int firstElement, int lastElement) {
        List<ChatMessage> allChatMessages = mongoTemplate.findDistinct(new Query(Criteria.where("user").is(user)
                ).
                with(new Sort(Sort.Direction.DESC, "dateReceived")),"receiver","messageCollection",ChatMessage.class);
        int count = allChatMessages.size();
        if ( count >= lastElement){
            allChatMessages.subList(firstElement,lastElement);
        }
        if ( count <lastElement){
            if ( count<=firstElement){
                return null;
            }else{
                allChatMessages.subList(firstElement,count);
            }
        }
        return allChatMessages;
    }

    @Scheduled(cron = "0 59 23 1/1 * ? *")
    public void putDataFromMongoToPostgres(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");
        LocalDateTime now = LocalDateTime.now();
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(now.minusDays(1)));
            endDate =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(startDate);
        System.out.println(endDate);
        List<ChatMessage> chatMessages = mongoTemplate.find(new Query(Criteria.where("dateReceived").gt(startDate).lt(endDate)),ChatMessage.class);
        for ( ChatMessage chatMessage1 : chatMessages){
            System.out.println(chatMessage1.getReceiver());
            System.out.println(chatMessage1.getId());
            System.out.println(chatMessage1.getContent());
            System.out.println(chatMessage1.getDateReceived());
        }

    }
}
