package services.guider;


import entities.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import services.GeneralServiceImpl;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class PostServiceImpl implements PostService {

    private Logger logger;
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public PostServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public List<Post> findAllPost(long guider_id) {
        try {
            return jdbcTemplate.query("select * from post where guider_id = ?", new RowMapper<Post>() {
                @Override
                public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Post(
                            resultSet.getLong("post_id"),
                            resultSet.getString("title"),
                            generalService.checkForNull(resultSet.getArray("picture_link")),
                            resultSet.getString("description"),
                            resultSet.getBoolean("active")
                    );

                }
            }, guider_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause());
        }
        return null;
    }

    @Override
    public List<Post> findAllPostByCategoryId(long category_id) {
        try {
            return jdbcTemplate.query("select * from post where category_id = ?", new RowMapper<Post>() {
                @Override
                public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Post(
                            resultSet.getLong("post_id"),
                            resultSet.getString("title"),
                            generalService.checkForNull(resultSet.getArray("picture_link")),
                            resultSet.getString("description"),
                            resultSet.getBoolean("active")
                    );

                }
            }, category_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause());
        }
        return null;
    }

    @Override
    public Post findSpecificPost(long post_id) {

        try {
            return jdbcTemplate.queryForObject("select * from  post as p, locations as l where p.location_id = l.location_id and p.post_id = ?", new RowMapper<Post>() {
                @Override
                public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Post(
                            resultSet.getLong("post_id"),
                            resultSet.getString("title"),
                            resultSet.getString("video_link") != null ? resultSet.getString("video_link") : "unkonw",
                            generalService.checkForNull(resultSet.getArray("picture_link")),
                            resultSet.getInt("total_hour"),
                            resultSet.getString("description"),
                            generalService.checkForNull(resultSet.getArray("including_service")),
                            resultSet.getBoolean("active"),
                            resultSet.getString("place"),
                            resultSet.getLong("price"),
                            resultSet.getLong("rated"),
                            resultSet.getString("reasons")

                    );
                }
            }, post_id);
        } catch (Exception e) {
            System.out.println("select * from  post as p, locations as l where p.location_id = l.location_id and p.post_id = ");
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + e.getLocalizedMessage());
        }
        return new Post();
    }

    @Override
    public void updatePost(long post_id, Post post) {
        String query = "UPDATE post SET  title='" + post.getTitle() + "', picture_link='" + generalService.createSqlArray(Arrays.asList(post.getPicture_link())) + "', video_link ='"
                + post.getVideo_link() + "',total_hour=" + post.getTotal_hour() + ",description = '" + post.getDescription() + "',including_service='" +
                generalService.createSqlArray(Arrays.asList(post.getIncluding_service())) + "',active = " + post.isActive() + ",location_id = " + Integer.parseInt(post.getLocation())
                + ",rated =" +post.getRated()+",price =" +post.getPrice()+",reasons =" +post.getReasons()+" WHERE post_id =1";
        System.out.println("@@@@" + post.getLocation() + "@@@@@");
        //System.out.println(Integer.getInteger(post.getLocation()));
        jdbcTemplate.update(query);


    }

    @Override
    public int insertNewPost(long guider_id, Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        //try{
            String query = "INSERT INTO public.post(" +
                    "guider_id, location_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons)" +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, new String[]{"post_id"});
                ps.setLong(1, guider_id);
                ps.setLong(2, Long.parseLong(post.getLocation()));
                ps.setString(3, post.getTitle());
                ps.setString(4, post.getVideo_link());
                ps.setArray(5, generalService.createSqlArray(Arrays.asList(post.getPicture_link())));
                ps.setInt(6, post.getTotal_hour());
                ps.setString(7, post.getDescription());
                ps.setArray(8, generalService.createSqlArray(Arrays.asList(post.getIncluding_service())));
                ps.setBoolean(9, post.isActive());
                ps.setLong(10,post.getPrice());
                ps.setLong(11,post.getRated());
                ps.setString(12,post.getReasons());
                return ps;
            }, keyHolder);
        /*}catch(Exception e){

        }*/
        return (int) keyHolder.getKey();
    }
}
