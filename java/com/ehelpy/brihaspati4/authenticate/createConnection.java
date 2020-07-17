package com.ehelpy.brihaspati4.authenticate ;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

//Last updated by Maj Dushyant Choudhary April 2020
//This function creates connection with the identity server with the
//certificate signing request using send get and send post methods
public class createConnection
{
    private final static String USER_AGENT = "Chrome";
    // HTTP GET request
    public static boolean sendGet(String urlmaster) throws Exception
    {
        boolean serverstat = false;
        URL obj = new URL(urlmaster);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        StringBuffer response =null;
        int responseCode =0;
        try {
            responseCode= con.getResponseCode();
        } catch (Exception e)
        {
            server_down.id_exist();
        }
        System.out.println("\nSending 'GET' request to URL : " + urlmaster);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                serverstat = true;
            }
            in.close();
        } catch (Exception e) {
        }
        return serverstat ;
    }
    // HTTP POST request
    void sendPost(String urlmaster)  {
        try {
            URL obj = new URL(urlmaster);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'POST' request to URL : " + urlmaster);
            System.out.println("\nSending 'POST' request to server URL :sendPost - authenticate/createConnection " );
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine = null;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        }
        catch(Exception e) {
            return;
        }
    }

// HTTP POST request
    void sendJKSPost(String urlmaster,String data,String certificate)  {
        try {
            URL obj = new URL(urlmaster);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345&req=storejks&cert="+ URLEncoder.encode(certificate, "UTF-8")+"&jks="+URLEncoder.encode(data, "UTF-8");
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to server URL :sendJKSPost - authenticate/createConnection " );
//            System.out.println("\nSending 'POST' request to URL : " + urlmaster);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine = null;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        }
        catch(Exception e) {
            return;
        }
    }

    static int sendPost(String urlmaster,String reqtype)  {
        @SuppressWarnings("unused")
        boolean flag = false ;
        URL obj = null;
        try {
            obj = new URL(urlmaster);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //add request header
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(con.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            wr.writeBytes(urlParameters);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            wr.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            wr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            System.out.println("\nSending 'POST' request to server URL :sendPost(1,2) - authenticate/createConnection " );
 //       debug_level.debug(1,"\nSending 'POST' request to URL : " + urlmaster);
        debug_level.debug(1,"Post parameters : " + urlParameters);
        debug_level.debug(1,"Response Code : " + responseCode);
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String recdmessage = response.toString();
        debug_level.debug(1,"Recd Message : " + recdmessage);
//        System.out.println("The responce from get post is "+response.toString()); 
        if(reqtype=="forgotpasswordotpverify")
        {
            return Integer.parseInt(recdmessage);
        }
        else if(reqtype=="certificaterevocationotpverify")
        {
            return Integer.parseInt(recdmessage);
        }
        else if(reqtype=="certificaterevocationreason")
        {
            return Integer.parseInt(recdmessage);
        }
        else if(reqtype=="checkkeystore")
        {
            return Integer.parseInt(recdmessage);
        }
        else if(reqtype=="checkcrl")
        {
            return Integer.parseInt(recdmessage);
        }
        else if(reqtype=="keystorecheckotpverify")
        {
            if (!recdmessage.equals("17"))
            {
                debug_level.debug(1, "Recd Message : " + recdmessage);
                final String[] ServerCert = recdmessage.split("ClientCert");
                String[] ClientCert = null;
                try {
                    ClientCert = ServerCert[1].split("ClientCert");
                }
                catch (Exception e11) {
                    Emailid_exist.id_exist();
                }
                X509Certificate server_certificate = null;
                try
                {
                    server_certificate = convertcerttox509.convertToX509Cert(ServerCert[0]);
                }
                catch (IOException e9) {
                    e9.printStackTrace();
                }
                X509Certificate Client_certificate = null;
                try {
                    Client_certificate = convertcerttox509.convertToX509Cert(ClientCert[0]);
                }
                catch (IOException e9) {
                    e9.printStackTrace();
                }
                debug_level.debug(1, "ServerCert" + server_certificate.toString());
                debug_level.debug(1, "ClientCert" + Client_certificate.toString());
                final X509Certificate[] Certs = { server_certificate, Client_certificate };
                try {
                    final String keyStorePass = Gui.getkeystorepass();
                    final String keystorealias = Gui.getaliasname();
                    final KeyStore keystore = KeyStore.getInstance("JKS");
                    final char[] keyPass = keyStorePass.toCharArray();
                    keystore.load(null, keyPass);
                    final String pkey = new String(Files.readAllBytes(Paths.get("key.txt", new String[0])));
                    final byte[] keybytes = Base64.getDecoder().decode(pkey);
                    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybytes);
                    final KeyFactory fact = KeyFactory.getInstance("RSA");
                    final PrivateKey priv_client = fact.generatePrivate(keySpec);
                    keystore.setKeyEntry(keystorealias, priv_client, keyPass, Certs);
                    final FileOutputStream fos = new FileOutputStream("SignedClientKeyStore.JKS");
                    keystore.store(fos, keyPass);
                    final String h = Integrity.fileHash("SignedClientKeyStore.JKS");
                    try {
                        final File auth = new File("authenticate.txt");
                        final FileWriter myWriter = new FileWriter("authenticate.txt");
                        myWriter.write(h);
                        myWriter.close();
                    }
                    catch (IOException e10) {
                        System.out.println("An error occurred.");
                        e10.printStackTrace();
                    }
                    ClientMain.main(null);
                }
                catch (Exception ex) {}
                return 16;
            }
            return Integer.parseInt(recdmessage);
        }
        else
        {

            return 1;
        }
    }

// HTTP POST request
    static	X509Certificate[] sendRevokePost(String urlmaster)  {
        @SuppressWarnings("unused")
        boolean flag = false ;
        URL obj = null;
        try {
            obj = new URL(urlmaster);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //add reuqest header
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(con.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            wr.writeBytes(urlParameters);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            wr.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            wr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            System.out.println("\nSending 'POST' request to server URL :sendRevokePost - authenticate/createConnection " );
        //debug_level.debug(1,"\nSending 'POST' request to URL : " + urlmaster);
        debug_level.debug(1,"Post parameters : " + urlParameters);
        debug_level.debug(1,"Response Code : " + responseCode);
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String recdmessage = response.toString();
        debug_level.debug(1,"Recd Message : " + recdmessage);
        String[] ServerCert = recdmessage.split("ClientCert");
        String [] ClientCert = null;
        try {
            ClientCert = ServerCert[1].split("ClientCert");
        }
        catch(Exception e) {
            Emailid_exist.id_exist();
        }
        X509Certificate server_certificate = null;
        try {
            server_certificate = convertcerttox509.convertToX509Cert(ServerCert[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        X509Certificate Client_certificate = null;
        try {
            Client_certificate = convertcerttox509.convertToX509Cert(ClientCert[0]);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        debug_level.debug(1,"ServerCert"+server_certificate.toString());
        debug_level.debug(1,"ClientCert"+Client_certificate.toString());
        X509Certificate[] Certs	= new  X509Certificate[2];
        Certs[0] = server_certificate;
        Certs[1] = Client_certificate;
        return Certs;
    }
}

