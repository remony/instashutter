package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.stores.Pic;

/**
 * Servlet implementation class Post
 */
public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
        super();
        HashMap<String, Integer> commandsMap = new HashMap<>();
        commandsMap.put("post", 2);
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String args[] = Convertors.SplitRequestPath(request);
        if(args.length != 3){
        	try {
        		if (args[3] != null && args[3].toLowerCase().equals("delete"))	{
        			deletePost(args[2], request, response);
        			response.sendRedirect("/instashutter/dashboard");
        		}
        	}	catch(Exception e)	{
        		e.printStackTrace();
        	}
    	}	else	{
    		 DisplayPost(args[2], request, response);
    	}
            
	}
	private void DisplayPost(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Attempted to display image");
		PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPost(User);
        RequestDispatcher rd = request.getRequestDispatcher("/post.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);
    }
	
	private void deletePost(String picid, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		PicModel pic = new PicModel();
		PicModel tm = new PicModel();
		String username = request.getSession().getAttribute("user").toString();
		tm.setCluster(cluster);
		
		System.out.println("user wants to delete image " + picid);
		
		if (session.getAttribute("LoggedIn")!= null)	{
			System.out.println("User is logged in");
			
			String username2 = pic.getUserPosted(UUID.fromString(picid));
			
			if (username.equals(username2))	{
				tm.deletePost(username, UUID.fromString(picid));
				System.out.println("User " + username + "has successfully delete own post " + picid);
				response.setStatus(HttpServletResponse.SC_OK);
				
			}	else	{
				System.out.println("User " + username + " has attempted to ilegally delete post " + picid);
			}
		}	else	{
			System.out.println("User is not logged in");
		}	
	}
}
