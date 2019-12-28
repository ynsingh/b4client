package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 3 May 2018
import java.util.Arrays;

public class PurgeEntry extends RTManager
{
//	we pass a 40 Hex string Id (dead node form Heartbeat monitor) to it for purging from the RT
	
	public static void purge(String Purge)
	{
		SysOutCtrl.SysoutSet("Node to be Purged from RT is :"+Purge, 2);
	//	System.out.println("pred list"+   Arrays.asList(Pred));
//		System.out.println("succ list"+   Arrays.asList(Succ));
	//	System.out.println("mid list"+   Arrays.asList(Mid));
	
		for(int i=0;i<40;i++){
			if(Purge==Pred[i][0]) {
				SysOutCtrl.SysoutSet("Found Entry to be Purged in RT", 2);
				Pred[i][0]=null;
				Pred[i][1]=null;
				SysOutCtrl.SysoutSet("Node :"+Purge+" purged from RT Pred", 2);
				break;
			}
			if(Purge==Succ[i][0]) {
				SysOutCtrl.SysoutSet("Found Entry to be Purged in RT", 2);
				Succ[i][0]=null;
				Succ[i][1]=null;
				SysOutCtrl.SysoutSet("Node :"+Purge+" purged from RT Succ", 2);
				break;
			}
			if(Purge==Mid[i][0]) {
				SysOutCtrl.SysoutSet("Found Entry to be Purged in RT", 2);
				Mid[i][0]=null;
				Mid[i][1]=null;
				SysOutCtrl.SysoutSet("Node :"+Purge+" purged from RT Mid", 2);
				break;
			}
		}
		
		SysOutCtrl.SysoutSet("RT Table after Purged Entry", 2);	
		
		PrintRT9 PrintRT = new PrintRT9();
		PrintRT.PrintMatrix();

	
	}
}
