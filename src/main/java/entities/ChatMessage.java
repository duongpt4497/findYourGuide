package entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;


@Document(collection = "messageCollection")
public class ChatMessage {

        @Id
        private String id ;


        private String content;


        private String user;


        private String receiver;

        private Date dateReceived;



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
