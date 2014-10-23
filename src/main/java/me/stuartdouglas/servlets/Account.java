package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Account
 */

public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Account() {
        super();
        HashMap<String, Integer> commandsMap = new HashMap<>();
        commandsMap.put("account", 1);
        commandsMap.put("editprofile", 2);
        commandsMap.put("editpassword", 2);
        
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("LoggedIn") != null) {
		User tm = new User();
        tm.setCluster(cluster); 
        String Username = request.getSession().getAttribute("user").toString();
        
        LinkedList<UserSession> lsUser = tm.getUserInfo(Username);
		request.setAttribute("UserInfo", lsUser);
		RequestDispatcher rd = request.getRequestDispatcher("/account.jsp");
        
        rd.forward(request, response);
		} else {
			response.sendRedirect("/instashutter/login");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
	}
	
	


	

}
