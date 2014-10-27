package me.stuartdouglas.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.jhlabs.image.ChromeFilter;
import com.jhlabs.image.CrystallizeFilter;
import com.jhlabs.image.ExposureFilter;
import com.jhlabs.image.FlareFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.PointillizeFilter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;



import static org.imgscalr.Scalr.*;
import me.stuartdouglas.lib.*;
import me.stuartdouglas.stores.CommentStore;
import me.stuartdouglas.stores.PicStore;

/*
 * 				PicModel	
 * 	
 * 		Handles all db transactions regarding images
 * 
 * 
 */


public class PicModel {
	private static Cluster cluster;
	//Defines values for use for filters. 
	int width;
	int height;

    public PicModel() {
    	
    }
    
    /*
     * getPublicPosts()
     * Gets all public posts from all users along with 5 comments
     * This is for use on pages with multiple images showing. 
     */
    
    public static LinkedList<PicStore> getPublicPosts() {
    	LinkedList<PicStore> instaList = new LinkedList<>();
    	LinkedList<PicStore> instaSortedList = new LinkedList<>();
    	LinkedList<PicStore> comments = new LinkedList<>();
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("SELECT * from pics where public = true allow filtering");
    	PreparedStatement pss = session.prepare("SELECT * from comments where picid =? limit 5");
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement);
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    		return null;
    	} else {
    		for (Row row : rs) {
    			//Gets the posts
    			PicStore pic = new PicStore();
    			UUID picid = row.getUUID("picid");
    			pic.setUUID(row.getUUID("picid"));
    			pic.setCaption(row.getString("caption"));
    			pic.setPostedUsername(row.getString("user"));
    			pic.setPicAdded(row.getDate("interaction_time"));
    			instaList.add(pic);
    			//Get comments from post and limit to 5 (stops endless list on dashboard)
    			
    	    	BoundStatement boundStatementComment = new BoundStatement(pss);
    	    	ResultSet rss = session.execute(boundStatementComment.bind(picid));
    	    	if (rss.isExhausted())	{
                    System.out.println("No comments");
    	    	}	else	{
    	    		for (Row rows : rss)	{
    	    			CommentStore commentS = new CommentStore();
    	    			UUID picid2 = rows.getUUID("picid");
    	    			String username2 = rows.getString("username");
    	    			Date date2 = rows.getDate("comment_added");
    	    			String comment2 = rows.getString("comment_text");
    	    			commentS.setUuid(picid2);
    					commentS.setUsername(username2);
						commentS.setPosted_time(date2);
						commentS.setCommentMessage(comment2);
    	    			pic.setCommentlist(picid2, username2, date2, comment2);
    	    		}
    	    	}
    		}
    		session.close();
    	}
    	//Sorting posts by pic_added, this is to ensure that the results are in order.
    	instaList.stream()
        .sorted((image1, image2) -> image2.getPicAdded()
                .compareTo(image1.getPicAdded()))
        .forEach(instaSortedList::add);
    	//Returns the images with comments sorted.
		return instaSortedList;
		
    }
    
    /*
     * GetPosts()
     * Gets all posts public and private from all users
     * 
     */
    public static LinkedList<PicStore> getPosts() {
    	LinkedList<PicStore> instaList = new LinkedList<>();
    	LinkedList<PicStore> comments = new LinkedList<>();
    	LinkedList<PicStore> instaSortedList = new LinkedList<>();
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("SELECT * from pics");
    	PreparedStatement pss = session.prepare("SELECT * from comments where picid = ? limit 5");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement);
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    		return null;
    	} else {
    		for (Row row : rs) {
    			PicStore pic = new PicStore();
    			UUID picid = row.getUUID("picid");
    			pic.setUUID(row.getUUID("picid"));
    			pic.setCaption(row.getString("caption"));
    			pic.setPostedUsername(row.getString("user"));
    			pic.setPicAdded(row.getDate("interaction_time"));
    			instaList.add(pic);
    			//Get comments from post and limit to 5 (stops endless list on dashboard)
    	    	BoundStatement boundStatementComment = new BoundStatement(pss);
    	    	ResultSet rss = session.execute(boundStatementComment.bind(picid));
    	    	
    	    	//gets the comments
    	    	if (rss.isExhausted())	{
                    System.out.println("no comments");
    	    	}	else	{
    	    		for (Row rows : rss)	{
    	    			CommentStore commentS = new CommentStore();
    	    			UUID picid2 = rows.getUUID("picid");
    	    			String username2 = rows.getString("username");
    	    			Date date2 = rows.getDate("comment_added");
    	    			String comment2 = rows.getString("comment_text");
    	    			commentS.setUuid(picid2);
    					commentS.setUsername(username2);
						commentS.setPosted_time(date2);
						commentS.setCommentMessage(comment2);
    	    			pic.setCommentlist(picid2, username2, date2, comment2);
    	    		}
    	    	}
    		}
    		session.close();
    	}

    	//Sorting posts by pic_added
    	instaList.stream()
        .sorted((image1, image2) -> image2.getPicAdded()
                .compareTo(image1.getPicAdded()))
        .forEach(instaSortedList::add);
    	
		return instaSortedList;
		
    }
    
    

    /*
     * 	getPost()
     * 
     * 	Gets a single post and returns with full comment listing
     * 	This is mainly used by the post servlet
     */
    public LinkedList<PicStore> getPost(String user) {
    	LinkedList<PicStore> instaList = new LinkedList<>();
    	LinkedList<PicStore> comments = new LinkedList<>();
    	Session session = cluster.connect("instashutter");
    	UUID picid = UUID.fromString(user);
    	PreparedStatement statement = session.prepare("SELECT * from pics where picid =?");
    	PreparedStatement pss = session.prepare("SELECT * from comments where picid =?");
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement.bind(picid));
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    	} else {
    		//sets the posts
    		for (Row row : rs) {
    			PicStore ps = new PicStore();
    			ps.setUUID(row.getUUID("picid"));
    			ps.setCaption(row.getString("caption"));
    			ps.setPostedUsername(row.getString("user"));
    			ps.setPicAdded(row.getDate("interaction_time"));
    			instaList.add(ps);
    			//Get comments for the posts
    	    	BoundStatement boundStatementComment = new BoundStatement(pss);
    	    	ResultSet rss = session.execute(boundStatementComment.bind(picid));
    	    	if (rss.isExhausted())	{
                    System.out.println("no comments");
    	    	}	else	{
    	    		for (Row rows : rss)	{
    	    			CommentStore commentS = new CommentStore();
    	    			UUID picid2 = rows.getUUID("picid");
    	    			String username2 = rows.getString("username");
    	    			Date date2 = rows.getDate("comment_added");
    	    			String comment2 = rows.getString("comment_text");
    	    			commentS.setUuid(picid2);
    					commentS.setUsername(username2);
						commentS.setPosted_time(date2);
						commentS.setCommentMessage(comment2);
    	    			ps.setCommentlist(picid2, username2, date2, comment2);
    	    		}
    	    	}
    		}
    	}
    	//returns the image data 
		return instaList;
    }
    
    /*
     * 	GetPostsContaining()
     * 
     * 	This method gets posts which the caption contains a keyword or string 
     */
    
    public static LinkedList<PicStore> getPostsContaining(String keyword)	{
    	try {
    		//gets the public posts
			LinkedList<PicStore> instaList = getPublicPosts();
			LinkedList<PicStore> comments = new LinkedList<>();
			LinkedList<PicStore> instaSortedList = new LinkedList<>();
			
			//Sorts the posts in order and filtering out posts which do not contain the defined keyword
			instaList.stream()
			.sorted((post1, post2) -> post2.getPicAdded()
			        .compareTo(post1.getPicAdded()))
			        .filter(post1 -> post1.getCaption().toLowerCase().contains(keyword.toLowerCase())) //Changes strings to lowercase to improve results
			.forEach(instaSortedList::add);
			//If the list is not empty return the list
			if (!instaSortedList.isEmpty())	{
				System.out.println("got results");
				return instaSortedList;
			//List is empty so return null
			}	else	{
				System.out.println("no results");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Error getting posts filtered: " + e);
		}
		return null;
    	
    }
    
    /*
     * 	getPicsForUser()
     * 
     * 	Gets posts made by defined user
     */
    public static LinkedList<PicStore> getPicsForUser(String User) {
    	LinkedList<PicStore> instaList = new LinkedList<>();
    	LinkedList<PicStore> instaSortedList = new LinkedList<>();
    	LinkedList<PicStore> comments = new LinkedList<>();
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("SELECT * from userpiclist where user =?");
    	PreparedStatement pss = session.prepare("SELECT * from comments where picid = ? limit 10");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement.bind(User));
    	if (rs.isExhausted()){
    		System.out.println("No posts for user " + User + " returned");
    		return null;
    	} else {
    		for (Row row : rs) {
    			PicStore pic = new PicStore();
    			UUID picid = row.getUUID("picid");
    			pic.setUUID(row.getUUID("picid"));
    			pic.setCaption(row.getString("caption"));
    			pic.setPostedUsername(row.getString("user"));
    			pic.setPicAdded(row.getDate("pic_added"));
    			instaList.add(pic);
    			//Get comments from post and limit to 5 (stops endless list on dashboard)
    			
    	    	BoundStatement boundStatementComment = new BoundStatement(pss);
    	    	ResultSet rss = session.execute(boundStatementComment.bind(picid));
    	    	if (rss.isExhausted())	{
                    System.out.println("no comments");
    	    	}	else	{
    	    		for (Row rows : rss)	{
    	    			CommentStore commentS = new CommentStore();
    	    			UUID picid2 = rows.getUUID("picid");
    	    			String username2 = rows.getString("username");
    	    			Date date2 = rows.getDate("comment_added");
    	    			String comment2 = rows.getString("comment_text");
    	    			commentS.setUuid(picid2);
    					commentS.setUsername(username2);
						commentS.setPosted_time(date2);
						commentS.setCommentMessage(comment2);
    	    			pic.setCommentlist(picid2, username2, date2, comment2);
    	    		}
    	    	}
    		}
    		session.close();
    	}
    	
    	//Sorting posts by pic_added
    	instaList.stream()
        .sorted((post1, post2) -> post2.getPicAdded()
                .compareTo(post1.getPicAdded()))
        .forEach(instaSortedList::add);
    	
    	
		return instaSortedList;
    }
    
    
    //sets the cluster
    public void setCluster(Cluster cluster) {
        PicModel.cluster = cluster;
    }
    
    
    
    /*
     * insertPic()
     * 
     * Processes and uploads the image to the database
     * 
     */
    public void insertPic(byte[] b, String type, String name, String user, String caption, String filter, boolean publicPhoto) {
    	
    	try {
            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = Convertors.getTimeUUID();
            
            
            @SuppressWarnings("unused")
			Boolean success = (new File("/var/tmp/instashutter/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instashutter/" + picid));

            //Writes the file to the filesystem
            output.write(b);

            //Apply the filter to the image
            applyFilter(filter, picid);
            
            //Processing a thumbnail version of the image	
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = null;

            
            
            
            //If the uploaded image is a gif then upload direct file to db
            if (type.equals("image/gif"))	{
            	processedb = b;
            }	else {
            	//else process
            	processedb = picdecolour(picid.toString(),types[1]);
            }
            ByteBuffer processedbuf= ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instashutter");
            //Prepare query to insert the image in the pics and userpiclist tables
            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name, caption, public) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added, caption, public) values(?,?,?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);
            //creates a established date
            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name, caption, publicPhoto));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded, caption, publicPhoto));
            //Executes the queries and closes the connection
            output.close();
            //Checks if the file exists, if the file does exist. Delete it.
            //Since the file is stored in the database as a blob a physical copy is not required.
            try {
            	File location = new File("/var/tmp/instashutter/" + picid);
                if (location.exists()) {
                	location.delete(); 
                }
            	
            }	catch(Exception e)	{
            	System.out.println("Error deleting file: " + e);
            }
            
        	//closes session
        	session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }
    
    /*
     * 
     * 	Processes image
     * 
     * 
     */

    byte[] picresize(String picid, String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instashutter/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {
            System.out.println("Error resizing");
            et.printStackTrace();
        }
        return null;
    }
    
    /*
     * 	Applies an effect to an image
     * 
     */
    
    public void applyFilter(String filter, UUID picid)	{
    	try {
    		//Get file location of the file
    		File input = new File("/var/tmp/instashutter/" + picid);
    		File output1 = new File("/var/tmp/instashutter/" + picid);
    		//Read in the iamge as a buffered image
        	BufferedImage imageIn = ImageIO.read(input);
        	BufferedImage imageOut = ImageIO.read(input);

        	//select which filter to apply to the image
        	if (filter.equals("bw"))	{
        		//Applies a greyscale effect on th eimage
        		GrayscaleFilter greyFilter = new GrayscaleFilter();
        		System.out.println("applied filter grey scale");
            	greyFilter.filter(imageIn, imageOut);  
            }	else if (filter.equals("invert"))	{
            	InvertFilter invert = new InvertFilter();
            	invert.filter(imageIn, imageOut);
            	System.out.println("applied filter invert");
            }	else if (filter.equals("exposure"))	{
            	ExposureFilter exposure = new ExposureFilter();
            	exposure.filter(imageIn, imageOut);
            	System.out.println("applied filter explosure");
           }	else if (filter.equals("flare"))	{
            	FlareFilter flare = new FlareFilter();
            	flare.filter(imageIn, imageOut);
            	System.out.println("applied filter flare");
            }	else if (filter.equals("pointillize"))	{
            	PointillizeFilter pointillize = new PointillizeFilter();
            	pointillize.filter(imageIn, imageOut);
            	System.out.println("applied filter point");
            }	else if (filter.equals("crystallize"))	{
            	CrystallizeFilter crystallize = new CrystallizeFilter();
            	crystallize.filter(imageIn, imageOut);
            	System.out.println("applied filter crystal");
            }	else if (filter.equals("chrome"))	{
            	ChromeFilter chrome = new ChromeFilter();
            	chrome.filter(imageIn, imageOut);
            	System.out.println("applied filter chrome");
            }
            
            
            else	{
            	System.out.println("not applying filter");
            }
            	
            
        	//Writes the new image
            ImageIO.write(imageOut, "jpg", output1);
        }	catch(Exception e)	{
        	System.out.println("Error applying filter");
        }

    }
    
    //Processes the image
    byte[] picdecolour(String picid, String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instashutter/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();     
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {
            System.out.println("Error decolour");
            et.printStackTrace();
        }
        return null;
    }

    
	//Created a thumbnail of the image
    private static BufferedImage createThumbnail(BufferedImage img) {
    	//Resizes the image to 500 width, with automatic quality.
        img = resize(img, Method.AUTOMATIC, 500, OP_ANTIALIAS);
        return img;
    }
    
   //Creates the processed version of the images, The full image
   private static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.AUTOMATIC, Width, OP_ANTIALIAS);
        return img;
    }
   
   //Gets the post image
   public PicStore getPic(int image_type, java.util.UUID picid) {
       Session session = cluster.connect("instashutter");
       ByteBuffer bImage = null;
       String type = null;
       int length = 0;
       boolean privacy = false;
       
       try {
           ResultSet rs;
           PreparedStatement ps = null;
           
           if (image_type == Convertors.DISPLAY_IMAGE) {
               ps = session.prepare("select image,imagelength,type,public from pics where picid =?");
           } else if (image_type == Convertors.DISPLAY_THUMB) {
               ps = session.prepare("select thumb,imagelength,thumblength,type,public from pics where picid =?");
           } else if (image_type == Convertors.DISPLAY_PROCESSED) {
               ps = session.prepare("select processed,processedlength,type,public from pics where picid =?");
           }
           BoundStatement boundStatement = new BoundStatement(ps);
           //executes the query
           rs = session.execute(boundStatement.bind(picid));

           if (rs.isExhausted()) {
        	   //No images was found
               System.out.println("No Images returned");
               return null;
           } else {
               for (Row row : rs) {
                   if (image_type == Convertors.DISPLAY_IMAGE) {
                       bImage = row.getBytes("image");
                       length = row.getInt("imagelength");
                   } else if (image_type == Convertors.DISPLAY_THUMB) {
                       bImage = row.getBytes("thumb");
                       length = row.getInt("thumblength");
               
                   } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                       bImage = row.getBytes("processed");
                       length = row.getInt("processedlength");
                   }
                   privacy = row.getBool("public");
                   
                   type = row.getString("type");

               }
           }
       } catch (Exception et) {
           System.out.println("Can't get PicStore" + et);
           return null;
       }
       session.close();
       PicStore p = new PicStore();
       p.setPic(bImage, length, type);
       p.setIsPublic(privacy);
       return p;

   }

   
   	//Inserts a new comment to a post
	public void postComment(String username, String picid, String comment) {
		try {
			Session session = cluster.connect("instashutter");
            
            PreparedStatement ps = session.prepare("insert into comments (comment_added, picid, comment_text, username) values(?,?,?,?)");
            BoundStatement bs = new BoundStatement(ps);
            UUID uuid = UUID.fromString(picid);
            Date date = new Date();
            session.execute(bs.bind(date, uuid, comment, username));

            session.close();
		}	catch (Exception e)	{
			System.out.println("Error posting comment to database: " + e);
		}
		
	}

	//returns the username of the user which posted the image
	public String getUserPosted(UUID picid)	{
		try {
			Session session = cluster.connect("instashutter");
			String username = null;
			PreparedStatement ps = session.prepare("select user from pics where picid=?");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(picid));
			if (rs.isExhausted()) {
                System.out.println("No user returned");
                return null;
            } else {
                for (Row row : rs) {
                    username = row.getString("user");
                }
                return username;
            }
		}	catch(Exception e)	{
			System.out.println("Error getting username: " + e);
		}
		return null;
	}

	//deletes the post from the database
	public void deletePost(String username, UUID picid) {
		try {
			Session session = cluster.connect("instashutter");
			//prepares the queries to delete the image from the tables including there comments
			PreparedStatement psPicList = session.prepare("delete from pics where picid =?");
			PreparedStatement psUserPicList = session.prepare("delete from userpiclist where user = ? and picid =?");
			PreparedStatement psComments = session.prepare("delete from comments where picid=?");
			BoundStatement bsPicList = new BoundStatement(psPicList);
			BoundStatement bsUserPicList = new BoundStatement(psUserPicList);
			BoundStatement bsComments = new BoundStatement(psComments);
			//Executes the queries
			session.execute(bsPicList.bind(picid));
			session.execute(bsUserPicList.bind(username, picid));
			session.execute(bsComments.bind(picid));
		}	catch(Exception e)	{
			System.out.println("Error deleting post" + e);
		}
		
	}
	
	//returns a random post
	public LinkedList<PicStore> getRandomPost() {
    	LinkedList<PicStore> instaList = getPublicPosts();
		LinkedList<PicStore> randomPost = new LinkedList<PicStore>();
		int size = 0;

			if (instaList != null){
			size = instaList.size();
			}

    	Random random = new Random();
    	System.out.println("size is " + size);
    	int randomPick = random.nextInt(size+1);
    	//If randomPic is 0 add one (this stopped no results appear)
    	if (randomPick == 0){
    		randomPick++;
    	}
    	if (instaList != null)	{
    	ListIterator<PicStore> listIterator = instaList.listIterator();
    	int count = 0;
    	//Goes through the list of posts and selects out the nth post
    	while (listIterator.hasNext()) {
    		count++;
            PicStore fs = listIterator.next();
            	if (count == randomPick){
            		randomPost.add(fs);
            	}
        }	
    	}
    	if (!randomPost.isEmpty()){
    		return randomPost;
    	} else	{
    		return null;
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