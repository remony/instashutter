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
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Cluster cluster = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
    	cluster = CassandraHosts.getCluster();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserSession usrSession = new UserSession();
		String message = null;
		if (! usrSession.getUserSession()){
		RequestDispatcher rd=request.getRequestDispatcher("login.jsp");
	    rd.forward(request,response);
		} else {
			response.sendRedirect("/instashutter/dashboard");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password =request.getParameter("password");
		if (username != null || password != null) {
			User user = new User();
			user.setCluster(cluster);
			//Calls to User.java to confirm if the user details are correct
			boolean isValid= user.IsValidUser(username, password);
	
			HttpSession session = request.getSession();
	
			System.out.println("Session in servlet " + session);
			if (isValid){
				//Login was successful
				UserSession usrSession = new UserSession();
				usrSession.setUserSession();
				usrSession.setUsername(username);
				String message = "Success";
				session.setAttribute("message", message); 
				session.setAttribute("LoggedIn", usrSession);
				response.sendRedirect("/instashutter/dashboard");
			} else {
				//Login was not successful
				String message = "Incorrect Username or Password.";
				session.setAttribute("message", message); 
				response.sendRedirect("/instashutter/login");
			}
		} else {
			response.sendRedirect("/instashutter/login");
		}

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
