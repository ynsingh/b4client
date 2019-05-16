package com.ehelpy.brihaspati4.Address_Book;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.ehelpy.brihaspati4.Address_Book.Show_Details_Window;
import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.sms.Send_SMS_Window;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import net.proteanit.sql.DbUtils;

public class Show_Details_Fetch
{
	static String email_id;
	public static ServerSocketChannel ss = null;
	void Upload_Details_In_Window(String Email_Id)
	{
				
		email_id = Email_Id;
		try 
		{
			Connection con = null;//new
			
			con = sqlite_connection.db_connector();//new
			
			Statement stmt = con.createStatement();
			
			String query = "select * from information2 where Email_Id =('"+Email_Id+"')";
			ResultSet rs = stmt.executeQuery(query);
			if(!rs.next())
			{
				JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
				stmt.close();
				con.close();
			}
			else
			{
				
				Show_Details_Window.name_text.setText(rs.getString(1));
				Show_Details_Window.email_id_text.setText(rs.getString(2));
				Show_Details_Window.mobile_no_1_text.setText(rs.getString(3));
				Show_Details_Window.mobile_no_2_text.setText(rs.getString(4));
				Show_Details_Window.phone_number_text.setText(rs.getString(5));
				Show_Details_Window.postal_address_text.setText(rs.getString(6));
				Show_Details_Window.gender_text.setText(rs.getString(7));
				stmt.close();
				con.close();
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void message(String Email_Id)
	{
		Send_SMS_Window.get_email_id(Email_Id, "AddressBook");
		
		Send_SMS_Window obj = new Send_SMS_Window();
		obj.setVisible(true);
	}
	
	void delete()
	{
				
		Connection con = null;//new
		
		con = sqlite_connection.db_connector();//new
		
		String query = "delete  from information2 where Email_Id =('"+email_id+"')";
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stmt .executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Record Deleted Sucessfully. PRESS OK");
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
		con = sqlite_connection.db_connector();//new
		String query1 = "SELECT Name,Email_Id  FROM information2";
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
}
