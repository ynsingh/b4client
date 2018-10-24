package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;
//Maj Piyush Tiwari 20 April 2018.
//This code is for maintaining list of 5 predecessors and 5 successors of a node.

public class PredecessorSuccessor {

    public static String myPredecessors[]= {"","","","",""};
//	public static String myPredecessors[]= new String[5];//{"","","","",""};
    public static String mySuccessors[] = {"","","","",""};










    public static String[] getMyPredecessors(String[] nodeId,String selfNodeId) {








        Arrays.sort(nodeId);


        for(int i=0; i<nodeId.length; i++)
        {
            System.out.println(nodeId[i]);
        }

        int index=Arrays.asList(nodeId).indexOf(selfNodeId);
        SysOutCtrl.SysoutSet("Index of Self Node :"+index, 2);


        if(nodeId.length>4)
        {
            int length=0;
            for(int j = -1; j>-6; j-- )
            {



                if((index +j)>=0)
                {

                    length++;

                    String Predecessor = nodeId[(index+j)];
                    SysOutCtrl.SysoutSet("My predecessors are " +Predecessor, 3);

                    myPredecessors[5 +j] = Predecessor;
                    //SysOutCtrl.SysoutSet("length of array is " +length, 3);


                }



                else if((index +j)<0)

                {


                    for(int j1 = (5-length); j1>0; j1--)

                    {


                        String Predecessor = nodeId[(nodeId.length - j1)];
                        SysOutCtrl.SysoutSet("My predecessors are " +Predecessor, 3);
                        myPredecessors[5-length-j1] = Predecessor;

                    }
                    SysOutCtrl.SysoutSet("closing first loop of my Predecessor");
                }
            }


        }
        else if(nodeId.length==4)
        {
            if(index==0)
            {
                String Predecessor1 = nodeId[3];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[3];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;
            }
            else if(index==1)
            {
                String Predecessor1 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[3];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[3];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;
            }
            else if(index == 2)
            {
                String Predecessor1 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[3];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;
            }

            else if(index == 3)
            {
                String Predecessor1 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;
            }
        }
        else if(nodeId.length == 3)
        {
            if(index==0)
            {
                String Predecessor1 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;

            }
            else if(index==1)
            {
                String Predecessor1 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[2];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;

            }
            else if(index == 2)
            {
                String Predecessor1 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;

            }
        }
        else if(nodeId.length == 2)
        {
            if(index==0)
            {
                String Predecessor1 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[1];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;

            }
            else if(index==1)
            {
                String Predecessor1 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor1, 3);
                String Predecessor2 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor2, 3);
                String Predecessor3 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor3, 3);
                String Predecessor4 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor4, 3);
                String Predecessor5 = nodeId[0];
                SysOutCtrl.SysoutSet("My Predecessors are " +Predecessor5, 3);
                myPredecessors[4] = Predecessor1;
                myPredecessors[3] = Predecessor2;
                myPredecessors[2] = Predecessor3;
                myPredecessors[1] = Predecessor4;
                myPredecessors[0] = Predecessor5;


            }
            SysOutCtrl.SysoutSet("list of My Predecessors is " +myPredecessors, 2);

        }

        return myPredecessors;

    }






    public static String[] getMySuccessors(String[] nodeId,String selfNodeId)
    {
        SysOutCtrl.SysoutSet("Self ID Input recd by the Succ update :"+selfNodeId, 2);







        //for( int i3 = 0;i3<HeartbeatMonitoring.DeadAfterThirdPingArray.length;i3++)
        //{

        //if(HeartbeatMonitoring.DeadAfterThirdPingArray[i3][0] != null) {






        Arrays.sort(nodeId);

        for(int i=0; i<nodeId.length; i++)
        {
            System.out.println(nodeId[i]);
        }

        int index=Arrays.asList(nodeId).indexOf(selfNodeId);
        System.out.println("Index of Self Node :"+index);


        if(nodeId.length>4) {

            int length=0;
            for(int j = index+1; j<(index + 6); j++ )
            {



                if( j<nodeId.length)
                {

                    length++;

                    String Successor = nodeId[j];
                    //SysOutCtrl.SysoutSet("My successors are " +Successor, 3);
                    mySuccessors[(j - index-1)] = Successor;
                    //SysOutCtrl.SysoutSet("length of array is " +length, 3);
                }



                else if(j>nodeId.length-1)
                {

//		   String Successor = nodeId[0];
//		   SysOutCtrl.SysoutSet("My successors are " +Successor, 3);
//		   mySuccessors[length +1] = Successor;
                    for(int j1 = 0; j1<5-length; j1++)
                    {

                        String Successor = nodeId[j1];
                        //SysOutCtrl.SysoutSet("My succcessors are " +Successor, 3);
                        mySuccessors[(length +j1)] = Successor;
                    }

                }
                SysOutCtrl.SysoutSet("list of My Successors is " +mySuccessors, 2);
            }

        }
        else if(nodeId.length==4)
        {
            if(index==0) {
                String Successor1 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor1, 3);
                String Successor2 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[3];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
            else if(index==1)
            {
                String Successor1 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor1, 3);
                String Successor2 = nodeId[3];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[3];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
            else if(index == 2)
            {
                String Successor1 = nodeId[3];
                SysOutCtrl.SysoutSet("My Sucessors are " +Successor1, 3);
                String Successor2 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[3];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
            else if(index == 3)
            {
                String Successor1 = nodeId[0];
                SysOutCtrl.SysoutSet("My Sucessors are " +Successor1, 3);
                String Successor2 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }

        }
        else if(nodeId.length == 3)
        {
            if(index==0) {
                String Successor1 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor1, 3);
                String Successor2 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
            else if(index==1)
            {
                String Successor1 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor1, 3);
                String Successor2 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[2];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
            else if(index == 2)
            {
                String Successor1 = nodeId[0];
                SysOutCtrl.SysoutSet("My Sucessors are " +Successor1, 3);
                String Successor2 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
//	   for (int i = 0; i<5; i++) {
//		   SysOutCtrl.SysoutSet("my Successor list id "+mySuccessors[i]);
//	   }
        }
        else if(nodeId.length == 2)
        {
            if(index==0) {
                String Successor1 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor1, 3);
                String Successor2 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[1];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;
            }
            else if(index==1)
            {
                String Successor1 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor1, 3);
                String Successor2 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor2, 3);
                String Successor3 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor3, 3);
                String Successor4 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor4, 3);
                String Successor5 = nodeId[0];
                SysOutCtrl.SysoutSet("My Successors are " +Successor5, 3);
                mySuccessors[0] = Successor1;
                mySuccessors[1] = Successor2;
                mySuccessors[2] = Successor3;
                mySuccessors[3] = Successor4;
                mySuccessors[4] = Successor5;

            }


            SysOutCtrl.SysoutSet("list of My Successors is " +mySuccessors, 2);

        }

        return mySuccessors;



    }

}


