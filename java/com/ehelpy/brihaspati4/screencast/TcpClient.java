package com.ehelpy.brihaspati4.screencast;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.*;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class TcpClient {
    private final static int     MIN_PORT    = Global1.MIN_PORT.val();
    private final static int     MAX_PORT    = Global1.MAX_PORT.val();
    // newObj restricts creation of new instances
    private       static boolean newObj      = false;
    // getInstance(port) does NOT return reserved TcpServer objects
    private              boolean reserved;

    private static HashMap<String, TcpClient> hashMap = new HashMap<>();
    private InputStream inStream = null;
    private OutputStream outStream = null;
    private Scanner scanner = null;
    private DataInputStream inData = null;
    private DataOutputStream outData = null;
    private Socket socket = null;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private TcpClient() {if (!newObj) throw new AssertionError();}

    private static TcpClient newInstance(String ip, int port)
            throws IOException {
        newObj = true;
        TcpClient tcp = new TcpClient();
        newObj = false;
        tcp.reserved = false;
        tcp.socket = new Socket(ip, port);
        tcp.inStream = tcp.socket.getInputStream();
        tcp.outStream = tcp.socket.getOutputStream();
        tcp.scanner = new Scanner(tcp.socket.getInputStream());
        tcp.inData = new DataInputStream(tcp.inStream);
        tcp.outData = new DataOutputStream(tcp.outStream);
        String ipPort = ip + ":" + port;
        hashMap.put(ipPort, tcp);
        System.out.println("TCP Client connected to " + ipPort);
        return tcp;
    }

    private static TcpClient newInstance(Socket client) throws IOException {
        newObj = true;
        TcpClient tcp = new TcpClient();
        newObj = false;
        tcp.reserved = false;
        tcp.socket = client;
        tcp.inStream = tcp.socket.getInputStream();
        tcp.outStream = tcp.socket.getOutputStream();
        tcp.scanner = new Scanner(tcp.socket.getInputStream());
        tcp.inData = new DataInputStream(tcp.inStream);
        tcp.outData = new DataOutputStream(tcp.outStream);
        String ipPort = client.getInetAddress().getHostAddress()
                + ":" + client.getPort();
        hashMap.put(ipPort, tcp);
        System.out.println("TCP Client connected to " + ipPort);
        return tcp;
    }

    public static TcpClient getInstance(String host, int port)
            throws IOException {
        String ip = InetAddress.getByName(host).getHostAddress();
        if (port >= MIN_PORT && port <= MAX_PORT) {
            String ipPort = ip + ":" + port;
            TcpClient tcp = hashMap.get(ipPort);
            if (tcp == null) tcp = newInstance(ip, port);
            if (tcp.reserved) throw new IllegalArgumentException();
            else return tcp;
        } else throw new IllegalArgumentException();
    }

    // does NOT create new TcpClient is another is connected to same ip:port
    public static TcpClient getInstance(Socket client) throws IOException {
        String ipPort = client.getInetAddress().getHostAddress()
                + ":" + client.getPort();
        TcpClient tcp = hashMap.get(ipPort);
        if (tcp == null) tcp = newInstance(client);
        return tcp;
    }

    public void setReserved(boolean val) {reserved = val;}

    public String getIpPort() {
        return socket.getInetAddress().getHostAddress()
                + ":" + socket.getPort();
    }

    // Accessors for Input/Output Streams

    public boolean writeImage(RenderedImage image, String imageFormat)
            throws IOException {
        return ImageIO.write(image, imageFormat, outStream);
    }

    public BufferedImage readImage()
            throws IOException {
        byte[] bytes = new byte[1024*1024];
        int count = 0;
        do{
            count+= inStream.read(bytes,count,bytes.length-count);
        }while(!(count>4 && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39));

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        return image;
    }

    // Accessors for Scanner Input Stream

    public boolean hasNextInt() {return scanner.hasNextInt();}

    public int nextInt() {return scanner.nextInt();}

    // Accessors for Data Input/Output Streams

    public void flush()
            throws IOException {outData.flush();}

    public int read(byte[] b)
            throws IOException {return inData.read(b);}

    public int read(byte[] b, int off, int len)
            throws IOException {return inData.read(b, off, len);}

    public void write(byte[] b, int off, int len)
            throws IOException {outData.write(b, off, len);}

    public void write(int b)
            throws IOException {outData.write(b);}

    public boolean readBoolean()
            throws IOException {return inData.readBoolean();}

    public void writeBoolean(boolean v)
            throws IOException {outData.writeBoolean(v);}

    public byte readByte()
            throws IOException {return inData.readByte();}

    public void writeByte(int v)
            throws IOException {outData.writeByte(v);}

    public void writeBytes(String s)
            throws IOException {outData.writeBytes(s);}

    public char readChar()
            throws IOException {return inData.readChar();}

    public void writeChar(int v)
            throws IOException {outData.writeChar(v);}

    public void writeChars(String s)
            throws IOException {outData.writeChars(s);}

    public double readDouble()
            throws IOException {return inData.readDouble();}

    public void writeDouble(double v)
            throws IOException {outData.writeDouble(v);}

    public float readFloat()
            throws IOException {return inData.readFloat();}

    public void writeFloat(float v)
            throws IOException {outData.writeFloat(v);}

    public void readFully(byte[] b)
            throws IOException {inData.readFully(b);}

    public void readFully(byte[] b, int off, int len)
            throws IOException {inData.readFully(b, off, len);}

    public int readInt()
            throws IOException {return inData.readInt();}

    public void writeInt(int v)
            throws IOException {outData.writeInt(v);}

    public long readLong()
            throws IOException {return inData.readLong();}

    public void writeLong(long v)
            throws IOException {outData.writeLong(v);}

    public short readShort()
            throws IOException {return inData.readShort();}

    public void writeShort(int v)
            throws IOException {outData.writeShort(v);}

    public int readUnsignedByte()
            throws IOException {return inData.readUnsignedByte();}

    public int readUnsignedShort()
            throws IOException {return inData.readUnsignedShort();}

    public String readUTF()
            throws IOException {return inData.readUTF();}

    public void writeUTF(String str)
            throws IOException {outData.writeUTF(str);}

    public int skipBytes(int n)
            throws IOException {return inData.skipBytes(n);}
}
