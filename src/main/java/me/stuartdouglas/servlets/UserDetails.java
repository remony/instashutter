package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

/**
 * Servlet implementation class UserDetails
 */
public class UserDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDetails() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
    	cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		User user = new User();
		user.setCluster(cluster);
		
		String username = request.getSession().getAttribute("user").toString();
		
		LinkedList<UserSession> lsUser = user.getUserInfo(username);
		
		
		
		RequestDispatcher rd=request.getRequestDispatcher("/account/userdetails.jsp");
	    request.setAttribute("UserInfo", lsUser);
		rd.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getSession().getAttribute("user").toString();
		//String username = request.getParameter("previousFname").toString();
		String newFname = request.getParameter("fname");
		String prevFname = request.getParameter("prevFname");
		String newLname = request.getParameter("lname");
		String prevLname = request.getParameter("prevLname");
		String password = request.getParameter("password");
		
		User user = new User();
		user.setCluster(cluster);
		
		boolean isValidUser = user.IsValidUser(username, password);
		if (isValidUser) {
			System.out.println("Correct");
		} else {
			System.out.println("Incorrect password or something broke.");
		}
		if (isValidUser) {
			try {
				user.updateUserDetails(username, newFname, newLname);
				response.sendRedirect("/instashutter/account/editdetails");
			} catch (Exception e) {
				System.out.println("Error editing user information: " + e);
			}
		}
		
	}

}
