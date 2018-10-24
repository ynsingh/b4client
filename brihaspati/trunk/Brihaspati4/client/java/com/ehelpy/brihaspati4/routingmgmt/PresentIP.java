package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 15 Apr 2018
public class PresentIP extends UpdateIP
{
    public static String MyPresentIP = null;
    public static String IP=null;

//	continuously runs an IP update and checks to ensure we dont have local host

    public static String MyPresentIP()
    {

        try {


            String IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();

            if(!IPAdd.isEmpty()&& !IPAdd.equals("127.0.0.1"))
            {
                Connected = true;
                SysOutCtrl.SysoutSet("IP Update Running. My IP :"+MyIP,3);
                MyPresentIP =IPAdd;
            }
            else
            {
                Connected = false;
                SysOutCtrl.SysoutSet("CHECK YOUR CONNECTION STATUS",0);
            }
        }


        catch(Exception e1)
        {
            Connected = false;
            SysOutCtrl.SysoutSet("CHECK YOUR CONNECTION STATUS",0);
        }

        return MyPresentIP;


    }
}
