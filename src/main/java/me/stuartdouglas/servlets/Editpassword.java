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
 * Servlet implementation class Editpassword
 */
public class Editpassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Editpassword() {
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

		String username = request.getSession().getAttribute("user").toString();
		String previousPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String newPasswordVerify = request.getParameter("newPasswordVerify");
		
		System.out.println("Previous password: " + previousPassword + "\nNew password: " + newPassword + "\nNew password verify: " + newPasswordVerify);
		
		User user = new User();
		user.setCluster(cluster);
		
		boolean isValid = user.IsValidUser(username, previousPassword);
		
		if (isValid){
			if (newPassword.equals(previousPassword)) {
				System.out.println("Passwords are the same");
			} else {
				if (newPassword.equals(newPasswordVerify)){
					System.out.println("UPDATING PASSWORD");
					user.updateUserPassword(username, newPassword);
					response.sendRedirect("/instashutter/account");
				} else {
					System.out.println("Both passwords are not the same");
					response.sendRedirect("/instashutter/account");
				}
			}
			
		}
		
		
		
		
	}

}
