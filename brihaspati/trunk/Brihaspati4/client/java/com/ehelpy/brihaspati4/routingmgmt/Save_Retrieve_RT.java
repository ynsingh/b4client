package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 26 Apr 2018
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Save_Retrieve_RT extends UpdateTabFromQuery
{
    public static String[] NodesInRT = new String[120];

    public static class Save_RT
    {
        int LenP ;
        int LenS;
        FileWriter RTWriter = null;

        public void Save_RTNow()
        {


            try {
                RTWriter = new FileWriter("RTP2P.xml");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for (int i = 0; i < Pred.length; i++)
            {
                if(Pred[i]!=null)
                {
                    NodesInRT[i] = Pred[i];
                }
                else
                {
                    LenP = i;
                    SysOutCtrl.SysoutSet("Lenght in Pred is :"+LenP,3);
                    break;
                }
            }

            for (int i = 0; i < Succ.length; i++)
            {
                if(Succ[i]!=null)
                {
                    NodesInRT[LenP+i] = Succ[i];
                }
                else
                {
                    LenS=LenP+i;
                    SysOutCtrl.SysoutSet("LenP + LenS is :"+ LenS,3);
                    break;
                }
            }

            for (int i = 0; i < Mid.length; i++)
            {
                if(Mid[i]!=null)
                {
                    NodesInRT[LenS+i] = Mid[i];
                }
            }

            for(int i=0; i<NodesInRT.length; i++)
            {
                if(NodesInRT[i]!=null)
                {
                    SysOutCtrl.SysoutSet(i+" Node in RT :"+ NodesInRT[i],3);
                    try
                    {
                        RTWriter.write(NodesInRT[i]);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            try
            {
                RTWriter.flush();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
//				e.printStackTrace();
            }
        }

    }

    static class Retrieve_RT
    {
        public String[] Retrieve_RT_Now()
        {
            CheckRTExists CheckRT = new CheckRTExists();
            boolean RTExists = CheckRT.ImportRT();

            if(RTExists=true)
            {
                try
                {
                    BR = new BufferedReader(new FileReader("RTP2P.xml"));

//				BR = new BufferedReader(new FileReader("D:\\roy\\aaaaaaroy\\IITK\\Java practice\\Wk space prac\\Trial\\RTP2P.xml"));
//				Change address above for testing purposes

                    StringBuffer StrBuff = new StringBuffer();
                    String Line;
                    String NodeId;

                    try
                    {
                        Line = BR.readLine();
                        if (Line!=null)
                        {
                            int Length = Line.length();
                            int Iterator = Length/40;
                            for(int l=0,j=0; l<Iterator; j=j+40,l++)
                            {
                                NodeId = Line.substring(j,j+40);
//								String Node = line.substring(j,j+40);
                                RoutingInptBuff[l] = NodeId;
                                SysOutCtrl.SysoutSet(l+" Entry in RotingInputBuff :"+RoutingInptBuff[l],3);
                            }
                        }
                    }

                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
//					e.printStackTrace();
                    }
                }


                catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
//				e.printStackTrace();
                    SysOutCtrl.SysoutSet("RT NOT FOUND, by Save_Retrieve_RT",3);
                    SysOutCtrl.SysoutSet("Initialising NULL RT...",3);

                    for(int m=0; m<120; m++)
                    {
                        RoutingInptBuff[m]=null;
                        SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m],3);
                    }
                }
            }

            else
            {
                SysOutCtrl.SysoutSet("RT NOT FOUND",1);
                SysOutCtrl.SysoutSet("Initialising NULL RT...",1);

                for(int m=0; m<120; m++)
                {
                    RoutingInptBuff[m]=null;
                    SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m], 3);
                }
            }
            return RoutingInptBuff;
        }
    }
}

