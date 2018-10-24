package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 3 May 2018
import java.util.Arrays;

public class PurgeEntry extends RTUpdate9
{
//	we pass a 40 Hex string Id (dead node form Heartbeat monitor) to it for purging from the RT

    public void purge(String Purge)
    {
        SysOutCtrl.SysoutSet("Node to be Purged from RT is :"+Purge, 2);

        if(Arrays.asList(Pred).contains(Purge))
        {
            SysOutCtrl.SysoutSet("Found Entry to be Purged in RT", 2);
            int Loc = Arrays.asList(Pred).indexOf(Purge);
            Pred[Loc]=null;
            SysOutCtrl.SysoutSet("Node :"+Purge+" purged from RT Pred", 2);

        }

        else if(Arrays.asList(Succ).contains(Purge))
        {
            SysOutCtrl.SysoutSet("Found Entry to be Purged in RT", 2);
            int Loc = Arrays.asList(Succ).indexOf(Purge);
            Succ[Loc]=null;
            SysOutCtrl.SysoutSet("Node :"+Purge+" purged from RT Succ", 2);

        }

        if(Arrays.asList(Mid).contains(Purge))
        {
            SysOutCtrl.SysoutSet("Found Entry to be Purged in RT", 2);
            int Loc = Arrays.asList(Mid).indexOf(Purge);
            Mid[Loc]=null;
            SysOutCtrl.SysoutSet("Node :"+Purge+" purged from RT Mid", 2);

        }


        Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
        save.Save_RTNow();

        SysOutCtrl.SysoutSet("RT saved after Purging", 2);

        SysOutCtrl.SysoutSet("RT Table after Purged Entry", 2);

        PrintRT9 PrintRT = new PrintRT9();
        PrintRT.PrintMatrix();


    }
}
