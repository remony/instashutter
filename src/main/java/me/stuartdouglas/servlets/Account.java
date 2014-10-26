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
import me.stuartdouglas.models.UserModel;
import me.stuartdouglas.stores.UserStore;

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
		UserModel tm = new UserModel();
        tm.setCluster(cluster); 
        String Username = request.getSession().getAttribute("user").toString();
        
        LinkedList<UserStore> lsUser = tm.getUserInfo(Username);
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
		            	}	else if (args[2] != null && args[2].toLowerCase().equals("deleteAccount")){
	            			deleteAccount(args[2], request, response);
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
	
	private void deleteAccount(String string, HttpServletRequest request, HttpServletResponse response) {
		String username = request.getSession().getAttribute("user").toString();
		String password = request.getParameter("password");
		UserModel UserModel = new UserModel();
		UserModel.setCluster(cluster);
		
		try {
			boolean isValidUser = UserModel.IsValidUser(username, password);
			if (isValidUser) {
				UserModel.deleteUser(username);
				response.sendRedirect("/instashutter/account");
			} else {
				System.out.println("Incorrect password or something broke.");
			}

		} catch (Exception e) {
			System.out.println("Error deleting account: " + e);
		}
	}

	private void editEmail(String string, HttpServletRequest request, HttpServletResponse response) {
		String username = request.getSession().getAttribute("user").toString();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UserModel UserModel = new UserModel();
		UserModel.setCluster(cluster);
		boolean isValidUser = UserModel.IsValidUser(username, password);
		if(isValidUser)	{
			System.out.println("UserModel with correct credentials is editing email.");
			try {
				Set<String> set = new HashSet<>();
				set.add(email);
				UserModel.updateUserEmail(username, set);
			}	catch (Exception e)	{
				System.out.println("Error changing UserModel email: " + e);
			}
			try {
				response.sendRedirect("/instashutter/account");
			} catch (IOException e) {
				System.out.println("error sending redirect" + e);
			}
		} else {
			System.out.println("UserModel with incorrect credentials attempted to edit email, nothing was changed.");
		}
	}

	private void editProfile(String string, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Changing UserModel background");
		String username = request.getSession().getAttribute("user").toString();
		String url = request.getParameter("url");
		UserModel UserModel = new UserModel();
		UserModel.setCluster(cluster);
		try {
			UserModel.updateBackground(username, url);
			response.sendRedirect("/instashutter/account");
		} catch (Exception e) {
			System.out.println("Error editing UserModel background: " + e);
		}
	}

	private void editBio(String string, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String username = request.getSession().getAttribute("user").toString();
		String bio = request.getParameter("bio");
		UserModel UserModel = new UserModel();
		UserModel.setCluster(cluster);
		try {
			UserModel.updateBio(username, bio);
			response.sendRedirect("/instashutter/account");
		} catch (Exception e) {
			System.out.println("Error editing UserModel background: " + e);
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
					UserModel tm = new UserModel();
					tm.setCluster(cluster);
					HttpSession session = request.getSession();
					String username = session.getAttribute("user").toString();
					tm.setProfilePic(b, username, type);
					is.close();
				}
				response.sendRedirect("/instashutter/account");
			}
		} catch (Exception e)	{
			System.out.println("Error uploading new profile image: " + e);
			e.printStackTrace();
		}
	}

	private void editPassword(String string, HttpServletRequest request, HttpServletResponse response) {
		String username = request.getSession().getAttribute("user").toString();
		String previousPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String newPasswordVerify = request.getParameter("newPasswordVerify");
		UserModel UserModel = new UserModel();
		UserModel.setCluster(cluster);
		
		boolean isValid = UserModel.IsValidUser(username, previousPassword);
		
		if (isValid){
			if (newPassword.equals(previousPassword)) {
			} else {
				if (newPassword.equals(newPasswordVerify)){
					UserModel.updateUserPassword(username, newPassword);
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
		UserModel UserModel = new UserModel();
		UserModel.setCluster(cluster);
		
		try {
			boolean isValidUser = UserModel.IsValidUser(username, password);
			if (isValidUser) {
				System.out.println("Correct");
			} else {
				System.out.println("Incorrect password or something broke.");
			}
			if (isValidUser) {
				UserModel.updateUserDetails(username, newFname, newLname,location);
				response.sendRedirect("/instashutter/account");
			}
		} catch (Exception e) {
			System.out.println("Error changing UserModel information: " + e);
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
