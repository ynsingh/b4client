package com.ehelpy.brihaspati4.authenticate ;
import java.io.InputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function carries out a date time check of the system with the network server
//so that the user is not able to forge validity of the certificate and thus the security
public class dateTimeCheck {
    public static String l_datetime;
    public static String c_date_time;
    public final static int DEFAULT_PORT = 37;

    public static boolean checkDate()
    {
        boolean flag=false;

        String hostname;
        int port = DEFAULT_PORT;
        hostname = "time.nist.gov";
        //hostname = "ntp.iitk.ac.in";

        // The time protocol sets the epoch at 1900,
        // the java Date class at 1970. This number
        // differenceBetweenEpochs converts between them.

        long differenceBetweenEpochs = 2208988800L;

        // If you'd rather not use the magic number uncomment
        // the following section which calculates it directly.

        /*
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        Calendar epoch1900 = Calendar.getInstance(gmt);
        epoch1900.set(1900, 01, 01, 00, 00, 00);
        long epoch1900ms = epoch1900.getTime().getTime();
        Calendar epoch1970 = Calendar.getInstance(gmt);
        epoch1970.set(1970, 01, 01, 00, 00, 00);
        long epoch1970ms = epoch1970.getTime().getTime();

        long differenceInMS = epoch1970ms - epoch1900ms;
        long differenceBetweenEpochs = differenceInMS/1000;
        */

        InputStream raw = null;
        /*try {
          Socket theSocket = new Socket(hostname, port);
          raw = theSocket.getInputStream();

          long secondsSince1900 = 0;
          for (int i = 0; i < 4; i++) {
            secondsSince1900 = (secondsSince1900 << 8) | raw.read();
          }

          long secondsSince1970
           = secondsSince1900 - differenceBetweenEpochs;
          long msSince1970 = secondsSince1970 * 1000;
          Date time = new Date(msSince1970);

          //Date date=new Date();
          SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

          String yourDate=sdf.format(time);
          //c_date_time = yourDate;

          System.out.println("The actual date and time as per Time Server   " + hostname+"   is:   "+ yourDate  );

        }  // end try
        catch (UnknownHostException e) {
          System.err.println(e);
        }
        catch (IOException e) {
          System.err.println(e);
        }
        finally {
          try {
            if (raw != null) raw.close();
          }
          catch (IOException e) {}
        }    */
        c_date_time = getCurrentDateTime();
        //Date server_date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(c_date_time);
        //c_date_time = yourDate;
        // l_date_time=call getLastLogoutTime()
        //l_datetime = getLastLogoutTime();// get from config option
        //l_datetime = "01-06-2017 09:35:00";//hard coded input option
        // l_datetime = raw;
        // option to take last logout time as user input in subsequent commented line
        // JFrame frame = new JFrame("Enter last log out time and date in the format  dd-mm-yyyy hh:mm:ss");
        // l_datetime = JOptionPane.showInputDialog(frame, "Please enter your last logout time and dateas per format");

        // Compare c_date_time and l_datetime

        try {
            @SuppressWarnings("resource")
            Socket theSocket = new Socket(hostname, port);
            raw = theSocket.getInputStream();

            long secondsSince1900 = 0;
            for (int i = 0; i < 4; i++) {
                secondsSince1900 = (secondsSince1900 << 8) | raw.read();
            }
            long secondsSince1970 = secondsSince1900 - differenceBetweenEpochs;
            long msSince1970 = secondsSince1970 * 1000;
            Date time = new Date(msSince1970);

            //Date date=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            String yourDate=sdf.format(time);

            if (yourDate.compareTo(c_date_time) >= 0 ) {

                // If c_date_time is greater than l_date_time then set flag true
                debug_level.debug(3,"Current date & time of your system is:   " + c_date_time);
                debug_level.debug(0,"Last logout date & time of your system is:   " + l_datetime);
                debug_level.debug(3,"System clock is correct");
                flag=true;
            }
            else {

                debug_level.debug(0,"System's current date & time is:   " + c_date_time);
                debug_level.debug(0,"last logout date & time is:   " + l_datetime);
                debug_level.debug(3,"System clock is not correct");
                flag=false;
            }
        }
        catch (Exception e) {
        }

        return true;
    }

    public static String getCurrentDateTime() {

        //get the current DateTime
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /***This code segment to be used when we get last logout time from config class***
     public static String getLastLogoutTime()
    {
    	try{
    	//Read the last usage time from B4conf.properties file.
      	//l_datetime = Config.getConfigObject().getLastLogoutTime();
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
    }*/
}
