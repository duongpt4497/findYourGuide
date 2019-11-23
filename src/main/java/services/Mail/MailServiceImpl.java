package services.Mail;

import entities.Order;
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
import java.util.List;

@Repository
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private JavaMailSender emailSender;
    private GuiderService guiderService;
    private TravelerService travelerService;

    @Value("${sysadmin.email}")
    private String sysadminEmail;

    @Autowired
    public MailServiceImpl(JdbcTemplate jt, JavaMailSender jm, GuiderService gs, TravelerService ts) {
        this.jdbcTemplate = jt;
        this.emailSender = jm;
        this.guiderService = gs;
        this.travelerService = ts;
    }

    @Override
    public boolean sendMail(Order order) {
        String message;
        // Create a mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo();
        mail.setSubject("TravelWithLocal's");
        mail.setText("");
        // Send mail
        try {
            this.emailSender.send(mail);
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
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
            logger.warn(e.getMessage());
            return null;
        }
    }
}
