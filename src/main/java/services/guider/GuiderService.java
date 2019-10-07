package services.guider;



import entities.Guider;

import java.sql.Array;
import java.util.List;

public interface GuiderService {
    public Guider findGuiderWithID(long id);
    //public void updateGUiderPost(long post_id, String video_link, List<String> photo, String[] activities, String about_post);
}
