 	
package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.IOException;
import java.util.Collection;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class HeartbeatMonitoring extends OverlayManagementUtilityMethods{
	//Maj Piyush Tiwari
	//this code is for checking the reachability of nodes list available with this node.

		
	   
		public static String[][] DeadAfterFirstPingArray = new String[CommunicationManager.myIpTable.size()][2];//Create a 2d array of the nodes which were dead after first ping
        public static String[][] DeadAfterSecondPingArray = new String[CommunicationManager.myIpTable.size()][2];//Create a 2d array of the nodes which were dead after second ping
        public static String[][] DeadAfterThirdPingArray = new String[CommunicationManager.myIpTable.size()][2];//Create a 2d array of the nodes which were dead after third ping
        
        
        
        
        public static void heartbeatCheck() {
                // TODO Auto-generated method stub
        	SysOutCtrl.SysoutSet("you are in heartbeatcheck method cheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeckkkkkkkkkkkkkkkkkkkkk", 2);
        	
       
            			Collection<String> ipList=CommunicationManager.myIpTable.values();//convert the linked hash map of node id and iptable from comn mngr to collection of string.
            			Object[] ip_array = ipList.toArray();//convert the collection to array.						
            			
            			int IPSize =ip_array.length;
            			
            			SysOutCtrl.SysoutSet("size of ip array :"+IPSize, 2);
            			
            			for(int i1=0;i1<ip_array.length;i1++) 
            			{
                        
                        
            				boolean bool;
            				String ip = (String) ip_array[i1];
            				SysOutCtrl.SysoutSet("Ip table from first method " +ip, 2);
                
                        	try {
                        			bool = pingCheck(ip);
                        			SysOutCtrl.SysoutSet("bool"+bool, 2);
                        			if(bool==false)
                        			{
                        				
                        				SysOutCtrl.SysoutSet("you are in if block of first pingCheck", 3);
                        				SysOutCtrl.SysoutSet("size of the array " +ip_array.length , 3);
                        				
                        				DeadAfterFirstPingArray[i1][0] = pingArray[i1][0];
                        				DeadAfterFirstPingArray[i1][1] = pingArray[i1][1];
                        				SysOutCtrl.SysoutSet(DeadAfterFirstPingArray[i1][0], 2);
                        				SysOutCtrl.SysoutSet(DeadAfterFirstPingArray[i1][1], 2);
                              
                                
                        			}  
                        			
                        			 
                        			else 
                        			{
                        				SysOutCtrl.SysoutSet("you are in else block of first pingCheck", 3);
                        				DeadAfterFirstPingArray[i1][0] = "0";
                        				DeadAfterFirstPingArray[i1][1] = "0";
                        				SysOutCtrl.SysoutSet(DeadAfterFirstPingArray[i1][0], 2);
                        				SysOutCtrl.SysoutSet(DeadAfterFirstPingArray[i1][1], 2);
                        			}	
                        		}	
                        	
                        	catch (Exception e) {
                        		// TODO Auto-generated catch block
                        		e.printStackTrace();
                        		System.out.println(e);
                    			e.toString();
                        		}
            			}
                        	
            			SysOutCtrl.SysoutSet("DeadAfterFirstPingArray", 2);               
                
                  
                                
            			System.out.println("Checking the DeadAfterFirstPing nodes by pinging 2nd time");
                  
            			for(int i2=0;i2<ip_array.length;i2++) 
            			{
                
                
            				boolean bool = true;
            				String FirstTimeDeadIp = DeadAfterFirstPingArray[i2][1];
            				System.out.println("ip variable from 2nd method "+FirstTimeDeadIp);
                
            				if(FirstTimeDeadIp  == "0")
                
            				{   }
            				else
            				{
            					System.out.println("you are in if loop of ip equals null");
            					try {
            						bool =pingCheck(FirstTimeDeadIp);
            						} catch (Exception e) {
                // TODO Auto-generated catch block
            							e.printStackTrace();
            							System.out.println(e);
            							e.toString();
            						}
        
            				if(bool==false)
            				{
                
        
            					DeadAfterSecondPingArray[i2][0] = DeadAfterFirstPingArray[i2][0];
            					DeadAfterSecondPingArray[i2][1] = DeadAfterFirstPingArray[i2][1];
                
            				}        
            				else {
            					DeadAfterSecondPingArray[i2][0] = "0";
            					DeadAfterSecondPingArray[i2][1] = "0";
            					}
            				}}
            			for( int i2 = 0;i2<DeadAfterFirstPingArray.length;i2++)
            			{
            				System.out.println(DeadAfterSecondPingArray[i2][0]);
            			}

            			System.out.println("Checking the DeadAfterSecondPing nodes by pinging 3rd time");
            			for(int i3=0;i3<ip_array.length;i3++) 
            			{
                
                
            				boolean bool = true;
            				String SecondTimeDeadIp = DeadAfterFirstPingArray[i3][1];
            				System.out.println("ip variable from 2nd method "+SecondTimeDeadIp);
                
            				if(SecondTimeDeadIp  == "0")
                
            				{   }
            				else
            				{
            					System.out.println("you are in if loop of ip equals null");
            					try {
            						bool =pingCheck(SecondTimeDeadIp);
            					} catch (Exception e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            						System.out.println(e);
            						e.toString();
            					}
        
            			if(bool==false)
                         {
                
        
            				DeadAfterThirdPingArray[i3][0] = DeadAfterSecondPingArray[i3][0];
            				DeadAfterThirdPingArray[i3][1] = DeadAfterSecondPingArray[i3][1];
                
                         }        
            			else {
            				DeadAfterThirdPingArray[i3][0] = "0";
            				DeadAfterThirdPingArray[i3][1] = "0";
            			}
        
            				}
            			}
        com.ehelpy.brihaspati4.routingmgmt.PurgeEntry deleteFromRt = new com.ehelpy.brihaspati4.routingmgmt.PurgeEntry();
        
        	for( int i3 = 0;i3<DeadAfterThirdPingArray.length;i3++) {
        	
        	if(DeadAfterThirdPingArray[i3][0] != null) {
        		
        		deleteFromRt.purge(DeadAfterThirdPingArray[i3][0]);
        		
        		CommunicationManager.myIpTable.remove(DeadAfterThirdPingArray[i3][0], DeadAfterThirdPingArray[i3][1]);
        		IndexManagementUtilityMethods.removeIndexing(DeadAfterThirdPingArray[i3][0]);
        	}
//        	else {
//        		pingArray[i3][0] = "0";
//        	   	pingArray[i3][1] = "0";
//        		}
                System.out.println(DeadAfterThirdPingArray[i3][0]);

                }
      	
        SysOutCtrl.SysoutSet("HM run completed doooooooooooooooooooooooooooooooooneeeeeeeeeeeeeeeeeeeeeeeeeee", 2);
		        
        }

    	public static boolean pingCheck(String ip) throws IOException, InterruptedException  
	{

		Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 4 "+ip);
  	    int returnVal = p1.waitFor();
  	    System.out.println(returnVal);
  	    boolean reachable = (returnVal==0);
  			if (reachable==true)
  			{
  				return reachable;
  			}else
  			{
  				Process p2 = java.lang.Runtime.getRuntime().exec("ping -c 4 "+ip);
  		  	    int returnVal2 = p2.waitFor();
  		  	    System.out.println(returnVal2);
  		  	boolean reachable2 = (returnVal2==0);
  		  return reachable2;
  			}
		}
        
      /* public static boolean pingCheck(String ip) throws IOException {
	
        	InetAddress address = InetAddress.getByName(ip);
        	System.out.println("Is host reachable- get address? " + address.toString());
            boolean reachable = address.isReachable(10000);

            System.out.println("Is host reachable? " + reachable);
            return reachable;
          }
        */
            
//        public static boolean checkNull(String[][] array)
//    	{boolean bool= false;
//
//    	
//    		if(!Arrays.asList(array).isEmpty())
//    			
//    		{bool= false;}
//    		bool=true;
//    		
//    		
//    		return bool;
//    	
//    	}
  
        }
