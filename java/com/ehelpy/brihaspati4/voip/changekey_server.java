package com.ehelpy.brihaspati4.voip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import java.util.Base64;

import com.ehelpy.brihaspati4.authenticate.ConvertStringCertToX509;
import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;


import com.ehelpy.brihaspati4.authenticate.debug_level;

public class changekey_server extends Thread
{
	static boolean flag = false;
	boolean verification = false;
	public static boolean changekeysflag=false;
    private static Socket socket;
    private static ServerSocket serverSocket =null; ;
    private static long sym_key = 0;  
	static byte[] key_bytes = null;
	 static X509Certificate client_cert = null;
	 static X509Certificate[] farend_cert = null;
	int port1=0;
    public changekey_server(int port)
    {
    	port1 = port;
    }    
    @Override
    public void run()
    {
       try
            {
            	 debug_level.debug(1, "///////  Caller end : New key generating ///////");
        	    
            	client_cert = ReadVerifyCert.returnClientCert(); //own cert loaded
            	 //Reading the message from the client
            	serverSocket=new ServerSocket(port1);  
        		socket=serverSocket.accept();
            	
            	
            	DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            	byte[] certbyte = client_cert.getEncoded();
    			String certstringbyte = new String(Base64.getEncoder().encode(certbyte)); // own cert converted to byte array before txn for secure handshake
    			String sendMessage = certstringbyte ;
                dos.writeUTF(sendMessage); //own cert sent to client
                System.out.println("/////// Sending cert to reciever  "+sendMessage);
        	    DataInputStream dis = new  DataInputStream(socket.getInputStream());
                String recieved = dis.readUTF();
                System.out.println("/////// Recieving cert & random no from reciever  "+recieved);
             
                String[] recieved_cert_key = recieved.split("this is the separator");
                recieved_cert_key[0]= recieved_cert_key[0].replaceAll("this is the separator", "");
                key_bytes = Base64.getDecoder().decode(recieved_cert_key[0]);
                byte[] certEntryBytes =  Base64.getDecoder().decode(recieved_cert_key[1]);
            
                PrivateKey own_priv = ReadVerifyCert.getKeyPair();//load own private key to decrypt the random number
                byte[] decoded_strfarendkey = decrypt_msg.decrypt_message(own_priv, key_bytes);// recover the random number of farend user
                long randmkey_farend = longtobyte_bytetolongarray.bytearraytolong(decoded_strfarendkey);
                debug_level.debug(1,"decoded_strfarendkey" + randmkey_farend);
                farend_cert = ConvertStringCertToX509.convertToX509Certarray(new String(Base64.getEncoder().encode(certEntryBytes))) ;
                   
	            long own_randm = encrypt_msg.generaterandomnumber(9);// generate own random number
	            System.out.println("/////// New own 9 random no //////  "+own_randm);
	            PublicKey Farend_public = farend_cert[0].getPublicKey(); //get public key of farend client
	            byte[] key_part = longtobyte_bytetolongarray.longtobytearray(own_randm);
	            byte[] key_partone = encrypt_msg.encrypt_message(Farend_public, key_part);
	         	String encrypt_key = new String(Base64.getEncoder().encode(key_partone));
	         	dos.writeUTF(encrypt_key);
	         	
	         	sym_key = symmetric_key.longkey(own_randm,randmkey_farend);// combine own and farend random nos for generating kay
	         	
	         	changekeysflag=true;
	         	    
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
            	    
            	    if(serverSocket!=null)serverSocket.close();
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
    /*while (sym_key == 0)
    wait();*/
    
    return sym_key;
   }//return new sym_Key;
        	
 		 	
   
}
