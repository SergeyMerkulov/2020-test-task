package com.haulmont.testtask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
	
	public static void tables() {
		
		Connection connection = null;
		Statement statement = null;
		BufferedReader reader = null;
		String line = null;
		
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
		
		try {
			reader = new BufferedReader(new FileReader("createTables.sql"));
				
			while ((line = reader.readLine()) != null) {
				statement.execute(line);
			}
		
			if (reader != null) {
				reader.close();
			}
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
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
	
}