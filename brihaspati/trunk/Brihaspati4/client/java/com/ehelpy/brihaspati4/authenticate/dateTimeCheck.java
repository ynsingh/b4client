package com.ehelpy.brihaspati4.authenticate ;
//This program has been built by Lt Col Ankit Singhal Dated 08 July 2019; 2330 Hrs
//It carries out a date time check of the system with the network server so that the user is not able to forge validity of the certificate.
//The code retrieves a network packet from B4 or Google server (or any server defined) and extracts timestamp from it as Date1.
//Date1 is compared with system date and time (Date2). The system date has to be equal or ahead of server time so as to ensure that user is not able to forge
//certificate validity.
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class dateTimeCheck 
{
	public static String l_datetime;
    public static String c_date_time; 
    public static boolean checkDate() throws Exception{
		System.out.println("DATE AND TIME CHECK OF YOUR SYSTEM IS UNDER PROGRESS") ; 
    	boolean flag=false;	// flag which returns true if date time is correct or false if incorrect.    
		//String serverUrl = "http://172.20.82.6:8080/b4server"; // here the server ip can be specified as desired.
		String serverUrl = "https://www.google.co.in";
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault());//to fetch date in local (zone)date time format 
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    Date date1 = null; //server date and time
	        try
	        {
	            date1 = sdf.parse(getServerHttpDate(serverUrl)); // fetches date of the server
	        }
	        catch (ParseException e)
	        {
	            e.printStackTrace();
	        }       

	     if (sdf2.format(date1).compareTo(getCurrentDateTime()) <= 0 ) {					// if server date is less than or equal to system date then system clock ok
	    	System.out.println("Server Date & Time is :   " +sdf2.format(date1) );
	    	debug_level.debug(3,"Your System Date & Time is :   " + getCurrentDateTime());                
	    	//debug_level.debug(0,"Last logout date & time of your system is:   " + getLastLogoutTime());
	    	debug_level.debug(3,"Your System Clock is CORRECT");          
	    	flag=true;
	        
	     	} else     	{										// if server date is not less than or equal to system date then system clock incorrect
	     		System.out.println("Server Date & Time is :   " +sdf2.format(date1) );
	    	debug_level.debug(3,"Your System Date & Time is :   " + getCurrentDateTime());
	    	//debug_level.debug(0,"Last logout date & time of your system is:   " + getLastLogoutTime());
	    	debug_level.debug(3,"Your System clock is INCORRECT");
	    	flag=false;
	     	}
	    return flag; // returns false to intimate user to correct system date and time and true to log on the network if system clock correct
	  }
    
    
		private static String getServerHttpDate(String serverUrl) throws IOException {
	    URL url = new URL(serverUrl);
	    URLConnection connection = url.openConnection();
	    Map<String, List<String>> httpHeaders = connection.getHeaderFields();
	    for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
	      String headerName = entry.getKey();
	      if (headerName != null && headerName.equalsIgnoreCase("date")) {
	        return entry.getValue().get(0);						// retrieving server date and time and returning to main function
	      }
	    }
	    return null;											// returns null if server not online
	  }
  

	  public static String getCurrentDateTime() {				// fetches current system date and time
	        //DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	        Date date = new Date();
	        return dateFormat.format(date);
	    }
	    /***This code segment to be used when we get last logout time from config class***/
	     public static String getLastLogoutTime()
	    {
	    	try{
	    	//Read the last usage time from B4conf.properties file.
	      	l_datetime = Config.getConfigObject().getLastLogoutTime();
	        }
	    	catch(Exception e){
	    		l_datetime=null;
	    	}
	    	return  l_datetime;
	    }
	    public static void setLastLogoutTime()
	    {
	            System.out.println("setting the logout time in properties file..........");
	    	l_datetime = getCurrentDateTime();
	    	//Set this time in B4conf.properties file when user logouts.
	    	Config.getConfigObject().saveLastLogoutTime(l_datetime);
	    }

	}
