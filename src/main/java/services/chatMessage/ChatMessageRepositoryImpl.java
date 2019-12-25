package services.chatMessage;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import entities.Account;
import entities.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import services.account.AccountRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCus {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ChatMessage chatMessage) {
        mongoTemplate.save(chatMessage, "messageCollection");
    }

    @Autowired
    private AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public List<ChatMessage> get(String firstUser, String secondUser, int firstElement, int lastElement) {
        try{
            List<ChatMessage> allChatMessages = new ArrayList<>();
            Account account = accountRepository.findAccountByName(firstUser);
            if ( account.getRole().equals("GUIDER")){
                allChatMessages =mongoTemplate.find(new Query(Criteria.where("guider").is(firstUser)
                        .andOperator(Criteria.where("traveler").is(secondUser))).
                        with(new Sort(Sort.Direction.DESC, "dateReceived")), ChatMessage.class,"messageCollection");
            }else {
                allChatMessages=mongoTemplate.find(new Query(Criteria.where("guider").is(secondUser)
                        .andOperator(Criteria.where("traveler").is(firstUser))).
                        with(new Sort(Sort.Direction.DESC, "dateReceived")), ChatMessage.class,"messageCollection");
            }

            int count = allChatMessages.size();
            if (count >= lastElement) {
                allChatMessages=allChatMessages.subList(firstElement, lastElement);
            }
            if (count < lastElement) {
                if (count < firstElement) {
                    return new ArrayList<>();
                } else {
                    allChatMessages=allChatMessages.subList(firstElement, count);
                }
            }
//            for ( ChatMessage chatMessage : allChatMessages){
//                System.out.println(chatMessage.getDateReceived().toString());
//            }
           // Collections.reverse(allChatMessages);
            return allChatMessages;
        }catch(Exception e ){
            logger.debug(e.getMessage());
            return new ArrayList<>();
        }

    }

    @Override
    public void updateSeen(String firstUser, String secondUser) {
        try{
            Account account = accountRepository.findAccountByName(firstUser);
            Query query = new Query();
            Query query2 = new Query();
            if ( account.getRole().equals("GUIDER")){
                query.addCriteria(Criteria.where("guider").is(firstUser).andOperator(Criteria.where("traveler").is(secondUser).andOperator(Criteria.where("isSeen").is(false))));
                query2.addCriteria(Criteria.where("guider").is(firstUser).andOperator(Criteria.where("traveler").is(secondUser).andOperator(Criteria.where("isSeen").is(false))));
            }else{
                query.addCriteria(Criteria.where("guider").is(secondUser).andOperator(Criteria.where("traveler").is(firstUser).andOperator(Criteria.where("isSeen").is(false))));
                query2.addCriteria(Criteria.where("guider").is(secondUser).andOperator(Criteria.where("traveler").is(firstUser).andOperator(Criteria.where("isSeen").is(false))));
            }
            query2.limit(1);
            query2.with(new Sort(Sort.Direction.DESC, "dateReceived"));
            List<ChatMessage> allChatmessages = mongoTemplate.find(query2, ChatMessage.class,"messageCollection");
            for (ChatMessage chatMessage:allChatmessages){
                if(chatMessage.getSender() != firstUser){
                    Update update = new Update();
                    update.set("isSeen", true);
                    mongoTemplate.updateMulti(query, update, ChatMessage.class,"messageCollection");
                }
            }

        }catch(Exception e){
            logger.debug(e.getMessage());
        }

    }

    @Override
    public List<ChatMessage> getReceiver(String user, int firstElement, int lastElement) {
        try {
            List<ChatMessage> allChatMessages = new ArrayList<>();
            List<ChatMessage> allChatMessages2 = new ArrayList<>();
            List<String> allUser = new ArrayList<>();
            String user3 ="";
            mongoTemplate.getCollection("messageCollection").distinct("traveler",ChatMessage.class);
            Account account = accountRepository.findAccountByName(user);
            if ( account.getRole().equals("GUIDER") ){
                /*allUser =mongoTemplate.findDistinct(new Query(Criteria.where("guider").is(user)).
                        with(new Sort(Sort.Direction.ASC, "dateReceived")),"traveler","messageCollection",String.class);*/
                Aggregation agg = Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("guider").is(user)),
                        Aggregation.group("traveler"),
                        Aggregation.sort(Sort.Direction.DESC, "_id")
                );
                AggregationResults<ChatMessage> result= mongoTemplate.aggregate(agg, "messageCollection", ChatMessage.class);
                allChatMessages2 = result.getMappedResults();
            }else{
               /* allUser =mongoTemplate.findDistinct(new Query(Criteria.where("traveler").is(user)).
                        with(new Sort(Sort.Direction.ASC, "dateReceived")),"guider","messageCollection",String.class);*/
                Aggregation agg = Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("traveler").is(user)),
                        Aggregation.group("guider"),
                        Aggregation.sort(Sort.Direction.DESC, "_id")
                );
                AggregationResults<ChatMessage> result= mongoTemplate.aggregate(agg, "messageCollection", ChatMessage.class);
                allChatMessages2 = result.getMappedResults();
            }

            //System.out.println(allChatMessages2.size());

            //Collections.reverse(allUser);

            int count = allChatMessages2.size();

            if ( count >= lastElement){

                allChatMessages2=allChatMessages2.subList(firstElement,lastElement);
            }
            if ( count <lastElement){
                if ( count<firstElement){
                    return new ArrayList<>();
                }else{

                    allChatMessages2=allChatMessages2.subList(firstElement,count);
                }
            }

            for(ChatMessage allChatMessage2 :allChatMessages2){
                Query query = new Query();
                String recentUser =allChatMessage2.getId();
                if (account.getRole().equals("GUIDER")){
                    //recentUser= allChatMessage2.getTraveler();
                    //System.out.println("@"+recentUser);
                    query.addCriteria(Criteria.where("guider").is(user).andOperator(Criteria.where("traveler").is(recentUser)));
                }else{
                    //recentUser = allChatMessage2.getGuider();
                    //System.out.println("@"+recentUser);
                    query.addCriteria(Criteria.where("guider").is(recentUser).andOperator(Criteria.where("traveler").is(user)));
                }
                query.limit(1);
                query.with(new Sort(Sort.Direction.DESC, "dateReceived"));
                List<ChatMessage> chatMessage = mongoTemplate.find(query, ChatMessage.class,"messageCollection");
                if (chatMessage.size() >= 1) {
                    allChatMessages.add(chatMessage.get(0));
                }

            }
            return allChatMessages;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Scheduled(cron = "0 59 23 1/1 * *")
    public void putDataFromMongoToPostgres() throws ParseException {
        /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");
        LocalDateTime now = LocalDateTime.now();
        Date startDate = new Date();
        Date endDate = new Date();

        startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(now.minusDays(1)));
        endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dtf.format(now));

        System.out.println(startDate);
        System.out.println(endDate);
        List<ChatMessage> chatMessages = mongoTemplate.find(new Query(Criteria.where("dateReceived").gt(startDate).lt(endDate)), ChatMessage.class);
        for (ChatMessage chatMessage1 : chatMessages) {
            System.out.println(chatMessage1.getReceiver());
            System.out.println(chatMessage1.getId());
            System.out.println(chatMessage1.getContent());
            System.out.println(chatMessage1.getDateReceived());
        }*/

    }
}
