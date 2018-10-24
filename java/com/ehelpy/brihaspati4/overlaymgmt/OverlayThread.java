package com.ehelpy.brihaspati4.overlaymgmt;

public class OverlayThread {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        OverlayManagementUtilityMethods.fillMyIptable();
        OverlayManagementUtilityMethods.fillRoutingTable();
        //PredecessorSuccessor.getMyPredecessors(OverlayManagement.getMyPredecessors()getMyNodeID, selfNodeId)

        OverlayManagement olay = new OverlayManagement();
        olay.start();


    }

}
