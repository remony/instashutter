package me.stuartdouglas.servlets;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import me.stuartdouglas.models.*;
import me.stuartdouglas.stores.*;


@Path("/")
public class Controller {

	@GET
	@Path("/users/{user}")
	@Produces("application/json")
	
	public Response getPosts(@PathParam("user") String user,
			@QueryParam("start") int start, 
			@QueryParam("count") int count) {
		
		try {
			return Response.ok().entity(toWeb(PicModel.getPicsForUser(user))).build();
		} catch (Exception e) {
			return Response.status(404).entity("Invalid user " + user).build();
		}
	}
	
	
	private List<Pic> toWeb(List<Pic> pics) {
		List<Pic> webTweets = new ArrayList<Pic>();
        
        for (Pic rs: pics) {
            webTweets.add(new Pic());
        }
        return webTweets;
	}
	
}
