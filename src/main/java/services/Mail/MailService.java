package services.Mail;

import entities.Order;

public interface MailService {
    public boolean sendMail(String email, String subject, String content) throws Exception;

    public String getMailContent(Order order, String orderStatus) throws Exception;
}
