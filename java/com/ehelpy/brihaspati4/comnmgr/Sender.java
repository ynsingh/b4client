package com.ehelpy.brihaspati4.comnmgr;

import java.io.*;
import java.net.Socket;

public class Sender {
    public static void start(String xmlPath, String IP) throws IOException {
        Socket socket = null;
        Boolean flag = false;
        socket = new Socket(IP, 3333);

        File file = new File(xmlPath);
        // Get the size of the file
        long length = file.length();
        byte[] bytes = new byte[2 * 1024];

        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }
        out.close();
        in.close();
        socket.close();
    }
}
