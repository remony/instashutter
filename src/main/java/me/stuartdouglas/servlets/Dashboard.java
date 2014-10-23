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
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.Pic;

/**
 * Servlet implementation class Dashboard
 */
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
	private HashMap<String, Integer> CommandsMap = new HashMap<String, Integer>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        CommandsMap.put("Image", 1);
        CommandsMap.put("dashboard", 2);
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
		HttpSession session = request.getSession();
		//If the user is not logged in display index
		boolean isLoggedIn = false;
		if(session.getAttribute("LoggedIn") != null){
			isLoggedIn = true;
			};
		if (isLoggedIn) {
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
	                DisplayImage(Convertors.DISPLAY_IMAGE,args[1], response);
	                break;
	            case 2:
	                DisplayImageList(Convertors.DISPLAY_IMAGE, args[1], request, response);
	                break;
	            case 3:
	                DisplayImage(Convertors.DISPLAY_THUMB,args[1],  response);
	                
	                break;
	            default:
	                error("Bad Operator", response);
	        }
		}	else	{
			response.sendRedirect("/instashutter/login");
		}
		
	}

	private void DisplayImageList(int type, String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        try {
        tm.setCluster(cluster);
        LinkedList<Pic> lsPics = tm.getPosts();
        int start = 0;
        if (request.getParameter("count") != null)	{
        	start =  Integer.parseInt(request.getParameter("count"));
        }
        
        
        request.setAttribute("Pics", lsPics);
        
        } catch (Exception e) {
        	System.out.println("error: " + e);
        }

        
        RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
        rd.forward(request, response);
        

    }

	private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);


        Pic p = tm.getPic(type,java.util.UUID.fromString(Image));

        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());

        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
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
			
		}	catch (Exception e)	{
			System.out.println("Error posting new comment: " + e);
		}
		pic.postComment(username, picid, comment);

		
		
		response.sendRedirect("/instashutter/post/" + picid);
	}


	private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
