/**
 * @author egegen
 *
 */
package com.example.listening;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class UpdateXMLHandler {
	
	private static final String ns = null;
	
	public List<HashMap<String, String>> parse(Reader reader) 
    		throws XmlPullParserException, IOException{
    	try{    		
    		XmlPullParser parser = Xml.newPullParser();
    		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);    		
    		parser.setInput(reader);
    		parser.nextTag();    		
    		return readCountries(parser);    		
    	}finally{
    		
    	}
    }        
    

    private List<HashMap<String, String>> readCountries(XmlPullParser parser) 
    		throws XmlPullParserException,IOException{
    	
    	List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();    	
    	
    	parser.require(XmlPullParser.START_TAG, ns, "terminal");
    	
    	while(parser.next() != XmlPullParser.END_TAG){    		
    		if(parser.getEventType() != XmlPullParser.START_TAG){
    			continue;
    		}
    		
    		String name = parser.getName();    		
    		if(name.equals("activity")){
    			list.add(readCountry(parser));    			
    		}
    		else{
    			skip(parser);
    		}
    	}    	
    	return list;
    }
    
    private HashMap<String, String> readCountry(XmlPullParser parser) 
    		throws XmlPullParserException, IOException{
    	
    	parser.require(XmlPullParser.START_TAG, ns, "activity");
    	
        String activityid = parser.getAttributeValue(ns, "activityid");
        String names = parser.getAttributeValue(ns, "name");
        String startdatetime = parser.getAttributeValue(ns, "startdatetime");
        String enddatetime = parser.getAttributeValue(ns, "enddatetime");
        String file = parser.getAttributeValue(ns,"file");
        String filetype = parser.getAttributeValue(ns,"filetype");
        String bgcolor = parser.getAttributeValue(ns,"bgcolor");
        String component="";
        String com_name ="";
        @SuppressWarnings("unused")
        String com_type ="";
        @SuppressWarnings("unused")
		String com_x ="";
        @SuppressWarnings("unused")
        String com_y ="";
        @SuppressWarnings("unused")
        String com_w ="";
        @SuppressWarnings("unused")
        String com_h ="";
        @SuppressWarnings("unused")
        String com_borderwidth ="";
        @SuppressWarnings("unused")
        String com_bordercolor ="";
        @SuppressWarnings("unused")
        String com_rounded ="";
        @SuppressWarnings("unused")
        String com_shadowsize = "";
        @SuppressWarnings("unused")
        String com_file = "";
        @SuppressWarnings("unused")
        String com_filetype="";
        
        while(parser.next() != XmlPullParser.END_TAG){
        	if(parser.getEventType() != XmlPullParser.START_TAG){
        		continue;
        	}
        	
        	String name = parser.getName();
    		
        	if(name.equals("component")){
        			
        			parser.require(XmlPullParser.START_TAG, ns, "component");
        			com_name = parser.getAttributeValue(ns,"name");
            		com_type = parser.getAttributeValue(ns,"type");
            		com_x = parser.getAttributeValue(ns,"x");
            		com_y = parser.getAttributeValue(ns,"y");
            		com_w = parser.getAttributeValue(ns,"w");
            		com_h = parser.getAttributeValue(ns,"h");
            		com_borderwidth = parser.getAttributeValue(ns,"borderwidth");
            		com_bordercolor = parser.getAttributeValue(ns,"bordercolor");
            		com_rounded = parser.getAttributeValue(ns,"rounded");
            		com_shadowsize = parser.getAttributeValue(ns,"shadowsize");
            		com_file = parser.getAttributeValue(ns,"file");
            		com_filetype = parser.getAttributeValue(ns,"filetype");
            		//component = readComponent(parser);
            		
        		
        	} else {
        		skip(parser);
        	}
        }
        
        String components = component;	            			

        
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("activityid", activityid);
        hm.put("name", names);
        hm.put("startdatetime",startdatetime);  
        hm.put("enddatetime",enddatetime); 
        hm.put("component",components);
        hm.put("file", file);
        hm.put("filetype", filetype);
        hm.put("bgcolor", bgcolor);
        hm.put("com_name",com_name);
        
        
        
    	return hm;
    	
    }
    
    /** Process language tag in the xml data */    
    @SuppressWarnings("unused")
	private String readComponent(XmlPullParser parser) 
    		throws IOException, XmlPullParserException {    
    	parser.require(XmlPullParser.START_TAG, ns, "component");
    	String component = readText(parser);
    	return component;
    }
    
    /** Getting Text from an element */
    private String readText(XmlPullParser parser) 
    		throws IOException, XmlPullParserException{
    	String result = "";
    	if(parser.next()==XmlPullParser.TEXT){
    		result = parser.getText();
    		parser.nextTag();
    	}
    	return result;    	
    }
    
    private void skip(XmlPullParser parser) 
    		throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
}
