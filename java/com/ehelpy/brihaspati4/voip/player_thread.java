package com.ehelpy.brihaspati4.voip;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.sound.sampled.SourceDataLine;

import com.ehelpy.brihaspati4.authenticate.Gui;
import com.ehelpy.brihaspati4.authenticate.debug_level;


public class player_thread extends Thread 
{ 
  public static DatagramSocket din=null;
  public static SourceDataLine audio_out=null;
  public static boolean flag = true;
  public static boolean status = false;
  byte[] buffer= new byte[512];
  byte[] buffer_dat = null;
  private static SecretKey seckey1 = null;
  static SecretKey seckey2 = null;
  public static DatagramPacket incoming = null;
    public player_thread(SecretKey sec_key) 
    {
	seckey1= sec_key;
     }
    public void stopRunning()
    {
        flag = false;
    }
	@Override
    public void run()
	{
			
    try 
      {
        
        incoming = new DatagramPacket(buffer, buffer.length);
        	
        debug_level.debug(1,"Server socket created. Waiting for incoming data...");
        while(voip_start.flag)
            {
        		
        		debug_level.debug(0, "Entered player thread");
                din.receive(incoming);
                buffer = incoming.getData();
                debug_level.debug(0,"secret key  for decryption is " + seckey1);
                if(seckey2==null) 
	                {
	                  buffer_dat = decrypt_msg.decrypt_calldata(seckey1, buffer);
	                }
	            else 
	               {
	            	buffer_dat = decrypt_msg.decrypt_calldata(seckey2, buffer);
	            	seckey1=seckey2;
	            	System.out.println("/////// New secret key for decryption at caller end is "+seckey1);
	            	seckey2=null;
	               }
                
                audio_out.write(buffer_dat, 0, buffer_dat.length);
                
                
            }
            
      }
    catch (SocketException x) 
        {
    	
    	voip_start.flag = false;
        	din.disconnect();
        	din.close();
        	
		   x.printStackTrace();
        } 
    
    catch (SocketTimeoutException ex) {
    	voip_start.flag =false;
    	if (!voip_rxcall.flag)
    	{
    		 
    		 String msg="CALL HAS ENDED. PRESS OK AND STOP";
	         Gui.showMessageDialogBox(msg);
	      }
    	din.disconnect();
    	din.close();
    }
    catch (IOException ex) 
        {
            Logger.getLogger(player_thread.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    		
    
    finally 
       {
		debug_level.debug(1,"call in player: player is stop");
		if(audio_out!=null) audio_out.drain();
     	if(audio_out!=null) audio_out.close();
     	
	    status =false;
		if(din!=null) 
		   {
			try 
			  {
				if(din.isConnected())din.setSoTimeout(1);
			  } 
			catch (SocketException e) 
			  {
				
				e.printStackTrace();
			  }
		     din.disconnect();
		     din.close();
		   }
		
		 debug_level.debug(1,"call in player: audio is drain and close");
		}
	
	}
	
}
