package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.indexmanager.SHA1;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;
//public static LinkedList<File> TempBuffer = new LinkedList<File>();
public class OverlayManagementUtilityMethods extends OverlayManagement {
    public static LinkedList<File> TempBuffer = new LinkedList<File>();

    public static Map<String, String>  newNodeCheckReply = new LinkedHashMap<String, String>();
    static volatile String[][] pingArray= null;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //SysOutCtrl.SysoutSet("hello");
        fillMyIptable();
        fillRoutingTable();
        //HeartbeatMonitoring hm= new HeartbeatMonitoring();
        HeartbeatMonitoring.heartbeatCheck();
    }



    public static void fillRoutingTable() {

        //int columns = 2;
        //int rows = 2;
        SysOutCtrl.SysoutSet("you are in fill Routing Table");
        Map<String,String> ipList = CommunicationManager.myIpTable;
        //SysOutCtrl.SysoutSet(ipList);
        Collection<String> key_extracted = ipList.keySet();			/// code to extract hash_id from array by first converting it into collection then to an array
        Object[] key_array = key_extracted.toArray();

        int a =CommunicationManager.myIpTable.size();
        SysOutCtrl.SysoutSet("value of a is:" +a);
//					if(a==0)
//					{
//						pingArray= new String[1024][2];
//						for(int i=0;i<1024;i++)
//
//						{
//						SysOutCtrl.SysoutSet("you are in for loop", 3);
//						SysOutCtrl.SysoutSet("the key array is " +key_array[i], 3);
//						SysOutCtrl.SysoutSet("the value array is " +ipList.get(key_array[i]), 3);
//						String nodeId = (String) key_array[i];
//						String ipAddress = ipList.get(key_array[i]);
//						SysOutCtrl.SysoutSet(""+nodeId+""+ipAddress, 3);
//						pingArray[i][0] = nodeId;   // second argument will extract value iro this key i.e. self_node_id
//						pingArray[i][1] = ipAddress;   // second argument will extract value iro this key i.e. self_node_id
//							SysOutCtrl.SysoutSet("you are at end of for loop", 3);
//
//
//							SysOutCtrl.SysoutSet("pingArrayCopiedFrommMyIpTable");
//							for(int j = 0; j<pingArray.length;i++) {
//								SysOutCtrl.SysoutSet("iptable"  +pingArray[i][1] , 3);
//						}
//						}
//					}
//					else
//					{
        pingArray= new String[a][2];
        SysOutCtrl.SysoutSet("size of pingarray is:" +pingArray.length);

        for(int i=0; i<a; i++)

        {
            SysOutCtrl.SysoutSet("you are in for loop", 3);
            SysOutCtrl.SysoutSet("the key array is " +key_array[i], 3);
            SysOutCtrl.SysoutSet("the value array is " +ipList.get(key_array[i]), 3);
            String nodeId = (String) key_array[i];
            String ipAddress = ipList.get(key_array[i]);
            SysOutCtrl.SysoutSet(""+nodeId+""+ipAddress, 3);
            pingArray[i][0] = nodeId;   // second argument will extract value iro this key i.e. self_node_id
            pingArray[i][1] = ipAddress;   // second argument will extract value iro this key i.e. self_node_id
            SysOutCtrl.SysoutSet("you are at end of for loop", 3);


//							SysOutCtrl.SysoutSet("pingArrayCopiedFrommMyIpTable");
//							for(int j = 0; j<a;i++) {
//								SysOutCtrl.SysoutSet("iptable"  +pingArray[i][1] , 3);
//						}
//						}
        }

        SysOutCtrl.SysoutSet("pingArrayCopiedFrommMyIpTable");
        for(int j = 0; j<a; j++) {
            SysOutCtrl.SysoutSet("iptable"  +pingArray[j][1] , 3);
        }

//					}
        SysOutCtrl.SysoutSet(" the key array length is " +key_array.length);
//					SysOutCtrl.SysoutSet("len :"+key_array.length, 2);
//					for(int i=0;i<a;i++)
//
//						{
//						SysOutCtrl.SysoutSet("you are in for loop", 3);
//						SysOutCtrl.SysoutSet("the key array is " +key_array[i], 3);
//						SysOutCtrl.SysoutSet("the value array is " +ipList.get(key_array[i]), 3);
//						String nodeId = (String) key_array[i];
//						String ipAddress = ipList.get(key_array[i]);
//						SysOutCtrl.SysoutSet(""+nodeId+""+ipAddress, 3);
//						pingArray[0][0] = "abcd";//nodeId;   // second argument will extract value iro this key i.e. self_node_id
//						pingArray[0][1] = "efgh";//ipAddress;   // second argument will extract value iro this key i.e. self_node_id
//							SysOutCtrl.SysoutSet("you are at end of for loop", 3);
//
//
//							SysOutCtrl.SysoutSet("pingArrayCopiedFrommMyIpTable");
//							for(int j = 0; j<pingArray.length;i++) {
//								SysOutCtrl.SysoutSet("iptable"  +pingArray[i][1] , 3);
//						}
//						}
    }
    public static String searchNodeId(String newNodeId) {
        String reply="false";
        SysOutCtrl.SysoutSet("NewNodeId "+newNodeId+" received by Bootstrap for checking its availablity: ",2);

        if(IndexManagement.myindex.containsValue(newNodeId))
        {   reply="true";
            SysOutCtrl.SysoutSet("NewNodeId exists in myIndex ",2);

            return reply;
        }

        else

        {

            newNodeCheckReply.put(newNodeId, "false");
            SysOutCtrl.SysoutSet("setting newNodeSearchReplyReceived flag to false ",2);
            SysOutCtrl.SysoutSet("creating newNodeSearchQuery.xml with Tag=0205 ",2);
            File searchNewNodeQuery=createXmlNewNodeSearchQuery(newNodeId);
            CommunicationManager.TransmittingBuffer.add(searchNewNodeQuery);


            SysOutCtrl.SysoutSet("waiting newNodeSearchReplyReceived flag to be true ",2);
            while(!newNodeSearchReplyReceived==true)
            {
                SysOutCtrl.SysoutSet("please wait");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            SysOutCtrl.SysoutSet("ip from search query reply "+newNodeCheckReply.get(newNodeId),1);

            {   // to display a user with a messege box
                JLabel frame = new JLabel("IP_Address");
                JFrame frame1 = new JFrame("Message");
                //show a joptionpane dialog using showMessageDialog
                JOptionPane.showMessageDialog(frame1, "Searched IP address is "+newNodeCheckReply.get(newNodeId));
            }
            return newNodeCheckReply.get(newNodeId);




        }
    }
    private static File createXmlNewNodeSearchQuery(String newNodeId) {
        // TODO Auto-generated method stub
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0205"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(newNodeId);
        //String nextHopNodeId = GiveNextHop.NextHop(key);// next hop will be given by routing module
        Text t2 = doc.createTextNode("NewNodeSearchQuery");
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = IndexManagementUtilityMethods.getMyIp();
        //String selfIp = "129999999";
        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222";
        Text t5 = doc.createTextNode(selfPortNo);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileOutputStream("NewNodeSearchQuery.xml"));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        File file1 = new File("NewNodeSearchQuery.xml");

        SysOutCtrl.SysoutSet("NewNodeSearchQuery.xml file Generated.",2);

        return file1;

    }



    public static void fillMyIptable() {

        if(com.ehelpy.brihaspati4.routingmgmt.GetProperties.Backbone)
        {
            CommunicationManager.myIpTable.put(BootstrapNodeId,BootstrapIP);
            CommunicationManager.myIpTable.put(com.ehelpy.brihaspati4.routingmgmt.GetProperties.Property_NodeId1,com.ehelpy.brihaspati4.routingmgmt.GetProperties.Property_IP1);
            CommunicationManager.myIpTable.put(com.ehelpy.brihaspati4.routingmgmt.GetProperties.Property_NodeId2,com.ehelpy.brihaspati4.routingmgmt.GetProperties.Property_IP2);
//   CommunicationManager.myIpTable.put("3333333333333333333333333333333333333333","192.168.43.32");
            //CommunicationManager.myIpTable.put("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","127.0.0.3");
            //CommunicationManager.myIpTable.put("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD","127.0.0.4");
            //CommunicationManager.myIpTable.put("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF","127.0.0.4");
//	   CommunicationManager.myIpTable.put("6666666666666666666666666666666666666666","172.26.187.168");
//			   CommunicationManager.myIpTable.put("9999999999999999999999999999999999999999","172.26.187.73");

            SysOutCtrl.SysoutSet("filled myIpTable"+CommunicationManager.myIpTable, 2);
        }
        CommunicationManager.myIpTable.put(BootstrapNodeId,BootstrapIP);
        SysOutCtrl.SysoutSet("filled myIpTable"+CommunicationManager.myIpTable, 2);
    }

    public static boolean updateMyPredecessors() {
        boolean myPredecessorsChanged=false;
        {
//	 		String[] myPredecessorsUpdated = PredecessorSuccessor.myPredecessors;
//
//	 		for(int j = 0;j<myPredecessorsUpdated.length;j++)
//	 		{
//
//	 			if(!PredecessorSuccessor.myPredecessors[j].equals(myPredecessorsUpdated[j])) {
//	 				PredecessorSuccessor.myPredecessors[j] = myPredecessorsUpdated[j];
            OverlayManagement.flagMyPredecessorsUpdatedForIndexManager = true;
            OverlayManagement.flagMyPredecessorsUpdatedForRoutingManager = true;
            myPredecessorsChanged=true;
        }
//	 			else {
//	 				SysOutCtrl.SysoutSet("No change in predecessors.");
//	 			}
//	 		}


        return myPredecessorsChanged;
    }



    public static boolean updateMySuccessors () {
        boolean mySuccessorsChanged=false;
        {

//			String[] mySuccessorsUpdated = PredecessorSuccessor.mySuccessors;
//
//			for(int j = 0;j<mySuccessorsUpdated.length;j++) {
//				if(!PredecessorSuccessor.myPredecessors[j].equals(mySuccessorsUpdated[j]))
//				{
//					PredecessorSuccessor.myPredecessors[j]=mySuccessorsUpdated[j];
            OverlayManagement.flagMySuccessorsUpdatedForIndexManager = true;
            OverlayManagement.flagMySuccessorsUpdatedForRoutingManager = true;
            mySuccessorsChanged=true;
//				}
//				else {
//					SysOutCtrl.SysoutSet("No change in Successors.");
//				}
        }

        return mySuccessorsChanged;

    }



    public static File convert_hashmap_toxml(Map<String, String> myindex1,String to_hash_id, String toNodeId,String selfNodeId,String selfIp, String self_port_no) throws FileNotFoundException, TransformerException, ParserConfigurationException
    {


        SysOutCtrl.SysoutSet("Indextable " + myindex1);
        Collection<String> hash_id_extracted = myindex1.keySet();			/// code to extract hash_id from array by first converting it into collection then to an array
        Object[] hashid_array = hash_id_extracted.toArray();

        Collection<String> node_id_extracted = myindex1.values();/// code to extract node_id from array by first converting it into collection then to an array
        Object[] nodeid_array = node_id_extracted.toArray();

        /*	for(int i=0;i<hashid_array.length;i++)
        		SysOutCtrl.SysoutSet(hashid_array[i]);

        	for(int j=0;j<nodeid_array.length;j++)
        		SysOutCtrl.SysoutSet(nodeid_array[j]);


        	SysOutCtrl.SysoutSet(myindex1.keySet());
        	SysOutCtrl.SysoutSet(myindex1.values());*/

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("XML");
        doc.appendChild(rootele);
        Element tagidele = doc.createElement("Query_");
//	 		Element hashidele = doc.createElement("hash_id");
//	 		Element tonodeidele = doc.createElement("to_node_id");
//	 		Element fromnodeidele = doc.createElement("from_node_id");
//	 		Element ipele = doc.createElement("ip_address");
//	 		Element portele = doc.createElement("port_no");

        //String[] tag = {"0000", "0001"};
        //Random random = new Random();

        // int select = random.nextInt(tag.length); 			/// randomly chooses the string index from the string array

        //String tagvalue = tag [select];						// use random index to store corresponding string value in another string

        String tagvalue="0032";
        SysOutCtrl.SysoutSet("Tagvalue for indexingQuery: " + tagvalue);			// prints out the value at the randomly selected index

        ((Element)tagidele).setAttribute("tag", tagvalue);
        rootele.appendChild(tagidele);
        int i=0;
        //SysOutCtrl.SysoutSet("hashid array length " + hashid_array.length);
        for(int j=0; j<hashid_array.length; j++)
        {
            Element codeele = doc.createElement("indexentries");
            tagidele.appendChild(codeele);
            ((Element)codeele).setAttribute("record_no", Integer.toString(i));			// here using i as tagvalue
            i++;

            Element hashidele = doc.createElement("hash_id");
            hashidele.appendChild(doc.createTextNode((String) hashid_array[j]));
            codeele.appendChild(hashidele);

            Element nodeidele = doc.createElement("node_id");
            nodeidele.appendChild(doc.createTextNode((String) nodeid_array[j]));
            codeele.appendChild(nodeidele);

        }
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        Text t0 = doc.createTextNode(to_hash_id);
        Text t1 = doc.createTextNode(toNodeId);
        Text t2 = doc.createTextNode(selfNodeId);
        Text t3 = doc.createTextNode(selfIp);

        Text t4 = doc.createTextNode(self_port_no);

        hashidele.appendChild(t0);
        tonodeidele.appendChild(t1);

        selfnodeidele.appendChild(t2);
        ipele.appendChild(t3);
        portele.appendChild(t4);

        tagidele.appendChild(hashidele);
        tagidele.appendChild(tonodeidele);
        tagidele.appendChild(selfnodeidele);
        tagidele.appendChild(ipele);
        tagidele.appendChild(portele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new FileOutputStream("ipTableReply.xml"));
        transformer.transform(source, result);
        SysOutCtrl.SysoutSet("ipTableReply generated");




        return (new File("ipTableReply.xml"));
    }





    public static Map<String, String> convertXmlToIpTable(File inFile) {
        //private static Map<String, String> convertXmlToIndexTable(File inFile) {

        // TODO Auto-generated method stub
        Map<String, String> tempIpTable= new LinkedHashMap<String, String>();
        // this method should convert the incoming xml file to myindex9hashmap)
        SysOutCtrl.SysoutSet("you are in convertXmlToIpTable method");
        try {

            //File fxmlFile = filetoparse;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inFile);
            //	Element rootElement = doc.getDocumentElement();
            //	SysOutCtrl.SysoutSet("root element " + rootElement.getNodeName());
            //	SysOutCtrl.SysoutSet("---------------------  " + rootElement.getAttribute());

            NodeList nlist = doc.getElementsByTagName("indexentries");
            //		NodeList nlist = doc.getElementsByTagName("Request_for_Indexing");
            //		SysOutCtrl.SysoutSet("---------------------  " + nlist.getLength());
            // int nlistlength= nlist.getLength();
            //SysOutCtrl.SysoutSet("nlist lenght"+nlistlength);
            for(int i=0; i<nlist.getLength(); i++)

            {
                //SysOutCtrl.SysoutSet("for loop i "+i);
                Node nNode = nlist.item(i);


                //	SysOutCtrl.SysoutSet("current element selected from xml: " + nNode.getNodeName());
                //SysOutCtrl.SysoutSet("nodetype " + nNode.getNodeType());
                //SysOutCtrl.SysoutSet("ELEMENT NODE " + Node.ELEMENT_NODE);

                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;					//SysOutCtrl.SysoutSet(eElement.getAttribute("id"));

                    String record_no = eElement.getAttribute("record_no");
                    //	SysOutCtrl.SysoutSet();
                    NodeList nodeList = eElement.getChildNodes();
                    for(int x=0; x< 2; x++) 			// to get  tag value from each xml file.
                    {
                        //	SysOutCtrl.SysoutSet("for loop x "+x);
                        Node n= nodeList.item(x);
                        if(n.getNodeType()==Node.ELEMENT_NODE) {
                            Element name=(Element) n;
                            //	SysOutCtrl.SysoutSet("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());


//							SysOutCtrl.SysoutSet(nodeList.item(x).getAttributes().getNamedItem("record_no").getNodeValue());

//							details[0] = nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue();
//							details[1] = eElement.getElementsByTagName("to_hash_id").item(0).getTextContent();				//	SysOutCtrl.SysoutSet("Reading hash_id in readxmlfile : " + details);
//							details[2] = eElement.getElementsByTagName("to_node_id").item(0).getTextContent();				//	SysOutCtrl.SysoutSet("Reading node_id in readxmlfile : " + nodeid);
//							details[3] = eElement.getElementsByTagName("self_node_id").item(0).getTextContent();

                            String key = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
                            String value = eElement.getElementsByTagName("node_id").item(0).getTextContent();

//							details[4] = eElement.getElementsByTagName("self_ip_address").item(0).getTextContent();
//							details[5] = eElement.getElementsByTagName("self_port_no").item(0).getTextContent();
//							SysOutCtrl.SysoutSet("tag: "+details[0]);
//							SysOutCtrl.SysoutSet("to_hash_id"+details[1]);
//							SysOutCtrl.SysoutSet("to_node_id"+details[2]);
//							SysOutCtrl.SysoutSet("self_node_id"+details[3]);
//							SysOutCtrl.SysoutSet("ip_address"+details[4]);
//							SysOutCtrl.SysoutSet("port_no"+details[5]);


                            tempIpTable.put(key, value);
                            SysOutCtrl.SysoutSet("record_no: "+record_no+"   hashId: "+key+"  nodeId: "+value);
                            //SysOutCtrl.SysoutSet();
                        }

                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //SysOutCtrl.SysoutSet("\n");
        //	SysOutCtrl.SysoutSet("tag value is " + details[0]);
        //	SysOutCtrl.SysoutSet("Parsing XML file " + filetoparse +"  and Returning hash_id from ReadXmlFile class to IndexManagement Class  : " + details[1]  + " : its corresponding nodeid is : "+ details[2] );
//				return details;
        return tempIpTable;
    }





    public static File searchNewNodeReply(String to_hash_id, String to_node_id,String self_node_id,String self_ip_address,String self_port_number ) throws TransformerException, ParserConfigurationException, FileNotFoundException


    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        String tagvalue = "0305";
        ((Element)codeele).setAttribute("tag", tagvalue);
        Text t1 = doc.createTextNode(to_hash_id);
        //String nextHopNodeId=to_hash_id;// next hop will be given by routing module
        Text t2 = doc.createTextNode(to_node_id);
        //String selfNodeId=self_node_id;

        Text t3 = doc.createTextNode(self_node_id);
        //String selfIp= self_ip_address;

        Text t4 = doc.createTextNode(self_ip_address);
        String selfPortNo= self_port_number;
        Text t5 = doc.createTextNode(selfPortNo);
        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new  FileOutputStream("NewNodeSearchReply.xml"));
        transformer.transform(source, result);
        StreamResult file = new StreamResult(new File("Documents"));
        SysOutCtrl.SysoutSet("NewNodeSearchReply.xml file generated", 2);

        return (new File("NewNodeSearchReply.xml"));
    }


    public static File getIpTableRequest(String to_hash_id, String to_node_id,String self_node_id,String self_ip_address,String self_port_number ) throws TransformerException, ParserConfigurationException, FileNotFoundException


    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        String tagvalue = "0022";
        ((Element)codeele).setAttribute("tag", tagvalue);
        Text t1 = doc.createTextNode(to_hash_id);
        //String nextHopNodeId=to_hash_id;// next hop will be given by routing module
        Text t2 = doc.createTextNode(to_node_id);
        //String selfNodeId=self_node_id;

        Text t3 = doc.createTextNode(self_node_id);
        //String selfIp= self_ip_address;

        Text t4 = doc.createTextNode(self_ip_address);
        //String selfPortNo= self_port_number;
        Text t5 = doc.createTextNode(self_port_number);
        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new  FileOutputStream("getIpTableRequest.xml"));
        transformer.transform(source, result);
        //StreamResult file = new StreamResult(new File("Documents"));
        SysOutCtrl.SysoutSet("getIpTableRequest file generated", 2);

        return (new File("getIpTableRequest.xml"));
    }

    public static void sendTag22ToBootStrap() {
        // TODO Auto-generated method stub
        File getIpTableRequestFile=null;

        while(!UpdateIP.Connected)
        {
            SysOutCtrl.SysoutSet("Aquiring IP for OM");
        }
        String MyIP = PresentIP.MyPresentIP();
        try {
            getIpTableRequestFile = OverlayManagementUtilityMethods.getIpTableRequest(OverlayManagement.BootstrapNodeId, "getIpTableRequest",myNodeId , MyIP, "2222");
        } catch (FileNotFoundException | TransformerException | ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sendFileDirect(OverlayManagement.BootstrapIP,getIpTableRequestFile);


    }
    public static void sendFileDirect(String bootstrapIP2, File newNodeXml) {
        // TODO Auto-generated method stub
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(newNodeXml.toString()));

            String s = "", line = null;
            try {
                while ((line = r.readLine()) != null) {
                    s += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String toIp = bootstrapIP2; // calling function to get the ip address of the root node id
            SysOutCtrl.SysoutSet("contacting neighbour for table exchange " );

            fileSend(toIp, s);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void fileSend(String toNodeIp, String s) {
        SysOutCtrl.SysoutSet("you are in fileSender method",2);
        SysOutCtrl.SysoutSet("Temp buffer status: "+TempBuffer);
        int port = 2222;
        Socket sock = null;
        try {
            sock = new Socket(toNodeIp, port);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            SysOutCtrl.SysoutSet("1111");
            //e1.printStackTrace();
            SysOutCtrl.SysoutSet("check connection");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            SysOutCtrl.SysoutSet("Connection timed out: Please check your connectivity with"+ toNodeIp,1);

            //fileSender(toNodeIp, s);
            //e1.printStackTrace();

        }

//		if (!sock.isConnected())
//			SysOutCtrl.SysoutSet("Socket Connection Not established",2);
//		else
//			SysOutCtrl.SysoutSet("Socket Connection established : " + sock.getInetAddress(),1);

        byte[] mybytearray = s.getBytes();
        //SysOutCtrl.SysoutSet("file lenght before sending of" + s.toString() + ":" + s.length());
        BufferedInputStream bis = null;

        OutputStream os = null;
        try {
            // bis.read(mybytearray, 0, mybytearray.length);
            //SysOutCtrl.SysoutSet(s.toString() + "bytes sent" + mybytearray.length);
            os = sock.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            // bis.close();
            sock.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            SysOutCtrl.SysoutSet("CHECK CONNECTION");
        } catch (NullPointerException e1) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            SysOutCtrl.SysoutSet("CHECK CONNECTION-NPE");
        }
    }



//	 public static String[][] convertXmlToipTableArray(File inFile) {
//			//private static Map<String, String> convertXmlToIndexTable(File inFile) {
//
//				// TODO Auto-generated method stub
//			String[][] tempIpList = new String[2][2];
//				// this method should convert the incoming xml file to myindex9hashmap)
//				SysOutCtrl.SysoutSet("you are in convertXmlToipTable method");
//				try{
//
//					//File fxmlFile = filetoparse;
//					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//					DocumentBuilder db = dbf.newDocumentBuilder();
//					Document doc = db.parse(inFile);
//				//	Element rootElement = doc.getDocumentElement();
//				//	SysOutCtrl.SysoutSet("root element " + rootElement.getNodeName());
//				//	SysOutCtrl.SysoutSet("---------------------  " + rootElement.getAttribute());
//
//					NodeList nlist = doc.getElementsByTagName("indexentries");
//			//		NodeList nlist = doc.getElementsByTagName("Request_for_Indexing");
//			//		SysOutCtrl.SysoutSet("---------------------  " + nlist.getLength());
//			  // int nlistlength= nlist.getLength();
//			//SysOutCtrl.SysoutSet("nlist lenght"+nlistlength);
//					for(int i=0; i<nlist.getLength(); i++)
//
//					{
//						//SysOutCtrl.SysoutSet("for loop i "+i);
//						Node nNode = nlist.item(i);
//
//
//				//	SysOutCtrl.SysoutSet("current element selected from xml: " + nNode.getNodeName());
//					//SysOutCtrl.SysoutSet("nodetype " + nNode.getNodeType());
//					//SysOutCtrl.SysoutSet("ELEMENT NODE " + Node.ELEMENT_NODE);
//
//						if(nNode.getNodeType() == Node.ELEMENT_NODE)
//						{
//							Element eElement = (Element) nNode;					//SysOutCtrl.SysoutSet(eElement.getAttribute("id"));
//
//							String record_no = eElement.getAttribute("record_no");
//						//	SysOutCtrl.SysoutSet();
//							NodeList nodeList = eElement.getChildNodes();
//							for(int x=0;x< 2; x++) 			// to get  tag value from each xml file.
//							{
//							//	SysOutCtrl.SysoutSet("for loop x "+x);
//								Node n= nodeList.item(x);
//								if(n.getNodeType()==Node.ELEMENT_NODE) {
//									Element name=(Element) n;
//								//	SysOutCtrl.SysoutSet("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());
//
//
////							SysOutCtrl.SysoutSet(nodeList.item(x).getAttributes().getNamedItem("record_no").getNodeValue());
//
////							details[0] = nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue();
////							details[1] = eElement.getElementsByTagName("to_hash_id").item(0).getTextContent();				//	SysOutCtrl.SysoutSet("Reading hash_id in readxmlfile : " + details);
////							details[2] = eElement.getElementsByTagName("to_node_id").item(0).getTextContent();				//	SysOutCtrl.SysoutSet("Reading node_id in readxmlfile : " + nodeid);
////							details[3] = eElement.getElementsByTagName("self_node_id").item(0).getTextContent();
//							String nodeid = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
//							String ipadd = eElement.getElementsByTagName("node_id").item(0).getTextContent();
////							details[4] = eElement.getElementsByTagName("self_ip_address").item(0).getTextContent();
////							details[5] = eElement.getElementsByTagName("self_port_no").item(0).getTextContent();
////							SysOutCtrl.SysoutSet("tag: "+details[0]);
////							SysOutCtrl.SysoutSet("to_hash_id"+details[1]);
////							SysOutCtrl.SysoutSet("to_node_id"+details[2]);
////							SysOutCtrl.SysoutSet("self_node_id"+details[3]);
////							SysOutCtrl.SysoutSet("ip_address"+details[4]);
////							SysOutCtrl.SysoutSet("port_no"+details[5]);
//
//
//
//							tempIpList[i][0] = nodeid;
//							tempIpList[i][1] = ipadd;
//								SysOutCtrl.SysoutSet(tempIpList);
//
//
//							//SysOutCtrl.SysoutSet();
//						}
//
//					}}}
//				}
//				catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//			//SysOutCtrl.SysoutSet("\n");
//			//	SysOutCtrl.SysoutSet("tag value is " + details[0]);
//			//	SysOutCtrl.SysoutSet("Parsing XML file " + filetoparse +"  and Returning hash_id from ReadXmlFile class to IndexManagement Class  : " + details[1]  + " : its corresponding nodeid is : "+ details[2] );
////				return details;
//				return tempIpList;
//			}
}

/*public class Xml_ip {
public static String[][] convert_to_ip(String Inputfile) {
	String [][] Output_RT = new String[2][2];
	try {
		File inputFile = new File(Inputfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		XPathFactory xPathFactory =  XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		List<String> nodes = getNodeid(doc, xpath, "Nodeid");
		SysOutCtrl.SysoutSet("list of nodeids:" + Arrays.toString(nodes.toArray()));
		List<String> ips = getIpadd(doc, xpath, "Nodeid");
		SysOutCtrl.SysoutSet("list of ips:" + Arrays.toString(ips.toArray()));
	} catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
    }


		//NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc,XPathConstants.NODESET);
	return Output_RT;

}
	private static List<String> getIpadd(Document XML, XPath xpath, String colmn2) {
	// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                xpath.compile("/XML/Query/indexentries[hash_id='']/hash_id/text()");
            //evaluate expression result on XML document
            String list = Element.getElementsByTagName("hash_id").item(0).getTextContent();
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
	return list;
}
	public static List<String> getNodeid(Document XML, XPath xpath, String colmn1) {
	// TODO Auto-generated method stub
	return null;
}*/
