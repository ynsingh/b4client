package com.ehelpy.brihaspati4.voip;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.Base64;

import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.debug_level;

public class Cert_trans {

    public static void certexchange(Socket sock) {
        //Send the cert to the far end client
        try
        {
            OutputStream os = sock.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            X509Certificate cert = ReadVerifyCert.returnClientCert();
            byte[] certbyte = cert.getEncoded();
            String certstringbyte = new String(Base64.getEncoder().encode(certbyte));
            bw.write(certstringbyte);
            debug_level.debug(0,"Message sent to the client is "+certstringbyte);
            bw.flush();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

}
