package com.ehelpy.brihaspati4.messagesend.client;

import static com.ehelpy.brihaspati4.messagesend.encryption.EncryptionDecryption_Client.PRIVATE_KEY_FILE;
import static com.ehelpy.brihaspati4.messagesend.encryption.EncryptionDecryption_Client.PUBLIC_KEY_FILE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ehelpy.brihaspati4.messagesend.encryption.AESEncryptionDecryptionClient;
import com.ehelpy.brihaspati4.messagesend.encryption.EncryptionDecryption_Client;
import com.ehelpy.brihaspati4.messagesend.encryption.Ping;


public class ClientMain  {
    public static boolean flag;

                
    public static void main (String [] args ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, Exception , FileNotFoundException
    {   
        // ip address is of the other user, 
        String ipAddress="127.0.0.1";
        int portNo=15001;
        
        //Generation of a Random String used thereby for encryption, along with the making of offline session key written to a file
        WriteToFile wtf2 = new WriteToFile();
        wtf2.getClass();
        
        //Generation of (public key,private key)-(key pair)for this particular client, using RSA Algorithm
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024); //1024 used for normal securities
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();   
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);   
        EncryptionDecryption_Client rsaObj = new EncryptionDecryption_Client();
        rsaObj.saveKeys(PUBLIC_KEY_FILE, rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());
        rsaObj.saveKeys(PRIVATE_KEY_FILE, rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());
           
        // Sending your public key to the other user 
        Sender.send(portNo, "client_pub.key" ); 
                
        //Receiving the public key and the encrypted random string of the other user    
        Receiver.receiveFile(ipAddress, portNo,"server_pub.key"); //Receive public key - MyKeys/publicKey2.txt        
        Receiver.receiveFile(ipAddress, portNo,"server_encrypt.txt");///Receive encrypted random string - home/guest/NetBeansProjects/org.iitk.brihaspati4.messagesend/MyData2/Rand1.txt
                
        //Encrypt your random string generated using the other user's Public Key
        rsaObj.encryptData(WriteToFile.sess_client);   
          
        //Reads the encrypted file and converts it to byte array
        Path fileLocation = Paths.get("server_encrypt.txt");
        byte[] data = Files.readAllBytes(fileLocation);    
        
        //Decrypts the encrypted random string received, which was first converted to bytes form 
        rsaObj.decryptData(data);               
        
        //Sends your encrypted random string to the other user
        Sender.send(portNo ,"client_encrypt.txt");
        
        //Concatenation of both the random strings forms the session key
        SessionKey sk = new SessionKey();
        sk.getClass() ;           
       
        //Begining of instant messaging
        Socket sock = new Socket(ipAddress,portNo);                                             
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        // sending to client (pwrite object)               
        OutputStream os = sock.getOutputStream(); 
        PrintWriter pwrite = new PrintWriter(os, true);
        // receiving from server ( receiveRead  object)
        InputStream is = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(is));
       // System.out.println("Start the chitchat, type and press Enter key");        
        String receiveMessage, sendMessage; 
        // to ping the client whom you wish to have a chat with 
        Ping ping = new Ping();
        List<String> commands = new ArrayList<String>();
        commands.add("ping");
        commands.add(ipAddress);
        boolean s =  ping.doCommand(commands);
        System.out.println(s);
        int i=0;
        int a=0;
        flag = false;
        // flag to made true by the route node for the sake being that there are messages for you, retrieving all of them
       System.out.println("Start chatting, you have to type the first message");
     while(true)
        {
          // to get the messages from its successor node and decrypting the same, and also to receive the messages if your predecessor node is not online
           if ( true  )// flag == true, to retrieve the messages first,since flag is turned true 
           {
               
             do{
                if(i==0){
                   
                   Receiver.receiveFile(ipAddress,15000,"off_session_server.txt");
                   Path fileLocation1 = Paths.get("off_session_server.txt");
                   byte[] data_off;    
                   data_off= Files.readAllBytes(fileLocation1);
        
                   //Decrypts the encrypted random string received, which was first converted to bytes form 
                   rsaObj.decryptoff_sess(data_off);
                   
                  i++;}
                TimeUnit.SECONDS.sleep(10);// its not necessary in all systems but when system gives error ConnectionRefused then use this to solve the problem
                   Receiver.receiveFile(ipAddress,15000,"files_"+(a)+".txt");
                   Path fileLocation2 = Paths.get("files_"+(a)+".txt");
                   byte[] data_offmessages;    
                   data_offmessages= Files.readAllBytes(fileLocation2);
                   String string = AESEncryptionDecryptionClient.decrypt_offsess(data_offmessages);
                   
                   System.out.println(string);
                      a++;
                        }while(s==true);
               }
            if (!s)//if client(recipient) of messages is online
          {
              
             // first client sends the message 
             sendMessage = keyRead.readLine();  // keyboard reading
             String valueEnc = AESEncryptionDecryptionClient.encrypt(sendMessage);
             pwrite.println(valueEnc);       // sending to server
             pwrite.flush();                 // flushing the data 
             if((receiveMessage = receiveRead.readLine()) != null)  //  if client is online and is sending the message
               {
              String string = AESEncryptionDecryptionClient.decrypt(receiveMessage);
              System.out.println(string);
                 }    
          }
            else// if the receiver of messages is offline
          {
           
            	 do{
         		    rsaObj.encryptoff_sess(WriteToFile.off_sess);
              	   // first encrypt this file with the public key of other offline user
                      if(i==0){
                     	Sender.send(15000,"off_session.txt");
                     	//System.out.println(WriteToFile.off_sess);
                 	  i++;
                      }
                      sendMessage = keyRead.readLine();
                      byte[] valueEnc = AESEncryptionDecryptionClient.encrypt_offsess(sendMessage);
                      File files_c = new File("files_"+(a)+".txt");
                      try (FileOutputStream fos = new FileOutputStream("files_"+(a)+".txt")) {
                          fos.write(valueEnc);}
                       catch (Exception e) {
                      e.printStackTrace();
                     } 
                      Sender.send(15000,files_c.getName());
                      a++;
                 }while(s==true);// keep on doing this till the user is offline
          
             
             }
        }
        } 
     
      
     }            
