package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.sound.sampled.SourceDataLine;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class player_thread extends Thread { 
public static DatagramSocket din=null;
public static SourceDataLine audio_out=null;
public static boolean flag = true;
public static boolean status = false;
byte[] buffer= new byte[512];
byte[] buffer_dat = null;
private static SecretKey seckey = null;
public static DatagramPacket incoming = null;
    public player_thread(SecretKey sec_key) {
	seckey= sec_key;
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
        while(voip_start.flag){
        		
        		debug_level.debug(0, "Entered player thread");
                din.receive(incoming);
                buffer = incoming.getData();
                debug_level.debug(0,"secret key  for decryption is " + seckey);
                buffer_dat = decrypt_msg.decrypt_calldata(seckey, buffer);
                audio_out.write(buffer_dat, 0, buffer_dat.length);
        	
        } 
    }catch (SocketException ex) {
        	voip_start.flag = false;
        	din.disconnect();
        	din.close();
        } catch (IOException ex) {
            Logger.getLogger(player_thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    		
    
finally {
		debug_level.debug(1,"call in player: player is stop");

		if(audio_out!=null) audio_out.drain();
		
		if(audio_out!=null) audio_out.close();
		
		status =false;
		
		if(din!=null) {
			try {
				if(din.isConnected())din.setSoTimeout(1);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			din.disconnect();
			din.close();
			
		}
		
				debug_level.debug(1,"call in player: audio is drain and close");
		}
	
	}
	
}
