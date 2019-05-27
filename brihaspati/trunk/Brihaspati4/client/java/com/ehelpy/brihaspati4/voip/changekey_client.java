package com.ehelpy.brihaspati4.voip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import com.ehelpy.brihaspati4.authenticate.ConvertStringCertToX509;
import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.debug_level;

public class changekey_client extends Thread
{
	static boolean flag = false;
	boolean verification = false;
	public static boolean changekeycflag=false;
    private static Socket socket;
    private static long sym_key = 0;  
	static byte[] key_bytes = null;
	static X509Certificate own_cert = null;
	private static String farendip = null;
	
	
    public changekey_client (String farend_ip)
    {
    	farendip=farend_ip;
    }
            
    @Override
    public void run()
            {
            	try 
            	  {
            		debug_level.debug(1, "///// Receiver end : New key generating /////"); 
            	    Socket socket=new Socket(farendip,6666);
            		DataInputStream dis = new  DataInputStream(socket.getInputStream());
            		String recieved = dis.readUTF(); //far end cert recieved
            		 System.out.println("/////// Recieved cert from caller  "+recieved);
            		 
            		X509Certificate[] Farend_Cert = ConvertStringCertToX509.convertToX509Certarray(recieved);     
            		PublicKey Farend_public = Farend_Cert[0].getPublicKey(); //get public key of farend 
            		long own_randm = encrypt_msg.generaterandomnumber(9);// generate own random number
            		byte[] key_part = longtobyte_bytetolongarray.longtobytearray(own_randm);
            		byte[] key_partone = encrypt_msg.encrypt_message(Farend_public, key_part);
            		String encrypt_key = new String(Base64.getEncoder().encode(key_partone));
      		 
            		own_cert = ReadVerifyCert.returnClientCert();
            		byte[] certbyte = own_cert.getEncoded();
            		String own_encryptcert = new String(Base64.getEncoder().encode(certbyte));
            		String tobesend = encrypt_key + "this is the separator" + own_encryptcert;
            		System.out.println("/////// Sending cert & key to caller  "+tobesend);
            		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            		dos.writeUTF(tobesend);
             
              		recieved = dis.readUTF();
              		
              		System.out.println("/////// Recieved random no from caller  "+recieved);
              		
              		byte[] farend_keybytes=Base64.getDecoder().decode(recieved);
              		PrivateKey own_priv = ReadVerifyCert.getKeyPair();//load own private key to decrypt the random number
              		byte[] decoded_strfarendkey = decrypt_msg.decrypt_message(own_priv, farend_keybytes);
              		long randmkey_farend = longtobyte_bytetolongarray.bytearraytolong(decoded_strfarendkey);
              		debug_level.debug(0,"farend key is" + randmkey_farend);
 		 	
              		sym_key = symmetric_key.longkey(randmkey_farend, own_randm);// combine farend random no and own random no for generating kay
              		
	         		  changekeycflag=true;
	         	      System.out.println("/////// New sym key at reciever end is "+sym_key);
	         	   
              		socket.close();
              		
            }
           catch (Exception e)
             {
        	    debug_level.debug(1,"Error in port");
        	
             }
           
           finally
             {
               try
                 {
            	    
            	    if(socket!=null)socket.close();
            	    
            	    
                 }
               catch(Exception e){
            	System.out.println("Socket not closed");
                 }
             }
        	
            	synchronized(this)
    	        {
    	            notifyAll();
    	        }
    }

    public synchronized static long get()
	          throws InterruptedException
	    {
	      
	        return sym_key;
	    }//return sym_Key;
}
