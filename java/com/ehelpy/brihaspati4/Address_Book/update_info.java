package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import net.proteanit.sql.DbUtils;

public class update_info 
{
	void Upload(String Name,String Email_Id,String Mobile_No_1,String Mobile_No_2,String Phone_Number,String Postal_Address,String Gender)
	{
		try 
		{
			Connection con = null;//new
			
			con = sqlite_connection.db_connector();//new
			
			
			Statement stmt = con.createStatement();
			
			String query =  "select Email_Id, count (Email_Id) as num FROM information2 WHERE Email_Id =('"+Email_Id+"') ";
			
			ResultSet rs1 = stmt.executeQuery(query);
			int count =  rs1.getInt(2);
			
			if(count>0)
			{
				stmt.close();
				con.close();
				update_if_email_exists update_exists = new update_if_email_exists();
				update_exists.update(Name, Email_Id, Mobile_No_1, Mobile_No_2, Phone_Number, Postal_Address, Gender);
			}
			
			else if(count == 0)
			{
				stmt.close();
				con.close();
				update_if_email_not_exist update_not_exists = new update_if_email_not_exist();
				update_not_exists.update(Name, Email_Id, Mobile_No_1, Mobile_No_2, Phone_Number, Postal_Address, Gender);
			}
			
			load_updated_table refresh_table = new load_updated_table();
			refresh_table.update();
		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Something Has gone Wrong! please check.");
			
		}
	}
}
