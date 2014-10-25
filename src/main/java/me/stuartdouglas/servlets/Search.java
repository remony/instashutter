package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.Pic;
import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Profile
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);
    	try {
			DisplaySearchResults(args[2], request, response);         		
    	}	catch(Exception e)	{
    		System.out.println("Error: " + e);
    		noResults(" ", request, response);
    	}
	}
	
	private void DisplaySearchResults(String keyword, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        User user = new User();
        tm.setCluster(cluster);
        user.setCluster(cluster);
        keyword = keyword.toLowerCase();
        try {
        
            LinkedList<Pic> lsPics = PicModel.getPostsContaining(keyword);
            request.setAttribute("Pics", lsPics);
            RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
            rd.forward(request, response);
        }	catch (Exception e)	{
        	System.out.println("Error getting posts from model " + e);
        }

    }
	
	//Sends an error message containing the keyword used to search using JSON
	@SuppressWarnings("unchecked")
	private void noResults(String keyword, HttpServletRequest request, HttpServletResponse response) {

		RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
		JSONObject json = new JSONObject();
    	Date date = new Date();
    	String dates = date.toString();
    	json.put("title", "No results");
    	json.put("time_issued", dates);
    	json.put("message", "No results for " + keyword + ".");

    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("message", json);
        try {
			rd.forward(request, response);
		} catch (ServletException | IOException e) {
			System.out.println("Error forwarding");
		}
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		response.sendRedirect("/instashutter/search/" + keyword);
		
	}
	
	public void destroy()	{
		try {
			if(cluster != null) cluster.close();
		}	catch(Exception e)	{
			System.out.println("error closing cassandra connection " + e);
		}
	}
}
