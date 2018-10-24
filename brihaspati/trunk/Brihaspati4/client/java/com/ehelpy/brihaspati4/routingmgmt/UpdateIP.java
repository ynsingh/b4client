package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 12 Apr 2018

import java.io.File;

// 03rd April 2018 1440h

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;

public class UpdateIP extends RTUpdate9
{
//	this keeps a handle on the IP and updates
//	the moment a change in IP is detected it informs all the nodes that self node is aware of the
//	changed IP, the same gets updated in all the nodes.

    public static String MyIP = null;
    public static String StrIPnow;
    public static boolean CheckIP=true;
    public static String PresentIP=null;
    public static boolean Connected = true;

    public synchronized void run() //String UpdateThisIP()// throws UnknownHostException
    {
        com.ehelpy.brihaspati4.indexmanager.CreateXmlFile IPUpdateQuery = new com.ehelpy.brihaspati4.indexmanager.CreateXmlFile();

        DelayInMills CreateDelay = new DelayInMills();
        long Delay = 3000;// This needs to be changed to the amount of time after
        // which the IP needs to be checked(in mills)

        while (CheckIP=true)
        {
//		 InetAddress IPAdd = null;
            String IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();


            MyIP = IPAdd;


            String MyPresentIP = MyIP;

            if(!MyPresentIP.isEmpty()&& !MyPresentIP.equals("127.0.0.1"))
            {
                Connected = true;
                SysOutCtrl.SysoutSet("IP Update Running. My IP :"+MyIP,3);
            }

            try {
                CreateDelay.sleep(5000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
//			InetAddress IPNow = InetAddress.getLocalHost();
                String IPNow = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();


                MyPresentIP= IPNow;

                if(!MyPresentIP.isEmpty() && !MyPresentIP.equals("127.0.0.1"))
                {
                    Connected = true;
                    SysOutCtrl.SysoutSet("IP now is :" +IPNow,1);
                }

                else
                {
                    Connected = false;
                    SysOutCtrl.SysoutSet("Please Check connection. System is getting Local Host, CONNECTION STATUS :"+ Connected,3);
                }



                if (IPNow.equals(IPAdd))

                {
//			String Now =IPNow.toString();
//
//			Match = Now.indexOf("/");
//			CutAt= Match+1;
//
                    MyPresentIP= IPNow;

                    if(!MyPresentIP.isEmpty() && !MyPresentIP.equals("127.0.0.1"))
                    {
                        SysOutCtrl.SysoutSet("IP add same",1);
                    }

                    else {
                        SysOutCtrl.SysoutSet("Please Check connection. System is getting Local Host,CONNECTION STATUS : "+ Connected,1);
                    }
                    //CreateDelay.start();

                    try {
                        CreateDelay.sleep(Delay);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };
                }
                else
                {
                    MyPresentIP = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
//				MyIP = IPAdd1;
//				PresentIP=MyIP;

//				Match = PresentIP.indexOf("/");
//				CutAt= Match+1;
//
//				MyPresentIP= PresentIP.substring(CutAt, PresentIP.length());

                    if(!MyPresentIP.isEmpty() && !MyPresentIP.equals("127.0.0.1"))
                    {
                        SysOutCtrl.SysoutSet("check internet connection. The system is getting present IP as :" +StrIPnow,1);
                    }

                    else {
                        Collection<String> Node_id_extracted = CommunicationManager.myIpTable.keySet(); /// code to extract hash_id from array by first
                        /// converting it into collection then to an array
                        Object[] Nodeid_array = Node_id_extracted.toArray();

                        for(int i=0; i<Nodeid_array.length; i++)
                        {
                            String Node_id= (String) Nodeid_array[i];
                            File IPUpdate = IndexManagementUtilityMethods.createXmlUpdate_IP(Node_id);
                            CommunicationManager.TransmittingBuffer.add(IPUpdate);
                            SysOutCtrl.SysoutSet("Updated IP file sent to TransmittingBuffer :"+Node_id, 2);
                            SysOutCtrl.SysoutSet("Tx Buffer state vis at UpdateIP :"+CommunicationManager.TransmittingBuffer, 2);
                        }

                        SysOutCtrl.SysoutSet("IP add updated to :" + MyIP,1);

                    }
                    try {
                        CreateDelay.sleep(Delay);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };
//			   }
                }
            }
            catch (Exception e)
            {
                SysOutCtrl.SysoutSet("No Network Connection Detected",3);
                e.printStackTrace();
            }


        }
    }
}

class DelayInMills extends Thread
{
    public void run()
    {
        try {
            Thread.sleep(10000);
            //long Delay = 1000;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
