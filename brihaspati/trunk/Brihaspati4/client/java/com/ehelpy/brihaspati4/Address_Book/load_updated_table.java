package com.ehelpy.brihaspati4.Address_Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.proteanit.sql.DbUtils;

public class load_updated_table {
	void update()
	{
		Connection con = sqlite_connection.db_connector();//new
		String query = "SELECT Name,Email_Id  FROM information2 order by Name";
		try 
		{
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			
			Display_Window_After_Login.table.setModel(DbUtils.resultSetToTableModel(rs));
			stmt.close();
			con.close();
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}

}
