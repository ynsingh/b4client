package com.ehelpy.brihaspati4.indexmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Random;

import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

// Sqn ldr Sandeep Sharma Dated 15 Apr 2017 ; 1255 Hrs
// this code is only for simulation purpose
// Here i have made a code to generate xml files whose tags will be randomly chosen out of
//0000 (signifies a query of type query where one node wants to communicate to another node) or
//0001	(signifies a query of type advertisement where one newly joined node wants to make his entry in index table of its root node)
public class CreateXmlFile {
    public static void main (String args[]) throws Exception
    {
        createXmlQueryFiles();
    }

    public static void createXmlQueryFiles ()

    {   for(int i =0; i<10; i++)
        {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = f.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Document doc = db.newDocument();

            Element rootele = doc.createElement("Query"+i);
            Element codeele = doc.createElement("Query_");
            Element hashidele = doc.createElement("to_hash_id");
            Element tonodeidele = doc.createElement("to_node_id");
            Element selfnodeidele = doc.createElement("self_node_id");
            Element ipele = doc.createElement("self_ip_address");
            Element portele = doc.createElement("self_port_no");

            String[] tag = {"0002", "0003","0004","0010","0012"};
            Random random = new Random();

            int select = random.nextInt(tag.length); 			/// randomly chooses the string index from the string array

            String tagvalue = tag [select];						// use random index to store corresponding string value in another string

            //System.out.println("Random String selected: " + tagvalue);			// prints out the value at the randomly selected index

            ((Element)codeele).setAttribute("tag", tagvalue);

            Text t1 = doc.createTextNode("48055D147FA3CEA159468FDDF7147C003CCF5BB"+i);
            Text t2 = doc.createTextNode("88055D147FA3CEA159468FDDF7147C003CCF5BB"+i);
            Text t3 = doc.createTextNode("F8055D147FA3CEA159468FDDF7147C003CCF5BB"+i);
            Text t4 = doc.createTextNode("192.36.45.22"+i);
            Text t5 = doc.createTextNode("2367"+i);

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
            String TagName=tagvalue;
            StreamResult result = null;
            try {
                result = new StreamResult(new  FileOutputStream("Query_"+i+".xml"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println("Query xml with tag "+TagName+ " created :" +"Query_"+i+".xml");
            //StreamResult result = new StreamResult(new  FileOutputStream("Query_"+TagName+"_"+i+".xml"));

            //System.out.println("Query xml with tag "+TagName+ " created :" +"Query_"+TagName+"_"+i+".xml");
            try {
                transformer.transform(source, result);
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StreamResult file = new StreamResult(new File("C:/tmp/Query.xml"));
        }
        /*
         t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("E:/Personal/Studies/M.Tech/Thesis/Code_Detail.xml"))); */



        System.out.println("XML file Generated.");



    }
}

//public static String convertToString( Source source )
//
//	{
//	    StreamResult result = new StreamResult();
//	    StringWriter writer = new StringWriter();
//	    result.setWriter( writer );
//	    transform( source, result );
//	    return writer.toString();
//	}
//}
