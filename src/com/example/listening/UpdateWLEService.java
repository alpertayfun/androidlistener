/**
 * @author egegen
 *
 */

package com.example.listening;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

@SuppressLint("WorldReadableFiles")
@SuppressWarnings("unused")
public class UpdateWLEService extends Service {

	private final String firm_name = "Gen";
	private final String TAG = firm_name + " Update WLE Listen Service";
	private String[] arrDownloadableFiles = {"weather", "rate", "list"};
    private final int listenerPort = 33301;
    private final int listenerDPort = 33302;
	private final int transmissionPort = 33311;
	private final int transmissionDPort = 33312;
	private final int weatherPanelRefreshPeriod = 600000;
 	private final int exchangePanelRefreshPeriod = 1;
 	
 	public static String WLE_listener_status = "OFF";
    private ArrayList<String> DownloadFiles = new ArrayList<String>();
    private ArrayList<String> DownloadFilesRate = new ArrayList<String>();
    private ArrayList<String> DownloadFilesList = new ArrayList<String>();
    private ArrayList<String> SuccessDownloadFiles = new ArrayList<String>();
    private String xml_data = null;
	private int DEFAULT_BUFFER_SIZE = 1024 * 20;
	private String Act_Id = "";
	private String Type = null;
	private String[] arrCities = null;
	private Socket DownloadSocket_passive = null;
	private Socket DownloadSocketD_passive = null;
	private String outss_passive = null;
	private FileOutputStream outputStreams = null;
	private PrintWriter outss_passivesss = null;
	private PrintWriter outp_filedownload = null;
	private ServerSocket downloadsocket_passives = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "WLE Service onCreate");
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "WLE Service onDestroy");
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Log.d(TAG, "WLE Service onStart");
		
		
        final Handler handlers = new Handler(); 
        Timer tt = new Timer(); 
        tt.schedule(new TimerTask() { 
                public void run() { 
                		handlers.post(new Runnable() { 
                                public void run() { 
                                	WLEStarter();
                                } 
                        }); 
                } 
        }, 0,weatherPanelRefreshPeriod);
	}

	
	public void WLEStarter() {
		WLE_listener_status = "ON";
		Log.d(TAG,"WLEListener : " + WLE_listener_status);
		FileInputStream fIn = null;
		Charset.forName("UTF-8").newEncoder();
		try{
	
			fIn = openFileInput("terminal_updater.xml");
		    InputStream in = fIn;
		    //System.out.println(System.getProperty("file.encoding"));
		    byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
		    int len;
		    while ((len = in.read(buf)) > 0){
		    
		       xml_data = new String(buf, 0, len);
		        	        	 
		    }
		        	          
		    try {
		        DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dbs = null;
		        dbs = dbfs.newDocumentBuilder();
		        					
		        Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(xml_data.getBytes("utf-8"))));
		        docs.getDocumentElement().normalize();
		        		        	
		        NodeList nodeListActivity=docs.getElementsByTagName("activity");
		        Element elementactivity=(Element) nodeListActivity.item(0);
		        Act_Id = elementactivity.getAttribute("activityid");

		        		for (int i = 0; i < nodeListActivity.getLength(); i++) {

		        		        	            	
		        		     Node node=nodeListActivity.item(i);
		        		     Element elementMain=(Element) node;
		        		     NodeList nodeListcomponent=elementMain.getElementsByTagName("component");
		        		     Element elementcomponent=(Element) nodeListcomponent.item(i);

		        		        	                
		        		        	for(int c = 0;c<nodeListcomponent.getLength();c++){
		        		        	                	
		        		        	                	
		        		        	    Element componentElements = (Element) nodeListcomponent.item(c);
		        		        	    String subtype = componentElements.getAttribute("type");
			        		        	                
			        		        	                
			        		        	                
			        		        	                
		        			        	                //Log.d(TAG,"2geldi subtype :" + componentElements.getAttribute("type"));
		        			        	                if(subtype.equalsIgnoreCase("weather")){
		        			               	                Node node1=nodeListcomponent.item(c);
		        				        	                Element elementMain1=(Element) node1;
		        				        	                NodeList nodeListtimeline=elementMain1.getElementsByTagName("timeline");
		        				        	                Element elementtimeline=(Element) nodeListtimeline.item(0);
		        				        	                
		        			        	                	for(int t = 0;t<nodeListtimeline.getLength();t++){
		        			        	                		
		        			        	                		
		        			        	                		Node node2 = nodeListtimeline.item(t);
		        			    		        			 	Element elementMain2=(Element) node2;
		        			    		        			 	NodeList nodeListsspecification=elementMain2.getElementsByTagName("specification");
		        			    		        			 	
		        			    		        			 	String getir = null;
		        			 	        	                	for(int s=0;s <nodeListsspecification.getLength();s++) {
		        			 	        	                		Element elementspecification=(Element) nodeListsspecification.item(s);
		        			 	        	                		if(elementspecification.getAttribute("datakey").equalsIgnoreCase("view_city"))
		        			 	        	                		getir += "" + ";;" + elementspecification.getAttribute("datavalue");
		        			 	        	                	}
		        			 	        	                	
		        			 	        	                	getir = Act_Id + "--" + "weather,weather.xml" + "--" + getir;
		        			 	        	                	//Log.d(TAG,getir.replace("null", "").trim());
		        			 	        	                	
		        			 	        	                	DownloadFiles.add(getir.replace("null", "").trim());
		        			        	                	}
		        			        	                	
		        			        	                }
		        			        	                
		        			        	                if(subtype.equalsIgnoreCase("rate")){
		        			        	                	Log.d(TAG,"Weather was Found");
		        			               	                Node node1=nodeListcomponent.item(c);
		        				        	                Element elementMain1=(Element) node1;
		        				        	                NodeList nodeListtimeline=elementMain1.getElementsByTagName("timeline");
		        				        	                Element elementtimeline=(Element) nodeListtimeline.item(0);
		        				        	                
		        			        	                	for(int t = 0;t<nodeListtimeline.getLength();t++){
		        			        	                		
		        			        	                		
		        			        	                		Node node2 = nodeListtimeline.item(t);
		        			    		        			 	Element elementMain2=(Element) node2;
		        			    		        			 	NodeList nodeListsspecification=elementMain2.getElementsByTagName("specification");
		        			    		        			 	
		        			    		        			 	String getir = null;
		        			 	        	                	for(int s=0;s <nodeListsspecification.getLength();s++) {
		        			 	        	                		Element elementspecification=(Element) nodeListsspecification.item(s);
		        			 	        	                		if(elementspecification.getAttribute("datakey").equalsIgnoreCase("exchange_code"))
		        			 	        	                		getir += "" + ";;" + elementspecification.getAttribute("datavalue");
		        			 	        	                	}
		        			 	        	                	
		        			 	        	                	getir = Act_Id + "--" + "exchange,rate.xml" + "--" + getir;
		        			 	        	                	//Log.d(TAG,getir.replace("null", "").trim());
		        			 	        	                	
		        			 	        	                	DownloadFiles.add(getir.replace("null", "").trim());
		        			        	                	}
		        			        	                	
		        			        	                }
		        			        	                
		        			        	                if(subtype.equalsIgnoreCase("content")){
		        			        	                	
		        			        	                	String component_name = componentElements.getAttribute("name");
		        			        	                	Log.d(TAG,"Component Name : " + component_name);
		        			        	                	
		        			               	                Node node1=nodeListcomponent.item(c);
		        				        	                Element elementMain1=(Element) node1;
		        				        	                NodeList nodeListtimeline=elementMain1.getElementsByTagName("timeline");
		        				        	                Element elementtimeline=(Element) nodeListtimeline.item(0);
		        				        	                

		        				        	                
		        				        	                if(elementtimeline.getAttribute("subtype").equalsIgnoreCase("list")){
		        				        	                	
		        				        	                	Log.d(TAG,"List was Found");
			        				        	                String timeline_name = elementtimeline.getAttribute("name");
			        				        	                String startdatetime = elementtimeline.getAttribute("startdatetime");
			        				        	                String enddatetime = elementtimeline.getAttribute("enddatetime");
			        				        	                
		        				        	                	for(int t = 0;t<nodeListtimeline.getLength();t++){
			        			        	                		
			        			        	                		
			        			        	                		Node node2 = nodeListtimeline.item(t);
			        			    		        			 	Element elementMain2=(Element) node2;
			        			    		        			 	NodeList nodeListsspecification=elementMain2.getElementsByTagName("specification");
			        			    		        			 	
			        			    		        			 	String getir = null;
			        			 	        	                	for(int s=0;s <nodeListsspecification.getLength();s++) {
			        			 	        	                		Element elementspecification=(Element) nodeListsspecification.item(s);
			        			 	        	                		if(elementspecification.getAttribute("datakey").equalsIgnoreCase("list_name"))
			        			 	        	                		getir =elementspecification.getAttribute("datavalue");
			        			 	        	                	}
			        			 	        	                	
			        			 	        	                	String Listname = "list_" + component_name + "_" + timeline_name + "_" + startdatetime + "_" + enddatetime + ".xml";
			        			 	        	                	
			        			 	        	                	getir = Act_Id + "--" + "list," + conver_string(Listname) + "--" + "\"" + getir + "\"";
			        			 	        	                	
			        			 	        	                	Log.d(TAG,"List : " + getir.replace("null", "").trim());
			        			 	        	                	
			        			 	        	                	DownloadFiles.add(getir.replace("null", "").trim());
			        			        	                	}
		        				        	                }
		        			        	                	
		        			        	                	
		        			        	                }
		        			        	                
		        		        	                }
		        		        	                
		        		    		        	 }
		        		        	              

		        		        }catch (ParserConfigurationException e) {
		        					Log.e(TAG,e.getMessage());
		        				} catch (SAXException e) {
		        					Log.e(TAG,e.getMessage());
		        				} catch (NumberFormatException e) {
		        					Log.e(TAG,e.getMessage());
		        				} 
		        	          
		        			} catch (Exception e) {
		        					Log.e(TAG,e.getMessage());
		        				}
		        	
		        		
		        		
		        		String [] stringArray = DownloadFiles.toArray(new String[DownloadFiles.size()]);
		                //Log.d(TAG,stringArray[0]);
		                for (int i=0; i<DownloadFiles.size(); i++) {
		               	 //Log.d(TAG,stringArray[i]);
		               	 if (TextUtils.isEmpty(stringArray[i])) {
		               		    //Log.d(TAG, "String is empty or null!");
		               		} else {
		               			//Log.d(TAG,stringArray[i]);
		               			String[] separated = stringArray[i].split("--");
		               			
		               			String[] findfiletype = separated[1].split(",");
		               			String spes = separated[2].substring(2, separated[2].length());
		            			//String cities = findcitys[1] + ";;" + findcitys[2];
		               			
		               			
		               			if(findfiletype[0].equalsIgnoreCase("list")){
		               				try {
			               				FileDownload(separated[0],findfiletype[0],findfiletype[1],separated[2],null);
			        				} catch (IOException e) {
			        					Log.e(TAG,e.getMessage());
			        				}
		               			}else if(findfiletype[0].equalsIgnoreCase("weather")){
		               				try {
			               				FileDownload(separated[0],findfiletype[0],findfiletype[1],spes,null);
			        				} catch (IOException e) {
			        					Log.e(TAG,e.getMessage());
			        				}
		               			}else if(findfiletype[0].equalsIgnoreCase("exchange")){
		               				try {
			               				FileDownload(separated[0],findfiletype[0],findfiletype[1],spes,null);
			        				} catch (IOException e) {
			        					Log.e(TAG,e.getMessage());
			        				}
		               			}
		               				
		               			
		               			

		               		}
		                }
		                
		    Log.d(TAG,"SuccessDownloadFiles : " + SuccessDownloadFiles.size());
		if (SuccessDownloadFiles.size() == DownloadFiles.size()){
			WLE_listener_status= "OFF";
			removeAll(DownloadFiles);
			removeAll(SuccessDownloadFiles);
		     	
			Log.d(TAG,"WLEListener : " + WLE_listener_status);
		}
				         
		removeAll(DownloadFiles);
		removeAll(SuccessDownloadFiles);
		                
	}
	
	public void removeAll(Collection<?> c) {
	    Iterator<?> e = c.iterator();
	    while (e.hasNext()) {
	        if (c.contains(e.next())) {
	            e.remove();
	        }
	    }
	}
	
	public String conver_string(String datas){
		 datas = datas.replace(" ", "");
		 datas = datas.replace(":","_");
		 datas = datas.replace("-","_");
		 datas = datas.replace("__", "_");
		 /*
		 Pattern p = Pattern.compile(":");
		 Matcher m = p.matcher(datas);
		 StringBuffer sb = new StringBuffer();
		 while (m.find()) {
		     m.appendReplacement(sb, "_");
		 }
		 m.appendTail(sb);
		 */
		 
		return datas;
		
	}
	
	@SuppressWarnings("deprecation")
	public void FileDownload(final String activityid,final String filetype,final String filenames,final String specification, Object object) throws IOException{
			
    		Log.d(TAG,"Filedownload Started");
    		Socket s_filedownload = null;
        	try {
        		
        		s_filedownload = new Socket("192.168.2.51", transmissionPort);
    			BufferedReader in_filedownload = new BufferedReader(new InputStreamReader(s_filedownload.getInputStream()));
    			BufferedWriter out_filedownload = new BufferedWriter(new OutputStreamWriter(s_filedownload.getOutputStream()));

    			String inMsgs_filedownload = in_filedownload.readLine() + System.getProperty("line.separator");
    			Log.d(TAG,inMsgs_filedownload);
    			
    			String outMsg = "TRANSFER " + activityid + " " + filetype + " " + filenames + " 0 " + specification;
    			out_filedownload.write(outMsg);
    			out_filedownload.flush();
    			Log.d(TAG, outMsg);
    			
    			String inMsg_filedownload = in_filedownload.readLine() + System.getProperty("line.separator");
    			Log.d(TAG, inMsg_filedownload);
    			
    			if(inMsg_filedownload.trim().equals("201 ready for transfer")){
    				downloadsocket_passives = null;
    				int count;

    				try {
    					
    					//DownloadSocketD_passive = new Socket(getIP, transmissionDPort);
    					
    					downloadsocket_passives = new ServerSocket(transmissionDPort);
    					DownloadSocketD_passive = downloadsocket_passives.accept();
    					
    					DataInputStream iss_filedownloadpassive = new DataInputStream(DownloadSocketD_passive.getInputStream());
    	          		
    	                Log.d(TAG, "Downloading...");
    	                
    	                Log.d(TAG,"File Downloading Started : " + filenames);
    	                
    	                	 FileOutputStream outputStreams = null;
    	                	 Charset.forName("UTF-8").newEncoder();
    	                	 outputStreams = openFileOutput(filenames, MODE_WORLD_READABLE);


    	                		byte[] buffer = new byte[2048];
    	                        int length;
    	                        while ((length = iss_filedownloadpassive.read(buffer))>0){
    	                        	outputStreams.write(buffer, 0, length);
    	                        }

    	                        outputStreams.flush();
    	                        outputStreams.close();
    	                        iss_filedownloadpassive.close();
    	                	 
    	                	
    	                downloadsocket_passives.close();
    					DownloadSocketD_passive.close();
    	                Log.d(TAG,"File Downloading Finished : " + filenames);
    					
    				} catch (UnknownHostException e) {
    	                downloadsocket_passives.close();
	    					DownloadSocketD_passive.close();
    					Log.e(TAG,e.getMessage());
    				} catch (IOException e) {
    	                downloadsocket_passives.close();
    					DownloadSocketD_passive.close();
    					Log.e(TAG,e.getMessage());
    				} 
    				
    			}
    			in_filedownload.close();
    			out_filedownload.close();
    			s_filedownload.close();
    			
    		} catch (UnknownHostException e) {
    			Log.e(TAG,e.getMessage());
    			
    		} catch (IOException e) {
    			Log.e(TAG,e.getMessage());
    		}

        	Log.d(TAG,"File Downloading Closed");
        	SuccessDownloadFiles.add("File Download ok" + filenames);
		}
		
		public static String GetFileMD5(String FileName) throws IOException  {
			java.io.File file = new java.io.File(FileName);
			if(file.exists()){
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("MD5");
					InputStream is = new FileInputStream(FileName);
					try {
						  is = new DigestInputStream(is, md);
						}finally {
							  is.close();
						}
					
					byte[] digest = md.digest();
					StringBuilder b = new StringBuilder(digest.toString());
					int i = 0;
					do {
					  b.replace(i, i + 1, b.substring(i,i + 1).toUpperCase());
					  i =  b.indexOf(" ", i) + 1;
					} while (i > 0 && i < b.length());

					return b.toString();

					
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
			} else {
				//char[] resChars = new char[32];
				return new String("0");
			}
			
			return FileName;
				
		}
		
}
