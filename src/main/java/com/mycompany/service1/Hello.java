/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.service1;

import Entity.Account;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dgdbp
 */
@Path("/hello/")
public class Hello {

    @GET
    @Consumes("text/plain")
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getMsg(@PathParam("param") String msg) {
//        String output = "Jersey say : " + msg;
//        return Response.status(200).entity(output).build();
        return new Account(msg,"1");
    }
    
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response regist( Account acc){
        return Response.status(200)
                .entity(new String("Jersey say hello " + acc.getName())).build();
    }
    
}
