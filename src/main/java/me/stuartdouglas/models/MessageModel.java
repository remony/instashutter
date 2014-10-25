package me.stuartdouglas.models;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import me.stuartdouglas.lib.Convertors;
import me.stuartdouglas.stores.MessageStore;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class MessageModel {
	private static Cluster cluster;
	
	public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
	
	public static LinkedList<MessageStore> getComments(String username, String otherUsername) {
    	LinkedList<MessageStore> commentList = new LinkedList<>();
    	LinkedList<MessageStore> sortedCommentList = new LinkedList<>();
    	
    	LinkedList<MessageStore> commentList1 = getMessagesFromUserToUser(username, otherUsername);
    	LinkedList<MessageStore> commentList2 = getMessagesFromUserToUser(otherUsername, username);
    	commentList.addAll(commentList1);
    	commentList.addAll(commentList2);
    	
   
    	//Sorting posts by pic_added
    	commentList.stream()
        .sorted((e1, e2) -> e1.getDate_sent()
                .compareTo(e2.getDate_sent()))
        .forEach(sortedCommentList::add);
    	
    	
    	return sortedCommentList;
    	
	}
	
	public static LinkedList<MessageStore> getMessagesFromUserToUser(String username, String otherUsername) {
    	LinkedList<MessageStore> commentList = new LinkedList<>();
    	
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("select * from messaging where recipient =? and sender = ?");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement.bind(username, otherUsername));
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    		return null;
    	} else {
    		for (Row row : rs) {
    			MessageStore ms = new MessageStore();
    			ms.setSender(row.getString("sender"));
    			ms.setRecipient(row.getString("recipient"));
    			ms.setDate_sent(row.getDate("date_sent"));
    			ms.setMessage(row.getString("message"));
    			commentList.add(ms);
    		}
    		session.close();
    	}
    	return commentList;
    	
	}
	
	/*
	 * 	Gets a list of usernames which they have messages from
	 * 
	 */
	
	public LinkedList<MessageStore> getMessageList(String username) {
		// TODO Auto-generated method stub
		LinkedList<MessageStore> messageList = new LinkedList<>();
		
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("select * from messaging where recipient =?");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement.bind(username));
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    		return null;
    	} else {
    		for (Row row : rs) {
    			MessageStore ms = new MessageStore();
    			ms.setSender(row.getString("sender"));
    			ms.setRecipient(row.getString("recipient"));
    			ms.setDate_sent(row.getDate("date_sent"));
    			ms.setMessage(row.getString("message"));
    			messageList.add(ms);
    		}
    		session.close();
    	}
    	return messageList;
	}

	public void sendMessage(String username, String otherUsername, String message) {
		try {
			Session session = cluster.connect("instashutter");
            
            PreparedStatement ps = session.prepare("insert into messaging (message, sender, recipient, date_sent) values (?,?,?,?) if not exists");
            BoundStatement bs = new BoundStatement(ps);
            Date date_sent = new Date();
            session.execute(bs.bind(message, username, otherUsername, date_sent));

            session.close();
		}	catch (Exception e)	{
			System.out.println("Error posting new message to cassandra: " + e);
		}
		
	}
}


