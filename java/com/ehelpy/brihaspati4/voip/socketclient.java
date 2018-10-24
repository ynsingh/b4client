package com.ehelpy.brihaspati4.voip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

import com.ehelpy.brihaspati4.authenticate.ConvertStringCertToX509;
import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.b4server_services;
import com.ehelpy.brihaspati4.authenticate.certificate_verification;
import com.ehelpy.brihaspati4.authenticate.debug_level;
import com.ehelpy.brihaspati4.authenticate.keystore_save;
import com.ehelpy.brihaspati4.authenticate.properties_access;
 
public class socketclient
{
	static boolean flag = false;
	boolean verification = false;
    private static Socket socket;
    static X509Certificate own_cert = null;
    private static long sym_key = 0;   //static boolean flag = false;
    public static long client_socket(String farendip, int port, String option)
    {
        try
        {
            String host = farendip;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            
            //Send the message to the server
            DataOutputStream dos = new  DataOutputStream(socket.getOutputStream());
            dos.writeUTF(option);
            debug_level.debug(1,"Message sent to the server : "+option);
            DataInputStream dis = new  DataInputStream(socket.getInputStream());
            String received = dis.readUTF();
            X509Certificate[] Farend_Cert = ConvertStringCertToX509.convertToX509Certarray(received);
            flag = certificate_verification.clientcert_verify(Farend_Cert[0]);
            if(flag)
            {
            	long random_own = encrypt_msg.generaterandomnumber(9);// Generate a random integer for symmetric key
            	PublicKey Farend_public = Farend_Cert[0].getPublicKey(); //get public key of farend client 
            	System.out.println(random_own);
     		 	byte[] key_part = longtobyte_bytetolongarray.longtobytearray(random_own);
     		 	byte[] key_partone = encrypt_msg.encrypt_message(Farend_public, key_part);
     		 	String encrypt_key = new String(Base64.getEncoder().encode(key_partone));
     		 	own_cert = ReadVerifyCert.returnClientCert();
     		 	byte[] certbyte = own_cert.getEncoded();
     		 
     		 	String own_encryptcert = new String(Base64.getEncoder().encode(certbyte));
     		 	String tobesend = encrypt_key + "this is the separator" + own_encryptcert;
     		 	debug_level.debug(1,"Key and the cert to be send"+tobesend);
     		 	dos.writeUTF(tobesend);
     		 	received = dis.readUTF();
     		 	byte[] farend_keybytes=Base64.getDecoder().decode(received);
     		 	PrivateKey own_priv = ReadVerifyCert.getKeyPair();//load own private key to decrypt the random number
	            byte[] decoded_strfarendkey = decrypt_msg.decrypt_message(own_priv, farend_keybytes);
     		 	long randmkey_farend = longtobyte_bytetolongarray.bytearraytolong(decoded_strfarendkey);
     		 	debug_level.debug(0,"farend key is" + randmkey_farend);
     		 	sym_key = symmetric_key.longkey(random_own,randmkey_farend);
     		 	debug_level.debug(0,"the symmetric key for txn is" + sym_key);
     		 	 
            }
            else {
            	
            	debug_level.debug(3,"Certificate is not valid");
            	//socket.close();
            	socket.close();
            	ma_fail.id_exist();
           
            }
        }
        catch (Exception exception)
        {
        	
        	ma_fail.id_exist();
        }
        finally
        {
            try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
       
       return sym_key;
    }
    public static void disconnect(String farendip, int port, String option) {
    	String host = farendip;
        //int port = 25000;
        InetAddress address;
		try {
			address = InetAddress.getByName(host);
			socket = new Socket(address, port);
		        
		    //Send the message to the server
		    DataOutputStream dos = new  DataOutputStream(socket.getOutputStream());
		    dos.writeUTF(option);
		    return;
		    
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
}