package services.Post;

import entities.Post;

import java.util.List;

public interface PostService {
    List<Post> findAllPostOfOneGuider(long guider_id);

    List<Post> findAllPostByCategoryId(long category_id);

    Post findSpecificPost(long post_id);

    List<Post> findAllPostWithGuiderName(String name);

    List<Post> findAllPostWithLocationName(String name);

    void updatePost(long post_id, Post post);

    int insertNewPost(long guider_id, Post post);

    List<Post> getTopTour();

    void activeDeactivePost(long post_id);
}
