package com.ehelpy.brihaspati4.screencast;
// lt col jitesh ps updated this on 14 june 2020 ; 0500 Hrs
//this code is for declaring enums

public enum Global {
    // Both AUTH enums MUST have equal length
    AUTH_PASS ("AuthPass"),
    AUTH_FAIL ("AuthFail");

    private final String aString;
    Global(String value) {aString = value;}
    public String val() {return aString;}


}
