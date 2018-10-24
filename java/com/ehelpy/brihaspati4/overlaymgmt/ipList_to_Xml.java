package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ipList_to_Xml {



//	      System.out.println(firstRow(b)); // line 8



//	    public static String[] firstRow(String[][] a) {
//	    	String[][] b = {{"John", "Abby"},
//                    {"Sally", "Tom"}};
//	    	S tring[] result = firstRow(b);
//	        for(int i = 0; i < result.length; i++)
//	            System.out.print(firstRow(b)[i] + " ");
//	        return a[0];
//	    }

    public static File convert_hashmap_toxml(String[][] nodeid, String[][] ipadd ,String to_hash_id, String selfNodeId,String toNodeId,String selfIp, String self_port_no) throws FileNotFoundException, TransformerException, ParserConfigurationException
    {


        //System.out.println("Routing Table " + myindex1);
        Object[][] hashid_array = nodeid;

        Object[][] nodeid_array = ipadd;

        for(int i=0; i<hashid_array.length; i++)
            System.out.println(hashid_array[0][i]);

        for(int j=0; j<nodeid_array.length; j++)
            System.out.println(nodeid_array[1][j]);


        //System.out.println(myindex1.keySet());
        //System.out.println(myindex1.values());

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("XML");
        doc.appendChild(rootele);
        Element tagidele = doc.createElement("Query_");
//		Element hashidele = doc.createElement("hash_id");
//		Element tonodeidele = doc.createElement("to_node_id");
//		Element fromnodeidele = doc.createElement("from_node_id");
//		Element ipele = doc.createElement("ip_address");
//		Element portele = doc.createElement("port_no");

        //String[] tag = {"0000", "0001"};
        //Random random = new Random();

        // int select = random.nextInt(tag.length); 			/// randomly chooses the string index from the string array

        //String tagvalue = tag [select];						// use random index to store corresponding string value in another string

        String tagvalue="0011";
        System.out.println("Tagvalue for indexingQuery: " + tagvalue);			// prints out the value at the randomly selected index

        ((Element)tagidele).setAttribute("tag", tagvalue);
        rootele.appendChild(tagidele);
        int i=0;
        //System.out.println("hashid array length " + hashid_array.length);
        for(int j=0; j<ipadd.length; j++)
        {
            Element codeele = doc.createElement("indexentries");
            tagidele.appendChild(codeele);
            ((Element)codeele).setAttribute("record_no", Integer.toString(i));			// here using i as tagvalue
            i++;

            Element hashidele = doc.createElement("hash_id");
            hashidele.appendChild(doc.createTextNode((String) hashid_array[0][j]));
            codeele.appendChild(hashidele);

            Element nodeidele = doc.createElement("node_id");
            nodeidele.appendChild(doc.createTextNode((String) nodeid_array[1][j]));
            codeele.appendChild(nodeidele);

        }
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        //Text t0 = doc.createTextNode(to_hash_id);
        Text t0 = doc.createTextNode(to_hash_id);
        Text t1 = doc.createTextNode(toNodeId);
        //Text t1 = doc.createTextNode(toNodeId);
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

        StreamResult result = new StreamResult(new FileOutputStream("XML.xml"));
        transformer.transform(source, result);
        System.out.println("xml generated");




        return (new File("XML.xml"));
    }


    public static void main(String[] args) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        // TODO Auto-generated method stub
        String[][] a= {{"abcde","abcdf"},{"192.168.0.1","192.168.1.1"}};
        String[][] b= {{"abcde","abcdf"},{"192.168.0.1","192.168.1.1"}};
        File f = ipList_to_Xml.convert_hashmap_toxml(a, b, "xxxx", "abcd1", "abcd2", "192.168.0.2", "2222");
        String node = f.toString();
        System.out.println(node);
    }

}
