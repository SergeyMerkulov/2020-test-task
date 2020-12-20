package com.haulmont.testtask.managers;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.lang.Long;

import com.haulmont.testtask.models.Client;

public class ClientsManager {
	
	private static Connection connection = null;
	private static Statement statement = null;
	
	private static void openDB()
	{
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		try {
			connection = DriverManager.getConnection("jdbc:hsqldb:file:workshopdb", "SA", "");
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		try {
			statement = connection.createStatement();
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	
	private static void closeDB()
	{
		try {
			if (statement != null) {
				statement.execute("SHUTDOWN");
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}	
		
		try {
			if (connection != null) {
				connection.close();
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void add( Client client ) {
		
		String addClientCommand = "INSERT INTO clients ( first_name , second_name , third_name , phone ) VALUES ( ";
		addClientCommand += " '"+client.getFirstName()+"', ";
		addClientCommand += " '"+client.getSecondName()+"', ";
		addClientCommand += " '"+client.getThirdName()+"', ";
		addClientCommand += " '"+client.getPhone()+"' ";
		addClientCommand += "); ";
		
		openDB();
		
		try {
			statement.executeUpdate( addClientCommand );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}
	
	public static void update( Client client ) {
		
		String addClientCommand = "UPDATE clients SET ";
		addClientCommand += " first_name='"+client.getFirstName()+"' ";
		addClientCommand += ", second_name='"+client.getSecondName()+"' ";
		addClientCommand += ", third_name='"+client.getThirdName()+"' ";
		addClientCommand += ", phone='"+client.getPhone()+"' ";
		addClientCommand += " WHERE id ="+client.getId()+" ;";
		
		openDB();
		
		try {
			statement.executeUpdate( addClientCommand );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}
	
	public static Client getClientById( long id ) {
		
		Client client = new Client();
			
		openDB();
		
		try {
			ResultSet resultSet = statement.executeQuery( "SELECT * FROM clients WHERE id="+id );
			resultSet.next();
			client.setId( resultSet.getLong("id") );
			client.setFirstName( resultSet.getString("first_name") );
			client.setSecondName( resultSet.getString("second_name") );
			client.setThirdName( resultSet.getString("third_name") );
			client.setPhone( resultSet.getString("phone") );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		return client;	
	}
	
	public static Client[] getClients() {
		
		List<Client> clients = new ArrayList<>();
			
		openDB();
		
		int i = 0;
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
			while(resultSet.next()){
				Client client = new Client();
				client.setId(resultSet.getLong("id"));
				client.setFirstName(resultSet.getString("first_name"));
				client.setSecondName(resultSet.getString("second_name"));
				client.setThirdName(resultSet.getString("third_name"));
				client.setPhone(resultSet.getString("phone"));
				clients.add(client);
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		return clients.toArray(new Client[0]);	
	}
	
	
	public static int getNumber() {
		
		int numberOfRows = 0;
			
		openDB();
		
		try {
			ResultSet numbOfRowsSet = statement.executeQuery("SELECT COUNT(*) FROM clients");
			numbOfRowsSet.next();
			numberOfRows = numbOfRowsSet.getInt(1);
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		return numberOfRows;
	}
	
	public static void delete( long id ) {
			
		openDB();	
		
		try {
			statement.executeUpdate( "DELETE FROM clients WHERE id="+id );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}
}