package com.accelo.api.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase 
{
public static Connection con;
	/**
	*	Please edit this method with the database credentials.
	*/
	public static void connectDB() throws ClassNotFoundException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try 
		{
			con = DriverManager.getConnection(url,"ACCELO", "accelo");
		} 
		catch (SQLException e) 
		{
			System.out.println("Connection failed");
			System.out.println("Reason for failure: "+e.toString());
		}
		if(con!=null) {
			// DO NOTHING
		}
	}

}

