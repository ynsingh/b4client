package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.crypto.SecretKey;
import javax.sound.sampled.TargetDataLine;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class recorder_rxthread extends Thread{
	public TargetDataLine audio_in;
	public static DatagramSocket line ;
	byte byte_buff[] = new byte[512];
	byte[] data_send,enc_data = new byte[512];
	public InetAddress far_endip;
	public int comn_port;
	public static boolean flag = true;
	public static boolean status = false;
	private SecretKey sec_key = null;
	public recorder_rxthread(SecretKey sec_key) {
		// TODO Auto-generated constructor stub
		this.sec_key = sec_key;
	}
	
	@Override
	public void run() {
		Long pack = 0l;
		
			try {
				
				while (voip_receive.calling) {
					
				audio_in.read(byte_buff, 0, byte_buff.length);
				DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, far_endip, comn_port);
				data_send = data.getData();
				debug_level.debug(0,"secret key is"+ sec_key);
				enc_data = encrypt_msg.encrypt_calldata(sec_key, data_send);
				data = new DatagramPacket(enc_data, enc_data.length,far_endip, comn_port );
				debug_level.debug(1,"send: #"+ pack++);
				line.send(data);
				debug_level.debug(1, "thread is running");
				
					}
			}
				catch (IOException e) {
					voip_receive.calling = false;
					if(audio_in!=null) audio_in.close();
		    		status =true;
		    		if (line!= null) line.disconnect();
		    		if (line!= null) line.close();
				}
			  finally{
		    		debug_level.debug(1,"call in recorder: recorder is stop");
		    		if(audio_in!=null) audio_in.close();
		    		status =true;
		    		if (line!= null) line.disconnect();
		    		if (line!= null) line.close();
		    		
		    
		    		debug_level.debug(1,"call in recorder: audio is drain and close");
		    	}
		status = true;
		debug_level.debug(1, "call in recorder: audio is drain and close");
	}
	


}
