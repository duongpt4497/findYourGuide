/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import entity.Location;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author dgdbp
 */
@Repository
public class LocationRepo {

    private JdbcTemplate jdbcTemplate;


    public LocationRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
   
    }

    
    public List<Location> showAllLocation() {
        return jdbcTemplate.query("select * from locations", new RowMapper<Location>() {

            @Override
            public Location mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Location(
                        resultSet.getInt("location_id"),
                        resultSet.getString("place")
                );
            }
        });

    }
}
