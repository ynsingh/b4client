package com.ehelpy.brihaspati4.authenticate ; 

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function creates connection with the identity server with the 
//certificate signing request using send get and send post methods 
public class createConnection   {
	private final static String USER_AGENT = "Chrome";
	
	
	// HTTP GET request
			public static boolean sendGet(String urlmaster) throws Exception {

			//String url = "http://www.google.com/search?q=mkyong";	
			boolean serverstat = false;
			URL obj = new URL(urlmaster);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			StringBuffer response =null;
			int responseCode =0;
			try{
				responseCode= con.getResponseCode();
			}catch (Exception e)
			{
				server_down.id_exist();
			
			}
			System.out.println("\nSending 'GET' request to URL : " + urlmaster);
			System.out.println("Response Code : " + responseCode);
			BufferedReader in = null;
			try{
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			    			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				serverstat = true;
			}
			
			in.close();
}catch (Exception e) {
				
			}
			
			//print result
			//System.out.println(response.toString());
			return serverstat ;
		}

		// HTTP POST request
		void sendPost(String urlmaster)  {

			//String mserverurl ="http://localhost:8080/IS";				
			//String MSrequrl = mserverurl +"/ProcessRequest?req=sscccertsign&";

		try {	
			URL obj = new URL(urlmaster);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
		
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + urlmaster);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine = null;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				
			}
			in.close();

			//print result
			System.out.println(response.toString());
		}
		catch(Exception e) {
			return;
		}
		
		}
	}

