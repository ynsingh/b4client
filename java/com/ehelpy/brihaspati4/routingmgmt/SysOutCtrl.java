package com.ehelpy.brihaspati4.routingmgmt;

import com.ehelpy.brihaspati4.authenticate.Config;
// Major Niladri Roy 29 Apr 2018

public class SysOutCtrl extends RMThreadPrimary
{
//	This is used by the query managing modules for controlling console outputs
//	basic thread view for flow analysis 1
//	Debug 2
//	Deep debug 3

    public static void SysoutSet(String Print,int OutCtrl)
    {

        Config conf = Config.getConfigObject();
        if(OutCtrl <= conf.getCtrlConsoleOut()){
                System.out.println(Print);
        }
    }


    // added for single argument
    // outctrl=3
    public static void SysoutSet(String Print)
    {
        SysoutSet(Print, 3);
    }
}
