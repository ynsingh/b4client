package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.sound.sampled.SourceDataLine;
import com.ehelpy.brihaspati4.authenticate.Gui;
import com.ehelpy.brihaspati4.authenticate.debug_level;

public class player_rxthread extends Thread {
	public DatagramSocket din;
	public static SourceDataLine audio_out;
	public static boolean flag = true;
	public static boolean status = false;
	byte[] buffer= new byte[512];
	byte[] buffer_dat = null;
	private static SecretKey seckey1 = null;
	static SecretKey seckey2 = null;
	private static DatagramPacket incoming = null;

	    public player_rxthread(SecretKey sec_key) {
		seckey1 = sec_key;
	}
	    public void stopRunning()
	    {
	        flag = false;
	    }
		@Override
	    public void run(){
			
	    try {
	         incoming = new DatagramPacket(buffer, buffer.length);
	         
	        	
	        debug_level.debug(1,"Server socket created. Waiting for incoming data...");
	        while(voip_receive.flag)
	            {
	        	
	        		din.receive(incoming);
	 	            buffer = incoming.getData();
	 	            debug_level.debug(0,"secret key is " + seckey1);
	 	            if(seckey2==null) 
	 	             {
	 	               buffer_dat = decrypt_msg.decrypt_calldata(seckey1, buffer);
	 	             }
	 	            else 
	 	             {
	 	            	buffer_dat = decrypt_msg.decrypt_calldata(seckey2, buffer);
	 	            	seckey1=seckey2;
	 	            	seckey2=null;
	 	             }
	 	            audio_out.write(buffer_dat, 0, buffer_dat.length);
	 	        
	        	}
	        } 
	    
	    catch (SocketException ex) {
	        	voip_receive.flag =false;
	        	debug_level.debug(1,"call in player: player is stop");
	    		if(audio_out!=null) audio_out.drain();
	    		if(audio_out!=null) audio_out.close();
	    		status =false;
	    		if(din!=null) din.disconnect();
	    		if(din!=null) din.close();
	    		
	        	
	        } 
	    catch (SocketTimeoutException ex) {
        	voip_receive.flag =false;
        	if (!voip_rxcall.flag)
        	{
	    		 
	    		 String msg="CALL HAS ENDED. PRESS OK AND STOP";
		         Gui.showMessageDialogBox(msg);}
        	debug_level.debug(1,"call in player: player is stop");
    		if(audio_out!=null) audio_out.drain();
    		if(audio_out!=null) audio_out.close();
    		status =false;
    		if(din!=null) din.disconnect();
    		if(din!=null) din.close();
    		
        	
        } 
	    
	    catch (IOException ex) {
	            Logger.getLogger(player_thread.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    		
	    
	    finally{
	    		debug_level.debug(1,"call in player: player is stop");
	    		if(audio_out!=null) audio_out.drain();
	    		if(audio_out!=null) audio_out.close();
	    		status =false;
	    		if(din!=null)  din.disconnect();
	    		if(din!=null)  din.close();}
	    		debug_level.debug(1,"call in player: audio is drain and close");
	    	}

	    	
}
		

