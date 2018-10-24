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
import com.ehelpy.brihaspati4.authenticate.b4server_services;
import com.ehelpy.brihaspati4.authenticate.certificate_verification;
import com.ehelpy.brihaspati4.authenticate.debug_level;
import com.ehelpy.brihaspati4.authenticate.keystore_save;
import com.ehelpy.brihaspati4.authenticate.properties_access;

public class socketserver
{
    static boolean flag = false;
    boolean verification = false;
    private static Socket socket;
    private static ServerSocket serverSocket =null; ;
    static X509Certificate client_cert = null;
    static X509Certificate[] farend_cert = null;
    static X509Certificate server_cert = null;
    static byte[] key_bytes = null;
    private static long sym_key = 0;
    public static long server_socket(int port)
    {

        try
        {

            serverSocket = new ServerSocket(port);
            client_cert = ReadVerifyCert.returnClientCert();
            server_cert = ReadVerifyCert.returnServerCert();
            //Server is running always. This is done using this while(true) loop
            //while(true)
            {
                //Reading the message from the client
                socket = serverSocket.accept();
                DataInputStream dis=new DataInputStream(socket.getInputStream());
                String received = dis.readUTF();
                // in case far end agrees for the call
                if (received.equals("Yes would like to connect"))
                {
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    byte[] certbyte = client_cert.getEncoded();
                    String certstringbyte = new String(Base64.getEncoder().encode(certbyte)); // own cert converted to byte array before txn for secure handshake
                    String sendMessage = certstringbyte ;
                    dos.writeUTF(sendMessage);
                    received = dis.readUTF();// read the random number and certificate oof the far end
                    String[] received_cert_key = received.split("this is the separator");
                    received_cert_key[0]= received_cert_key[0].replaceAll("this is the separator", "");
                    key_bytes = Base64.getDecoder().decode(received_cert_key[0]);
                    byte[] certEntryBytes =  Base64.getDecoder().decode(received_cert_key[1]);
                    String path = properties_access.read_property("client.properties","home" );
                    //PrivateKey own_priv = keystore_save.LoadKeyPair(path, "RSA");
                    PrivateKey own_priv = ReadVerifyCert.getKeyPair();//load own private key to decrypt the random number
                    byte[] decoded_strfarendkey = decrypt_msg.decrypt_message(own_priv, key_bytes);// recover the random number of farend user
                    long randmkey_farend = longtobyte_bytetolongarray.bytearraytolong(decoded_strfarendkey);
                    debug_level.debug(1,"decoded_strfarendkey" + randmkey_farend);
                    farend_cert = ConvertStringCertToX509.convertToX509Certarray(new String(Base64.getEncoder().encode(certEntryBytes))) ;
                    debug_level.debug(1,"farend certificate is" +farend_cert[0]);
                    flag = certificate_verification.clientcert_verify(farend_cert[0]);//verify farend certificate
                    if(flag)
                    {
                        long own_randm = encrypt_msg.generaterandomnumber(9);// generate own random number
                        debug_level.debug(1,"own random key is " + own_randm);
                        PublicKey Farend_public = farend_cert[0].getPublicKey();// encrypt using faren public key
                        byte[] key_part = longtobyte_bytetolongarray.longtobytearray(own_randm);
                        byte[] key_partone = encrypt_msg.encrypt_message(Farend_public, key_part);
                        String encrypt_key = new String(Base64.getEncoder().encode(key_partone));

                        dos.writeUTF(encrypt_key);
                        debug_level.debug(0,"The ServerSocket at Socketserver  is closed  "+ serverSocket.isClosed());
                        long sym_key = symmetric_key.longkey(randmkey_farend, own_randm);// combine own and farend random nos for generating kay
                        if(socket!=null)socket.close();
                        //serverSocket.close();

                        return sym_key;
                        //byte[] sym_byte_key = longtobyte_bytetolongarray.longtobytearray(sym_key);
                        //SecretKeySpec secretKeySpec = new SecretKeySpec(sym_byte_key, "AES");
                        //System.out.println("finally its encrypted");
                    }
                    else
                    {
                        debug_level.debug(3,"Certificate is not valid");
                        //socket.close();
                        serverSocket.close();
                        ma_fail.id_exist();
                    }


                }
                else {
                    debug_level.debug(1,received);
                    discon.disconnect();
                    return 0 ;
                }
                // System.out.println(dis.readUTF());
                //String cert = br.readLine();
                //System.out.println("Message received from client is "+ cert);

                //Multiplying the number by 2 and forming the return message

            }
        }
        catch (Exception e)
        {
            debug_level.debug(1,"Error in certificate Exchange");
            debug_level.debug(3,"Certificate is not valid");
            //socket.close();
            ma_fail.id_exist();
        }
        finally
        {
            try
            {
                //socket.close();
                if(serverSocket!=null)serverSocket.close();
            }
            catch(Exception e) {
                System.out.println("Socket not closed");
            }
        }
        return sym_key;
    }
}