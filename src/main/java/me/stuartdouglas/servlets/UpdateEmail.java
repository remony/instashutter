package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.User;

/**
 * Servlet implementation class UpdateEmail
 */
public class UpdateEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateEmail() {
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
			
			
			response.sendRedirect("/instashutter/account");
		} else {
			System.out.println("User with incorrect credentials attempted to edit email, nothing was changed.");
		}
	}

}
