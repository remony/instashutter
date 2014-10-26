package me.stuartdouglas.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datastax.driver.core.Cluster;




import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.UserModel;


/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        cluster = CassandraHosts.getCluster();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("LoggedIn") == null) {
				//If the user is not logged in or new display the register page
				RequestDispatcher rd=request.getRequestDispatcher("/register.jsp");
			    rd.forward(request,response);
			} else {
				//If the user if logged in redirect them out of the register
				response.sendRedirect("/instashutter/");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String password2 = request.getParameter("passwordAgain");
			String email = request.getParameter("email");
			String location = request.getParameter("location");
			String bio = request.getParameter("bio");
	
			UserModel user = new UserModel();
			user.setCluster(cluster);
			//If the username is already taken redirect back to register form. 
			ServletContext servletContext = request.getSession().getServletContext();
			if (user.isUserRegistered(username))	{
				response.sendRedirect("register");
			}	else 	{
				if (password.equals(password2)){
					user.RegisterUser(fname, lname, username, password, email, location, bio, servletContext);
					response.sendRedirect("Login");
				}	else	{
					System.out.println("Passwords are not the same");
					response.sendRedirect("/instashutter/register");
				}
				
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
			response.sendRedirect("/instashutter/");
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
