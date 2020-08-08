package com.ehelpy.brihaspati4.authenticate ;

import com.ehelpy.brihaspati4.DFS.DFSUI;
//import com.ehelpy.brihaspati4.DFS.Save_Retrive_data_Structures;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.NATHandler;
import com.ehelpy.brihaspati4.comnmgr.NATServer;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.routingmgmt.RMThreadPrimary;
import com.ehelpy.brihaspati4.routingmgmt.RTManager;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;
import com.ehelpy.brihaspati4.voip.B4services;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ClientMain extends Thread
{
    private static X509Certificate client_cert = null;
    private static X509Certificate server_cert = null;
    private static boolean flagset = false;
    public static int CtrlConsoleOut=0;
    public static String Device_Id="";
    public static String Node_Id="";

    public static void main(String args[]) throws Exception
    {
        // @SuppressWarnings("unused")

        // Create a singleton global object and set run status as true.
        // GlobalObject will keep status of various threads and run status. This will be used
        // for proper closure of threads when closing the application.

        GlobalObject globj= GlobalObject.getGlobalObject();
        globj.setRunStatus(true);

        // Configuration object created and object referece is saved in
        // GlobalObject.
        Config conf= Config.getConfigObject();

        // Config initialization from configuration file done during call of the
        // constructor of Config.
        // Config_object will keep the data after reading from configuration file.
        // On each change, the data should also be written back to config file.
        // It implies, in each write api, write to config file on disk is to be implemented.
        // Debug level (CtrlConsoleOut) is read from Config object which in
        // turn is to be read from configuration file.
        // Can be modified in GUI, which will update it in the configuration file.

        CtrlConsoleOut = conf.getCtrlConsoleOut();

        /* Commented - to be removed when the branch is to be finally merged to
         * the master.
         */
        SysOutCtrl.SysoutSet("iptable initiated"+CommunicationManager.myIpTable);
        UpdateIP IPUpdate = new UpdateIP();
        IPUpdate.start();
        IPUpdate.setName("IPUpdate");
        SysOutCtrl.SysoutSet("Thread Id : "+IPUpdate.getName(), 1);
        /* */

        boolean timeflg=dateTimeCheck.checkDate();
        /* Date and time is to be checked. It should be same as on standard time
        * server or greater than equal to last logout date time value.  If the returns
        * value false (in case the above conditions fails) then exit the user from the
        * system with advise to user to correct the system date and time. otherwise
        * start the services.
        */

        // Start the singleton object for UI
        // UIObject ui = UIObject.getUIObject();

        if (!timeflg)
        {
            String msg = "Please reset your system time and try again." ;
            Gui.showMessageDialogBox(msg);
            System.exit(0);
        }
        else {
            try
            {
                // Getting the MAC Address of the Machine
                Device_Id=Integrity.getMACAddress();
                // Getting the Node ID of the Client
                Node_Id=Integrity.getNodeID();

                System.out.println(Integrity.checkKeyStoreExists());

                //Checking whether KeyStoreExists or Not and Creating new Certificate if keystore does not Exists
                if(!Integrity.checkKeyStoreExists())
                {
                    String email_id=Gui.getemailid();
                    String mserverurl ="http://ictwiki.iitk.ac.in:8080/b4server";
                    //String mserverurl ="http://localhost:8084/beans_b4server";
                    String MSrequrl = mserverurl +"/ProcessRequest?req=checkkeystore&emailid="+email_id;

                    createConnection http_1 = new createConnection ();

                    int val=http_1.sendPost(MSrequrl,"checkkeystore");

                    System.out.println(val);
                    if(val==15)
                    {
                        X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                        debug_level.debug(0,"New certificate of node is " + newcert);
                        debug_level.debug(0,"New certificate of node will now be sent for signature to Identity Server ");
                        boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                        debug_level.debug(0,"status = "+ staus);
                        setFlagforcert();
                    }
                    if(val==14)
                    {

                        MSrequrl = mserverurl +"/ProcessRequest?req=keystorecheckotpgen&emailid="+email_id;


                        if(http_1.sendPost(MSrequrl,"keystorecheckotpgen")==1)
                        {
                            String otp=Gui.getotp();
                            MSrequrl = mserverurl +"/ProcessRequest?req=keystorecheckotpverify&emailid="+email_id+"&otp="+otp+"&deviceid="+Device_Id;
                            val=http_1.sendPost(MSrequrl,"keystorecheckotpverify");
                            if(val==16)
                            {
//                                flagset = ReadVerifyCert.verifyCert();
                                // check if there is valid certificate is present in the keystore of of the client.
                                setFlagforcert();
                                // In case of invalid certificate, new certificate generation and identity verification should be done.
                                client_cert = ReadVerifyCert.returnClientCert();
                                server_cert = ReadVerifyCert.returnServerCert();
                                //debug_level.debug(0,"clientcertsaved is =" + client_cert );
                                //debug_level.debug(0,"servercertsaved is =" + server_cert );
                                email_id=emailid.getemaild();
                                debug_level.debug(0,"My Email-Id is =" + email_id );
                            }
                            else if(val==17)
                            {
                                Gui.showMessageDialogBox("Record Doesnot Exists, so for generate new certificate");
                                System.exit(0);
                            }
                        }
                    }
                }// integrity check if close
                else
                {
                    setFlagforcert();
                    /* 		flagset = ReadVerifyCert.verifyCert();
                     		// check if there is valid certificate is present in the keystore of of the client.
                             	// In case of invalid certificate, new certificate generation and identity verification should be done.
                     		client_cert = ReadVerifyCert.returnClientCert();
                             	server_cert = ReadVerifyCert.returnServerCert();
                             	//debug_level.debug(0,"clientcertsaved is =" + client_cert );
                             	//debug_level.debug(0,"servercertsaved is =" + server_cert );
                             	String email_id=emailid.getemaild();
                             	debug_level.debug(0,"My Email-Id is =" + email_id );
                    */
                }

            } catch (CertificateException e) {
                // In case of exception during the certificate verification, the stacktrace is to be printed
                // for debugging purpose and program should terminate.
                // We can have an log submission mail of development team for identifying the issue and improving
                // the product. The emailing of this stacktrace can be added later.
                e.printStackTrace();
                System.exit(0);
            }
        }// time flag else close
        if(flagset) {
            // get singleton object for DHTRouter, RTManager, DHTable,
            // SpillOverTable, ComnMgr, ProxyRouter, MulticastMgr, MediaBridge,
            // IndexingMgr, KeyCache, SearchEngine, ContentCache,
            // Broadcast-RWRouter.
            // DHTRouter dhtr = DHTRouter.getDHTRouter();
            // RTManager rtmgr = RTManager.getRTManager();
            // DHTable dhtable = DHTable.getDHTable();
            // start the nat server
            boolean bootflg = Config.getConfigObject().getBootValue();
            if(bootflg == true) {
                NATServer ns=new NATServer();
                ns.start();
            } else {
                NATHandler nh=new NATHandler();
                nh.callNATHandler();
            }
            // debug_level.debug(0,"The private key of client is  =" + ReadVerifyCert.getKeyPair() );
            // sms_methods.choose_loc();
            // sms_send_rec_management.empty_cache_folder();
            // sms_send_rec_management.empty_rec_folder();
            IndexManagementUtilityMethods.Ip_txt_empty();
            // call objects and methods from classes of - communication
            // CommunicationManager cm= CommunicationManager.getCM(); //todo
            CommunicationManager cm= new CommunicationManager();
            cm.start();
            // Communication manager thread started. The thread will have buffers to keep incoming messages
            // which can be read by various modules (RTManager to update Routing Tables in all the DHT
            // layers,)

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                //RTManager rm = RTManager.getRTMgr(); //TODO
                //rm.start();//TODO
                RTManager.initiateRT();
                //Save_Retrive_data_Structures.Save_nodeFileChunkMap();
//                 Save_Retrive_data_Structures.Save_nodefilemap();
                //               Save_Retrive_data_Structures.Save_root_Fileinfo_Map();
                //             Save_Retrive_data_Structures.Save_shared_Fileinfo_Map();
                //           Save_Retrive_data_Structures.Retrive_nodeFileChunkMap();
                //         Save_Retrive_data_Structures.Retrive_nodefilemap();
                //       Save_Retrive_data_Structures.Retrive_root_Fileinfo_Map();
                //     Save_Retrive_data_Structures.Retrive_shared_Fileinfo_Map();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            OverlayManagement.nodeStartUp();
            //    OverlayManagement olay = OverlayManagement.getInstance(); //ToDo
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

            /*   sms_retrival_thread sms= new sms_retrival_thread();
               sms.start();

               DistFileSys dfs = new DistFileSys();
               dfs.start();
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e1) {
                   // TODO Auto-generated catch block
                   e1.printStackTrace();
               }
            */
            B4services.service();
            // start user specific services
            // user specific DFS mount service,
            // call objects and methods from classes of - routing and overlay mangement
            // All generic services Interface
            // VOIP call, storage services, messaging service
            // }
            DFSUI.essentials();
        }
        while(globj.getRunStatus()) {
            Thread.sleep(30000);
        }
    }

    private static void setFlagforcert() {
        try {
            flagset = ReadVerifyCert.verifyCert();
            // check if there is valid certificate is present in the keystore of of the client.
            // In case of invalid certificate, new certificate generation and identity verification should be done.
            client_cert = ReadVerifyCert.returnClientCert();
            server_cert = ReadVerifyCert.returnServerCert();
            //debug_level.debug(0,"clientcertsaved is =" + client_cert );
            //debug_level.debug(0,"servercertsaved is =" + server_cert );
            String email_id=emailid.getemaild();
            debug_level.debug(0,"My Email-Id is =" + email_id );
        }
        catch(CertificateException e) {
            System.out.println("Exception in read and verify certifcate - setFlagforcert()-ClientMain "+e);
            e.printStackTrace();
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
