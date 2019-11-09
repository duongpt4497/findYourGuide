package services.Feedback;

import entities.Feedback;
import entities.Guider;
import entities.Traveler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import services.guider.GuiderService;
import services.traveler.TravelerService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FeedbackServiceImpl implements FeedbackService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private JavaMailSender emailSender;
    private GuiderService guiderService;
    private TravelerService travelerService;

    @Value("${sysadmin.email}")
    private String sysadminEmail;

    @Autowired
    public FeedbackServiceImpl(JdbcTemplate jt, JavaMailSender jm, GuiderService gs, TravelerService ts) {
        this.jdbcTemplate = jt;
        this.emailSender = jm;
        this.guiderService = gs;
        this.travelerService = ts;
    }

    @Override
    public boolean sendFeedback(Feedback feedback) {
        String message;
        // Get sender info
        String role = this.getUserRole(feedback.getAccount_id());
        if (role == null) {
            return false;
        }
        if (role.equalsIgnoreCase("guider")) {
            Guider sender = guiderService.findGuiderWithID(feedback.getAccount_id());
            message = sender.getFirst_name() + " " + sender.getLast_name() + "\n"
                    + "Message: " + feedback.getMessage();
        } else {
            Traveler sender = travelerService.findTravelerWithId(feedback.getAccount_id());
            message = sender.getFirst_name() + " " + sender.getLast_name() + "\n"
                    + "Message: " + feedback.getMessage();
        }
        // Create a mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(sysadminEmail);
        mail.setSubject("TravelWithLocal's feedback");
        mail.setText(message);
        // Send mail
        try {
            this.emailSender.send(mail);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createFeedback(Feedback newFeedback) {
        try {
            String query = "insert into feedback (account_id, time_sent, message) values (?,?,?)";
            jdbcTemplate.update(query, newFeedback.getAccount_id(), Timestamp.valueOf(LocalDateTime.now()), newFeedback.getMessage());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    private String getUserRole(int id) {
        try {
            String query = "select role from account where account_id = ?";
            List<String> result = jdbcTemplate.query(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString(1);
                }
            }, id);
            if (result == null || result.isEmpty()) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
