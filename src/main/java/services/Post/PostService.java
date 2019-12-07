package services.Post;

import entities.Post;

import java.util.List;

public interface PostService {
    List<Post> findAllPostOfOneGuider(long guider_id) throws Exception;

    List<Post> findAllPostOfOneGuiderAdmin(long guider_id) throws Exception;

    List<Post> findAllPostByCategoryId(long category_id) throws Exception;

    Post findSpecificPost(long post_id) throws Exception;

    List<Post> findAllPostWithGuiderName(String name) throws Exception;

    List<Post> findAllPostWithLocationName(String name) throws Exception;

    void updatePost(long post_id, Post post) throws Exception;

    int insertNewPost(long guider_id, Post post) throws Exception;

    List<Post> getTopTour() throws Exception;

    void activeDeactivePostForGuider(long post_id) throws Exception;

    void authorizePost(long post_id) throws Exception;
}
