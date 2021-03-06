package me.stuartdouglas.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ErrorDetect
 */
public class ErrorDetect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ErrorDetect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 		Used to handle 404 errors
		 * 
		 * 
		 * 
		 */
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		RequestDispatcher rd=request.getRequestDispatcher("/404.jsp");
	    rd.forward(request,response);
	}
	
	


}
