/**
 * @author egegen
 *
 */

package com.example.listening;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;
public class RssFunctions {
	private final static String firm_name = "Gen";
	private final static String TAG = firm_name + " Update Listen Service";
	
	public final static Document XMLfromString(String xml){
		  
		  Document doc = null;
		  
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        try {
		         
		   DocumentBuilder db = dbf.newDocumentBuilder();
		   
		   InputSource is = new InputSource();
		         is.setCharacterStream(new StringReader(xml));
		         doc = db.parse(is);
		        
		  } catch (ParserConfigurationException e) {
		   Log.e(TAG,e.getMessage());
		   return null;
		  } catch (SAXException e) {
		   Log.e(TAG,e.getMessage());
		   return null;
		  } catch (IOException e) {
		   Log.e(TAG,e.getMessage());
		   return null;
		  }
		        
		        return doc;
	}

		  public final static String getElementValue( Node elem ) {
		      Node kid;
		      if( elem != null){
		          if (elem.hasChildNodes()){
		              for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
		                  if( kid.getNodeType() == Node.TEXT_NODE  ){
		                      return kid.getNodeValue();
		                  }
		              }
		          }
		      }
		      return "";
		  }
		  
		  
		 public static int numResults(Document doc){  
		  Node results = doc.getDocumentElement();
		  int res = -1;
		  
		  try{
		   res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
		  }catch(Exception e ){
		   res = -1;
		  }
		  
		  return res;
		 }

		 public static String getValue(Element item, String str) {  
		  NodeList n = item.getElementsByTagName(str);  
		  return RssFunctions.getElementValue(n.item(0));
		 }
}
