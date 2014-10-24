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
 * Servlet implementation class ProfilePersonalization
 */
public class ProfilePersonalization extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Cluster cluster;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilePersonalization() {
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
	public void destroy()	{
		try {
			if(cluster != null) cluster.close();
		}	catch(Exception e)	{
			System.out.println("error closing cassandra connection " + e);
			e.printStackTrace();
		}
	}
}
