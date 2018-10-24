package com.ehelpy.brihaspati4.messagesend.server;
import static com.ehelpy.brihaspati4.messagesend.encryption.EncryptionDecryption_Server.PRIVATE_KEY_FILE;
import static com.ehelpy.brihaspati4.messagesend.encryption.EncryptionDecryption_Server.PUBLIC_KEY_FILE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
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

import com.ehelpy.brihaspati4.messagesend.encryption.Ping;
import com.ehelpy.brihaspati4.messagesend.server.Receiver;
import com.ehelpy.brihaspati4.messagesend.encryption.AESEncryptionDecryptionTest;
import com.ehelpy.brihaspati4.messagesend.encryption.EncryptionDecryption_Server;

public class ServerMain
{
    public static boolean flag;
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, Exception
    {

        //Generates and stores random string into a file, right now kept in the default folder
        WriteToFile wtf = new WriteToFile();
        wtf.getClass() ;
        //Generating Public and Private Key    And Saving them in (MyKeys/....) see in the EncryptionDecryption Code
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024); //1024 used for normal securities
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        //Share public key with other so they can encrypt data and decrypt those using private key(Don't share with Other)
        EncryptionDecryption_Server rsaObj = new EncryptionDecryption_Server();
        rsaObj.saveKeys(PUBLIC_KEY_FILE, rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());
        rsaObj.saveKeys(PRIVATE_KEY_FILE, rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());

        // Opening a port on your system so as to run the program, this has to be done by going in the windows firewall and making a new inbound rule
        int portNo=15001;
        String ipAddress="127.0.0.1";// this is the IP address of the other user(client B)
        // Receiving the Public Key of the other user, eventually has to be done through the extraction from Certificate
        Receiver.receiveFile(ipAddress, portNo, "client_pub.key");
        Sender.send(portNo,"server_pub.key");
        //Encrypt Data using Public Key of the other User
        rsaObj.encryptData(WriteToFile.sess_serv);

        //Sends encrypted random string file to the client
        Sender.send(portNo,"server_encrypt.txt");

        Receiver.receiveFile(ipAddress, portNo,"client_encrypt.txt" );//You can change the location where to store the File system dependent

        Path fileLocation = Paths.get("client_encrypt.txt");
        // this has to be read in the form of bytes otherwise decryption wont be happening
        byte[] data = Files.readAllBytes(fileLocation);
        // Decrypting the data received and storing in Rand1.txt
        rsaObj.decryptData(data);

        //Generation of session key
        SessionKey sk1 = new SessionKey();
        sk1.getClass() ;

        //Instant messaging begins
        // sending to client (pwrite object),you will have to open the port
        ServerSocket sersock = new ServerSocket(portNo);
        Socket sock = sersock.accept();
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream os = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(os, true);

        // receiving from server ( receiveRead  object)
        InputStream is = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(is));
        //File file
        // System.out.println("before pinging");

        String receiveMessage, sendMessage;
        int a= 0;
        int i=0;
        Ping ping = new Ping();
        List<String> commands = new ArrayList<String>();
        commands.add("ping");
        commands.add(ipAddress);
        boolean s = ping.doCommand(commands);
        flag= false;
        //  System.out.println(!s);
        System.out.println("Wait for the other client to send the first message");
        while(true)
        {
            // to get the messages from its successor node and decrypting the same, and also to receive the messages if your predecessor node is not online
            if ( flag )// flag == true, to retrieve the messages first,since flag is turned true
            {

                do {

                    if(i==0) {
                        // the ipAdress is of the user(successor node) from whom the files of user (offline then online) are to be retrieved back
                        Receiver.receiveFile(ipAddress,15000,"off_session_client.txt");
                        Path fileLocation1 = Paths.get("off_session_client.txt");
                        byte[] data_off;
                        data_off= Files.readAllBytes(fileLocation1);

                        //Decrypts the encrypted random string received, which was first converted to bytes form
                        rsaObj.decryptoff_sess(data_off);

                        i++;
                    }
                    TimeUnit.SECONDS.sleep(10);
                    Receiver.receiveFile(ipAddress,15000,"files_"+(a)+".txt");
                    Path fileLocation2 = Paths.get("files_"+(a)+".txt");
                    byte[] data_offmessages;
                    data_offmessages= Files.readAllBytes(fileLocation2);
                    String string = AESEncryptionDecryptionTest.decrypt_offsess(data_offmessages);

                    System.out.println(string);
                    a++;
                } while(s==true);
            }
            // if a query has come to you, for receiving someone else's message
            // Calling the routing table to get the successor's node id
            if (!s)//if client(recipient) of messages is online
            {
                if((receiveMessage = receiveRead.readLine()) != null)  //  if client is online and is sending the message
                {
                    String string = AESEncryptionDecryptionTest.decrypt(receiveMessage);
                    System.out.println(string);
                }
                sendMessage = keyRead.readLine();  // keyboard reading
                String valueEnc =  AESEncryptionDecryptionTest.encrypt(sendMessage);
                pwrite.println(valueEnc);       // sending to server
                pwrite.flush();                    // flush the data

            }
            else// if the receiver of messages is offline
            {

                do {
                    rsaObj.encryptoff_sess(WriteToFile.off_sess);
                    // first encrypt this file with the public key of other offline user
                    if(i==0) {
                        Sender.send(15000,"off_session.txt");
                        //System.out.println(WriteToFile.off_sess);
                        i++;
                    }
                    sendMessage = keyRead.readLine();
                    byte[] valueEnc = AESEncryptionDecryptionTest.encrypt_offsess(sendMessage);
                    File files_c = new File("files_"+(a)+".txt");
                    try (FileOutputStream fos = new FileOutputStream("files_"+(a)+".txt")) {
                        fos.write(valueEnc);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    Sender.send(15000,files_c.getName());
                    a++;
                } while(s==true);// keep on doing this till the user is offline


            }
        }

    }
}


