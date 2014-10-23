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
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.stores.Pic;

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
		String args[] = Convertors.SplitRequestPath(request);
		DisplayTimeline(Convertors.DISPLAY_IMAGE, args[1], request, response);
	}

	private void DisplayTimeline(int dISPLAY_IMAGE, String string, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		PicModel pic = new PicModel();
		try {
			pic.setCluster(cluster);
			LinkedList<Pic> lsPics = pic.getPublicPosts();
			
			if (lsPics != null)	{
				System.out.println("yay posts");
				request.setAttribute("Pics",  lsPics);
			}	else	{
				System.out.println("Boo, no posts...");
				request.setAttribute("Pics",  lsPics);
			}
			RequestDispatcher rd = request.getRequestDispatcher("/explore.jsp");
	        rd.forward(request, response);
			
			
		}	catch(Exception e)	{
			System.out.println("Error getting timeline");
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
