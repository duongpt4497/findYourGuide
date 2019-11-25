package services.Mail;

import entities.Guider;
import entities.Order;
import entities.Post;
import entities.Traveler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import services.Post.PostService;
import services.guider.GuiderService;
import services.ordertrip.OrderTripService;
import services.traveler.TravelerService;

import java.time.format.DateTimeFormatter;

@Repository
public class MailServiceImpl implements MailService {
    private static final String UNCONFIRMED = "UNCONFIRMED";
    private static final String ONGOING = "ONGOING";
    private static final String FINISHED = "FINISHED";
    private static final String CANCELLED = "CANCELLED";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JavaMailSender emailSender;
    private TravelerService travelerService;
    private GuiderService guiderService;
    private PostService postService;
    private OrderTripService orderTripService;

    @Value("${sysadmin.email}")
    private String sysadminEmail;

    @Autowired
    public MailServiceImpl(JavaMailSender jm, TravelerService ts, GuiderService gs, PostService ps, OrderTripService ots) {
        this.emailSender = jm;
        this.travelerService = ts;
        this.guiderService = gs;
        this.postService = ps;
        this.orderTripService = ots;
    }

    @Override
    public boolean sendMail(String email, String subject, String content) {
        // Create a mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(content);
        // Send mail
        try {
            this.emailSender.send(mail);
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public String getMailContent(Order order, String orderStatus) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        String content = "";
        try {
            Traveler traveler = travelerService.findTravelerWithId(order.getTraveler_id());
            Post post = postService.findSpecificPost(order.getPost_id());
            Guider guider = guiderService.findGuiderWithPostId(order.getPost_id());

            // create mail content
            content = content.concat("Dear Mr/Ms " + traveler.getLast_name() + "\n\n");
            switch (orderStatus) {
                case UNCONFIRMED:
                    content = content.concat("Your tour has been booked successfully.\n");
                    break;
                case ONGOING:
                    content = content.concat("Your tour has been accepted by " + guider.getFirst_name() + " " + guider.getLast_name() + ".\n");
                    break;
                case CANCELLED:
                    content = content.concat("Your tour has been cancelled by " + guider.getFirst_name() + " " + guider.getLast_name() + ".\n");
                    break;
                case FINISHED:
                    content = content.concat("Your tour has finished.\n");
                    break;
            }
            content = content.concat("Below is the information of your tour:\n");
            content = content.concat("Tour: " + post.getTitle() + "\n");
            content = content.concat("Your guider: " + guider.getFirst_name() + " " + guider.getLast_name() + "\n");
            content = content.concat("Begin on: " + order.getBegin_date().format(formatter)
                    + " - Expected end on: " + order.getFinish_date().format(formatter) + "\n");
            content = content.concat("The tour has " + order.getAdult_quantity() + " adults and " + order.getChildren_quantity() + " childrens.\n");
            content = content.concat("Total: " + order.getFee_paid() + "$\n\n");
            String tourStatus = "";
            if (order.getStatus() == null) {
                tourStatus = "Waiting for confirmation";
            } else {
                switch (order.getStatus()) {
                    case ONGOING:
                        tourStatus = "Ongoing";
                        break;
                    case CANCELLED:
                        tourStatus = "Cancelled";
                        break;
                    case FINISHED:
                        tourStatus = "Finished";
                        break;
                }
            }
            content = content.concat("Status: " + tourStatus + "\n\n");
            content = content.concat("Thank your for using our service. We wish you a great trip and happy experience.\n\n");
            content = content.concat("Sincerely,\n");
            content = content.concat("TravelWLocal");
            return content;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
    }
}
