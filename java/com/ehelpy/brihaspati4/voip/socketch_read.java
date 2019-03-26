package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class socketch_read {
	public static String ch_read(SocketChannel sc) {
		Charset charset = Charset.forName("ISO-8859-1");
	    try {
			sc.configureBlocking(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    ByteBuffer b = ByteBuffer.allocate(100);
	    @SuppressWarnings("unused")
		int a = 0;
		try {
			a = sc.read(b);
		    sc.close();
		    } 
		catch (IOException e) {
		 	// TODO Auto-generated catch block
			//e.printStackTrace();
			debug_level.debug(1,"The far end connection has been disconnected");
		}
	    b.flip();//sets the Position to 0 and limit to the number of bytes to be read.
	    CharBuffer c = charset.decode(b);
	    
	    try {
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return c.toString();
	}

}
