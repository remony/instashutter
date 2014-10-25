package me.stuartdouglas.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Account
 */
@MultipartConfig
public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final HashMap<String, Integer> CommandsMap = new HashMap<>();
	private Cluster cluster;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Account() {
        super();
        CommandsMap.put("account", 1);
    }
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if (session.getAttribute("LoggedIn") != null) {
		User tm = new User();
        tm.setCluster(cluster); 
        String Username = request.getSession().getAttribute("user").toString();
        
        LinkedList<UserSession> lsUser = tm.getUserInfo(Username);
		request.setAttribute("UserInfo", lsUser);
		RequestDispatcher rd = request.getRequestDispatcher("/account.jsp");
        
        rd.forward(request, response);
		} else {
			response.sendRedirect("/instashutter/login");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);
        int command;
        System.out.println(args[2]);
        try {
            command = CommandsMap.get(args[1]);
        } catch (Exception et) {
            //error(response);
            return;
        }
        switch (command) {
            case 1:
            	try {
            		if(args.length != 2){
	            		if (args[2] != null && args[2].toLowerCase().equals("details"))	{
	            			editDetails(args[2], request, response);
	            		}	else if (args[2] != null && args[2].toLowerCase().equals("password")){
	            			editPassword(args[2], request, response);
		            	}	else if (args[2] != null && args[2].toLowerCase().equals("avatar")){
	            			editAvatar(args[2], request, response);
		            	}	else if (args[2] != null && args[2].toLowerCase().equals("bio")){
	            			editBio(args[2], request, response);
		            	}	else if (args[2] != null && args[2].toLowerCase().equals("profile")){
	            			editProfile(args[2], request, response);
		            	}	else if (args[2] != null && args[2].toLowerCase().equals("email")){
	            			editEmail(args[2], request, response);
		            	}	else	{
		            		response.sendRedirect("/instashutter/account");
		            	}
	            	}
            	}	catch(Exception e)	{
            		System.out.println("Error: " + e);
        		}
                break;
            default:
            	System.out.println("invalid");
        }
	}
	
	private void editEmail(String string, HttpServletRequest request, HttpServletResponse response) {
		String username = request.getSession().getAttribute("user").toString();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		User user = new User();
		user.setCluster(cluster);
		boolean isValidUser = user.IsValidUser(username, password);
		if(isValidUser)	{
			System.out.println("User with correct credentials is editing email.");
			try {
				Set<String> set = new HashSet<>();
				set.add(email);
				user.updateUserEmail(username, set);
			}	catch (Exception e)	{
				System.out.println("Error changing user email: " + e);
			}
			try {
				response.sendRedirect("/instashutter/account");
			} catch (IOException e) {
				System.out.println("error sending redirect" + e);
			}
		} else {
			System.out.println("User with incorrect credentials attempted to edit email, nothing was changed.");
		}
	}

	private void editProfile(String string, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Changing user background");
		String username = request.getSession().getAttribute("user").toString();
		String url = request.getParameter("url");
		User user = new User();
		user.setCluster(cluster);
		try {
			user.updateBackground(username, url);
			response.sendRedirect("/instashutter/account");
		} catch (Exception e) {
			System.out.println("Error editing user background: " + e);
		}
	}

	private void editBio(String string, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String username = request.getSession().getAttribute("user").toString();
		String bio = request.getParameter("bio");
		User user = new User();
		user.setCluster(cluster);
		try {
			user.updateBio(username, bio);
			response.sendRedirect("/instashutter/account");
		} catch (Exception e) {
			System.out.println("Error editing user background: " + e);
		}		
	}

	private void editAvatar(String string, HttpServletRequest request, HttpServletResponse response) {
		try {
			for (Part part : request.getParts())	{
				System.out.println("Part name: " + part.getName());
				String type = part.getContentType();
				InputStream is = request.getPart(part.getName()).getInputStream();
				int i = is.available();
				if (i > 0) {
					byte[] b = new byte[i+1];
					is.read(b);
					System.out.println("Length: " + b.length);
					User tm = new User();
					tm.setCluster(cluster);
					HttpSession session = request.getSession();
					String user = session.getAttribute("user").toString();
					System.out.println("User: " + user);
					tm.setProfilePic(b, user, type);
					is.close();
				}
				response.sendRedirect("/instashutter/account");
			}
		} catch (Exception e)	{
			System.out.println("Error uploading new profile image: " + e);
		}
	}

	private void editPassword(String string, HttpServletRequest request, HttpServletResponse response) {
		String username = request.getSession().getAttribute("user").toString();
		String previousPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String newPasswordVerify = request.getParameter("newPasswordVerify");
		User user = new User();
		user.setCluster(cluster);
		
		boolean isValid = user.IsValidUser(username, previousPassword);
		
		if (isValid){
			if (newPassword.equals(previousPassword)) {
			} else {
				if (newPassword.equals(newPasswordVerify)){
					user.updateUserPassword(username, newPassword);
					try {
						response.sendRedirect("/instashutter/account");
					} catch (IOException e) {
						System.out.println("Error redirecting "+ e);
					}
				} else {
					System.out.println("Both passwords are not the same");
					try {
						response.sendRedirect("/instashutter/account");
					} catch (IOException e) {
						System.out.println("Error redirecting "+ e);
					}
				}
			}
		}
	}

	private void editDetails(String string, HttpServletRequest request, HttpServletResponse response) {
		String username = request.getSession().getAttribute("user").toString();
		String newFname = request.getParameter("fname");
		String newLname = request.getParameter("lname");
		String password = request.getParameter("password");
		String location = request.getParameter("location");
		User user = new User();
		user.setCluster(cluster);
		
		try {
			boolean isValidUser = user.IsValidUser(username, password);
			if (isValidUser) {
				System.out.println("Correct");
			} else {
				System.out.println("Incorrect password or something broke.");
			}
			if (isValidUser) {
				user.updateUserDetails(username, newFname, newLname,location);
				response.sendRedirect("/instashutter/account");
			}
		} catch (Exception e) {
			System.out.println("Error changing user information: " + e);
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
