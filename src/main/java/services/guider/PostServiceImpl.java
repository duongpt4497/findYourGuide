package services.guider;

import entities.Activity;
import entities.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralServiceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                    return new Post(resultSet.getString("title"),
                            (String[]) resultSet.getArray("picture_link").getArray(),
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
    public Post findSpecificPost(long post_id) {

        try{
            return jdbcTemplate.queryForObject("select * from  post as p, locations as l where p.location_id = l.location_id and p.post_id = ?", new RowMapper<Post>() {
                @Override
                public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Post(
                            resultSet.getString("title"),
                            resultSet.getString("video_link") != null ? resultSet.getString("video_link") : "unkonw",
                            generalService.checkForNull(resultSet.getArray("picture_link")),
                            resultSet.getInt("total_hour"),
                            resultSet.getString("description"),
                            generalService.checkForNull(resultSet.getArray("including_service")),
                            resultSet.getBoolean("active"),
                            resultSet.getString("place")

                    );
                }
            },post_id);
        }catch(Exception e ){
            System.out.println("select * from  post as p, locations as l where p.location_id = l.location_id and p.post_id = ");
            System.out.println(e.getMessage()+ e.getStackTrace() + e.getCause() +e.getLocalizedMessage());
        }
        return new Post();
    }
}
