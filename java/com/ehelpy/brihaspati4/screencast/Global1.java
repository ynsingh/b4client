package com.ehelpy.brihaspati4.screencast;
// lt col jitesh ps updated this on 14 june 2020 ; 0500 Hrs
//this code is for declaring enums
//this gives range of port values

public enum Global1 {
    NULL_PORT (-1),
    MIN_PORT  (0),
    MAX_PORT  (65535),
    IP4_LIMIT (65507), // bufSize limit for single IPv4 packet
    IP6_LIMIT (65527); // bufSize limit for single IPv6 packet

    private final int anInt;Global1(int value) {anInt = value;}
    public int val() {return anInt;}
}
