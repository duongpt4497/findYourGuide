package services.Activity;

import entities.Activity;

import java.util.List;

public interface ActivityService {
    List<Activity> findActivityOfAPost(long post_id) throws Exception;
    void updateActivity(long post_id,List<Activity> activities);
}
