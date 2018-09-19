/**
 * @author egegen
 *
 */

package com.example.listening;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class UpdateListenService extends Service {
	
	private final int DEFAULT_BUFFER_SIZE = 1024 * 20;
	
	private final String firm_name = "Gen";
	private final String TAG = firm_name + " Update Listen Service";
	private final int listenerPort = 33301;
	private final int listenerDPort = 33302;
	private final int transmissionPort = 33311;
	private final int transmissionDPort = 33312;
 	@SuppressWarnings("unused")
 	private final int weatherPanelRefreshPeriod = 10;
 	@SuppressWarnings("unused")
 	private final int exchangePanelRefreshPeriod = 1;
	
 	public static String listener_status = "OFF";
    public Boolean readTimeLine = true;
    public Boolean systemTimerOnProcess = false;
    public Boolean datetimeTimerOnProcess = false;
    public Boolean firstprocess_afterstart = true;
    public Boolean needClearComponents = false;
    
    private String[] arrDownloadableFiles = {"image", "video", "document","textrss","flash"};
    
    private ArrayList<String> DownloadFiles = new ArrayList<String>();
    private ArrayList<String> SuccessDownloadFiles = new ArrayList<String>();
	@SuppressWarnings("unused")
    private ArrayList<String> DownloadFilesType = new ArrayList<String>();
    
	private String welcome = "001 Egegen Digital Signage Client Listener\r";
	private String asking = "101 Asking Update";
	private String asking_server = "UPDATEREADY";
    private String invalid = "998 empty command";
	
    private ServerSocket socket = null;
    private DataOutputStream os = null;
    private DataInputStream is = null;
    private PrintWriter outp = null;
    private Socket ClientSocket = null;
    private String getIP = null;
    
    private DataInputStream is_passive = null;
    private Socket ClientSocket_passive = null;

    @SuppressWarnings("unused")
	private BufferedReader inp_passive = null;
    private ServerSocket socket_passive = null;
    private Socket DownloadSocketD_passive = null;
    private ServerSocket downloadsocket_passives = null;
	
	private String xml_data = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "Update Listen Service onCreate");
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Update Listen Service onDestroy");
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		//Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "Update Listen Service onStart");
		Thread UpdateStart = null;
	    Runnable UpdateStartrunnable = new UpdateStartHandle();
	    UpdateStart= new Thread(UpdateStartrunnable);   
	    UpdateStart.start();
	    
	}
	
	
	class UpdateStartHandle implements Runnable{
	    public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                	Update();
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
	    }
	}
	
	public void Update() {
		socket = null;
        
        try
        {
         socket = new ServerSocket(listenerPort);
          Log.d(TAG, "Waiting...");
          ClientSocket = socket.accept();
          
          		os = new DataOutputStream(ClientSocket.getOutputStream());
          		is = new DataInputStream(ClientSocket.getInputStream());
          		new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
          		
          		outp = new PrintWriter(ClientSocket.getOutputStream(), true);
          		outp.println(welcome);
          		
                Log.d(TAG, "Connected.....");
                listener_status = "ON";
                Log.d(TAG,"Listener : " + listener_status);
                
                String ClientIP = ClientSocket.getInetAddress().getHostAddress();
                Log.d(TAG,"Connect ServerIP : " + ClientIP);
                
                getIP = ClientIP;
                
                byte[] buf = new byte[4096];
                int readLen = 0;
                while((readLen = is.read(buf, 0, 4096)) != 0)
                {                	
                	
                	String out = new String(buf, 0, readLen);
                	Log.d(TAG,out);

           
                 	if(out.trim().equals(asking_server)){
                 		
                 		Log.d(TAG,"Asking Update");
                 		outp.println(asking + "\r");
                		
                 		String result = AskUpdate();
                 		Log.d(TAG,result);
                 		
                  		outp = new PrintWriter(ClientSocket.getOutputStream(), true);
                  		outp.println(result);
                 		
                  		Thread UpdateThreadStart = null;
                	    Runnable UpdateThreadFunctionRunnable = new UpdateThreadFunctionRunnable();
                	    UpdateThreadStart= new Thread(UpdateThreadFunctionRunnable);   
                	    UpdateThreadStart.start();

                	    
                 	}
                 	
                 	if(out.trim() == null){
                  		outp = new PrintWriter(ClientSocket.getOutputStream(), true);
                  		outp.println(invalid);
                 	}
                 	
                } 
        }
        catch (UnknownHostException e) 
        {

           Log.e("Error in tcp connection","Unknown Host");
           Log.e(TAG,e.getMessage());

        }
        catch (IOException e) 
        {
            Log.e("Error in tcp connection", "Couldn't get I/O for the connection");
            Log.e(TAG,e.getMessage());
              
        }finally {
			if (socket != null) {
				try {
	                os.close();
	                is.close();
	                socket.close();
	                ClientSocket.close();
					Log.d(TAG, "Finished");
				} catch (IOException e) {
					Log.e(TAG,e.getMessage());
				}
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SdCardPath", "WorldReadableFiles" })
	private String AskUpdate() throws IOException{
		String success = "102 Ask Update finished\r";
		String broken = "103 Ask Update process broken\r";
		
		socket_passive = null;
        try
        {
        	
         socket_passive = new ServerSocket(listenerDPort);
          Log.d(TAG, "Waiting Downloading...");
          
          ClientSocket_passive = socket_passive.accept();
          
          		new DataOutputStream(ClientSocket_passive.getOutputStream());
          		is_passive = new DataInputStream(ClientSocket_passive.getInputStream());
          		inp_passive = new BufferedReader(new InputStreamReader(ClientSocket_passive.getInputStream()));
          		
                Log.d(TAG, "Downloading...");
                
                
                Log.d(TAG,"File Downloading Started : terminal_updater.xml file");
                
                
          		String filename = "terminal_updater.xml";
          		FileOutputStream myOutput = null;
            	Charset.forName("UTF-8").newEncoder();
            	myOutput = openFileOutput(filename, MODE_WORLD_READABLE);


            		byte[] buffer = new byte[9096];
                    int length=0;
                    while ((length = is_passive.read(buffer))>0){
                        myOutput.write(buffer, 0, length);
                    }

                    myOutput.flush();
                    myOutput.close();
                    is_passive.close();
              		
      		  	Log.d(TAG,"File Created");

                
                //Log.d(TAG, success);
                ClientSocket_passive.close();
                socket_passive.close();
                
                return success;
        }
        catch (UnknownHostException e) 
        {
        	Log.e(TAG,e.getMessage());
           return broken;
        }
        catch (IOException e) 
        {
        	Log.e(TAG,e.getMessage());  
        	return broken;
        }finally {
			if (socket_passive != null) {
				try {
	                socket_passive.close();
	                ClientSocket_passive.close();
	                return success;
				} catch (IOException e) {
					Log.e(TAG,e.getMessage());
					return broken;
				}
			}
		}
		
	}
	
	class UpdateThreadFunctionRunnable implements Runnable{

		public void run() {
                     try {
						UpdateThread();
					} catch (XmlPullParserException e) {
						Log.e(TAG,e.getMessage());
					} catch (IOException e) {
						Log.e(TAG,e.getMessage());
					}
		}
	}
	
	private static final String ns = null;
	private void UpdateThread() throws XmlPullParserException, IOException{
		
		
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
	            //Log.d(TAG,xml_data);
	          }
    
	          	 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		         factory.setNamespaceAware(true);
		         XmlPullParser xpp = factory.newPullParser();

		         xpp.setInput( new StringReader (xml_data) );
		         
		         int eventType = xpp.getEventType();
		         
		     	@SuppressWarnings("unused")
		     	ArrayList<String> ActivityArray = new ArrayList<String>();
		        ArrayList<String> ComponentArray = new ArrayList<String>();
		     	@SuppressWarnings("unused")
		        ArrayList<String> TimelineArray = new ArrayList<String>();
		   
		         while (eventType != XmlPullParser.END_DOCUMENT) {
		        	 xpp.next();
		        	 eventType = xpp.getEventType();
		        	 
		        	 String tag_name = null;
		             
		        	 @SuppressWarnings("unused")
		             String act_names = null;
		        	 @SuppressWarnings("unused")
		        	 String act_startdatetime = null;
		             
		        	 @SuppressWarnings("unused")
		        	 String act_enddatetime = null;
		             String act_file = null;
		             String act_filetype = null;
		         	 @SuppressWarnings("unused")
		             String act_bgcolor = null;
		             String com_name = null;
		             String com_type = null;
		        	@SuppressWarnings("unused")
		             String com_x = null;
		        	@SuppressWarnings("unused")
		             String com_y = null;
		        	@SuppressWarnings("unused")
		             String com_w = null;
		        	@SuppressWarnings("unused")
		             String com_h = null;
		        	@SuppressWarnings("unused") 
		        	String com_borderwidth = null;
		        	@SuppressWarnings("unused") 
		        	String com_bordercolor = null;
		        	@SuppressWarnings("unused")
		        	String com_rounded = null;
		        	@SuppressWarnings("unused")
		        	String com_shadowsize = null;
		             String com_file = null;
		             String com_filetype = null;
		         	@SuppressWarnings("unused")
		             String time_name = null;
		        	@SuppressWarnings("unused")
		         	String time_subtype = null;
		        	@SuppressWarnings("unused")
		         	String time_startdatetime = null;
		        	@SuppressWarnings("unused")
		         	String time_enddatetime = null;
		             String time_file = null;
		             String time_filesubtype = null;
		         	@SuppressWarnings("unused")
		             String time_screentype = null;
		        	@SuppressWarnings("unused")
		             String time_default = null;
		        	
		        	 switch (eventType)
		             {
		        	 
		             case XmlPullParser.START_TAG: 
		                 tag_name = xpp.getName();
		             
		              
		              
						xpp.getAttributeValue(ns, "activityid");
		              
		              final String asds = "2";
		              if(tag_name.equalsIgnoreCase("activity")){
		            	  
		            	  act_file = xpp.getAttributeValue(ns,"file");
		            	  act_filetype = xpp.getAttributeValue(ns, "filetype");
		            	  //asds = xpp.getAttributeValue(ns, "activityid");
		            	  Log.d(TAG,"Activity id :" + asds);
		            	  //Log.d(TAG,act_id);
		            	 
		            	  if (TextUtils.isEmpty(act_file)) {
		            		    //Log.d(TAG, "String is empty or null!");
		            		} else {
		            			//Log.d(TAG,"Not Null");
			            		//Log.d(TAG,act_file + "  " + act_filetype + "");
			            		
		   
			    		         for (int i=0; i<arrDownloadableFiles.length ; i++) {
			    		        	 //Log.d(TAG,arrDownloadableFiles[i]);
			    		        	 if (act_filetype.equals(arrDownloadableFiles[i])) {
			    		        		 	DownloadFiles.add(asds + "," + act_file + "," + act_filetype);
			    	            		} else {
			    	            			//DownloadFiles.add(act_file);
			    	            		}
			    		         }
		            		}
	            	  
		              }
		              
		              if(tag_name.equalsIgnoreCase("component")){
		            	  com_name = xpp.getAttributeValue(ns, "name");
		            	  com_file = xpp.getAttributeValue(ns, "file");
		            	  com_filetype = xpp.getAttributeValue(ns, "filetype");
		            	  com_type = xpp.getAttributeValue(ns, "type");
		            	  //Log.d(TAG,com_name + " " + com_file + " " + com_filetype + " ");
		            	  ComponentArray.add(com_name);

		            	  if (TextUtils.isEmpty(com_file)) {

		            		} else {


		            			for (int y=0; y<arrDownloadableFiles.length; y++) {
			    		        	 if (com_filetype.equals(arrDownloadableFiles[y])) {
			    		        		 	DownloadFiles.add(asds + "," + com_file + "," + com_filetype);
			    	            		} else {
			    	            			//DownloadFiles.add(act_file);
			    	            		}
			    		         }
		            			//DownloadFiles.add(com_file);
		            		}
		            	  
		            	  if(com_type.trim().equals("weather")){
		            		  //FileDownloadWeather(act_id + "");
		            	  }
		              
		              }
		              
		              if(tag_name.equalsIgnoreCase("timeline")){
		            	  time_name = xpp.getAttributeValue(ns, "name");
		            	  time_file = xpp.getAttributeValue(ns, "file");
		            	  time_filesubtype = xpp.getAttributeValue(ns, "filetype");

		            	  if (TextUtils.isEmpty(time_file)) {

		            		} else {

			    		         for (int x=0; x<arrDownloadableFiles.length; x++) {
			    		        	 if (time_filesubtype.equals(arrDownloadableFiles[x])) {
			    		        		 	DownloadFiles.add(asds + "," + time_file + "," + time_filesubtype);
			    	            		} else {
			    	            		}
			    		         }

		            		}
		              }
		              
		              
		              if(tag_name.equalsIgnoreCase("specification")){
		            	  String datakey = xpp.getAttributeValue(ns, "datakey");
		            	  String datavalue = xpp.getAttributeValue(ns, "datavalue");
		            	 
		            	  
		            	  if (TextUtils.isEmpty(datakey)) {

		            		} else {

			    		         for (int x=0; x<arrDownloadableFiles.length; x++) {
			    		        	 if (datakey.equals(arrDownloadableFiles[x])) {
			    		        		    Log.d(TAG, "Spec ekledi : " + datakey + " " + datavalue);
			    		        		 	DownloadFiles.add(asds + "," + datavalue + "," + datakey);
			    	            		} else {
			    	            		}
			    		         }

		            		}
		              }
		              
		              break;
		              
		            }

		         }
		         
		         String [] stringArray = DownloadFiles.toArray(new String[DownloadFiles.size()]);
		         Log.d(TAG,"Download Files : " + DownloadFiles.size());
		         //Log.d(TAG,stringArray[0]);
		         for (int i=0; i<DownloadFiles.size(); i++) {
		        	 //Log.d(TAG,stringArray[i]);
		        	 if (TextUtils.isEmpty(stringArray[i])) {
	            		    //Log.d(TAG, "String is empty or null!");
	            		} else {
	            			Log.d(TAG,stringArray[i]);
	            			String[] separated = stringArray[i].split(",");
	            			Log.d(TAG,"Download Filesss : " + separated[0] + "---" + separated[2] + "---" + separated[1]);
	            			FileDownload(separated[0],separated[2],separated[1]," ",null);
	            			//FileDownload(separated[0],separated[2],separated[1]," ",null);
	            			
	            		}
		         }
		         
		         //FileDownload("1","textrss","activity_1_marqueue_8_0.xml"," ",null);
		         Log.d(TAG,"SuccessDownloadFiles : " + SuccessDownloadFiles.size());
		         if (SuccessDownloadFiles.size() == DownloadFiles.size()){
		        	 listener_status= "OFF";
		        	 removeAll(DownloadFiles);
		        	 removeAll(SuccessDownloadFiles);
		        	
					Log.d(TAG,"Listener : " + listener_status);
		         }
		}catch(IOException e){

			Log.e(TAG,e.getMessage());
			
		}
        
	}
	
	public void removeAll(Collection<?> c) {
	    Iterator<?> e = c.iterator();
	    while (e.hasNext()) {
	        if (c.contains(e.next())) {
	            e.remove();
	        }
	    }
	}
	
	public void FileDownload(final String activityid,final String filetype,final String filenames,final String specification, Object object) throws IOException{
	
		    		Log.d(TAG,"Filedownload Started");
		    		Socket s_filedownload = null;
		        	try {
		        		
		        		s_filedownload = new Socket(getIP, transmissionPort);
		    			BufferedReader in_filedownload = new BufferedReader(new InputStreamReader(s_filedownload.getInputStream()));
		    			BufferedWriter out_filedownload = new BufferedWriter(new OutputStreamWriter(s_filedownload.getOutputStream()));

		    			String inMsgs_filedownload = in_filedownload.readLine() + System.getProperty("line.separator");
		    			Log.d(TAG,inMsgs_filedownload);
		    			
		    			String outMsg = "TRANSFER " + activityid + " " + filetype + " " + filenames + " " + GetFileMD5(filenames) + " " + specification;
		    			out_filedownload.write(outMsg);
		    			out_filedownload.flush();
		    			Log.d(TAG, outMsg);
		    			
		    			String inMsg_filedownload = in_filedownload.readLine() + System.getProperty("line.separator");
		    			Log.d(TAG, inMsg_filedownload);
		    			
		    			if(inMsg_filedownload.trim().equals("201 ready for transfer")){
		    				downloadsocket_passives = null;
		    				@SuppressWarnings("unused")
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
		    	                	outputStreams = openFileOutput(filenames, MODE_PRIVATE);


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
		    			s_filedownload.close();
		    			
		    		} catch (IOException e) {
		    			Log.e(TAG,e.getMessage());
		    			s_filedownload.close();
		    		}

		        	Log.d(TAG,"File Downloading Closed");
		        	SuccessDownloadFiles.add("File Download ok" + filenames);
		        	
	}
	
	
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	public Document XMLfromString(String xml){

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
	    
	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	}
	public String md5(String s) { 
		try { 
	        
	        // Create MD5 Hash 
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5"); 
	        digest.update(s.getBytes()); 
	        byte messageDigest[] = digest.digest();
	 
	         // Create Hex String
	         StringBuffer hexString = new StringBuffer();
	         for (int i=0; i<messageDigest.length; i++)
	             hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	 
	     } catch (NoSuchAlgorithmException e) {
	         Log.e(TAG,e.getMessage());
	     }
	     return "";
	}
	
	public String GetFileMD5(String Filename) {
        
		File file = new File("/data/data/com.example.DigitalSignage/files/" + Filename.trim());

		Log.d(TAG,"Md5 filepath " + file.getAbsolutePath());
		
		if(file.exists()){

			try {
			Process process;
			process = Runtime.getRuntime().exec("md5sum " + file.getAbsolutePath());
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log=new StringBuilder();
            String line = "";
            
            while ((line = bufferedReader.readLine()) != null) {
              log.append(line);
            }
            
            Log.d(TAG,log.toString().substring(0, 32).trim().toUpperCase());
            
            return log.toString().substring(0, 32).trim().toUpperCase();
			
			} catch (IOException e) {
			Log.e(TAG,e.getMessage());
			}
		}
		else {
			return new String("0");
		}

		return Filename;
	}
	
	
	public String GetFileMD5Hash(String FileName) throws IOException  {
		File file = new File("/data/data/com.example.DigitalSignage/files/" + FileName.trim());

		Log.d(TAG,"Md5 filepath " + file.getAbsolutePath());
		
		if(file.exists()){
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				InputStream is = new FileInputStream("/data/data/com.example.DigitalSignage/files/" + FileName);
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
