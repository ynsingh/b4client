package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class sqlite_connection 
{
	
	public static Connection db_connector()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			Connection conn = null;
			try {
				conn = DriverManager.getConnection("jdbc:sqlite::resource:address_book.sqlite");
			//	conn = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\address_book.sqlite");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			return conn;
		} 
		catch (ClassNotFoundException e)
		{
			JOptionPane.showMessageDialog(null,e);
			return null;
		}
		
	}
}
