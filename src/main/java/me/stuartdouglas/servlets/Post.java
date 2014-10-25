package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.LinkedList;
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
        	HttpSession session = request.getSession();
    		//If the user is already logged in redirect to dashboard
    		if (session.getAttribute("LoggedIn") != null) {
        	try {
        		if (args[3] != null && args[3].toLowerCase().equals("delete"))	{
        			deletePost(args[2], request, response);
        			response.sendRedirect("/instashutter/");
        		}	else	{
        			response.sendRedirect("/instashutter/post/"+ args[2]);
        		}
        	}	catch(Exception e)	{
        		System.out.println("Error deleting");
        	}
    		}	else	{
    			request.setAttribute("message", "You must be logged in to delete");
				RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
				rd.forward(request, response);
    		}
    	}	else	{
    		 DisplayPost(args[2], request, response);
    	}   
	}
	
	/*
	 * 	Displays the post to the user
	 * 
	 */
	private void DisplayPost(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PicModel tm = new PicModel();
        tm.setCluster(cluster);
        LinkedList<Pic> lsPics = tm.getPost(User);
        
        if (session.getAttribute("LoggedIn") == null) {
			request.setAttribute("message", "You must be logged in to post and comment.");
		}
        
        RequestDispatcher rd = request.getRequestDispatcher("/post.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);
    }
	
	private void deletePost(String picid, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		PicModel pic = new PicModel();
		String username = request.getSession().getAttribute("user").toString();
		pic.setCluster(cluster);
		
		//Ensures that the user is already logged in before attempting to delete the post
		if (session.getAttribute("LoggedIn")!= null)	{
			System.out.println("User is logged in");
			
			String username2 = pic.getUserPosted(UUID.fromString(picid));
			//Will only delete if the user is logged in. 
			if (username.equals(username2))	{
				pic.deletePost(username, UUID.fromString(picid));
				System.out.println("User " + username + "has successfully delete own post " + picid);
				try {
					response.sendRedirect("/instashutter/");
				} catch (IOException e) {
					System.out.println("Unable to redirect");
				}
				
			}	else	{
				System.out.println("User " + username + " has attempted to ilegally delete post " + picid);
				try {
					response.sendRedirect("/instashutter/");
				} catch (IOException e) {
					System.out.println("Unable to redirect");
				}
			}
		}	else	{
			System.out.println("User is not logged in");
			try {
				response.sendRedirect("/instashutter/");
			} catch (IOException e) {
				System.out.println("Unable to redirect");
			}
		}	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);

		HttpSession session = request.getSession();
		PicModel pic = new PicModel();
		String username = request.getSession().getAttribute("user").toString();
		pic.setCluster(cluster);
		
		//Ensures that the user is already logged in before attempting to delete the post
		if (session.getAttribute("LoggedIn")!= null)	{
			System.out.println("User is logged in");
			String username2 = pic.getUserPosted(UUID.fromString(args[2]));
			//Will only delete if the user is logged in. 
			if (username.equals(username2))	{
				pic.deletePost(username, UUID.fromString(args[2]));
				response.sendRedirect("/instashutter/");
			}	else	{
				response.sendRedirect("/instashutter/");
			}
		}	else	{
			response.sendRedirect("/instashutter/post/"+args[2]);
		}
	}
	
	public void destroy()	{
		try {
			if(cluster != null) cluster.close();
		}	catch(Exception e)	{
			System.out.println("error closing cassandra connection " + e);
			e.printStackTrace();
		}
	}
}
