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
		
		HttpSession session = request.getSession();
		
		Object isLogged  = session.getAttribute("login.isDone");
		
		if (isLogged == null) {
			session.setAttribute("Login.target", usrSession.getUsername());
			RequestDispatcher rd=request.getRequestDispatcher("login.jsp");
		    rd.forward(request,response);
		} else {
			response.sendRedirect("/instashutter/dashboard");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
			
			
			User user = new User();
			user.setCluster(cluster);
			
			boolean isValidUser = user.IsValidUser(username, password);
			
			System.out.println("Session established: " + session);
			
			if (isValidUser){
				System.out.println("Valid user has connected");
				 session = request.getSession(true);
				 
				 userSession.setUserSession(true);
				 userSession.setUsername(username);
				 session.setAttribute("LoggedIn", userSession);
				 response.sendRedirect("/instashutter/");
			} else {
				System.out.println("Invalid user attempted to connected");
				response.sendRedirect("/instashutter/login");
			}
			
		} catch (Throwable e) {
			System.out.println("ERROR: " + e);
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
