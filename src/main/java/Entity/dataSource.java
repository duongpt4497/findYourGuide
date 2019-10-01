/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author dgdbp
 */
public class dataSource {
    public Connection getConnection() throws Exception {
//        Context initCtx = new InitialContext();
//        Context ctx = (Context) initCtx.lookup("java:comp/env");
//        
//        String serverName = (String)ctx.lookup("server");
//        String portNumber = (String)ctx.lookup("port"); 
//        String dbName = (String)ctx.lookup("dbName");
//        String user = (String)ctx.lookup("user");
//        String password = (String)ctx.lookup("pwd");
       
        String serverName = "localhost";
        String portNumber = "5432"; 
        String dbName = "dvdrental";
        String user = "postgres";
        String password = "`1";
        String url = "jdbc:postgresql://" + serverName + ":" + portNumber + "/" + dbName;
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
