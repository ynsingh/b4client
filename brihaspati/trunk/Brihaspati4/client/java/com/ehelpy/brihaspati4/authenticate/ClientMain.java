package com.ehelpy.brihaspati4.authenticate ;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.routingmgmt.RMThreadPrimary;
import com.ehelpy.brihaspati4.routingmgmt.RTUpdate9;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;
import com.ehelpy.brihaspati4.sms.sms_methods;
import com.ehelpy.brihaspati4.sms.sms_retrival_thread;
import com.ehelpy.brihaspati4.sms.sms_send_rec_management;
import com.ehelpy.brihaspati4.DFS.DistFileSys;
import com.ehelpy.brihaspati4.DFS.Save_Retrive_data_Structures;

public class ClientMain extends Thread {
    private static X509Certificate client_cert = null;
    private static X509Certificate server_cert = null;
    private static boolean flagset = false;
    public static int CtrlConsoleOut=0;
    public static void main(String args[]) throws Exception
    {    	
        @SuppressWarnings("unused")
        
	GlobalObject globj= GlobalObject.getGlobalObject();
        globj.setRunStatus(true);
        // GlobalObject will keep status of various threads and run status. This will be used
        // for proper closure of threads when closing the application.

        // *com.ehelpy.brihaspati4.routingmgmt.GetProperties.Debug.Properties();
        // *CtrlConsoleOut=com.ehelpy.brihaspati4.routingmgmt.GetProperties.Property_sysout;
        
        Config conf=Config.getConfigObject();
        // Config initialization from configuration file during by the constructor of Config.
        // Config_object will keep the data after reading from configuration file.
        // On each change, the data should be written back to config file also.
        // It implies, in each write api, write to config file on disk is to be implemented.
        // debug level to be read from Config object which in turn to be read from configuration file. Can be modified in GUI, which
        // update it in the configuration file.

        CtrlConsoleOut = conf.getConsoleOut(); 

        SysOutCtrl.SysoutSet("iptable initiated"+CommunicationManager.myIpTable);
        UpdateIP IPUpdate = new UpdateIP();
        IPUpdate.start();
        IPUpdate.setName("IPUpdate");
        SysOutCtrl.SysoutSet("Thread Id : "+IPUpdate.getName(), 1);
        Config conf=Config.getConfigObject();
        // Config initialization from configuration file during by the constructor of Config.
        // Config_object will keep the data after reading from configuration file.
        // On each change, the data should be written back to config file also.
        // It implies, in each write api, write to config file on disk is to be implemented.
        boolean timeflg=dateTimeCheck.checkDate();
        // If the time flag returns false then exit the user from the system
        // otherwise start the services.
         if (!timeflg)
        {
            String msg = "Please reset your system time and try again." ;
            Gui.showMessageDialogBox(msg);
            System.exit(0);
        }
        else {
            try {
                flagset = ReadVerifyCert.verifyCert();
                client_cert = ReadVerifyCert.returnClientCert();
                server_cert = ReadVerifyCert.returnServerCert();
                debug_level.debug(0,"clientcertsaved is =" + client_cert );
                debug_level.debug(0,"servercertsaved is =" + server_cert );
                String email_id=emailid.getemaild();
                debug_level.debug(0,"My Email-Id is =" + email_id );
            } catch (CertificateException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        if(globj.getRunStatus())
        {
            if(flagset) 
            {
            	debug_level.debug(0,"The private key of client is  =" + ReadVerifyCert.getKeyPair() );
                sms_methods.choose_loc();
            	sms_send_rec_management.empty_cache_folder();
            	sms_send_rec_management.empty_rec_folder();
             	IndexManagementUtilityMethods.Ip_txt_empty();
                // call objects and methods from classes of - communication
                CommunicationManager cm= new CommunicationManager();
                cm.start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
        			RTUpdate9.InitiateRT();
				Save_Retrive_data_Structures.Save_nodeFileChunkMap();
        			Save_Retrive_data_Structures.Save_nodefilemap();
        			Save_Retrive_data_Structures.Save_root_Fileinfo_Map();
        			Save_Retrive_data_Structures.Save_shared_Fileinfo_Map();
        			Save_Retrive_data_Structures.Retrive_nodeFileChunkMap();
        			Save_Retrive_data_Structures.Retrive_nodefilemap();
        			Save_Retrive_data_Structures.Retrive_root_Fileinfo_Map();
        			Save_Retrive_data_Structures.Retrive_shared_Fileinfo_Map();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} 
                OverlayManagement.nodeStartUp();
                OverlayManagement olay = new OverlayManagement();
                olay.start();
                try {
        			Thread.sleep(5000);
        		} catch (InterruptedException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
                // call objects of routing  mangement
                
                RMThreadPrimary.update_rt();
                
                System.out.println("Starting IndexManagement thread");
               
                // call objects and methods from classes of - index management
                IndexManagement indmgt= new IndexManagement();
                indmgt.start();
               
                sms_retrival_thread sms= new sms_retrival_thread();
                sms.start();
               
		DistFileSys dfs = new DistFileSys();
                dfs.start();
               	try {
        			Thread.sleep(1000);
        	} catch (InterruptedException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        	}

                B4services.service();
                // start user specific services
                // user specific DFS mount service,
                // call objects and methods from classes of - routing and overlay mangement
                // All generic services Interface
                // VOIP call, storage services, messaging service
            }
        }
        else
        {
            globj.setRunStatus(true);
            ClientMain.main(args);
       }
    }
}
