package com.ehelpy.brihaspati4.routingmgmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;



//import threadimplementation.Save_Retrieve_RT;

public class RTManager extends Thread
{
//	public static String[][] NodesInRT = new String[120][2];

    public static RTManager rtmgr;
    public static String Succ[][] = new String[40][2];
    public static String Pred[][] = new String[40][2];
    public static String Mid[][] = new String[40][2];
    public static String[][] Matrix = new String[3][40];
    public static String[][] mat = new String[3][40];
    public static BufferedReader BR = null;
    public static String[][] RoutingInptBuff = new String[120][2];
    public static Map<String, String>  Routing_Table = new ConcurrentHashMap<String, String>();
    public static Lock lock_for_routoing_table = new ReentrantLock();

    public synchronized static RTManager getRTMgr() {
        if(rtmgr ==null) rtmgr = new RTManager();
        return rtmgr;
    }

// public synchronized static void start() throws IOException
    public synchronized static void initiateRT() throws IOException

    {
        Save_Retrieve_RT.Retrieve_RT Read = new Save_Retrieve_RT.Retrieve_RT();
        Read.Retrieve_RT_Now();


        String RoutingQuery;
        String ipadd;
        for(int i=0; i<120; i++) //RoutingInptBuff.size();i++)
        {
            if(RoutingInptBuff[i][0]!=null)
            {
                RoutingQuery = RoutingInptBuff[i][0].toString();
                ipadd=RoutingInptBuff[i][1].toString();
                SysOutCtrl.SysoutSet(i+" "+RoutingQuery,3);
                if(!RoutingQuery.equals("null")||!RoutingQuery.equals(null)||!RoutingQuery.equals("")||!RoutingQuery.isEmpty()||!RoutingQuery.equals(" "))
                {
                    if(CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ipadd))
                    {
                        CommunicationManager.myIpTable.put(RoutingQuery,ipadd);
                        OverlayManagement.AliveNodes.put(RoutingQuery,ipadd);
                        UpdateTabFromQuery.NextEntry(RoutingQuery,ipadd);
                    }

                    Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
                    save.Save_RTNow();

                    Set<String> nodeId_extracted = CommunicationManager.myIpTable.keySet(); /// code to extract hash_id from array by first
                    /// converting it into collection then to an array
                    Collection<String> ip_extracted = CommunicationManager.myIpTable.values();

                    String[] nodeIdArr = nodeId_extracted.toArray(new String[nodeId_extracted.size()]);
                    String[] ipAddArr = ip_extracted.toArray(new String[ip_extracted.size()]);

                    CommunicationManager.lock.lock();
                    try
                    {
                        XmlFileSegregation.ipTableWriter(nodeIdArr,ipAddArr);
                    }
                    finally
                    {
                        CommunicationManager.lock.unlock();
                    }
                    SysOutCtrl.SysoutSet("i :"+i,3);
                }
            }

            else
            {}
        }


    }
}


