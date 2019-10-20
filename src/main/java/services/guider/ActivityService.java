package services.guider;

import entities.Activity;

import java.util.List;

public interface ActivityService {
    List<Activity> findActivityOfAPost(long post_id);
    void updateActivity(long post_id,List<Activity> activities);
}
