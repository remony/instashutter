package me.stuartdouglas.lib;

//import java.util.ArrayList;
//import java.util.List;

import com.datastax.driver.core.*;

final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace if not exists instashutter  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreatePicTable = "CREATE TABLE if not exists instashutter.Pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " caption varchar,"
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + "  processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " public boolean,"
                    + " PRIMARY KEY (picid, public)"
                    + ")";
            String Createuserpiclist = "CREATE TABLE if not exists instashutter.userpiclist (\n"
                    + "picid uuid,\n"
                    + "caption varchar,\n"
                    + "user varchar,\n"
                    + "public boolean,"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user, picid, pic_added, public)\n"
                    + ") WITH CLUSTERING ORDER BY (picid desc, pic_added desc);";
            String CreateAddressType = "CREATE TYPE if not exists instashutter.address (\n"
                    + "      street text,\n"
                    + "      city text,\n"
                    + "      zip int\n"
                    + "  );";
            String CreateUserProfile = "CREATE TABLE if not exists instashutter.userprofiles (\n"
                    + "      login text PRIMARY KEY,\n"
                     + "     password text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "profileImage blob,\n"
                    + "bio text, \n"
                    + "background text, \n"
                    + "location text, \n"
                    + "      email set<text>,\n"
                    + "      addresses  map<text, frozen <address>>\n"
                    + "  );";
            String CreateFriendList = "CREATE TABLE if not exists instashutter.friends ("
            	    +"username text,"
            	    +"friend text,"
            	    +"since timestamp,"
            	    +"PRIMARY KEY (username, friend)"
            	+");";
            String CreateMessagingTable = "CREATE TABLE if not exists instashutter.messaging ("
            	    + "sender text,"
            	    + "recipient text,"
            	   	+ "message text,"
            	   	+ "date_sent timestamp,"
            	   	+  "PRIMARY KEY (sender, date_sent)"
            	+"); ";
            String CreateMessagingSendIndex = "CREATE INDEX if not exists ON instashutter.messaging(sender);";
            String CreateMessagingRecipientIndex = "CREATE INDEX if not exists ON instashutter.messaging(recipient);";
        	String CreateCommentsTable ="CREATE TABLE if not exists instashutter.comments ("
        			+ "username text,"
        			+ "picid uuid,"
        			+ "comment_text text,"
        			+ "comment_added timestamp,"
        			+ "PRIMARY KEY (picid, comment_added)"
        			+ ") WITH CLUSTERING ORDER BY (comment_added DESC);";
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                @SuppressWarnings("unused")
				ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("created instashutter ");
            } catch (Exception et) {
                System.out.println("Can't create instashutter " + et);
            }

            //now add some column families 
           // System.out.println("" + CreatePicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create pic table " + et);
            }
            //System.out.println("" + Createuserpiclist);

            
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            //System.out.println("" + CreateAddressType);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateAddressType);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address type " + et);
            }
            //System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create User Profile " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateFriendList);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Friend List " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateMessagingTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Messaging Table " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateMessagingRecipientIndex);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Messaging Table " + et);
            }
            
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateCommentsTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Comments Table " + et);
            }
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}