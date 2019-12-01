package services.Post;

import entities.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import services.GeneralService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class PostServiceImpl implements PostService {
    private JdbcTemplate jdbcTemplate;
    private GeneralService generalService;

    public PostServiceImpl(JdbcTemplate jdbcTemplate, GeneralService generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public List<Post> findAllPostOfOneGuider(long guider_id) throws Exception {
        return jdbcTemplate.query("select * from post where guider_id = ?", new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Post(
                        resultSet.getLong("post_id"),
                        resultSet.getLong("guider_id"),
                        resultSet.getLong("location_id"),
                        resultSet.getLong("category_id"),
                        resultSet.getString("title"),
                        resultSet.getString("video_link"),
                        generalService.checkForNull(resultSet.getArray("picture_link")),
                        resultSet.getInt("total_hour"),
                        resultSet.getString("description"),
                        generalService.checkForNull(resultSet.getArray("including_service")),
                        resultSet.getBoolean("active"),
                        resultSet.getFloat("price"),
                        resultSet.getFloat("rated"),
                        resultSet.getString("reasons"),
                        resultSet.getBoolean("authorized")
                );
            }
        }, guider_id);
    }

    @Override
    public List<Post> findAllPostByCategoryId(long category_id) throws Exception {
        return jdbcTemplate.query("select * from post where category_id = ?", new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Post(
                        resultSet.getLong("post_id"),
                        resultSet.getLong("guider_id"),
                        resultSet.getLong("location_id"),
                        resultSet.getLong("category_id"),
                        resultSet.getString("title"),
                        resultSet.getString("video_link"),
                        generalService.checkForNull(resultSet.getArray("picture_link")),
                        resultSet.getInt("total_hour"),
                        resultSet.getString("description"),
                        generalService.checkForNull(resultSet.getArray("including_service")),
                        resultSet.getBoolean("active"),
                        resultSet.getLong("price"),
                        resultSet.getInt("rated"),
                        resultSet.getString("reasons"),
                        resultSet.getBoolean("authorized")
                );
            }
        }, category_id);
    }

    @Override
    public Post findSpecificPost(long post_id) throws Exception {
        return jdbcTemplate.queryForObject("select * from  post as p, locations as l,category as c where c.category_id = p.category_id and p.location_id = l.location_id and p.post_id = ?", new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Post(
                        resultSet.getLong("post_id"),
                        resultSet.getString("title"),
                        resultSet.getString("video_link"),
                        generalService.checkForNull(resultSet.getArray("picture_link")),
                        resultSet.getInt("total_hour"),
                        resultSet.getString("description"),
                        generalService.checkForNull(resultSet.getArray("including_service")),
                        resultSet.getBoolean("active"),
                        resultSet.getString("place"),
                        resultSet.getLong("price"),
                        resultSet.getLong("rated"),
                        resultSet.getString("reasons"),
                        resultSet.getString("name"),
                        resultSet.getBoolean("authorized")
                );
            }
        }, post_id);
    }

    @Override
    public List<Post> findAllPostWithGuiderName(String name) throws Exception {
        List<Post> result = new ArrayList<>();
        name = "'%" + name.toUpperCase() + "%'";
        String query = "select post_id, title, video_link, picture_link, total_hour, description, including_service, " +
                "price, post.rated, reasons, locations.city, place, category.name " +
                "from post inner join guider on post.guider_id = guider.guider_id " +
                "inner join locations on post.location_id = locations.location_id " +
                "inner join category on post.category_id = category.category_id " +
                "inner join account on post.guider_id = account.account_id " +
                "where post.active = true " +
                "and (upper(first_name) like " + name +
                " or upper(last_name) like " + name +
                " or upper(user_name) like " + name + ")";
        result = jdbcTemplate.query(query, new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Post(rs.getLong("post_id"),
                        rs.getString("title"),
                        rs.getString("video_link"),
                        generalService.checkForNull(rs.getArray("picture_link")),
                        rs.getInt("total_hour"),
                        rs.getString("description"),
                        generalService.checkForNull(rs.getArray("including_service")),
                        rs.getString("city") + " " + rs.getString("place"),
                        rs.getString("name"),
                        rs.getLong("price"),
                        rs.getLong("rated"),
                        rs.getString("reasons"));
            }
        });
        return result;
    }

    @Override
    public List<Post> findAllPostWithLocationName(String name) throws Exception {
        List<Post> result = new ArrayList<>();
        name = "'%" + name.toUpperCase() + "%'";
        String query = "select post.*, name, city, place from post " +
                "inner join category on post.category_id = category.category_id " +
                "inner join locations on post.location_id = locations.location_id " +
                "where post.active = true " +
                "and (upper(country) like " + name +
                " or upper(city) like " + name +
                " or upper(place) like " + name + ")";
        result = jdbcTemplate.query(query, new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Post(rs.getLong("post_id"), rs.getString("title"), rs.getString("video_link"),
                        generalService.checkForNull(rs.getArray("picture_link")), rs.getInt("total_hour"),
                        rs.getString("description"), generalService.checkForNull(rs.getArray("including_service")),
                        rs.getString("city") + " " + rs.getString("place"), rs.getString("name"),
                        rs.getLong("price"), rs.getLong("rated"), rs.getString("reasons"));
            }
        });
        return result;
    }

    @Override
    public void updatePost(long post_id, Post post) throws Exception {
        String query = "update post set title = ?, picture_link = ?, video_link = ?, total_hour = ?, description = ?, " +
                "including_service = ?, active = ?, location_id = ?, category_id = ?, rated = ?, price = ?, reasons = ? " +
                "where post_id = ? and authorized = true";
        jdbcTemplate.update(query, post.getTitle(),
                generalService.createSqlArray(generalService.convertBase64toImageAndChangeName(post.getPicture_link())),
                post.getVideo_link(), post.getTotal_hour(), post.getDescription(),
                generalService.createSqlArray(Arrays.asList(post.getIncluding_service())),
                post.isActive(), post.getLocation_id(), post.getCategory_id(), post.getRated(),
                post.getPrice(), post.getReasons(), post_id);
    }

    @Override
    public int insertNewPost(long guider_id, Post post) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO public.post(" +
                "guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons)" +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, new String[]{"post_id"});
            ps.setLong(1, guider_id);
            ps.setLong(2, post.getLocation_id());
            ps.setLong(3, post.getCategory_id());
            ps.setString(4, post.getTitle());
            ps.setString(5, post.getVideo_link());
            ps.setArray(6, generalService.createSqlArray(generalService.convertBase64toImageAndChangeName(post.getPicture_link())));
            ps.setInt(7, post.getTotal_hour());
            ps.setString(8, post.getDescription());
            ps.setArray(9, generalService.createSqlArray(Arrays.asList(post.getIncluding_service())));
            ps.setBoolean(10, post.isActive());
            ps.setFloat(11, post.getPrice());
            ps.setFloat(12, post.getRated());
            ps.setString(13, post.getReasons());
            return ps;
        }, keyHolder);
        return (int) keyHolder.getKey();
    }

    @Override
    public List<Post> getTopTour() throws Exception {
        return jdbcTemplate.query("SELECT * FROM post order by rated desc limit 6", new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Post(
                        resultSet.getLong("post_id"),
                        resultSet.getLong("guider_id"),
                        resultSet.getLong("location_id"),
                        resultSet.getLong("category_id"),
                        resultSet.getString("title"),
                        resultSet.getString("video_link"),
                        generalService.checkForNull(resultSet.getArray("picture_link")),
                        resultSet.getInt("total_hour"),
                        resultSet.getString("description"),
                        generalService.checkForNull(resultSet.getArray("including_service")),
                        resultSet.getBoolean("active"),
                        resultSet.getLong("price"),
                        resultSet.getInt("rated"),
                        resultSet.getString("reasons"),
                        resultSet.getBoolean("authorized")
                );
            }
        });
    }

    @Override
    public void activeDeactivePostForGuider(long post_id) throws Exception {
        String check = "select active from post where post_id = ?";
        boolean isActive = jdbcTemplate.queryForObject(check, new Object[]{post_id}, boolean.class);
        String query;
        if (isActive) {
            query = "update post set active = false where post_id = ? and authorized = true";
        } else {
            query = "update post set active = true where post_id = ? and authorized = true";
        }
        jdbcTemplate.update(query, post_id);
    }

    @Override
    public void authorizePost(long post_id) throws Exception {
        String check = "select authorized from post where post_id = ?";
        boolean isAuthorized = jdbcTemplate.queryForObject(check, new Object[]{post_id}, boolean.class);
        String query;
        if (isAuthorized) {
            query = "update post set active = false, authorized = false where post_id = ?";
        } else {
            query = "update post set authorized = true where post_id = ?";
        }
        jdbcTemplate.update(query, post_id);
    }
}
