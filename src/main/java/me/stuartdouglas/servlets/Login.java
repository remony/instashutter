package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
    	cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//If the user is already logged in redirect to dashboard
		if (session.getAttribute("LoggedIn") != null) {
			response.sendRedirect("/instashutter/");
		}	else	{
			//If the user is not logged in display the login view
			UserSession usrSession = new UserSession();
			session.setAttribute("username", usrSession.getUsername());
			RequestDispatcher rd=request.getRequestDispatcher("login.jsp");
		    rd.forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserSession userSession = new UserSession();
		try {	
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (username != null || password != null)	{
				User user = new User();
				user.setCluster(cluster);
				boolean isValidUser = false;
				try {
					isValidUser = user.IsValidUser(username, password);
				} catch (Exception e)	{
					System.out.println("Error processing check if valid user: "+ e);
				}
				if (isValidUser){
					System.out.println("Session established: " + session);	
					 session = request.getSession(true);
					 request.getSession().setAttribute("user", username);
					 userSession.setUserSession(true);
					 userSession.setUsername(username);
					 
					 session.setAttribute("LoggedIn", userSession);
					 response.sendRedirect("/instashutter/");
				} else {
					request.setAttribute("message", "Incorrect username/password");
					RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
					rd.forward(request, response);
				}
			}	else	{
				request.setAttribute("message", "Please enter login details");
				RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
				rd.forward(request, response);
			}
		} catch (Throwable e) {
			System.out.println("Error processing login: " + e);
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
