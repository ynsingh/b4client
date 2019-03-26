package com.ehelpy.brihaspati4.comnmgr;

import java.io.File;

import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.routingmgmt.RMThreadPrimary;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;

public class P2PMain {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        //CommunicationUtilityMethods.fillReceiveBuffer();//to simulate running condition when 10 xml query files are loaded in RxBuffer.

//		Thisn is RM1 from RM threadPrimary, shifted here.

        UpdateIP IPUpdate = new UpdateIP();

        IPUpdate.start();
        IPUpdate.setName("IPUpdate");
        SysOutCtrl.SysoutSet("Thread Id : "+IPUpdate.getName(), 1);

        OverlayManagementUtilityMethods.fillMyIptable();
        SysOutCtrl.SysoutSet("iptable initiated"+CommunicationManager.myIpTable);


        OverlayManagement olay = new OverlayManagement();
        olay.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        RMThreadPrimary RM = new RMThreadPrimary();
        RM.start();
        try {
            RM.join(9000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CommunicationManager cm= new CommunicationManager();
        cm.start();
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        SysOutCtrl.SysoutSet("visibility of fromNodeIdList in P2PMain"+CommunicationManager.fromNodeIdList,3);

        SysOutCtrl.SysoutSet("Starting IndexManagement thread",1);

        IndexManagement indmgt= new IndexManagement();
        indmgt.start();
        indmgt.getState();
        SysOutCtrl.SysoutSet("changing iAmNewlyJoinedNode varialbe in Overlay to true",1);
        OverlayManagement.iAmNewlyJoinedNode=true;
        OverlayManagement.flagMyPredecessorsUpdatedForIndexManager=true;

        //SysOutCtrl.SysoutSet("filling RxBufferIM in Communiation Manger with dummy query files",3);
        //IndexManagementUtilityMethods.fillRxBufferIM();
        SysOutCtrl.SysoutSet("filling MyIndexTable with dummy entries",3);
        //IndexManagementUtilityMethods.fillMyIndexTable();


        SysOutCtrl.SysoutSet("creating search query for nodeId 'cdef1' ",2);
        String email="deepaksharma";
        String searchQueryReply=IndexManagementUtilityMethods.searchEmailId( email);
        //CommunicationManager.TransmittingBuffer.add(searchQueryReply);
        System.out.println(searchQueryReply+".c.c.c.c..c.");

        //String emailId="deepaksh@iitk.ac.in";// email id of the user to be serched
        //IndexManagementUtilityMethods.searchEmailId( email);// calling searchEmailId method of IndexManagement

        //wait
        //String ip=IndexManagement.callingTable.get(emailId);// searched ip is filled in callingTable hashMap
        //SysOutCtrl.SysoutSet(ip);

    }

}
