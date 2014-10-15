package me.stuartdouglas.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;




/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {

			HttpSession session = request.getSession();
			if (session.getAttribute("LoggedIn") == null) {
				response.sendRedirect("/instashutter/");
			} else {
			    //chain.doFilter(request, response); // Logged in, just continue chain.
				session.invalidate();
				RequestDispatcher rd=request.getRequestDispatcher("/logout.jsp");
			    rd.forward(request,response);
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
	}
	
	

}
