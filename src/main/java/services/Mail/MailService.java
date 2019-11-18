package services.Mail;

import entities.Order;

public interface MailService {
    public boolean sendMail(Order order);
}
