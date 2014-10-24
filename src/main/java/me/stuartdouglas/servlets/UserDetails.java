package me.stuartdouglas.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.User;

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getSession().getAttribute("user").toString();
		String newFname = request.getParameter("fname");
		String newLname = request.getParameter("lname");
		String password = request.getParameter("password");
		String location = request.getParameter("location");
		System.out.println("hey");
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
				user.updateUserDetails(username, newFname, newLname, location);
				response.sendRedirect("/instashutter/account");
			} catch (Exception e) {
				System.out.println("Error editing user information: " + e);
			}
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
