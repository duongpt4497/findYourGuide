package entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Document(collection = "messageCollection")
public class ChatMessage {
    /*private int message_id;
    private int traveler_id;
    private int guider_id;
    private String message;
    private Date date;

    public ChatMessage(int message_id, int traveler_id, int guider_id, String message, Date date) {
        this.message_id = message_id;
        this.traveler_id = traveler_id;
        this.guider_id = guider_id;
        this.message = message;
        this.date = date;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getTraveler_id() {
        return traveler_id;
    }

    public void setTraveler_id(int traveler_id) {
        this.traveler_id = traveler_id;
    }

    public int getGuider_id() {
        return guider_id;
    }

    public void setGuider_id(int guider_id) {
        this.guider_id = guider_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/



        //private MessageType type;
        @Id
        private String id ;


        private String content;


        private String user;


        private String receiver;

        private Date dateReceived;

       /* public enum MessageType {
            CHAT, JOIN, LEAVE
        }

        public MessageType getType() {
            return type;
        }

        public void setType(MessageType type) {
            this.type = type;
        }*/

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String sender) {
            this.user = sender;
        }

    }