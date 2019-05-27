package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.TargetDataLine;
import com.ehelpy.brihaspati4.authenticate.debug_level;

public class recorder_thread extends Thread {
	public TargetDataLine audio_in;
	byte byte_buff[] = new byte[512];
	byte[] data_send,enc_data = new byte[512];
	public InetAddress far_endip;
	public int comn_port;
	public static boolean flag = true;
	public static boolean status = false;
	public static String message= null;
	private static SecretKey seckeynew = null;
	private SecretKey sec_key = null;
	private static long symkeynew;
	public recorder_thread(SecretKey sec_key) {
		this.sec_key = sec_key;
	}
	
	@Override
	public void run() 
	  {
		Long pack = 0l;
	    
		
			try 
			   {  
				  while (voip_start.calling) 
				     {
			              
			          	if(player_thread.din.isClosed()) 
			          	   {
					        try {
						          Thread.sleep(1);
					            } 
					        catch (InterruptedException e) 
					            {
						            e.printStackTrace();
					            }
				           }
				        else 
				           {
					               if(pack==1500) 
					         	     {
					            	   changekey_server chks= new changekey_server(6666);
					                   Thread c= new Thread(chks);
					                   c.start();
					         		   pack=(long) 01;
					         	     }
				               	   
				        	       audio_in.read(byte_buff, 0, byte_buff.length);
						           DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, far_endip, comn_port);
						           data_send = data.getData();
						           debug_level.debug(0,"secret key is"+ sec_key);
						           enc_data = encrypt_msg.encrypt_calldata(sec_key, data_send);
						           data = new DatagramPacket(enc_data, enc_data.length,player_thread.incoming.getAddress(), player_thread.incoming.getPort()  );
						           debug_level.debug(1,"#####   send voice packet no : #"+ pack++);
						           player_thread.din.send(data);
						           debug_level.debug(1, "caller thread 1 is running");
						           
						           
						           if(changekey_server.changekeysflag==true) 
						            {
						             try {
									      symkeynew=changekey_server.get();
									      byte[] sym_byte_key = longtobyte_bytetolongarray.longtobytearraysymkey(symkeynew);
										  seckeynew = new SecretKeySpec(sym_byte_key, "AES");
										  this.sec_key=seckeynew;
										  System.out.println("/////// New secret key for encryption at caller end is "+sec_key);
										  player_thread.seckey2 = seckeynew;
										  changekey_server.changekeysflag=false;
								         }
						            catch (InterruptedException e) {
									      // TODO Auto-generated catch block
									      e.printStackTrace();
								         }
						          
						           }
						           
						  }
			        }
			   }				  
				catch (IOException e) {
					voip_start.calling = false;
					if(audio_in!=null) audio_in.close();
		    		player_thread.din.disconnect();
					player_thread.din.close();
					
		            }
				
			  finally{
		    		debug_level.debug(1,"call in recorder: recorder is stop");
		    		if(audio_in!=null) audio_in.close();
		    		status =true;
		    		if (player_thread.din!= null) player_thread.din.disconnect();
		    		if (player_thread.din!= null) player_thread.din.close();
		    		
		    
		    		debug_level.debug(1,"call in player: recorder is drain and close");
		    }
		status = true;
		debug_level.debug(1, "call in recorder: audio is drain and close");
	}
	
}
