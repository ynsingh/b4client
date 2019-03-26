package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class update_if_email_not_exist {
 void update(String Name,String Email_Id,String Mobile_No_1,String Mobile_No_2,String Phone_Number,String Postal_Address,String Gender)
 {
		Connection con = sqlite_connection.db_connector();//new
		
		String query = "insert into information2 (Name,Email_Id,Mobile_No_1,Mobile_No_2,Phone_Number,Postal_Address,Gender) values('"+Name+"','"+Email_Id+"','"+Mobile_No_1+"','"+Mobile_No_2+"','"+Phone_Number+"','"+Postal_Address+"','"+Gender+"')";	
		
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Record Udated.");
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
