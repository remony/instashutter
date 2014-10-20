package me.stuartdouglas.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.stores.Pic;

/**
 * Servlet implementation class Post
 */
public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
    private HashMap<String, Integer> CommandsMap = new HashMap<String, Integer>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
        super();
        CommandsMap.put("Image", 1);
        CommandsMap.put("post", 2);
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
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                break;
            default:
                error("Bad Operator", response);
        }
	}
	private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Attempted to display image");
		PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPost(User);
        RequestDispatcher rd = request.getRequestDispatcher("/post.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);
    }
	
	private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Attempted to print image");
		
    }

	
	private void error(String mess, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Oh an error..");
        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }

}
