package me.stuartdouglas.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
@WebServlet(urlPatterns = {
			"/picture/*"
	})
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    private HashMap<String, Integer> CommandsMap = new HashMap<String, Integer>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        CommandsMap.put("Image", 1);
        CommandsMap.put("profile", 2);
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
            error("Bad Operator", response);
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
            		userDoesntexist(" ", request, response);
            	}
                break;
            case 3:
            	DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            default:
                error("Bad Operator", response);
        }
	}
	
	@SuppressWarnings("unchecked")
	private void DisplayProfile(String Username, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        User user = new User();
        tm.setCluster(cluster);
        user.setCluster(cluster);
        try {
        LinkedList<UserSession> lsUser = user.getUserInfo(Username);
        if (user.isUserRegistered(Username)) {
        	request.setAttribute("UserInfo", lsUser);
            //java.util.LinkedList<UserSession> lsProfile = getUser.getUserInfo(Username);
            java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(Username.toString());
            RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
            request.setAttribute("viewingUser", Username);
            request.setAttribute("Pics", lsPics);
            rd.forward(request, response);
        }	else	{
        	userDoesntexist(Username, request, response);
        }
		
        }	catch (Exception e)	{
        	System.out.println("Error " + e);
        }

    }
	
	private void userDoesntexist(String username, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
		JSONObject json = new JSONObject();
    	Date date = new Date();
    	String dates = date.toString();
    	json.put("title", "Invalid username");
    	json.put("time_issued", dates);
    	json.put("message", "The user " + username + " does not exist");

    	//String message = "The user " + Username + " does not exist";
    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("message", json);
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

	private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setCluster(cluster);
 
        UserSession p = user.getProfilePic(Image);
            
        OutputStream out = response.getOutputStream();

        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
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
