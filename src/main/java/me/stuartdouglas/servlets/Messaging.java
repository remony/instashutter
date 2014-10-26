package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.MessageModel;
import me.stuartdouglas.models.UserModel;
import me.stuartdouglas.stores.FollowingStore;
import me.stuartdouglas.stores.MessageStore;

/**
 * Servlet implementation class Messaging
 */
public class Messaging extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    private final HashMap<String, Integer> CommandsMap = new HashMap<>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Messaging() {
        super();
        CommandsMap.put("messages", 1);
        CommandsMap.put("message", 2);
    }
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		//If the UserModel is already logged in redirect to dashboard
		if (session.getAttribute("LoggedIn") != null) {
			String args[] = Convertors.SplitRequestPath(request);
	        int command;
	        try {
	            command = CommandsMap.get(args[1]);
	        } catch (Exception e) {
	            System.out.println("Error getting args: " + e);
	            return;
	        }
	        switch (command) {
	            case 1:
	            	DisplayMessagingList(request, response); 
	                break;
	            case 2:
	            	try {
		            	if(args.length != 2){
		        			DisplayChat(args[2], request, response);
		            	}	else	{
		            		
		            		try {
								DisplayMessagingList(request, response);
							} catch (Exception e) {
								System.out.println("Error invalid user " + e);
							} 
		            	}
	            	}	catch(Exception e)	{
	            		System.out.println("Error: " + e);
	            	}
	                break;
	        }
		}	else	{
			response.sendRedirect("/instashutter/login");
		}
	}
	
	//Displays the chat messages between two users
	private void DisplayChat(String otherUsername, HttpServletRequest request, HttpServletResponse response) {
		MessageModel mm = new MessageModel();
        String username = request.getSession().getAttribute("user").toString();
        mm.setCluster(cluster);
        try {
	        LinkedList<MessageStore> lsMessage = MessageModel.getMessages(username, otherUsername);
	        if (lsMessage != null){
	        UserModel user = new UserModel();
	        if (user.isUserRegistered(otherUsername))	{
	        request.setAttribute("otherUsername", otherUsername);
	        request.setAttribute("MessageList", lsMessage);
	        RequestDispatcher rd = request.getRequestDispatcher("/messagepage.jsp");
	        rd.forward(request, response);
	        }	else	{
	        	noSuchUser(otherUsername, request, response);
	        }
	        }

        }	catch (Exception e)	{
        	System.out.println("Error display message list " + e);
        	noSuchUser(otherUsername, request, response);
        	e.printStackTrace();
        }
		
	}

	//Displays the list of users that the user is following, this allows the user to then message them
	public void DisplayMessagingList(HttpServletRequest request, HttpServletResponse response){
		UserModel mm = new UserModel();
        String username = request.getSession().getAttribute("user").toString();
        mm.setCluster(cluster);
        try {
	        LinkedList<FollowingStore> lsMessage = UserModel.getFollowing(username);
	        request.setAttribute("userList", lsMessage);
	        RequestDispatcher rd = request.getRequestDispatcher("/messaging.jsp");
	        rd.forward(request, response);
        }	catch (Exception e)	{
        	System.out.println("Error display message list " + e);
        	e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);
        System.out.println(args[2]);

    	try {
    		if(args.length != 3){
        		if (args[2] != null && args[3].toLowerCase().equals("send"))	{
        			sendMessage(args[2], request, response);
            	}	else	{
            		response.sendRedirect("/instashutter/message/" + args[2]);
            	}
        	}
    	}	catch(Exception e)	{
    		System.out.println("Error: " + e);
    	}
                
        
	}
	
	
	//sends the message to the database
	private void sendMessage(String otherUsername, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("sending message with args: " + otherUsername);
		String username = request.getSession().getAttribute("user").toString();
		String message = request.getParameter("message");

		
		MessageModel mm = new MessageModel();
		mm.setCluster(cluster);


		try {
			mm.sendMessage(username, otherUsername, message);
			response.sendRedirect("/instashutter/message/" + otherUsername);
		} catch (Exception e) {
			System.out.println("Error editing UserModel background: " + e);
		}
		
	}
	
	//Sends an error message containing the keyword used to search using JSON
		@SuppressWarnings("unchecked")
		private void noSuchUser(String keyword, HttpServletRequest request, HttpServletResponse response) {

			RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
			JSONObject json = new JSONObject();
	    	Date date = new Date();
	    	String dates = date.toString();
	    	json.put("title", "No results");
	    	json.put("time_issued", dates);
	    	json.put("message", "No results for " + keyword + ".");

	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        request.setAttribute("message", json);
	        try {
				rd.forward(request, response);
			} catch (ServletException | IOException e) {
				System.out.println("Error forwarding");
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
