package com.ehelpy.brihaspati4.Address_Book;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.ehelpy.brihaspati4.sms.Send_SMS_Window;
import com.ehelpy.brihaspati4.voip.voip_call;

import net.proteanit.sql.DbUtils;

public class Search_Info {
	void selection(String Received, String flag)
	{
		try 
		{
						
			String col_name = flag;
			
			Connection con = null;//new
			
			con = sqlite_connection.db_connector();//new
			
			Statement stmt = con.createStatement();
			
			if(col_name=="Name")
			{
				String query1 = "SELECT Name, count (Name) as num FROM information2 WHERE Name=('"+Received+"')";
				ResultSet rs1 = stmt.executeQuery(query1);
				int count =  rs1.getInt(2);
				if(count>1)
				{
					stmt.close();
					con.close();
					Multiple_Entries.Receive(Received,col_name);
					Multiple_Entries  multiEnt = new Multiple_Entries();
					multiEnt.setVisible(true);
						
				}
				else 
				{
					String query = "select * from information2 where Name =('"+Received+"')";
					ResultSet rs = stmt.executeQuery(query);
					if(!rs.next())
					{
						JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
						stmt.close();
						con.close();
						Search_Window search = new Search_Window();
						search.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Searching By Name. PRESS OK.");
						Search_Window search = new Search_Window();
						search.setVisible(true);
						Search_Window.name_text.setText(rs.getString(1));
						Search_Window.email_text.setText(rs.getString(2));
						Search_Window.mobile_no_1_text.setText(rs.getString(3));
						Search_Window.mobile_no_2_text.setText(rs.getString(4));
						Search_Window.phone_number_text.setText(rs.getString(5));
						Search_Window.address_text.setText(rs.getString(6));
						Search_Window.gender_text.setText(rs.getString(7));
						stmt.close();
						con.close();
					}
				 }
			}
			
			else if(col_name=="Email_Id")
			{
				String query1 = "SELECT Email_Id, count (Email_Id) as num FROM information2 WHERE Email_Id=('"+Received+"')";
				ResultSet rs1 = stmt.executeQuery(query1);
				int count =  rs1.getInt(2);
				if(count>1)
				{
					stmt.close();
					con.close();
					Multiple_Entries.Receive(Received,col_name);
					Multiple_Entries  multiEnt = new Multiple_Entries();
					multiEnt.setVisible(true);
				}
				else 
				{
					String query = "select * from information2 where Email_Id =('"+Received+"')";
					ResultSet rs = stmt.executeQuery(query);
					if(!rs.next())
					{
						JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
						stmt.close();
						con.close();
						Search_Window search = new Search_Window();
						search.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Searching By Email_Id. PRESS OK.");
						Search_Window search = new Search_Window();
						search.setVisible(true);
						Search_Window.name_text.setText(rs.getString(1));
						Search_Window.email_text.setText(rs.getString(2));
						Search_Window.mobile_no_1_text.setText(rs.getString(3));
						Search_Window.mobile_no_2_text.setText(rs.getString(4));
						Search_Window.phone_number_text.setText(rs.getString(5));
						Search_Window.address_text.setText(rs.getString(6));
						Search_Window.gender_text.setText(rs.getString(7));
						stmt.close();
						con.close();
					}
				 }
			}
			
			else if(col_name=="Mobile_No_1")
			{
				String query1 = "SELECT Mobile_No_1, count (Mobile_No_1) as num FROM information2 WHERE Mobile_No_1=('"+Received+"')";
				ResultSet rs1 = stmt.executeQuery(query1);
				if(rs1.getInt(2)>1)
				{
					stmt.close();
					con.close();
					Multiple_Entries.Receive(Received,col_name);
					Multiple_Entries  multiEnt = new Multiple_Entries();
					multiEnt.setVisible(true);
				}
				else 
				{
					String query = "select * from information2 where Mobile_No_1 =('"+Received+"')";
					ResultSet rs = stmt.executeQuery(query);
					if(!rs.next())
					{
						JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
						stmt.close();
						con.close();
						Search_Window search = new Search_Window();
						search.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Searching By Mobile_No_1. PRESS OK.");
						Search_Window search = new Search_Window();
						search.setVisible(true);
						Search_Window.name_text.setText(rs.getString(1));
						Search_Window.email_text.setText(rs.getString(2));
						Search_Window.mobile_no_1_text.setText(rs.getString(3));
						Search_Window.mobile_no_2_text.setText(rs.getString(4));
						Search_Window.phone_number_text.setText(rs.getString(5));
						Search_Window.address_text.setText(rs.getString(6));
						Search_Window.gender_text.setText(rs.getString(7));
						stmt.close();
						con.close();
					}
				 }
			}
			
			else if(col_name=="Mobile_No_2")
			{
				String query1 = "SELECT Mobile_No_2, count (Mobile_No_2) as num FROM information2 WHERE Mobile_No_2=('"+Received+"')";
				ResultSet rs1 = stmt.executeQuery(query1);
				if(rs1.getInt(2)>1)
				{	
					stmt.close();
					con.close();
					Multiple_Entries.Receive(Received,col_name);
					Multiple_Entries  multiEnt = new Multiple_Entries();
					multiEnt.setVisible(true);
				}
				else 
				{
					String query = "select * from information2 where Mobile_No_2 =('"+Received+"')";
					ResultSet rs = stmt.executeQuery(query);
					if(!rs.next())
					{
						JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
						stmt.close();
						con.close();
						Search_Window search = new Search_Window();
						search.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Searching By Mobile_No_2. PRESS OK.");
						Search_Window search = new Search_Window();
						search.setVisible(true);
						Search_Window.name_text.setText(rs.getString(1));
						Search_Window.email_text.setText(rs.getString(2));
						Search_Window.mobile_no_1_text.setText(rs.getString(3));
						Search_Window.mobile_no_2_text.setText(rs.getString(4));
						Search_Window.phone_number_text.setText(rs.getString(5));
						Search_Window.address_text.setText(rs.getString(6));
						Search_Window.gender_text.setText(rs.getString(7));
						stmt.close();
						con.close();
					}
				 }
			}
			
			else if(col_name=="Phone_Number")
			{
				String query1 = "SELECT Phone_Number, count (Phone_Number) as num FROM information2 WHERE Phone_Number=('"+Received+"')";
				ResultSet rs1 = stmt.executeQuery(query1);
				if(rs1.getInt(2)>1)
				{
					stmt.close();
					con.close();
					Multiple_Entries.Receive(Received,col_name);
					Multiple_Entries  multiEnt = new Multiple_Entries();
					multiEnt.setVisible(true);
				}
				else 
				{
					String query = "select * from information2 where Phone_Number =('"+Received+"')";
					ResultSet rs = stmt.executeQuery(query);
					if(!rs.next())
					{
						JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
						stmt.close();
						con.close();
						Search_Window search = new Search_Window();
						search.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Searching By Phone_Number. PRESS OK.");
						Search_Window search = new Search_Window();
						search.setVisible(true);
						Search_Window.name_text.setText(rs.getString(1));
						Search_Window.email_text.setText(rs.getString(2));
						Search_Window.mobile_no_1_text.setText(rs.getString(3));
						Search_Window.mobile_no_2_text.setText(rs.getString(4));
						Search_Window.phone_number_text.setText(rs.getString(5));
						Search_Window.address_text.setText(rs.getString(6));
						Search_Window.gender_text.setText(rs.getString(7));
						stmt.close();
						con.close();
					}
				 }
			}
			
			else if(col_name=="Postal_Address")
			{
				String query1 = "SELECT Postal_Address, count (Postal_Address) as num FROM information2 WHERE Postal_Address=('"+Received+"')";
				ResultSet rs1 = stmt.executeQuery(query1);
				if(rs1.getInt(2)>1)
				{
					stmt.close();
					con.close();
					Multiple_Entries.Receive(Received,col_name);
					Multiple_Entries  multiEnt = new Multiple_Entries();
					multiEnt.setVisible(true);
				}
				else 
				{
					String query = "select * from information2 where Postal_Address =('"+Received+"')";
					ResultSet rs = stmt.executeQuery(query);
					if(!rs.next())
					{
						JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
						stmt.close();
						con.close();
						Search_Window search = new Search_Window();
						search.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Searching By Postal_Address. PRESS OK.");
						Search_Window search = new Search_Window();
						search.setVisible(true);
						Search_Window.name_text.setText(rs.getString(1));
						Search_Window.email_text.setText(rs.getString(2));
						Search_Window.mobile_no_1_text.setText(rs.getString(3));
						Search_Window.mobile_no_2_text.setText(rs.getString(4));
						Search_Window.phone_number_text.setText(rs.getString(5));
						Search_Window.address_text.setText(rs.getString(6));
						Search_Window.gender_text.setText(rs.getString(7));
						stmt.close();
						con.close();
					}
				 }
			}
			
			else if(col_name=="Gender")
			{
				if(Received.equals("Male")||Received.equals("male"))
				{
					String query1 = "SELECT Gender, count (Gender) as num FROM information2 WHERE Gender='Male'or Gender='male' ";
					ResultSet rs1 = stmt.executeQuery(query1);
								
					if(rs1.getInt(2)>1)
					{
						stmt.close();
						con.close();
						Multiple_Entries.Receive(Received,col_name);
						Multiple_Entries  multiEnt = new Multiple_Entries();
						multiEnt.setVisible(true);
					}
					else 
					{
						String query = "select * from information2 where Gender ='Male'or Gender='male'";
						ResultSet rs = stmt.executeQuery(query);
						if(!rs.next())
						{
							JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
							stmt.close();
							con.close();
							Search_Window search = new Search_Window();
							search.setVisible(true);
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Searching By Gender. PRESS OK.");
							Search_Window search = new Search_Window();
							search.setVisible(true);
							Search_Window.name_text.setText(rs.getString(1));
							Search_Window.email_text.setText(rs.getString(2));
							Search_Window.mobile_no_1_text.setText(rs.getString(3));
							Search_Window.mobile_no_2_text.setText(rs.getString(4));
							Search_Window.phone_number_text.setText(rs.getString(5));
							Search_Window.address_text.setText(rs.getString(6));
							Search_Window.gender_text.setText(rs.getString(7));
							stmt.close();
							con.close();
						}
					}
				}
				else if(Received.equals("Female")||Received.equals("female"))
				{
					String query1 = "SELECT Gender, count (Gender) as num FROM information2 WHERE Gender='Female' or Gender='female' ";
					ResultSet rs1 = stmt.executeQuery(query1);
								
					if(rs1.getInt(2)>1)
					{
						stmt.close();
						con.close();
						Multiple_Entries.Receive(Received,col_name);
						Multiple_Entries  multiEnt = new Multiple_Entries();
						multiEnt.setVisible(true);
					}
					else 
					{
						String query = "select * from information2 where Gender ='Female' or Gender='female'";
						ResultSet rs = stmt.executeQuery(query);
						if(!rs.next())
						{
							JOptionPane.showMessageDialog(null, "No Record Found. PRESS OK TO TRY AGAIN.");
							stmt.close();
							con.close();
							Search_Window search = new Search_Window();
							search.setVisible(true);
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Searching By Gender. PRESS OK.");
							Search_Window search = new Search_Window();
							search.setVisible(true);
							Search_Window.name_text.setText(rs.getString(1));
							Search_Window.email_text.setText(rs.getString(2));
							Search_Window.mobile_no_1_text.setText(rs.getString(3));
							Search_Window.mobile_no_2_text.setText(rs.getString(4));
							Search_Window.phone_number_text.setText(rs.getString(5));
							Search_Window.address_text.setText(rs.getString(6));
							Search_Window.gender_text.setText(rs.getString(7));
							stmt.close();
							con.close();
						}
					}
				}
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
}
		
		






