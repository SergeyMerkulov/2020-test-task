package com.haulmont.testtask.managers;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.haulmont.testtask.models.Client;
import com.haulmont.testtask.models.Mechanic;
import com.haulmont.testtask.models.Order;

public class OrdersManager {

	private static Connection connection = null;
	private static Statement statement = null;
	private static String sqlCommand = null;

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
	
	public static void add( Order order ) {
		sqlCommand = "INSERT INTO orders ( description , id_of_client , id_of_mechanic , begin_date , end_date , cost , status ) VALUES ( ";
		sqlCommand += " '"+order.getDescription()+"', ";
		sqlCommand += " "+order.getIdOfClient()+", ";
		sqlCommand += " "+order.getIdOfMechanic()+", ";
		sqlCommand += " '"+order.getBeginDate()+"', ";
		sqlCommand += " '"+order.getEndDate()+"', ";
		sqlCommand += " "+order.getCost()+", ";
		sqlCommand += " '"+order.getStatus()+"' ";
		sqlCommand += "); ";
		
		openDB();
		
		try {
			statement.executeUpdate( sqlCommand );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}
	
	public static void update( Order order ) {
		
		sqlCommand = "UPDATE orders SET ";
		sqlCommand += " description='"+order.getDescription()+"' ";
		sqlCommand += ", id_of_client="+order.getIdOfClient()+" ";
		sqlCommand += ", id_of_mechanic="+order.getIdOfMechanic()+" ";
		sqlCommand += ", begin_date='"+order.getBeginDate()+"' ";
		sqlCommand += ", end_date='"+order.getEndDate()+"' ";
		sqlCommand += ", cost="+order.getCost()+" ";
		sqlCommand += ", status='"+order.getStatus()+"' ";
		sqlCommand += " WHERE id ="+order.getId()+" ;";
		
		openDB();
		
		try {
			statement.executeUpdate( sqlCommand );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}

	public static Order[] getOrders( String description, String statuses, String idOfClient ) {
		
		List<Order> ordersList = new ArrayList<>();
		
		String lookForPreamble;
		
		if( idOfClient.equals("everybody") ) {
			lookForPreamble = " SELECT (*) FROM orders WHERE ";
		} else {
			lookForPreamble = " SELECT (*) FROM orders WHERE id_of_client="+idOfClient+" AND ";
		}
		
		sqlCommand = " description LIKE '%"+description+"%' ";
		if( statuses.equals("planned") ) {
			sqlCommand += " AND status='planned' ";
		} else if ( statuses.equals("completed") ) {
			sqlCommand += " AND status='completed' ";
		} else if ( statuses.equals("accepted") ) {
			sqlCommand += " AND status='accepted' ";
		} else if ( statuses.equals("planned, completed") ) {
			sqlCommand += " AND ( status='planned' OR status='completed' ) ";
		} else if ( statuses.equals("planned, accepted") ) {
			sqlCommand += " AND ( status='planned' OR status='accepted' ) ";
		} else if ( statuses.equals("completed, accepted") ) {
			sqlCommand += " AND ( status='completed' OR status='accepted' ) ";
		}
		sqlCommand += " ;";
		
		openDB();
		
		try {
			ResultSet resultSet = statement.executeQuery( lookForPreamble + sqlCommand );
			while(resultSet.next()){

				Order order = new Order( );
				order.setId( resultSet.getLong("id") );
				order.setDescription( resultSet.getString("description") );
				order.setIdOfClient( resultSet.getLong("id_of_client") );
				order.setIdOfMechanic( resultSet.getLong("id_of_mechanic") );
				order.setBeginDate( resultSet.getString("begin_date") );
				order.setEndDate( resultSet.getString("end_date") );
				order.setCost( resultSet.getInt("cost") );
				order.setStatus( resultSet.getString("status") );
				ordersList.add(order);
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		Order[] orders = ordersList.toArray(new Order[0]);
		
		int i=0;
		for( Order order : orders ) {
			
			orders[i].setClientById( orders[i].getIdOfClient() );
			orders[i].setMechanicById( orders[i].getIdOfMechanic() );
			i++;
		}
		return orders;	
	}
	
	public static Order[] getOrders() {
		
		int numberOfRows = 0;
			
		openDB();
		
		//get number of rows from orders table from db
		try {
			ResultSet numbOfRowsSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
			numbOfRowsSet.next();
			numberOfRows = numbOfRowsSet.getInt(1);
			
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		//get orders from db
		Order[] orders = new Order[numberOfRows];
		int j = 0;
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM orders");
			while(resultSet.next()){

				orders[j] = new Order( );
				orders[j].setId( resultSet.getLong("id") );
				orders[j].setDescription( resultSet.getString("description") );
				orders[j].setIdOfClient( resultSet.getLong("id_of_client") );
				orders[j].setIdOfMechanic( resultSet.getLong("id_of_mechanic") );
				orders[j].setBeginDate( resultSet.getString("begin_date") );
				orders[j].setEndDate( resultSet.getString("end_date") );
				orders[j].setCost( resultSet.getInt("cost") );
				orders[j].setStatus( resultSet.getString("status") );
				j++;
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		int i=0;
		for( Order order : orders ) {
			
			orders[i].setClientById( orders[i].getIdOfClient() );
			orders[i].setMechanicById( orders[i].getIdOfMechanic() );
			i++;
		}
		
		return orders;	
	}
	
	private static final int ORDERS_ID = 1;
	private static final int ORDERS_DESCRIPTION = 2;
	private static final int ORDERS_ID_OF_CLIENT = 3;
	private static final int ORDERS_ID_OF_MECHANIC = 4;
	private static final int ORDERS_BEGIN_DATE = 5;
	private static final int ORDERS_END_DATE = 6;
	private static final int ORDERS_COST = 7;
	private static final int ORDERS_STATUS = 8;
	private static final int CLIENTS_FIRST_NAME = 9;
	private static final int CLIENTS_SECOND_NAME = 10;
	private static final int CLIENTS_THIRD_NAME = 11;
	private static final int CLIENTS_PHONE = 12;
	private static final int MECHANICS_FIRST_NAME = 13;
	private static final int MECHANICS_SECOND_NAME = 14;
	private static final int MECHANICS_THIRD_NAME = 15;
	private static final int MECHANICS_HOURLY_SALARY = 16;
	
	public static Order[] getOrdersFast( String description, String statuses, String idOfClient ) {
		
		List<Order> ordersList = new ArrayList<>();

		String preamble = " SELECT orders.id , orders.description , orders.id_of_client , ";
		preamble += " orders.id_of_mechanic , orders.begin_date , orders.end_date , orders.cost , orders.status , ";
		preamble += " clients.first_name , clients.second_name , clients.third_name , clients.phone , ";
		preamble += " mechanics.first_name , mechanics.second_name , mechanics.third_name , mechanics.hourly_salary ";
		preamble += " FROM orders INNER JOIN clients ON orders.id_of_client = clients.id INNER JOIN mechanics ON orders.id_of_mechanic = mechanics.id WHERE ";
		
		if( ! idOfClient.equals("everybody") ) {
			preamble += " orders.id_of_client = "+idOfClient+" AND ";
		}
		
		sqlCommand = " orders.description LIKE '%"+description+"%' ";
		if( statuses.equals("planned") ) {
			sqlCommand += " AND orders.status='planned' ";
		} else if ( statuses.equals("completed") ) {
			sqlCommand += " AND orders.status='completed' ";
		} else if ( statuses.equals("accepted") ) {
			sqlCommand += " AND orders.status='accepted' ";
		} else if ( statuses.equals("planned, completed") ) {
			sqlCommand += " AND ( orders.status='planned' OR orders.status='completed' ) ";
		} else if ( statuses.equals("planned, accepted") ) {
			sqlCommand += " AND ( orders.status='planned' OR orders.status='accepted' ) ";
		} else if ( statuses.equals("completed, accepted") ) {
			sqlCommand += " AND (orders.status='completed' OR orders.status='accepted' ) ";
		}
		sqlCommand += " ;";
		
		openDB();
		
		try {
			ResultSet resultSet = statement.executeQuery( preamble + sqlCommand );
			while(resultSet.next()){

				Order order = new Order( );
				order.setId( resultSet.getLong( ORDERS_ID ) );
				order.setDescription( resultSet.getString( ORDERS_DESCRIPTION ) );
				order.setBeginDate( resultSet.getString( ORDERS_BEGIN_DATE ) );
				order.setEndDate( resultSet.getString( ORDERS_END_DATE ) );
				order.setCost( resultSet.getInt( ORDERS_COST ) );
				order.setStatus( resultSet.getString( ORDERS_STATUS ) );
				
				Client client = new Client( );
				client.setId( resultSet.getLong( ORDERS_ID_OF_CLIENT ) );
				client.setFirstName( resultSet.getString( CLIENTS_FIRST_NAME ) );
				client.setSecondName( resultSet.getString( CLIENTS_SECOND_NAME ) );
				client.setThirdName( resultSet.getString( CLIENTS_THIRD_NAME ) );
				client.setPhone( resultSet.getString( CLIENTS_PHONE ) );
				order.setClient(client);
				
				Mechanic mechanic = new Mechanic( );
				mechanic.setId( resultSet.getLong( ORDERS_ID_OF_MECHANIC ) );
				mechanic.setFirstName( resultSet.getString( MECHANICS_FIRST_NAME ) );
				mechanic.setSecondName( resultSet.getString( MECHANICS_SECOND_NAME ) );
				mechanic.setThirdName( resultSet.getString( MECHANICS_THIRD_NAME ) );
				mechanic.setHourlySalary( resultSet.getInt( MECHANICS_HOURLY_SALARY ) );
				order.setMechanic(mechanic);
				
				ordersList.add(order);
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		return ordersList.toArray(new Order[0]);
	}
	
	public static int getNumber() {
		
		int numberOfRows = 0;
			
		openDB();
		
		try {
			ResultSet numbOfRowsSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
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
			statement.executeUpdate( "DELETE FROM orders WHERE id="+id );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}
}