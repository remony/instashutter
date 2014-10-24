package me.stuartdouglas.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.Pic;
import me.stuartdouglas.stores.UserSession;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Profile
 */
public class FollowUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    private final HashMap<String, Integer> CommandsMap = new HashMap<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowUser() {
        super();
        //CommandsMap.put("Image", 1);
        CommandsMap.put("follow", 2);
        //CommandsMap.put("picture", 3);
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
		HttpSession session = request.getSession();
		//If the user is not logged in display index
		boolean isLoggedIn = false;
		if(session.getAttribute("LoggedIn") != null){
			isLoggedIn = true;
			}
        if (isLoggedIn) {
			//String args[] = Convertors.SplitRequestPath(request);
			
			response.sendRedirect("/instashutter/");
				//DisplayProfile(Convertors.DISPLAY_IMAGE, args[1], request, response);
	                
		}	else	{
			response.sendRedirect("/instashutter/login");
		}
	}
	
	private void DisplayProfile(String keyword, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
        PicModel tm = new PicModel();
        User user = new User();
        tm.setCluster(cluster);
        user.setCluster(cluster);
        keyword = keyword.toLowerCase();
        try {
        
            LinkedList<Pic> lsPics = PicModel.getPostsContaining(keyword);
            request.setAttribute("Pics", lsPics);
            RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
            rd.forward(request, response);
        }	catch (Exception e)	{
        	System.out.println("Error getting posts from model " + e);
        }*/
		
		

    }
	
	private void noResults(String keyword, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		/*
		RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
		JSONObject json = new JSONObject();
    	Date date = new Date();
    	String dates = date.toString();
    	json.put("title", "No results");
    	json.put("time_issued", dates);
    	json.put("message", "No results for " + keyword + ".");

    	//String message = "The user " + Username + " does not exist";
    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("message", json);
        try {
			rd.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

	private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setCluster(cluster);
 
        UserSession p = user.getProfilePic(Image);
            
        OutputStream out = response.getOutputStream();

        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }
	
	private void error(HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + "Bad Operator" + "</h2>");
        out.close();
    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		//If the user is not logged in display index
		boolean isLoggedIn = false;
		if(session.getAttribute("LoggedIn") != null){
			isLoggedIn = true;
			}
        if (isLoggedIn) {
			String followingUser = request.getParameter("followingUser");
			String method = request.getParameter("method");
			String username = request.getSession().getAttribute("user").toString();
			User user = new User();
			user.setCluster(cluster);
			if (!username.equals(followingUser))	{
				if (method.equals("follow"))	{
					user.followUser(username, followingUser);
					response.sendRedirect("/instashutter/profile/"+ followingUser);
				}	else if (method.equals("unfollow"))	{
					user.unFollowUser(username, followingUser);
					response.sendRedirect("/instashutter/profile/"+ followingUser);
				}
			}	else 	{
				errorMessage("Unabled to follow user.", "You (" + username + ") attempted to follow " + followingUser, request, response);
			}
			
			
	                
		}	else	{
			response.sendRedirect("/instashutter/login");
		}
		
		
	}
	
	private void errorMessage (String title, String message, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
		JSONObject json = new JSONObject();
    	Date date = new Date();
    	String dates = date.toString();
    	json.put("title", title);
    	json.put("time_issued", dates);
    	json.put("message", message);

    	//String message = "The user " + Username + " does not exist";
    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("message", json);
        try {
			rd.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
