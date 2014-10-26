package me.stuartdouglas.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.stores.PicStore;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Explore
 */
public class Explore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Explore() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DisplayTimeline(request, response);
	}

	private void DisplayTimeline(HttpServletRequest request, HttpServletResponse response) {
		PicModel PicStore = new PicModel();
		try {
			PicStore.setCluster(cluster);
			LinkedList<PicStore> lsPics = PicModel.getPublicPosts();
            request.setAttribute("Pics",  lsPics);

			RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
			HttpSession session = request.getSession();
			//Displays a message to users that are not logged in.
			if (session.getAttribute("LoggedIn") == null) {
				request.setAttribute("message", "You must be logged in to post and comment.");
			}
			rd.forward(request, response);
		}	catch(Exception e)	{
			System.out.println("Error getting timeline " + e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/instashutter/404");
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
