package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.ehelpy.brihaspati4.authenticate.b4server_services;

public class socketch_write {
    public static void ch_write(SocketChannel sc, String writedata) {
        Charset charset = Charset.forName("ISO-8859-1");
        CharBuffer c = CharBuffer.wrap(writedata);
        ByteBuffer b = charset.encode(c);
        b.compact();
        b.flip();
        @SuppressWarnings("unused")
        int a = 0;
        while (b.hasRemaining())
        {
            try {
                a += sc.write(b);
                sc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                b4server_services.service();
                return;
            }
        }

    }
}
