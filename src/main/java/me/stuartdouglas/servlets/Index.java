package me.stuartdouglas.servlets;
import java.io.IOException;
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
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.models.UserModel;
import me.stuartdouglas.stores.FollowingStore;
import me.stuartdouglas.stores.PicStore;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Dashboard
 */
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index() {
        super();

    }

    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//If the UserModel is not logged in display index
		boolean isLoggedIn = false;
		if(session.getAttribute("LoggedIn") != null){
			isLoggedIn = true;
		}
        if (isLoggedIn) {
	        DisplayImageList(request, response);    
		}	else	{
			PicModel pic = new PicModel();
			pic.setCluster(cluster);
			LinkedList<PicStore> lsPics = pic.getRandomPost();
				if (lsPics != null){
			request.setAttribute("background", lsPics);
			}
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
	        rd.forward(request, response); 
		}
	}

	private void DisplayImageList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        UserModel UserModel = new UserModel();
        String username = request.getSession().getAttribute("user").toString();
        System.out.println("THE USRNAME IS" + username);
        try {
	        tm.setCluster(cluster);
	        UserModel.setCluster(cluster);
	        LinkedList<FollowingStore> lsFollowing = me.stuartdouglas.models.UserModel.getFollowing(username);
	        LinkedList<PicStore> lsPics = new LinkedList<PicStore>();
	        LinkedList<PicStore> lsUserPics = new LinkedList<PicStore>();
	        
	        try {
	        	lsUserPics = PicModel.getPicsForUser(username);
	        	if (!lsUserPics.isEmpty())	{
		        	lsPics.addAll(lsUserPics);
		        }
	        	
	        }	catch(Exception e)	{
	        	System.out.println("Could not get user's posts " + e);
	        }
	        
	        try {
	        	ListIterator<FollowingStore> listIterator = lsFollowing.listIterator();
	        	while (listIterator.hasNext()) {
                    FollowingStore fs = listIterator.next();
                    LinkedList<PicStore> lsUserPosts = PicModel.getPicsForUser(fs.getFollowing());
                    if (!lsUserPosts.isEmpty())	{
                		lsPics.addAll(lsUserPosts);
                    }
                }
	        	
                LinkedList<PicStore> lsPicsSorted = new LinkedList<>();

                if (!lsPics.isEmpty())	{
                lsPics.stream()
                .sorted((e1, e2) -> e2.getPicAdded()
                        .compareTo(e1.getPicAdded()))
                .forEach(lsPicsSorted::add);
                
                request.setAttribute("Pics", lsPicsSorted);
                }

            }	catch (Exception e)	{
	        	System.out.println("Error processing following users posts: " + e);
	        	e.printStackTrace();
	        }
	        
	        
	        
	        RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
	        rd.forward(request, response); 
        } catch (Exception e) {
        	System.out.println("Error getting UserModel dashboard: " + e);
        } 
    }

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		//If the UserModel is already logged in redirect to dashboard
		if (session.getAttribute("LoggedIn") != null) {
		String username = request.getSession().getAttribute("user").toString();
		String comment = request.getParameter("comment");
		String picid = request.getParameter("uuid");
		
		PicModel pic = new PicModel();
		pic.setCluster(cluster);
		pic.postComment(username, picid, comment);
		response.sendRedirect("/instashutter/post/" + picid);
		}	else	{
			request.setAttribute("message", "You must be logged in to comment");
			RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
			rd.forward(request, response);
		}
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