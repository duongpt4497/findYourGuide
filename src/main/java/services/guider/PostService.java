package services.guider;

import entities.Post;

import java.util.List;

public interface PostService {
    List<Post> findAllPost(long guider_id);

    Post findSpecificPost(long post_id);

    void updatePost(long post_id,Post post);

    int insertNewPost(long guider_id,Post post);
}
