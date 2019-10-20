package services.guider;

import entities.Post;

import java.util.List;

public interface PostService {
    List<Post> findAllPost(long guider_id);
}
