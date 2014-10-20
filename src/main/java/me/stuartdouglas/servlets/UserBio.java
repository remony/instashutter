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
 * Servlet implementation class UserBio
 */
public class UserBio extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserBio() {
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
		System.out.println("Changing user background");
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

}
