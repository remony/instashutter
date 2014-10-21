package me.stuartdouglas.models;


/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import javax.imageio.ImageIO;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr.Method;

import me.stuartdouglas.lib.*;
import me.stuartdouglas.stores.Pic;

public class PicModel {
	private static Cluster cluster;

    public PicModel() {
    	
    }
    
    
    
    public LinkedList<Pic> getPosts() {
    	LinkedList<Pic> instaList = new LinkedList<Pic>();
    	LinkedList<Pic> instaSortedList = new LinkedList<Pic>();
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("SELECT * from pics");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement);
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    	} else {
    		for (Row row : rs) {
    			Pic pic = new Pic();
    			UUID picid = row.getUUID("picid");
    			pic.setUUID(row.getUUID("picid"));
    			pic.setCaption(row.getString("caption"));
    			pic.setPostedUsername(row.getString("user"));
    			pic.setPicAdded(row.getDate("interaction_time"));
    			instaList.add(pic);
    			//Get comments from post and limit to 5 (stops endless list on dashboard)
    			PreparedStatement pss = session.prepare("SELECT * from comments where picid =? limit 5");
    	    	ResultSet rss = null;
    	    	BoundStatement boundStatement2 = new BoundStatement(pss);
    	    	rss = session.execute(boundStatement2.bind(picid));
    	    	if (rss.isExhausted())	{
    	    	}	else	{
    	    		for (Row rows : rss)	{
    	    			pic.setCommentlist(rows.getString("username"), rows.getUUID("picid"), rows.getString("comment_text"), rows.getDate("comment_added"));
    	    		}
    	    	}
    		}
    		session.close();
    	}
    	
    	
    	
    	
    	
    	//Sorting posts by pic_added
    	instaList.stream()
        .sorted((e1, e2) -> e2.getPicAdded()
                .compareTo(e1.getPicAdded()))
        .forEach(e ->  instaSortedList.add(e));
		return instaSortedList;
    }
    
    public LinkedList<Pic> getPost(String user) {
    	LinkedList<Pic> instaList = new LinkedList<Pic>();
    	LinkedList<Pic> instaSortedList = new LinkedList<Pic>();
    	Session session = cluster.connect("instashutter");
    	UUID picid = UUID.fromString(user);
    	PreparedStatement statement = session.prepare("SELECT * from pics where picid =?");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement.bind(picid));
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    	} else {
    		for (Row row : rs) {
    			Pic ps = new Pic();
    			ps.setUUID(row.getUUID("picid"));
    			ps.setCaption(row.getString("caption"));
    			ps.setPostedUsername(row.getString("user"));
    			ps.setPicAdded(row.getDate("interaction_time"));
    			instaList.add(ps);
    		}
    	}
    	instaList.stream()
        .sorted((e1, e2) -> e2.getPicAdded()
                .compareTo(e1.getPicAdded()))
        .forEach(e ->  instaSortedList.add(e));
    	session.close();
		return instaSortedList;
    }
    
    
    
    
    public static java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instashutter");
        PreparedStatement ps = session.prepare("select * from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                pic.setCaption(row.getString("caption"));
                pic.setPostedUsername(row.getString("user"));
                pic.setPicAdded(row.getDate("pic_added"));
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);
            }
        }
        session.close();
        return Pics;
    }
    
    
    
    public void setCluster(Cluster cluster) {
        PicModel.cluster = cluster;
    }
    
    
    
    
    public void insertPic(byte[] b, String type, String name, String user, String caption) {
    	
    	try {
        	
        	
            //Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = Convertors.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            //Boolean success = (new File("/var/tmp/instashutter/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instashutter/" + picid));

            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instashutter");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name, caption) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added, caption) values(?,?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name, caption));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded, caption));
            try {
            	session.close();
            } catch (Exception e) {
            	System.out.println("Session.close error " + e);
            }
            try {
            	output.close();
            } catch (Exception e) {
            	System.out.println("output.close error " + e);
            }
            

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid,String type) {
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

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
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

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 500, OP_ANTIALIAS);//, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 1);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS);//, OP_GRAYSCALE);
        return pad(img, 1);
    }
   
   public Pic getPic(int image_type, java.util.UUID picid) {
       Session session = cluster.connect("instashutter");
       ByteBuffer bImage = null;
       String type = null;
       int length = 0;
       try {
           //Convertors convertor = new Convertors();
           ResultSet rs = null;
           PreparedStatement ps = null;
        
           if (image_type == Convertors.DISPLAY_IMAGE) {
               ps = session.prepare("select image,imagelength,type from pics where picid =?");
           } else if (image_type == Convertors.DISPLAY_THUMB) {
               ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
           } else if (image_type == Convertors.DISPLAY_PROCESSED) {
               ps = session.prepare("select processed,processedlength,type from pics where picid =?");
           }
           BoundStatement boundStatement = new BoundStatement(ps);
           rs = session.execute( // this is where the query is executed
                   boundStatement.bind( // here you are binding the 'boundStatement'
                           picid));

           if (rs.isExhausted()) {
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
                   
                   type = row.getString("type");

               }
           }
       } catch (Exception et) {
           System.out.println("Can't get Pic" + et);
           return null;
       }
       session.close();
       Pic p = new Pic();
       p.setPic(bImage, length, type);

       return p;

   }
   public Pic delPic(int image_type, java.util.UUID picid) {
       Session session = cluster.connect("instashutter");
       ByteBuffer bImage = null;
       String type = null;
       int length = 0;
       try {
           //Convertors convertor = new Convertors();
           ResultSet rs = null;
           PreparedStatement ps = null;
        
           if (image_type == Convertors.DISPLAY_IMAGE) {
               
               ps = session.prepare("DELETE image,imagelength,type from pics where picid =?");
           } else if (image_type == Convertors.DISPLAY_THUMB) {
               ps = session.prepare("DELETE thumb,imagelength,thumblength,type from pics where picid =?");
           } else if (image_type == Convertors.DISPLAY_PROCESSED) {
               ps = session.prepare("DELETE processed,processedlength,type from pics where picid =?");
           }
           BoundStatement boundStatement = new BoundStatement(ps);
           rs = session.execute( // this is where the query is executed
                   boundStatement.bind( // here you are binding the 'boundStatement'
                           picid));

           if (rs.isExhausted()) {
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
                   
                   type = row.getString("type");

               }
           }
       } catch (Exception et) {
           System.out.println("Can't get Pic" + et);
           return null;
       }
       session.close();
       Pic p = new Pic();
       p.setPic(bImage, length, type);
       
       return p;

   }



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
}
