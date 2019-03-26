package com.ehelpy.brihaspati4.Address_Book;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class upload_csv
{
	public static void csv(String file)
	{
		//code for uploading csv file//
		try 
		{
			BufferedReader csv = new BufferedReader(new FileReader(file));
			
			Connection con = null;//new
			
			con = sqlite_connection.db_connector();//new
			
			String line;
			
			try 
			{
				while((line = csv.readLine())!=null)
				{
					String[] value = line.split(",");
					Statement stmt = null;
					
					try {
						stmt = con.createStatement();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
										
				//	String query = "insert into information (con_no,name,class,roll,adm,tution,gender) values('"+value[0]+"','"+value[1]+"','"+value[2]+"','"+value[3]+"','"+value[4]+"','"+value[5]+"','"+value[6]+"')";
					
					String query = "insert into information2 (Name,Email_Id,Mobile_No_1,Mobile_No_2,Phone_Number,Postal_Address,Gender) values('"+value[0]+"'"+","+"'"+value[1]+"'"+","+"'"+value[2]+"'"+","+"'"+value[3]+"'"+","+"'"+value[4]+"'"+","+"'"+value[5]+"'"+","+"'"+value[6]+"')";
					try {
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				
				csv.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
	}
}
