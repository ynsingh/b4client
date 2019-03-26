package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class update_if_email_exists {
	void update(String Name,String Email_Id,String Mobile_No_1,String Mobile_No_2,String Phone_Number,String Postal_Address,String Gender)
	{
		Connection con = sqlite_connection.db_connector();//new
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		String query = "UPDATE information2 SET Name = ('"+Name +"'), Mobile_No_1 = ('"+Mobile_No_1+"'), Mobile_No_2 = ('"+Mobile_No_2+"'), Phone_number = ('"+Phone_Number+"'), Postal_Address = ('"+Postal_Address+"'), Gender = ('"+Gender+"') WHERE Email_Id = ('"+Email_Id+"')";	

		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(null, "Record Updated.");
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
