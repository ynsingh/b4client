package com.ehelpy.brihaspati4.voip;

public class voip {
    static boolean status = false;
    public static boolean start_call(String IPaddr, long sym_key) {
        status = true;
        System.out.println("the IP addr is ="  + IPaddr);
        return status;
    }

    public static boolean rxcall(String IPaddr, long sym_key) {
        status = true;
        System.out.println("the IP addr is ="  + IPaddr);
        return status;
    }
}
