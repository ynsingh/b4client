package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import net.proteanit.sql.DbUtils;

public class New_Entry_Upload 
{
	void Upload(String Name,String Email_Id ,String Mobile_No_1,String Mobile_No_2,String Phone_Number,String Postal_Address,String Gender)
	{
		try 
		{
			Connection con = null;//new
			
			con = sqlite_connection.db_connector();//new
			
			String query = "insert into information2 (Name,Email_Id,Mobile_No_1,Mobile_No_2,Phone_Number,Postal_Address,Gender) values('"+Name+"','"+Email_Id+"','"+Mobile_No_1+"','"+Mobile_No_2+"','"+Phone_Number+"','"+Postal_Address+"','"+Gender+"')";	
			
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.execute();
			JOptionPane.showMessageDialog(null, "Record Inserted Sucessfully");
			stmt.close();
			con.close();
			
			con = sqlite_connection.db_connector();//new
			String query1 = "SELECT Name,Email_Id  FROM information2 order by Name";
			try 
			{
				PreparedStatement stmt1 = con.prepareStatement(query1);
				ResultSet rs = stmt1.executeQuery();
				
				Display_Window_After_Login.table.setModel(DbUtils.resultSetToTableModel(rs));
				stmt1.close();
				con.close();
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Entered Email Id has already been assigned to someone else.");
			
		}
	}
}
