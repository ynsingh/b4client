package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.TargetDataLine;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class recorder_rxthread extends Thread
 {
	public TargetDataLine audio_in;
	public static DatagramSocket line ;
	byte byte_buff[] = new byte[512];
	byte[] data_send,enc_data = new byte[512];
	public InetAddress far_endip;
	public int comn_port;
	public static boolean flag = true;
	public static boolean status = false;
	private static long symkeynew = 0;
	private SecretKey sec_key = null;
	private static SecretKey seckeynew = null;
	public recorder_rxthread(SecretKey sec_key) 
	  {
		// TODO Auto-generated constructor stub
		this.sec_key = sec_key;
	  }
	
	@Override
	public void run() 
	 {
		Long pack = 0l;
		
			try 
			 {
				
				while (voip_receive.calling) 
					{
					     if (pack==1500) 
	         	          {
					    	   changekey_client chkc= new changekey_client(far_endip.getHostAddress());
			                   Thread d= new Thread(chkc);
			                   d.start();
			         		   pack=(long) 01;
	         	            
	         	          }
					        audio_in.read(byte_buff, 0, byte_buff.length);
							DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, far_endip, comn_port);
							data_send = data.getData();
							debug_level.debug(0,"secret key is"+ sec_key);
							enc_data = encrypt_msg.encrypt_calldata(sec_key, data_send);
							data = new DatagramPacket(enc_data, enc_data.length,far_endip, comn_port );
							debug_level.debug(1,"#####  send voice packet no : #"+ pack++);
							line.send(data);
							debug_level.debug(1, "recorder thread is running");
							
							 if(changekey_client.changekeycflag==true) 
							    {
						           try {
									      symkeynew=changekey_client.get();
									      byte[] sym_byte_key = longtobyte_bytetolongarray.longtobytearraysymkey(symkeynew);
										  seckeynew = new SecretKeySpec(sym_byte_key, "AES");
										  this.sec_key=seckeynew;
										  System.out.println("/////// New secret key for encryption at reciever end is "+sec_key);
										  player_rxthread.seckey2 = seckeynew;
										  changekey_client.changekeycflag=false;
								       }
						           catch (InterruptedException e) {
									      // TODO Auto-generated catch block
									      e.printStackTrace();
								       }
						
					              }
					}
			 }
			 
			catch (IOException e) 
			   {
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
