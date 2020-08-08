package com.ehelpy.brihaspati4.authenticate ;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.X509Certificate;

//This program was last modified Maj Dushyant Choudhary
//This function send the OTP received to the Identity server for Authentication of the peer.
public class createotpConnection   {
    private final static  String USER_AGENT = "Chrome";
    private static X509Certificate server_certificate =null;
    private static X509Certificate Client_certificate =null;
    // HTTP GET request
    void sendGet(String urlmaster) throws Exception 
    {
        URL obj = new URL(urlmaster);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        debug_level.debug(1,"\nSending 'GET' request to URL : " + urlmaster);
        debug_level.debug(1,"Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        debug_level.debug(1,response.toString());
    }
    // HTTP POST request
    static	X509Certificate[] sendPost(String urlmaster)  {
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
        System.out.println("\nSending 'POST' request to server URL :sendPost(1) - authenticate/createotpConnection " );
        //debug_level.debug(1,"\nSending 'POST' request to URL : " + urlmaster);
        debug_level.debug(1,"Post parameters : " + urlParameters);
        debug_level.debug(1,"Response Code : " + responseCode);
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        debug_level.debug(1,"In put stream data : " + in.toString());
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
        try {
            server_certificate = convertcerttox509.convertToX509Cert(ServerCert[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public static X509Certificate returnServerCert() throws Exception {    	
        return server_certificate;
    }
    public static X509Certificate returnClientCert() throws Exception {
        return Client_certificate;
    }
}

