package com.ehelpy.brihaspati4.voip;

import java.nio.ByteBuffer;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class longtobyte_bytetolongarray {
private static long x = 0;
private static byte[] y = null;
public static long bytearraytolong(byte[] input ) {
	ByteBuffer buffer = ByteBuffer.allocate(256).put(input);
    buffer.flip();
    x = buffer.getLong();
    debug_level.debug(1, "Byte array to long completed");
    return x;
}
public static byte[] longtobytearray(long input ) {
	ByteBuffer buffer1 = ByteBuffer.allocate(256).putLong(input);
	y = buffer1.array();
	debug_level.debug(1, "long to byte array completed");
	return y;
}
public static long bytearraytolongsymkey(byte[] input ) {
	ByteBuffer buffer = ByteBuffer.allocate(16).put(input);
	buffer.flip();
    x = buffer.getLong();
    debug_level.debug(1, "Byte array to long sym key completed");
    return x;
}
public static byte[] longtobytearraysymkey(long input ) {
	ByteBuffer buffer1 = ByteBuffer.allocate(16).putLong(input);
	y = buffer1.array();
	debug_level.debug(1, "long to byte array sym key completed");
	return y;
}
}
