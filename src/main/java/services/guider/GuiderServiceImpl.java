package services.guider;


import entities.Guider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralServiceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@Repository
public class GuiderServiceImpl implements GuiderService {
    private Logger logger;
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public GuiderServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public Guider findGuiderWithID(long id) {
        try {
            String query = "select * from guider where guider_id = " + id;
            System.out.println(query);
            return jdbcTemplate.queryForObject("select * from guider where guider_id = ?", new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(
                            rs.getLong("guider_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("age"),
                            rs.getString("about_me"),
                            rs.getLong("contribution"),
                            rs.getString("city"),
                            rs.getBoolean("active"),
                            (String[]) rs.getArray("available_language").getArray()
                    );
                };
            }, id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public Guider findGuiderWithPostId(long id) {
        try {
            return jdbcTemplate.queryForObject("select g.* from guider as g, post as p where g.guider_id = p.guider_id and p.post_id = ? GROUP BY g.guider_id", new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(
                            rs.getLong("guider_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("age"),
                            rs.getString("about_me"),
                            rs.getLong("contribution"),
                            rs.getString("city"),
                            rs.getBoolean("active"),
                            generalService.checkForNull(rs.getArray("available_language"))
                    );
                };
            }, id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    /*private Logger logger;
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public GuiderServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public Guider findGuiderWithID(long id) {
        try {
            String query = "select * from guider_info,guider_post where guider_id = " + id;
            System.out.println(query);
            return jdbcTemplate.queryForObject("select * from guider_info where guider_id = ?", new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(
                       rs.getString("name"),
                        (String[]) rs.getArray("available_language").getArray(),
                        rs.getInt("age"),
                        rs.getBoolean("gender"),
                        rs.getLong("contribution_point"),
                        rs.getString("about_guider"),
                        rs.getString("phone_number"),
                        rs.getString("city"),
                        rs.getLong("rated_star")
                   );
               };
            }, id);
        } catch (Exception e) {
            System.out.println(id + e.getCause().toString() + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void updateGUiderPost(long post_id, String video_link, List<String> photo, String[] activities, String about_post) {
try{
//    Array videoLink = null;
//    videoLink = photo;
//    for (Array photo_link:photo) {
//        videoLink=photo_link;
//    }

    String query = "UPDATE public.guider_post SET  video_link='"+video_link+"', photo_link='"+generalService.createSqlArray(photo)+"'  WHERE post_id =1";
    System.out.println(query);
    jdbcTemplate.update(query);
    System.out.println(query);
    //jdbcTemplate.update("UPDATE public.guider_post SET  video_link="+video_link+", photo_link=array[]"+photo+"+  WHERE <condition>");
}catch(Exception e ){
    logger.warning(e.getMessage());
}
    }
*/

}
