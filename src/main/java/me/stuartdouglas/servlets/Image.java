package me.stuartdouglas.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.models.PicModel;
import me.stuartdouglas.stores.Pic;
import me.stuartdouglas.stores.UserSession;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Image
 */

@WebServlet(urlPatterns = {
	    "/Image",
	    "/Image/*",
	    "/Thumb/*",
	    "/Images",
	    "/Images/*",
	    "/upload"
	})
@MultipartConfig
public class Image extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private final HashMap<String, Integer> CommandsMap = new HashMap<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);
        CommandsMap.put("upload", 4);
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
            command = CommandsMap.get(args[1]);
        } catch (Exception et) {
            //error(response);
            return;
        }
        switch (command) {
            case 1:
            	DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
            	if(args.length != 3){
	            	System.out.println("Displaying image during image.java");
	            	try {
	            		if (args[3] != null && args[3].toLowerCase().equals("delete"))	{
	            			deletePost(args[2], request, response);
	            		}
	            	}	catch(Exception e)	{
	            		e.printStackTrace();
	            	}
            	}
            	break;
            case 2:
                DisplayImageList(args[2], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                break;
            case 4:
        		DisplayUpload(request, response);
            default:
                //error(response);
        }
	}
	
	
	private void DisplayUpload(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("LoggedIn") == null) {
				//If the user is not logged in or new display the register page
				response.sendRedirect("/instashutter/login");
			} else {
				//If the user if logged in redirect them out of the register
				
				RequestDispatcher rd = request.getRequestDispatcher("upload.jsp");
			    rd.forward(request,response);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
	}

	private void deletePost(String picid, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		PicModel pic = new PicModel();
		String username = request.getSession().getAttribute("user").toString();
		pic.setCluster(cluster);
		
		System.out.println("user wants to delete image " + picid);
		
		if (session.getAttribute("LoggedIn")!= null)	{
			System.out.println("User is logged in");
			
			String username2 = pic.getUserPosted(UUID.fromString(picid));
			
			if (username.equals(username2))	{
				pic.deletePost(username, UUID.fromString(picid));
				System.out.println("User " + username + "has successfully delete own post " + picid);
				
			}	else	{
				System.out.println("User " + username + " has attempted to ilegally delete post " + picid);
			}
		}	else	{
			System.out.println("User is not logged in");
		}
		
		
	}

	private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = PicModel.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/UserPics.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);

    }
	
	private void DisplayImage(int type,String Image, HttpServletResponse response) throws IOException {
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
        for (int length; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
        
    }

	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Part file = request.getPart("file");
			String description = request.getParameter("description");
			String type = file.getContentType();
	        String filename = file.getSubmittedFileName();
			String filter = request.getParameter("filterChoice");
			boolean publicPhoto;
            publicPhoto = request.getParameter("public").equals("yes");
			
			System.out.println(filter);
			
			InputStream is = request.getPart(file.getName()).getInputStream();
	        int i = is.available();
	        HttpSession session=request.getSession();
	        UserSession lg= (UserSession)session.getAttribute("LoggedIn");
	        String username="null";
	        if (lg.getUserSession()){
	            username=lg.getUsername();
	        }
	        if (i > 0) {
	            byte[] b = new byte[i + 1];
	            is.read(b);
	            System.out.println("Length: " + b.length);
	            System.out.println("Title: " + description);
	            PicModel tm = new PicModel();
	            tm.setCluster(cluster);
	
	            try {
	            	tm.insertPic(b, type, filename, username, description, filter, publicPhoto);
	            } catch (Exception e) {
	    			System.out.println("Error with uploading file: ");
	    			e.printStackTrace();
	    		}
	
	            is.close();
	        }
	         System.out.println("ended");
		} catch (Exception e) {
			System.out.println("Error processing upload request: " + e);
		}
		//response.setStatus(HttpServletResponse.SC_CREATED);
		response.sendRedirect("/instashutter/");
	}

	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Attempted Delete");
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