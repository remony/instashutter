package me.stuartdouglas.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.UserModel;
import me.stuartdouglas.stores.UserStore;

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
		//If the UserModel is already logged in redirect to dashboard
		if (session.getAttribute("LoggedIn") != null) {
			response.sendRedirect("/instashutter/");
		}	else	{
			//If the UserModel is not logged in display the login view
			UserStore usrSession = new UserStore();
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
		UserStore us = new UserStore();
		try {	
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (username != null || password != null)	{
				UserModel UserModel = new UserModel();
				UserModel.setCluster(cluster);
				boolean isValidUser = false;
				try {
					isValidUser = UserModel.IsValidUser(username, password);
				} catch (Exception e)	{
					System.out.println("Error processing check if valid UserModel: "+ e);
				}
				if (isValidUser){
					System.out.println("Session established: " + session);	
					 session = request.getSession(true);
					 request.getSession().setAttribute("user", username);
					 us.setUserSession(true);
					 us.setUsername(username);
					 
					 session.setAttribute("LoggedIn", us);
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
