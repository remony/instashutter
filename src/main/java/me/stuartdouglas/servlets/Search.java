package me.stuartdouglas.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.Pic;
import me.stuartdouglas.stores.UserSession;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Profile
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    private final HashMap<String, Integer> CommandsMap = new HashMap<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        //CommandsMap.put("Image", 1);
        CommandsMap.put("search", 2);
        CommandsMap.put("picture", 3);
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
        int command;
        try {
            command = CommandsMap.get(args[1]);
        } catch (Exception et) {
            error(response);
            return;
        }
        switch (command) {
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            case 2:
            	try {
        			DisplayProfile(args[2], request, response);         		
            	}	catch(Exception e)	{
            		System.out.println("Error: " + e);
            		noResults(" ", request, response);
            	}
                break;
            case 3:
            	DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            default:
                error(response);
        }
	}
	
	private void DisplayProfile(String keyword, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        User user = new User();
        tm.setCluster(cluster);
        user.setCluster(cluster);
        try {
        
            LinkedList<Pic> lsPics = PicModel.getPostsContaining(keyword);
            request.setAttribute("Pics", lsPics);
            RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
            rd.forward(request, response);
        }	catch (Exception e)	{
        	System.out.println("Error getting posts from model " + e);
        }

    }
	
	private void noResults(String keyword, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
		JSONObject json = new JSONObject();
    	Date date = new Date();
    	String dates = date.toString();
    	json.put("title", "No results");
    	json.put("time_issued", dates);
    	json.put("message", "No results for " + keyword + ".");

    	//String message = "The user " + Username + " does not exist";
    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("message", json);
        try {
			rd.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setCluster(cluster);
 
        UserSession p = user.getProfilePic(Image);
            
        OutputStream out = response.getOutputStream();

        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }
	
	private void error(HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + "Bad Operator" + "</h2>");
        out.close();
    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String keyword = request.getParameter("keyword");
		response.sendRedirect("/instashutter/search/" + keyword);
		
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
