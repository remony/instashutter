package me.stuartdouglas.models;

import java.util.Date;
import java.util.LinkedList;

import me.stuartdouglas.stores.MessageStore;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
/*
 * 			MessageModel
 * 
 * 		The MessageModel handles all database transactions regarding comments
 * 
 */
public class MessageModel {
	private static Cluster cluster;
	
	public void setCluster(Cluster cluster) {
        MessageModel.cluster = cluster;
    }
	
	/*
	 * 	GetMessage()
	 * 
	 * 	Gets all the messages made by 2 users.
	 */
	public static LinkedList<MessageStore> getMessages(String username, String otherUsername) {
    	LinkedList<MessageStore> messageList = new LinkedList<>();
    	LinkedList<MessageStore> sortedMessageList = new LinkedList<>();
    	//Gets results from messages made to user 1 and user 2 and the other way around to get the full conversation
    	LinkedList<MessageStore> commentList1 = getMessagesFromUserToUser(username, otherUsername);
    	LinkedList<MessageStore> commentList2 = getMessagesFromUserToUser(otherUsername, username);
    	//Protection against no values
    	if (commentList1 != null ){
    		messageList.addAll(commentList1);
    	}
    	
    	if (commentList2 != null)	{
    		messageList.addAll(commentList2);
    	}
    	
   
    	//Streams through the linkedlist sorting the date posted and sets it to a new linkedlist
    	messageList.stream()
        .sorted((message1, message2) -> message1.getDate_sent()
                .compareTo(message2.getDate_sent()))
        .forEach(sortedMessageList::add);
    	
		//Returns the sorted linkedlist of messages
    	return sortedMessageList;
    	
	}
	
	/*
	 * 	GetMessagesFromUserToUser()
	 * 
	 * 	Takes in 2 strings and queries cassandra to get the list of messages made
	 */
	public static LinkedList<MessageStore> getMessagesFromUserToUser(String username, String otherUsername) {
    	LinkedList<MessageStore> messageList = new LinkedList<>();
    	//Creates connection
    	Session session = cluster.connect("instashutter");
    	//Get all data from messaging where the recipicient is username and send is OtherUsername
    	PreparedStatement statement = session.prepare("select * from messaging where recipient =? and sender = ?");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	//Executes the query
    	ResultSet rs = session.execute(boundStatement.bind(username, otherUsername));
    	//If there is no results
    	if (rs.isExhausted()){
    		System.out.println("No messages returned");
    		return null;
    	} else {
    		//For each value returned
    		for (Row row : rs) {
    			MessageStore ms = new MessageStore();
    			//Set values in MessageStore
    			ms.setSender(row.getString("sender"));
    			ms.setRecipient(row.getString("recipient"));
    			ms.setDate_sent(row.getDate("date_sent"));
    			ms.setMessage(row.getString("message"));
    			messageList.add(ms);
    		}
    		//Closes the connection
    		session.close();
    	}
    	
    	//Returns the linkedlist for use of another method
    	return messageList;	
	}
	
	/*
	 * 	Gets a list of usernames which they have messages from
	 * 
	 */
	
	public LinkedList<MessageStore> getMessageList(String username) {
		LinkedList<MessageStore> messageList = new LinkedList<>();
		LinkedList<MessageStore> sortedMessageList = new LinkedList<>();
		Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("select sender from messaging where recipient =?");
    	
    	BoundStatement boundStatement = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(boundStatement.bind(username));
    	if (rs.isExhausted()){
    		System.out.println("No posts returned");
    		return null;
    	} else {
    		for (Row row : rs) {
    			MessageStore ms = new MessageStore();
    			ms.setSender(row.getString("sender"));
    			messageList.add(ms);
    		}
    		session.close();
    	}
    	
    	messageList.stream()
        .sorted((message1, message2) -> message1.getSender()
                .compareTo(message2.getSender()))
        .forEach(sortedMessageList::add);
    	
    	
    	
    	
    	return sortedMessageList;
	}

	
	/*
	 * 	sendMessage()
	 * 
	 * 	Sends the message to another user
	 */
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
	
	/*
	 * 	destroy()
	 * 	Closes connections
	 */
	public void destroy()	{
		try {
			if(cluster != null) cluster.close();
		}	catch(Exception e)	{
			System.out.println("error closing cassandra connection " + e);
		}
	}
}


