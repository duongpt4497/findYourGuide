package services.notification;


import entities.Notification;

import java.util.List;

public interface NotificationRepositoryCus {
    public void save(Notification notification);
    public List<Notification> get(String user, String receiver, int firstElement, int lastElement);
}
