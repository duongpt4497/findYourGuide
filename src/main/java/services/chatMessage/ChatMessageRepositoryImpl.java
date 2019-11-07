package services.chatMessage;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import entities.ChatMessage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.Iterator;
import java.util.List;



@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCus {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ChatMessage chatMessage) {
        mongoTemplate.save(chatMessage,"messageCollection");
        MongoCollection<Document> collection = mongoTemplate.getCollection("messageCollection");

        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };

        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2012-01-11T20:15:31");
            endDate =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2012-01-12T23:15:31");
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
