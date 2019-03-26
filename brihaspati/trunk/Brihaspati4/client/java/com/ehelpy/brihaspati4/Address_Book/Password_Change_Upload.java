package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class Password_Change_Upload
{
	void new_password(String name,String password)
	{
		try 
		{
			Connection con = null;//new
			
			con = sqlite_connection.db_connector();//new
			
			Statement stmt = con.createStatement();
			
			String query= "UPDATE login SET password= ('"+password+"') WHERE name = ('"+name+"')";
									
			stmt.executeUpdate(query);
			
			stmt.close();
			con.close();
			
			JOptionPane.showMessageDialog(null, "CHANGE UPDATED");
				
		
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
