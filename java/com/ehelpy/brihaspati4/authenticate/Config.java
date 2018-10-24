package com.ehelpy.brihaspati4.authenticate ;


/* License -
* Design - YNSingh (2016)
* Implementation - Seemanti (2016), Chetna (2016)
* Date, Change description - Get the configuration parameters.
* Copyright 2016 : YNSingh
*/

import java.io.File;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

//import com.ehelpy.brihaspati4.security;

@SuppressWarnings("unused")
public class Config {

    private static Config configObject;
// Config initialization from configuration file during object creation by the constructor of Config.
// Config_object will keep the data after reading from configuration file.
// On each change, the data should be written back to config file also. It implies, in each
// write api, write to config file on disk is to be implemented.
// Configuration file will reside in location from where the jar of Brihaspati4 will be started.
// {$user_home}/brihaspati4/B4conf.properties will be file holding the configuration.

// location of security certificate for local client inside the {$user_home}/brihaspati4.
// example {$user_home}/brihaspati4/certstore
//	{$user_home}/brihaspati4/keystore will contain the private key and certificate ID couplet.
//	It will be encrypted with keystore password.

// location of routing table in local disk storage for client. It is relative to $user_home.
// example {$user_home}/brihaspati4/routetable.txt

    //private String home_dir;
    // retrieve the absolute path of home directory;
    private String home_dir = System.getProperty("user.home");
    private String path = home_dir+"/B4/src/java/com/ehelpy/brihaspati4"+"/"+"B4conf.properties";

    private String SCSS_uri;
    // URI for secure certificate signing service access for local client.
    // example - https://scss.iitk.ac.in:8443/b4/servlet/certserv

    private boolean Storage_service_flag;
    // True - provide service to others, False - not to be provided.

    private boolean SandBox_VM_service_flag;
    // True - for providing VM service, False - not available.

    private boolean proxy_service;
    // True - can act as proxy for others, False - proxy function turned off.

    private int TargetIncomingTableBufferSize=5;
    // Incoming Tables buffer target size. All the incoming
    // routing tables will be bufferred. But if the size is more than this, the sleep time will be
    // reduced, if lower than it will be increased. Minimum sleep time will be zero. Default value
    // is 5 if not given in conf.properties. We would like to update the local routing table when
    // at least tables from five neighbours are available.

    private int PingMessageWaitingTime = 60000;
    // timeout (ms) if the reply to Ping message is not received.
    // Used to monitor if the neighbour is alive or not.

    private int PeriodicPingMessageWaitingTime = 1800000;
    // timeout after which a new request of ping is sent. This is reset after a response to Ping,
    // or information that neighbour is alive is received and acknowledged.

    private boolean CertExists;
    // Boolean true if the security certificate for the user of local client
    // is available in CertLocation and is verified.

    private int UDP_port=9000, TCP_port=9001, http_port=9002, https_port=9003;
    // port on which current client will listen for incoming requests.

    private String Last_Logout_time;
    // time at which user accessed the system for the last time.

    public String keystorelocation;

    public String keystorepass;

    public static Config getConfigObject()
    {
        try
        {
            if (configObject ==null)
                configObject = new Config();
        }
        catch(Exception e) {}
        return configObject;
    }

    public Config()
    {
        try {
            //retrieve the absolute path of home directory.
            home_dir = System.getProperty("D:\\study\\studymtrl\\codes\\client");
            path = home_dir+"src/java/com/ehelpy/brihaspati4"+"/"+"B4conf.properties";

            //SCSS_uri - read from file B4conf.properties.
            SCSS_uri = getConfigParamValue(path,"SCSS.uri.value");
            System.out.println("SCSS VALUE..."+SCSS_uri);

            //Storage - read from file B4conf.properties.
            String Storage = getConfigParamValue(path,"StorageService.flag.value");
            Storage_service_flag = Boolean.parseBoolean(Storage);

            //SandBoxVMservice - read from file B4conf.properties.
            String SandBoxVMservice = getConfigParamValue(path,"SandBoxVMService.flag.value");
            SandBox_VM_service_flag = Boolean.parseBoolean(SandBoxVMservice);

            //proxy_service - read from file B4conf.properties.
            String Proxyserv = getConfigParamValue(path,"proxyService.flag.value");
            proxy_service = Boolean.parseBoolean(Proxyserv);

            //Get the path of both the Certificate and Keystore Locations.
            String certLocation = path+"/brihaspati4/cert";
            String keystorelocation = path+"/brihaspati4/keystore";

            //from certificate manager get the status if the certificate variable to the current user exits.
            //CertExists = CertManager.ReadVerifyCert(certLocation,keystorelocation);

            //Get the location of Route Table.
            //String route_table_location = path+"/brihaspati4/RouteTable";

            //read TargetIncomingTableBufferSize from B4conf.properties;
            String TargetIncomingTable_BufferSize = getConfigParamValue(path,"TargetIncomingTableBufferSize.value");
            //if IncomingTableBufferSize not present then keep default;

            // Read the transport protocol port from B4conf.properties,
            // verify and store in configObject.
            String UDPport = getConfigParamValue(path,"UDP_port.value");
            String TCPport = getConfigParamValue(path,"TCP_port.value");
            String httpport = getConfigParamValue(path,"http_port.value");
            String httpsport= getConfigParamValue(path,"https_port.value");
            // 9000, 9001, 9002 for TCP, UDP and HTTP - default; but can be changed by user.
            // These are part of URI to communicate with the peer, thus different peers can
            // use different sets.
            //Last_Logout_time - read from file B4conf.properties.
            Last_Logout_time = getConfigParamValue(path,"LastLogoutTime.value");
        }
        catch(Exception e) {}
    }

    public String getConfigParamValue(String path,String key) throws Exception
    {

        FileInputStream f = new FileInputStream(path);
        Properties p = new Properties();
        p.load(f);
        String val = null;
        val=p.getProperty(key);
        f.close();
        return(val);
    }

    public static void setConfigParamValue(String path,String Value,String key)
    {
        try
        {
            Properties props = new Properties();
            FileOutputStream outstream=null;
            props.load(new FileInputStream(path));
            props.setProperty(key,Value);
            System.out.println("set property value....");
            //For overwriting prop file use the constructor that takes as an argument the String containing the path of the file to write to and boolean parameter "false".
            outstream = new FileOutputStream(path,false);
            props.store(outstream,"B4 PROPERTIES VALUES");
            //FileOutputStream instance needs to be closed after use.
            outstream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public String getRouteTableLocation(){
       return route_table_location;
    }*/

    public boolean ifNonExpiredVerifiedCertExist() {
        return CertExists;
    }

    public boolean isStorageServ() {
        return Storage_service_flag;
    }
    public boolean SandBoxVMServ() {
        return SandBox_VM_service_flag;
    }
    public boolean isProxyServ() {
        return proxy_service;
    }
    public String getLastLogoutTime() {
        return Last_Logout_time;
    }
    public void saveLastLogoutTime(String logoutDatetime) {
        setConfigParamValue(path,logoutDatetime,"LastLogoutTime.value");
    }
}




