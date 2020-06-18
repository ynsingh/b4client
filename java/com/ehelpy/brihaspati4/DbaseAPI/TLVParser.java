package com.ehelpy.brihaspati4.DbaseAPI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Set;

public class TLVParser {

    //byte order to enumerate the bytes from least to most significant value and arrange them
    private static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    //hash map to put the tag as key and content as value
    private HashMap<Integer, byte[]> frame;
    //counter to traverse through the data bytes
    private int TotalBytes = 0;

    public TLVParser() {
    frame = new HashMap<>();
    }

    public static byte[] startFraming(byte[] data,int type) {

        //create a new frame of TLVParser type object
        TLVParser frame = new TLVParser();
        //push the Tag and value into the frame
        frame.putBytesValue(type, data);
        //serialise the frame object
        return frame.serialize();
    }
    public static byte[] startParsing(byte[] frame,int type) {

        //parse the combined byte array as a TLVParser object
        TLVParser parsed = TLVParser.parse(frame, 0);
        // get the data back after parsing
        return parsed.getBytesValue(type);
    }
    public static TLVParser parse(byte[] framedData, int offset) {

         TLVParser frame = new TLVParser();
         int parsed = 0;
         int length;
         do {
             ByteBuffer wrap = ByteBuffer.wrap(framedData, offset + parsed, 4);
             wrap.order(DEFAULT_BYTE_ORDER);
             int type = wrap.getInt();
             parsed += 4;
             //wrap the byte buffer
             //read the byte buffer and convert the content to int
             length = ByteBuffer.wrap(framedData,offset + parsed, 4).order(DEFAULT_BYTE_ORDER).getInt();
             parsed += 4;
             //create a byte array of size "size"
             byte[] value = new byte[length];
             //copy the content of buffer into value
             System.arraycopy(framedData, offset+parsed, value, 0, length);
             //put the type and value into hashmap frame
             frame.putBytesValue(type, value);
             parsed += length;
         }while (parsed < length);
         return frame;
    }
    // method to serialise the frame object
    public byte[] serialize() {
        int offset = 0;
        byte[] result = new byte[TotalBytes];
        //create a collection for all keys in the hash map frame
        Set<Integer> keys = frame.keySet();
        for (Integer key : keys) {
            byte[] bytes = frame.get(key);//get the bytes against each key
            // allocate a new bytebuffer of 4 bytes.
            // order them from least to highest significant byte.
            // put the key and increment the offset by 4.
            // return the content of buffer as an array
            byte[] type   = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(key).array();
            //byte[] sequence = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(key).array();
            byte[] length = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(bytes.length).array();
            //copy the content of type field into result
            System.arraycopy(type, 0, result, offset, type.length);
            offset += 4;
            //copy the content of length filed into result
            System.arraycopy(length, 0, result, offset, length.length);
            offset += 4;
            //copy the content of bytes into result
            System.arraycopy(bytes, 0, result, offset, bytes.length);
            offset += bytes.length;
        }
        return result;
    }
    public void putBytesValue(int type, byte[] value) {
         frame.put(type, value);
         TotalBytes += value.length+8;
    }
     public byte[] getBytesValue(int type) {
        return frame.get(type);
    }
}
