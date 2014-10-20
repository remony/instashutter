package me.stuartdouglas.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

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

import com.datastax.driver.core.Cluster;

import me.stuartdouglas.lib.CassandraHosts;
import me.stuartdouglas.models.User;
import me.stuartdouglas.stores.UserSession;

/**
 * Servlet implementation class EditProfileImage
 */

@MultipartConfig
public class EditProfileImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfileImage() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
    	cluster = CassandraHosts.getCluster();
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("User has attempted to alter profile image");
		try {
			for (Part part : request.getParts())	{
				System.out.println("Part name: " + part.getName());
				String type = part.getContentType();
				InputStream is = request.getPart(part.getName()).getInputStream();
				int i = is.available();
				if (i > 0) {
					byte[] b = new byte[i+1];
					is.read(b);
					System.out.println("Length: " + b.length);
					User tm = new User();
					tm.setCluster(cluster);
					HttpSession session = request.getSession();
					String user = session.getAttribute("user").toString();
					System.out.println("User: " + user);
					tm.setProfilePic(b, user, type);
					is.close();
				}
				response.sendRedirect("/instashutter/account");
			}
		} catch (Exception e)	{
			System.out.println("Error uploading new profile image: " + e);
		}
		
	}

}
