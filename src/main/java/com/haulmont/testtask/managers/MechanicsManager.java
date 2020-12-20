package com.haulmont.testtask.managers;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.Long;
import java.lang.Integer;

import com.haulmont.testtask.models.Mechanic;

public class MechanicsManager {

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

	public static void add( Mechanic mechanic ) {
		
		sqlCommand = "INSERT INTO mechanics ( first_name , second_name , third_name , hourly_salary ) VALUES ( ";
		sqlCommand += " '"+mechanic.getFirstName()+"', ";
		sqlCommand += " '"+mechanic.getSecondName()+"', ";
		sqlCommand += " '"+mechanic.getThirdName()+"', ";
		sqlCommand += " '"+mechanic.getHourlySalary()+"' ";
		sqlCommand += "); ";
		
		openDB();
		
		try {
			statement.executeUpdate( sqlCommand );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}

	public static void update( Mechanic mechanic ) {
		
		sqlCommand = "UPDATE mechanics SET ";
		sqlCommand += " first_name='"+mechanic.getFirstName()+"' ";
		sqlCommand += ", second_name='"+mechanic.getSecondName()+"' ";
		sqlCommand += ", third_name='"+mechanic.getThirdName()+"' ";
		sqlCommand += ", hourly_salary="+mechanic.getHourlySalary()+" ";
		sqlCommand += " WHERE id ="+mechanic.getId()+" ;";
		
		openDB();
		
		try {
			statement.executeUpdate( sqlCommand );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}

	public static Mechanic getMechanicById( long id ) {
		
		Mechanic mechanic = new Mechanic();
			
		openDB();
		
		try {
			ResultSet resultSet = statement.executeQuery( "SELECT * FROM mechanics WHERE id="+id );
			resultSet.next();
			mechanic.setId( resultSet.getLong("id") );
			mechanic.setFirstName( resultSet.getString("first_name") );
			mechanic.setSecondName( resultSet.getString("second_name") );
			mechanic.setThirdName( resultSet.getString("third_name") );
			mechanic.setHourlySalary( resultSet.getInt("hourly_salary") );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		return mechanic;	
	}



	public static Mechanic[] getMechanics() {
		
		int numberOfRows = 0;
			
		openDB();
		
		//get number of rows from mechanics table from db
		try {
			ResultSet numbOfRowsSet = statement.executeQuery("SELECT COUNT(*) FROM mechanics");
			numbOfRowsSet.next();
			numberOfRows = numbOfRowsSet.getInt(1);
			
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		//get mechanics from db
		Mechanic[] mechanics = new Mechanic[numberOfRows];
		int i = 0;
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM mechanics");
			while(resultSet.next()){

				mechanics[i] = new Mechanic( );
				mechanics[i].setId( resultSet.getLong("id") );
				mechanics[i].setFirstName( resultSet.getString("first_name") );
				mechanics[i].setSecondName( resultSet.getString("second_name") );
				mechanics[i].setThirdName( resultSet.getString("third_name") );
				mechanics[i].setHourlySalary( resultSet.getInt("hourly_salary") );
				i++;
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
		
		return mechanics;	
	}


	public static int getNumber() {
		
		int numberOfRows = 0;
			
		openDB();
		
		try {
			ResultSet numbOfRowsSet = statement.executeQuery("SELECT COUNT(*) FROM mechanics");
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
			statement.executeUpdate( "DELETE FROM mechanics WHERE id="+id );
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();
	}
	
	public static String getStatistic( long id ) {
		
		int numberOfPlannedOrders = 0;
		int numberOfCompletedOrders = 0;
		int numberOfAcceptedOrders = 0;
		int numberOfAllOrders = 0;
		
		String statistic = "All orders: ";

		String sqlCountPlannedOrders = "SELECT COUNT(*) FROM orders WHERE id_of_mechanic="+id+" ";
		sqlCountPlannedOrders += " AND status='planned' ";
		sqlCountPlannedOrders += " ;";
		
		String sqlCountCompletedOrders = "SELECT COUNT(*) FROM orders WHERE id_of_mechanic="+id+" ";
		sqlCountCompletedOrders += " AND status='completed' ";
		sqlCountCompletedOrders += " ;";
		
		String sqlCountAcceptedOrders = "SELECT COUNT(*) FROM orders WHERE id_of_mechanic="+id+" ";
		sqlCountAcceptedOrders += " AND status='accepted' ";
		sqlCountAcceptedOrders += " ;";

		openDB();
		
		try {
			ResultSet numbOfRowsSet = statement.executeQuery( sqlCountPlannedOrders );
			numbOfRowsSet.next();
			numberOfPlannedOrders = numbOfRowsSet.getInt(1);
			
			numbOfRowsSet = statement.executeQuery( sqlCountCompletedOrders );
			numbOfRowsSet.next();
			numberOfCompletedOrders = numbOfRowsSet.getInt(1);
			
			numbOfRowsSet = statement.executeQuery( sqlCountAcceptedOrders );
			numbOfRowsSet.next();
			numberOfAcceptedOrders = numbOfRowsSet.getInt(1);
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		closeDB();

		numberOfAllOrders = numberOfPlannedOrders + numberOfCompletedOrders + numberOfAcceptedOrders;
		
		statistic += String.valueOf(numberOfAllOrders)+"\n";
		statistic += "Planned orders: "+String.valueOf(numberOfPlannedOrders)+"\n";
		statistic += "Completed orders: "+String.valueOf(numberOfCompletedOrders)+"\n";
		statistic += "Accepted orders: "+String.valueOf(numberOfAcceptedOrders);
		
		return statistic;
	}
}