package services.notification;

import entities.ChatMessage;
import entities.Notification;
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
import java.util.Date;
import java.util.List;

@Repository
public class NotificationRepositoryCusImpl implements NotificationRepositoryCus {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Notification notification) {
        mongoTemplate.save(notification,"notificationCollection");
    }

    @Override
    public List<Notification> get(String user, String receiver, int firstElement, int lastElement) {
        List<Notification> allNotifications = mongoTemplate.find(new Query(Criteria.where("user").is(user)
                .andOperator(Criteria.where("receiver").is(receiver)))
                .with(new Sort(Sort.Direction.DESC, "dateReceived")),Notification.class);
        int count = allNotifications.size();
        if ( count >= lastElement){
            allNotifications.subList(firstElement,lastElement);
        }
        if ( count <lastElement){
            if ( count<=firstElement){
                return null;
            }else{
                allNotifications.subList(firstElement,count);
            }
        }
        return allNotifications;
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
        List<Notification> notifications = mongoTemplate.find(new Query(Criteria.where("dateReceived").gt(startDate).lt(endDate)),Notification.class);
        for ( Notification notification : notifications){

        }

    }
}
