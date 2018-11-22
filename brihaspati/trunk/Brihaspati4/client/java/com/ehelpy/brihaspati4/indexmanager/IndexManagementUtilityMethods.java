package com.ehelpy.brihaspati4.indexmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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

import com.ehelpy.brihaspati4.authenticate.emailid;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;

//Sqn ldr Deepak Sharma Dated 15 Apr 2018 ; 1255 Hrs
// this class contains all the important functions used by index manager module

public class IndexManagementUtilityMethods extends IndexManagement
{
    static int interval= 10;
    static Timer timer;

    public static void addIndexRequest(String key, String value)
    {   // this method add index entry
        if(key.equals(OverlayManagement.myNodeId))
        {
            myIndexTable(key, value);
            SysOutCtrl.SysoutSet(" HashId stored at self node being responsible node");
        }


        else if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,key))
        {

            SysOutCtrl.SysoutSet("my node id is smaller than pred");

            myIndexTable(key, value);
            SysOutCtrl.SysoutSet(" HashId stored at self node being responsible node");
        }

        else
        {
            File file = IndexManagementUtilityMethods.createXmlAddIndexQuery(key, value);
            CommunicationManager.TransmittingBuffer.add(file);
        }
    }

    public static void Change_In_IP_Check()
    {
        while (CheckIP==true)
        {
            IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
            if(!MyIP.equals(IPAdd))
            {
                CheckIP=false;
            }
        }

        System.out.println("MY old IP was : "+MyIP);

        MyIP = IPAdd;

        String email_hash = selfHashId;
        String myNodeId = OverlayManagement.myNodeId;

        IndexManagementUtilityMethods.addIndexRequest(email_hash, myNodeId);

        Collection<String> Node_id_extracted = CommunicationManager.myIpTable.keySet();
        Object[] Nodeid_array = Node_id_extracted.toArray();

        for(int i=0; i<Nodeid_array.length; i++)
        {
            String Node_id= (String) Nodeid_array[i];
            System.out.println(" "+Node_id);
            File IPUpdate = IndexManagementUtilityMethods.createXmlUpdate_IP(Node_id);
            CommunicationManager.TransmittingBuffer.add(IPUpdate);
            SysOutCtrl.SysoutSet("Updated IP file sent to TransmittingBuffer :"+Node_id, 2);
            SysOutCtrl.SysoutSet("Tx Buffer state vis at UpdateIP :"+CommunicationManager.TransmittingBuffer, 2);
        }

        System.out.println("Updating my new Ip at Resposible Node and nodes present in my nodelist.");
        System.out.println("My New IP is : "+MyIP);

        CheckIP=true;

        Change_In_IP_Check();
    }

    public static void Add_in_Index(String[] xml_info) // xml_info[1]=hashid from readxmlfile
    {   /// advertisement to enter it into index table
        // // Here, the received query with tag value = 0002 means advertisement
        // then this node will put its value in its index table, if
        // (a) it is exact match, root_node_id.equals(xml_info[1]) or
        // (b) it is between self(incl) and first predecessor (excl)
        // How to get self node id;

        SysOutCtrl.SysoutSet("in add in index method for adding index entry",2);
        String self_node_id = OverlayManagement.myNodeId; // here calling a function myNodeId from the routing
        // table who will return node's node_id.
        String[] predecessor = PredecessorSuccessor.myPredecessors; // here calling a function getmypredecessor() from
        // the routing table who will return node's
        // predecessor.
        try
        {
            if (xml_info[1].equals(self_node_id)) // it returns boolean value// Here, if it is a exact match i.e.
                // queried hash_id matching exactly with root node id
            {
                SysOutCtrl.SysoutSet("i am root node",2);
                myIndexTable(xml_info[1], xml_info[2]);
            }
            else if ((xml_info[1].compareToIgnoreCase(self_node_id) < 0)&& (xml_info[1].compareToIgnoreCase(predecessor[4]) > 0))
                SysOutCtrl.SysoutSet("i am responsible node for this add request",2);
            // Here, if the queried hash_id is not exactly matching with root node id

            // then we need to compare it as follows :-
            // (a) first it should be smaller than root node means before it
            // (xmldetail1[1].compareToIgnoreCase(root_node_id)<0)
            // (b) and should be greater than its first predecessor
            // (xmldetail1[1].compareToIgnoreCase(predecessor)>0)
            // if it satisfies these two conditions then it lies between them then it need
            // to append that entry in index of root else discard.
            // compareToIgnoreCase Returns:a negative integer, zero, or a positive integer
            // as the specified String is greater than, equal to, or less than this String,
            // ignoring case considerations.

            myIndexTable(xml_info[1], xml_info[2]);
            SysOutCtrl.SysoutSet("myIndex is "+myindex,2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String Search_in_Index(String hashIdFromxml) // This method search an entry in index table, hashid from readxmlfile
    {
        String self_node_id = OverlayManagement.myNodeId; // here calling a function myNodeId from the routing
        // table who will return node's node_id.
        SysOutCtrl.SysoutSet("myNodeId is : " + self_node_id,3);
        String value = "";
        String hash_id = hashIdFromxml;
        try
        {
            if (hash_id.equals(self_node_id)) // it returns boolean value// Here, if it is a exact match i.e. queried
            {
                value = self_node_id;
            }
            else if (myindex.containsKey(hash_id)) // Returns true if this map contains a mapping for the specified
                // key.
            {
                value = myindex.get(hash_id);
                SysOutCtrl.SysoutSet("value of hashID from index table of search_in_index :"+value);
            }

            else if(!myindex.containsKey(hash_id))
            {
                value="NoEntryInIndexTable";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        SysOutCtrl.SysoutSet("value of hashID from index table of search_in_index b4 returning :"+value);

        return value;
    }

    final static void myIndexTable(String hash_id, String node_id) // saving my index table
    {
        SysOutCtrl.SysoutSet("index table before entry");
        SysOutCtrl.SysoutSet("myIndex"+myindex,2);
        
        myindex.put(hash_id, node_id); // here appending xml details i.e. hash_id and node_id in index

        String A =myindex.keySet().toString();
        FileWriter F=null;
        try
        {
            F = new FileWriter("MyIndexTable.txt");
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            F.write(A);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            F.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        printMap(myindex);

    }
    
    public static <K, V> void printMap(Map<K, V> map) 
	{
    	System.out.println("MY index: ");
    	System.out.println("");
		for (Map.Entry<K, V> entry : map.entrySet()) 
	    	System.out.println( entry.getKey() +" : "+entry.getValue());
	}

    public static void getIndexing() throws Exception
    {   // creating xml to get indexing from immdt successor
        // String[] mysuccessor = rout.getmysuccessors(); // asking successor details
        // from routing table to whom this request will be sent
        //String mysuccessor[] = OverlayManagement.getMyPredecessors();

        // now i hv to create a request message to send it to my successor that i need
        // those entries from its index table to whom now i am repsonsible, bcz i
        // recently joined the overlay.

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = f.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
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

        String tagvalue = "0004"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);
        String to_node_id = PredecessorSuccessor.mySuccessors[0];

        // SysOutCtrl.SysoutSet(to_node_id+"my succ 0");
        Text t1 = doc.createTextNode(to_node_id);
        // String nextHopNodeId=rout.getNextHop(");// next hop will be given by routing
        // module
        Text t2 = doc.createTextNode(selfHashId);// here instead of node id we are putting hash id so that successor can transfer my entry from his index table
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

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
        try
        {
            transformer = transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException e)
        {
            e.printStackTrace();
        }

        DOMSource source = new DOMSource(doc);
        StreamResult result = null;

        try
        {
            result = new StreamResult(new FileOutputStream("getIndexingQuery.xml"));
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            transformer.transform(source, result);
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
        }


        File file1 = new File("getIndexingQuery.xml");

        SysOutCtrl.SysoutSet("requestIndexingQuery.xml file Generated.",2);

        CommunicationManager.TransmittingBuffer.add(file1); // passing the info to addmessage method of tablebuffer
        // class
        SysOutCtrl.SysoutSet("Get indexing method sending request to immediate successor " + PredecessorSuccessor.mySuccessors[0],2);

    }

    static void UpdateIndexing()
    {   // when a node receive index table copy from predecessor, it will check its position wrt previous and update accordingly

        String[] myPredecessorsUpdated = PredecessorSuccessor.getMyPredecessors(OverlayManagement.nodeId, OverlayManagement.myNodeId);

        int index = (Arrays.asList(myPredecessorsCopy).indexOf(myPredecessorsUpdated[4]));

        switch (index)
        {
        case 0:
            SysOutCtrl.SysoutSet("immediate predecessor is same as earlier",2);
            break;

        case 1:
            SysOutCtrl.SysoutSet("immediate predecessor shifted to index 2",2);
            Map tmp = new LinkedHashMap(myindex1);
            tmp.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp);
            break;

        case 2:
            SysOutCtrl.SysoutSet("immediate predecessor shifted to index 3",2);
            Map tmp1 = new LinkedHashMap(myindex1);
            tmp1.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp1);
            tmp1 = new LinkedHashMap(myindex2);
            tmp1.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp1);
            break;

        case 3:
            SysOutCtrl.SysoutSet("immediate predecessor shifted to index 4",2);
            Map tmp11 = new LinkedHashMap(myindex1);
            tmp11.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp11);
            tmp11 = new LinkedHashMap(myindex2);
            tmp11.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp11);
            tmp11 = new LinkedHashMap(myindex3);
            tmp11.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp11);
            break;

        case 4:
            SysOutCtrl.SysoutSet("immediate predecessor shifted to index 5",2);
            Map tmp111 = new LinkedHashMap(myindex1);
            tmp111.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp111);
            tmp111 = new LinkedHashMap(myindex2);
            tmp111.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp111);
            tmp111 = new LinkedHashMap(myindex3);
            tmp111.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp111);
            tmp111 = new LinkedHashMap(myindex4);
            tmp111.keySet().removeAll(myindex.keySet());
            myindex.putAll(tmp111);
        }

        changeFlagIntimationForIndexTableUpdate = false;// manually changing the sattus after doing the required
        // update work
        changeFlagIntimationForIndexTableTransmit = true;
        // update is noticed and following actions are to be initieade
        // updating the index table -done
        // transmitting to the successors done
    }

    public static void TransmitMyIndexXmlFileToSuccessors()
    {   // create index table xml file to be forwarded to five successors

        String selfNodeId = OverlayManagement.myNodeId;
        String selfIp = getMyIp();
        String self_port_no = "2222";
        String toNodeId = "CopyOfIndexTable";

        for (int i = 0; i < 5; i++)
        {
            SysOutCtrl.SysoutSet("my successor "+i+" "+PredecessorSuccessor.mySuccessors[i]);
            String to_hash_id = PredecessorSuccessor.mySuccessors[i];
            SysOutCtrl.SysoutSet("successor[i]"+to_hash_id);
            File myIndexInXml = null;
            if(!to_hash_id.isEmpty())
            {
                try {
                    myIndexInXml = convert_hashmap_toxml(myindex, "0010",to_hash_id, selfNodeId, toNodeId, selfIp, self_port_no);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TransformerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                CommunicationManager.TransmittingBuffer.add(myIndexInXml);
            }
        }
    }

    // converting index table to xml file
    public static File convert_hashmap_toxml(Map<String, String> myindex1, String tag,String to_hash_id, String selfNodeId,
            String toNodeId, String selfIp, String self_port_no)
    throws FileNotFoundException, TransformerException, ParserConfigurationException
    {
        SysOutCtrl.SysoutSet("You are in convert HashMap to XML method " + myindex1,2);
        SysOutCtrl.SysoutSet("Index table " + myindex1,3);
        Collection<String> hash_id_extracted = myindex1.keySet(); /// code to extract hash_id from array by first
        /// converting it into collection then to an array
        Object[] hashid_array = hash_id_extracted.toArray();

        Collection<String> node_id_extracted = myindex1.values();/// code to extract node_id from array by first
        /// converting it into collection then to an array
        Object[] nodeid_array = node_id_extracted.toArray();



        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("XML");
        doc.appendChild(rootele);
        Element tagidele = doc.createElement("Query_");


        String tagvalue = tag;
        SysOutCtrl.SysoutSet("Tagvalue for indexingQuery: " + tagvalue,2); // prints out the value at the randomly selected
        // index

        ((Element) tagidele).setAttribute("tag", tagvalue);
        rootele.appendChild(tagidele);
        int i = 0;

        for (int j = 0; j < hashid_array.length; j++)
        {
            Element codeele = doc.createElement("indexentries");
            tagidele.appendChild(codeele);
            ((Element) codeele).setAttribute("record_no", Integer.toString(i)); // here using i as tagvalue
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

        Text t1 = doc.createTextNode(to_hash_id);
        Text t2 = doc.createTextNode(toNodeId);
        Text t3 = doc.createTextNode(selfNodeId);
        Text t4 = doc.createTextNode(selfIp);

        Text t5 = doc.createTextNode(self_port_no);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);

        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        tagidele.appendChild(hashidele);
        tagidele.appendChild(tonodeidele);
        tagidele.appendChild(selfnodeidele);
        tagidele.appendChild(ipele);
        tagidele.appendChild(portele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new FileOutputStream("XML.xml"));
        transformer.transform(source, result);
        SysOutCtrl.SysoutSet("HashMap converted to xml file");

        return (new File("XML.xml"));
    }

    public static File createXmlSearchQuery(String key)
    {   // creating serach query xml file from other node's index table

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

        String tagvalue = "0003"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);
        Text t2 = doc.createTextNode("SearchQuery");
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();
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
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileOutputStream("SearchQuery.xml"));
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


        File file1 = new File("SearchQuery.xml");

        SysOutCtrl.SysoutSet("SearchQuery.xml file Generated.",2);

        return file1;

    }

    public static Map<String, String> convertXmlToIndexTable(File inFile)
    {   // converting xml to index table ie hashmap

        Map<String, String> tempIndexTable = new LinkedHashMap<String, String>();
        // this method should convert the incoming xml file to myindex9hashmap)

        SysOutCtrl.SysoutSet("you are in convertXmlToIndexTable method",2);

        try {


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inFile);


            NodeList nlist = doc.getElementsByTagName("indexentries");
            System.out.println("nlist lenght"+nlist.getLength());

            for (int i = 0; i < nlist.getLength(); i++)
            {
                System.out.println("for loop i "+i);
                Node nNode = nlist.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode; // System.out.println(eElement.getAttribute("id"));

                    String record_no = eElement.getAttribute("record_no");

                    NodeList nodeList = eElement.getChildNodes();

                    for (int x = 0; x < 2; x++) // to get tag value from each xml file.
                    {
                        Node n = nodeList.item(x);
                        if (n.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element name = (Element) n;
                            SysOutCtrl.SysoutSet("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());
                            String key = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
                            String value = eElement.getElementsByTagName("node_id").item(0).getTextContent();
                            tempIndexTable.put(key, value);
                            SysOutCtrl.SysoutSet("record_no: " + record_no + "   hashId: " + key + "  nodeId: " + value,2);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempIndexTable;
    }

    public static Map<String, String> generateIndexingForNewlyJoinedNode(String[] info_from_xml)
    {   // generating index table form immdt newly joined predecessor

        SysOutCtrl.SysoutSet("you are in generate Indexing for newly joined node ",2);

        // now the newly joined node have its immediate successors and predecessors
        // now a series of activities will happen
        // (a) this node will check its index table to find out those key value pairs
        // for which the newly joined node is responsible
        // --- these will be those key value pairs for which the newly joined node is
        // the root node and
        // --- those key value pairs which lies between newly joined node and its
        // immediate predecessor
        // (b) these key value pairs need to be identified, placed in new index table
        // and to be handed over to newly joined node
        // (c) at same time these identified key value pairs need to be purged from the
        // existing index table.
        // (d) the index table after purging needs to be updated at successors
        // (e) the newly joined node has to send its index table to its 5 successors
        // (f) the newly joined node has to take over the responsibility as successor of
        // its immediate predecessor thus it has to take its data and maintain it

        // System.out.println("--------------------- Generate Indexing Method Starts
        // ------------------------------ ");

        // (a) this node will check its index table to find out those key value pairs
        // for which the newly joined node is responsible
        // --- these will be those key value pairs for which the newly joined node is
        // the root node and

        String newlyJoinedNodeId = info_from_xml[3];
        String newlyJoinedHashId= info_from_xml[2];// here hashid of newly joined node comes
        SysOutCtrl.SysoutSet("getIndexing request received from : " + newlyJoinedNodeId,2);

        // here when a node joins an overlay it has nothing, therefore it will first ask
        // from successor his predecessor list
        // String[] mypredecessors = olay.getmypredecessor();
        // now it will decide its immediate predecessor

        String my_immediate_predecessor = PredecessorSuccessor.myPredecessors[4];

        // and it will make the successor its immediate successor

        String my_immediate_successor = PredecessorSuccessor.mySuccessors[0]; // this will gv successor node id
        SysOutCtrl.SysoutSet("My predecessor was: " + my_immediate_predecessor,2);
        SysOutCtrl.SysoutSet("myindex.containsKey(newlyJoinedNodeId)"+myindex.containsKey(newlyJoinedNodeId),2);
        Map<String, String> fresh_index = new LinkedHashMap<String, String>();
        SysOutCtrl.SysoutSet("my index table size"+myindex.size());

        if (myindex.containsKey(newlyJoinedNodeId)) // Returns true if this map contains a mapping for the specified key.
        {   // this is for a case when the newly joined node is root node for those entries
            // which were in its successor index table due to its absence

            SysOutCtrl.SysoutSet("fresh index table before entry is " + fresh_index,2);


            fresh_index.put(newlyJoinedHashId, myindex.get(newlyJoinedHashId)); // second argument will extract value iro this key
            // i.e. self_node_id

            SysOutCtrl.SysoutSet(" index table for newly joined predecessor/node " + fresh_index,2);


            // (c) at same time these identified key value pairs need to be purged from the
            // existing index table.

            SysOutCtrl.SysoutSet("Previous my index table was " + myindex,2);
            myindex.remove(newlyJoinedHashId); // remove the identified entries from original index table
            SysOutCtrl.SysoutSet("Now my index table is " + myindex,2);
        }
        else
        {
            SysOutCtrl.SysoutSet("new node hash id entry does not exist in my table",2);
            SysOutCtrl.SysoutSet("extracting entries from my index table for his responsiblity",2);

            // (b) these key value pairs need to be identified, placed in new index table
            // and to be handed over to newly joined node
            // --- those key value pairs which lies between newly joined node and its
            // immediate predecessor

            // now is the turn for those entries in the original index table which lies
            // between newly joined node and its predecessor (earlier this was predecessor
            // to its successor)
            // -- for this we need to identify those hash (key) which are lesser than newly
            // joined node but greater than predecessor
            // -- for this we will extract keyset and convert it to array

            Collection<String> key_extracted = myindex.keySet(); /// code to extract hash_id from array by first
            /// converting it into collection then to an array
            Object[] key_array = key_extracted.toArray();

            for (int i = 0; i < key_array.length; i++)
            {
                // System.out.println(key_array[i]);
                // now we will do the comparison
                SysOutCtrl.SysoutSet(" "+((String) key_array[i]).compareToIgnoreCase(newlyJoinedNodeId));
                if(CommunicationUtilityMethods.responsibleNode(my_immediate_predecessor,newlyJoinedNodeId,(String) key_array[i]))
                {
                    fresh_index.put((String) key_array[i], myindex.get(key_array[i])); // second argument will extract
                    // value iro this key i.e.
                    // self_node_id

                    // (c) at same time these identified key value pairs need to be purged from the
                    // existing index table.
                    myindex.remove(key_array[i]); // remove the entries from original index table
                }
            }
            SysOutCtrl.SysoutSet("generated index table for newly joined node "+newlyJoinedNodeId+"is" + fresh_index,2);
            SysOutCtrl.SysoutSet("my index table size"+myindex.size());
            SysOutCtrl.SysoutSet("My index table changed to " + myindex);

        }

        return fresh_index;

        // also it will tell its successor to update its predecessor list by purging the
        // first entry i.e. predecessor[0] = predecessor[1] and so on.
        // by calling update predecessor method.
        // also it will tell its predecessor to update its successor list
        // SysOutCtrl.SysoutSet("--------------------- Generate Indexing Method Close
        // ------------------------------ ");

    }

    //generating xml search query reply
    public static void createXmlSearchQueryReply(String caller_Ip,String querried_ip, String caller_node_id, String selfNodeId, String selfIp,
            String querried_emailid_hash)
    {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder = null;
        try {
            documentbuilder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Document doc = documentbuilder.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0012"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(caller_node_id);

        // next hop will be given by routing module
        Text t2 = doc.createTextNode(querried_ip);

        // String selfNodeId1="aaaaa";

        Text t3 = doc.createTextNode(selfNodeId);

        Text t4 = doc.createTextNode(selfIp);

        Text t5 = doc.createTextNode(querried_emailid_hash);

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
        // System.out.println("1");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();

            // System.out.println("2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = null;

            try {
                result = new StreamResult(new FileOutputStream("SearchQueryReply.xml"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println("3");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            // System.out.println("4");

            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            // System.out.println("6");
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("SearchQueryReply.xml file Generated ",2);
        OverlayManagementUtilityMethods.sendFileDirect(caller_Ip, new File("SearchQueryReply.xml"));
        CommunicationManager.TransmittingBuffer.add( new File("SearchQueryReply.xml"));
        SysOutCtrl.SysoutSet("search query reply xml created and added to tx buffer",2);
        // return (new File("SearchQueryReply.xml"));

    }

    public static File createXmlAddIndexQuery(String key, String value) {// creating add index xml query

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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

        String tagvalue = "0002"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);

        Text t2 = doc.createTextNode(value);
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

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
            result = new StreamResult(new FileOutputStream("NewAddIndexQuery.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet(" NewAddIndexQuery.xml file Generated.",2);

        return (new File("NewAddIndexQuery.xml"));

    }

    public static void informNodeIdToCaller(String caller, String value, String key, boolean flag) {// at responsible node informing action to caller node
        // This method will inform the caller node the nodeId of the to_hash_id field in
        // his search query.
        // Now here caller's nodeId will be put as to_hash_id to route the query till
        // him and to_Node_id field
        // will have the desired node id
        // String nodeId=value;

        String querried_ip="yyyy";
        if(flag == true)
        {
        
        	if(value.equals(OverlayManagement.myNodeId))
        	{
        		querried_ip= getMyIp();
        	}
        
        	else
        	{ 
        		querried_ip = CommunicationManager.myIpTable.get(value);// here i am putting directly ip address of the called
        	}//String ip = OverlayManagement.myIpTable1.get(value);// here i am putting directly ip address of the called

        	SysOutCtrl.SysoutSet("ip"+querried_ip,2 );													// node.
        	// String nodeId = "IP_NOT_FOUND_AT RESP_NODE"; new
        	//	String str= null;
        	//    Optional<String> str2 = Optional.ofNullable(ip); new

        	//    if (str2.isPresent()) new
        	//	  {new
        	//         nodeId = ip; new
        	//       }new
       
        	//       SysOutCtrl.SysoutSet("destination ip not avaliable with comm mgr table", 3);
        	
        	String caller_node_id = caller;
        	// String selfNodeId=OverlayManagement.myNodeId;
        	
        	String selfNodeId = OverlayManagement.myNodeId;// putting searched hash in self node id. this needs to be put in separate filed
        	// later on.
        	String selfIp = getMyIp();
        	String querried_emailid_hash= key;
        	SysOutCtrl.SysoutSet("self ip add " + selfIp,2);
        	String caller_Ip=CommunicationManager.myIpTable.get(caller_node_id);
        	createXmlSearchQueryReply(caller_Ip, querried_ip, caller_node_id, selfNodeId, selfIp, querried_emailid_hash);
        	// CommunicationManager.TransmittingBuffer.add(searchQueryReply);
        }
        
        else
        {
        	querried_ip = value;
        	String caller_node_id = caller;
        	String selfNodeId = OverlayManagement.myNodeId;
        	String selfIp = getMyIp();
        	String querried_emailid_hash= key;
        	SysOutCtrl.SysoutSet("self ip add " + selfIp,2);
        	String caller_Ip=CommunicationManager.myIpTable.get(caller_node_id);
        	createXmlSearchQueryReply(caller_Ip, querried_ip, caller_node_id, selfNodeId, selfIp, querried_emailid_hash);
        }	
    }
    public static String getMyIp() {// getting my ip address		// TODO Auto-generated method stub
        while(!UpdateIP.Connected)
        {
            SysOutCtrl.SysoutSet("AQUIRING IP in index mgmt");
        }
        String ip = PresentIP.MyPresentIP();


        return ip;
    }

    public static void removeIndexing(String to_hash_id) {// remove index procedure..

        String self_node_id = OverlayManagement.myNodeId; // here calling a function myNodeId from the routing
        // table who will return node's node_id.
        String[] predecessor = PredecessorSuccessor.getMyPredecessors(OverlayManagement.nodeId, OverlayManagement.myNodeId); // here calling a function getmypredecessor() from
        // the routing table who will return node's
        // predecessor.
        try {
            if (to_hash_id.equals(self_node_id)) // it returns boolean value// Here, if it is a exact match i.e.
                // queried hash_id matching exactly with root node id
            {
                myindex.remove(to_hash_id);
                SysOutCtrl.SysoutSet("index entry removed for hash id "+to_hash_id);
            } else if ((to_hash_id.compareToIgnoreCase(self_node_id) < 0)
                       && (to_hash_id.compareToIgnoreCase(predecessor[4]) > 0))
                // Here, if the queried hash_id is not exactly matching with root node id
            {   // then we need to compare it as follows :-
                // (a) first it should be smaller than root node means before it
                // (xmldetail1[1].compareToIgnoreCase(root_node_id)<0)
                // (b) and should be greater than its first predecessor
                // (xmldetail1[1].compareToIgnoreCase(predecessor)>0)
                // if it satisfies these two conditions then it lies between them then it need
                // to append that entry in index of root else discard.
                // compareToIgnoreCase Returns:a negative integer, zero, or a positive integer
                // as the specified String is greater than, equal to, or less than this String,
                // ignoring case considerations.
                myindex.remove(to_hash_id);
                SysOutCtrl.SysoutSet("index entry removed for hash id "+to_hash_id);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeIndexRequest(String key, String value) {// creating remove index query for responsible node
        File file = IndexManagementUtilityMethods.createXmlRemoveIndexQuery(key, value);
        CommunicationManager.TransmittingBuffer.add(file);
    }

    private static File createXmlRemoveIndexQuery(String key, String value) {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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

        String tagvalue = "1111"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);

        Text t2 = doc.createTextNode(value);
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

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
            result = new StreamResult(new FileOutputStream("RemoveIndexQuery.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("RemoveIndexQuery.xml file Generated.",2);

        return (new File("RemoveIndexQuery.xml"));

    }

    public static String searchEmailId(String email)
    {   // search email request from auth module
        SysOutCtrl.SysoutSet("seached email "+email,2);
        String destIp=null;
        String sha1 = SHA1.getSha1(email);
        SysOutCtrl.SysoutSet("hash of email "+email+" is: "+sha1,2);
        if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,sha1))
        {
            SysOutCtrl.SysoutSet("I am responsible node for this email.",2);
            SysOutCtrl.SysoutSet("my index table"+IndexManagement.myindex,2);
            if(myindex.containsKey(sha1))
            {

                SysOutCtrl.SysoutSet("Indexting found in my index.",2);
                String destNodeId=Search_in_Index(sha1);
                SysOutCtrl.SysoutSet("destination node id is."+destNodeId,2);
                destIp=CommunicationManager.myIpTable.get(destNodeId);

                SysOutCtrl.SysoutSet("dest ip from my ip table "+destIp,1);

                // to display a user with a messege box
            }
            else
            {
                destIp="ENTRY_DOES_NOT_EXIST_AT_RESPONSIBLE_NODE";
                SysOutCtrl.SysoutSet("Indexting does not found in my index.",2);
            }

            JLabel frame = new JLabel("IP_Address");
            JFrame frame1 = new JFrame("Message");
            //show a joptionpane dialog using showMessageDialog
            JOptionPane.showMessageDialog(frame1, "Searched IP address is "+destIp);

            return destIp;
        }

        else
        {
            SysOutCtrl.SysoutSet("I am not a responsible node for this email.",2);
            SysOutCtrl.SysoutSet("creating search query.",2);
            emailSha1.put(sha1, email);
            searchReply.put(sha1, null);
            //searchReply.put(sha1, "127.0.0.1");


            File searchQuery=createXmlSearchQuery(sha1);
            CommunicationManager.TransmittingBuffer.add(searchQuery);

            timer = new Timer();


            Timer timer = new Timer();
            TimerTask startIM = new TimerTask()
            {
                @Override
                public void run()
                {

                    SysOutCtrl.SysoutSet(""+setInterval());

                    SysOutCtrl.SysoutSet("searchReplyReceived"+IndexManagement.searchReplyReceived);

                    if(IndexManagement.searchReplyReceived==false)
                    {
                        /*  SysOutCtrl.SysoutSet("please wait");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*/
                 
                        JFrame frame1 = new JFrame("Message");
                        //show a joptionpane dialog using showMessageDialog
                        JOptionPane.showMessageDialog(frame1, "Searched IP Not Found In Given Time ");
                        String destIp = "TimedOut";
                       
                    }
                    else
                    {
                        SysOutCtrl.SysoutSet("searchReplyReceived after  while"+IndexManagement.searchReplyReceived);
                        SysOutCtrl.SysoutSet("after while");
                        String destIp=IndexManagement.searchReply.get(sha1);

                        SysOutCtrl.SysoutSet("ip from search query reply "+destIp,1);
                        timer.cancel();
                        JLabel frame = new JLabel("IP_Address");
                        JFrame frame1 = new JFrame("Message");
                        //show a joptionpane dialog using showMessageDialog
                        JOptionPane.showMessageDialog(frame1, "Searched IP address is "+destIp);
                        IndexManagement.searchReplyReceived=false;
                    }
                }
            };

            timer.schedule(startIM, 30000);
            
            return destIp;
        }

    }

    private static final int setInterval()
    {
        if (interval == 1)
        {
            timer.cancel();
            IndexManagement.searchReplyReceived=true;
        }
        System.out.println("in setInterval");
        System.out.println(interval);
        return --interval;
    }

    public static String getSelfEmailId()
    {   // this method is to directly enter the email id from indexing.
        // create a jframe
        JLabel frame = new JLabel("EmailID");
        // prompt the user to enter their email id as common name.
        //email id is extracted later at server and used to send OTP for verification.
        String commonname = JOptionPane.showInputDialog(frame, "Please enter self Email-Id(for publishing youself at responsible node");
        return commonname;
    }

    public static void storeHashId()
    {

        selfEmailid = emailid.getemaild();//method from authentication will come heere


        selfHashId=SHA1.getSha1(selfEmailid);
        String nodeId=OverlayManagement.myNodeId;

        SysOutCtrl.SysoutSet("myIpTable is"+CommunicationManager.myIpTable, 2);


    }

    public static void newNodeCheckingReply(File inFile)
    {

        String[] xmlDetails=ParseXmlFile.ParseXml(inFile);
        String value=Search_in_Index( xmlDetails[1]);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder = null;
        try {
            documentbuilder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {

            e.printStackTrace();
        }
        Document doc = documentbuilder.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0031"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(xmlDetails[3]);

        // next hop will be given by routing module
        Text t2 = doc.createTextNode("xxxxx");

        Text t3 = doc.createTextNode(OverlayManagement.myNodeId);

        Text t4 = doc.createTextNode(getMyIp());

        Text t5 = doc.createTextNode("2222");

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


            DOMSource source = new DOMSource(doc);
            StreamResult result = null;

            result = new StreamResult(new FileOutputStream("checkNewNodeReply.xml"));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (TransformerException e) {
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("checkNewNodeReply.xml file Generated.",2);
        File checkNewNodeReply=new File("checkNewNodeReply.xml");

        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(checkNewNodeReply.toString()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String s = "", line = null;
        try {
            while ((line = r.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CommunicationUtilityMethods.fileSender(xmlDetails[4], s);
        SysOutCtrl.SysoutSet("Reply for the checkNewNode Query sent back to new node",2);
    }

    public static File createXmlUpdate_IP(String key) {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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

        String tagvalue = "0020"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);

        Text t2 = doc.createTextNode("Update IP Query");
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

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
            result = new StreamResult(new FileOutputStream("Update_IPQuery.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("Update IP xml query Generated.",2);

        return (new File("Update_IPQuery.xml"));

    }

    public static void storeIp(String hashId)
    {

        String searched_ip=searchReply.get(hashId);



        String emailId=emailSha1.get(hashId);
        callingTable.put(emailId, searched_ip);

        if(searched_ip=="xxxx")
            SysOutCtrl.SysoutSet("Searched  Ip details not available for eamilid:"+emailId,1 );

        SysOutCtrl.SysoutSet("Searched  emailid and Ip :"+emailId+"  "+searched_ip,1);

    }

    public static void fillRxBufferIM()
    {
        int i;
        for (i = 0; i < 1; i++)
        {

            File fetchNextXml = new File("Query1" + i + ".xml");

            CommunicationManager.RxBufferIM.add(CommunicationManager.RxBufferIM.size(), fetchNextXml); // this statement
            // will append
            // the msg ie
        }
        SysOutCtrl.SysoutSet("RxBufferIM filled with " + i + " query xml's",2);
    }



    public static void fillMyIndexTable()
    {

        myindex.put("A8055D147FA3CEA159468FDDF7147C003CCF5BB2" ,"C633336666666CEA159468FDDF7147C003CCF5BB2"  );
        myindex.put("88055D147FA3CEA159468FDDF7147C003CCF5BB2" ,"C666886666666CEA159468FDDF7147C003CCF5BB2"  );


        SysOutCtrl.SysoutSet("filled myIndex table "+myindex,2);
    }
}
