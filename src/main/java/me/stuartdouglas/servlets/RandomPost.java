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

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.stores.PicStore;

/**
 * Servlet implementation class RandomPost
 */
public class RandomPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RandomPost() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PicModel picstore = new PicModel();
		try {
			picstore.setCluster(cluster);
			LinkedList<PicStore> lsPics = picstore.getRandomPost();
            request.setAttribute("Pics",  lsPics);

			RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
			rd.forward(request, response);
		}	catch(Exception e)	{
			System.out.println("Error getting timeline " + e);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
