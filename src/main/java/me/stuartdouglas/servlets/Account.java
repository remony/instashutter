package me.stuartdouglas.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

/**
 * Servlet implementation class Account
 */
public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
	private HashMap<String, Integer> CommandsMap = new HashMap<String, Integer>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Account() {
        super();
        CommandsMap.put("account", 1);
        CommandsMap.put("edit", 2);
        CommandsMap.put("Thumb", 3);
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
		// TODO Auto-generated method stub
		String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        switch (command) {
            case 1:
            	displayAccountDetails(request, response);
                break;
            case 2:
                editAccountDetails(args[1], request, response);
                break;
            case 3:
                //DisplayImage(Convertors.DISPLAY_THUMB,args[1],  response);
                break;
            default:
                error("Bad Operator", response);
        }
		
	}
	
	
	private void editAccountDetails(String string, HttpServletRequest request,
			HttpServletResponse response) {
		String args[] = Convertors.SplitRequestPath(request);
		User tm = new User();
        tm.setCluster(cluster); 
        String Username = request.getSession().getAttribute("user").toString();

        LinkedList<UserSession> lsUser = tm.getUserInfo(Username);
        RequestDispatcher rd = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("UserInfo", lsUser);
        try {
			rd.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void displayAccountDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);
		User tm = new User();
        tm.setCluster(cluster); 
        String Username = request.getSession().getAttribute("user").toString();

        LinkedList<UserSession> lsUser = tm.getUserInfo(Username);
        RequestDispatcher rd = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("UserInfo", lsUser);
        rd.forward(request, response);
        
	}
	
	private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
