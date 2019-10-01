/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.DBContext;
import Entity.Actor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author dgdbp
 */
import java.util.List;

public class ActorModel {

    public static List<Actor> getAllActors() throws Exception {
        List<Actor> la = new ArrayList<>();
        DBContext context = new DBContext();
        Connection conn = context.getConnection();
        PreparedStatement query = conn.prepareStatement("select * from actor;");
        ResultSet rs = query.executeQuery();
        while(rs.next()){
            Actor a = new Actor();
            a.setId(rs.getInt("actor_id"));
            a.setFirst(rs.getString("first_name"));
            a.setLast(rs.getString("last_name"));
            a.setNow(rs.getDate("last_update"));
            System.out.println(a.toString());
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        getAllActors();
    }
}
