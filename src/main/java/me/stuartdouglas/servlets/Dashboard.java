package me.stuartdouglas.servlets;
/*
							TO DO
				- Display only to logged in users
				- If implemented only show followed users
					- Move code into Discovery (global public posts)
				- If implemented display only public posts
				- Display comments (limit to 3 with link to full image and comments page)
				- Display owner and posters profile pictures
				- If got time convert output to be in JSON
				- Make UI use bootstrap instead of writing css from scratch
				- Add like button or favorite or heart
				- Give a more twitter approach (since twitter does it better with the dashboard)
				- Add in searching?


*/
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

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
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.FollowingStore;
import me.stuartdouglas.stores.Pic;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Dashboard
 */
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
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
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		//If the user is not logged in display index
		boolean isLoggedIn = false;
		if(session.getAttribute("LoggedIn") != null){
			isLoggedIn = true;
			}
        if (isLoggedIn) {
			String args[] = Convertors.SplitRequestPath(request);
			
	        
	                DisplayImageList(Convertors.DISPLAY_IMAGE, args[1], request, response);
	                
		}	else	{
			response.sendRedirect("/instashutter/login");
		}
		
	}

	private void DisplayImageList(int type, String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        me.stuartdouglas.models.User user = new User();
        String username = request.getSession().getAttribute("user").toString();
        try {
	        tm.setCluster(cluster);
	        user.setCluster(cluster);
	        LinkedList<FollowingStore> lsFollowing = me.stuartdouglas.models.User.getFollowers(username);
	        LinkedList<Pic> lsPics = PicModel.getPicsForUser(username);
	        try {
	        	ListIterator<FollowingStore> listIterator = lsFollowing.listIterator();
	        	if (listIterator != null){
			        while (listIterator.hasNext()) {
			            FollowingStore fs = listIterator.next();
			            LinkedList<Pic> lsUserPosts = PicModel.getPicsForUser(fs.getFollowing());
			            lsPics.addAll(lsUserPosts);
			        }
			        
			        LinkedList<Pic> lsPicsSorted = new LinkedList<>();
			        lsPics.stream()
			        .sorted((e1, e2) -> e2.getPicAdded()
			                .compareTo(e1.getPicAdded()))
			        .forEach(lsPicsSorted::add);
			        request.setAttribute("Pics", lsPicsSorted);  
	        	}	else	{
	        		request.setAttribute("Pics", lsPics); 
	        	}
	        	 
	        }	catch (Exception e)	{
	        	System.out.println("Error processing following users posts: " + e);
	        	e.printStackTrace();
	        }
	        RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
	        rd.forward(request, response); 
	         
        } catch (Exception e) {
        	System.out.println("Error getting user dashboard: " + e);
        } 
    }

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getSession().getAttribute("user").toString();
		String comment = request.getParameter("comment");
		String picid = request.getParameter("uuid");
		System.out.println(comment + " " + picid);
		
		PicModel pic = new PicModel();
		pic.setCluster(cluster);
		
		try {
			//something should be there?
		}	catch (Exception e)	{
			System.out.println("Error posting new comment: " + e);
		}
		pic.postComment(username, picid, comment);

		
		
		response.sendRedirect("/instashutter/post/" + picid);
	}


	private void error(HttpServletResponse response) throws IOException {

        PrintWriter out;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + "Bad Operator" + "</h2>");
        out.close();
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