/**
 * @author egegen
 *
 */

package com.example.listening;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.net.MalformedURLException;
import com.example.DigitalSignage.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
@SuppressWarnings({ "deprecation", "unused" })
@SuppressLint("SimpleDateFormat")
public class Listening extends Activity{
	DrawRect drawView;
	 // All static variables
    static final String URL = "terminal_updater.xml";
    
	private final String firm_name = "Gen";
	private final String TAG = firm_name + " Digital Signage Client";
	//private static final String TAG = "Update Listen Service";
	private int listenerPort = 33301;    
	private final int transmissionPort = 33311;
 	private final int weatherPanelRefreshPeriod = 10;
 	private final int exchangePanelRefreshPeriod = 1;
 	private Handler mHandler = new Handler();
    public Boolean readTimeLine = true;
    public Boolean systemTimerOnProcess = false;
    public Boolean datetimeTimerOnProcess = false;
    public Boolean firstprocess_afterstart = true;
    public Boolean needClearComponents = false;
    
    public String[] arrDownloadableFiles = {"image", "video", "document","txtrss","flash"};
    public String[] arrWeekOfDays = {"Pazar","Pazartesi", "Salı", "Çarşamba","Perşembe","Cuma","Cumartesi"};
    
    public String welcome = "001 Egegen Digital Signage Client Listener\r";
    public String asking = "101 Asking Update";
    public String asking_server = "UPDATEREADY";
    public String success = "102 Ask Update finished";
    public String broken = "103 Ask Update process broken";
    
    public String xmlread_d = null;
    public String listen_status = "OFF";
    
    UpdateListenService listen_service;
    
    Paint paints = new Paint();
	public String xml_data = null;
	private int DEFAULT_BUFFER_SIZE = 1024 * 20;
	
	private ImageView bg_image = null;
	private View contentView = null;
	private int Measuredwidth;
	private int Measuredheight;
	
	private final String START_LOADING_VIDEO = "start";
	private final String STOP_LOADING_VIDEO = "stop";
	private final String ns = null;
	private ArrayList<String> RssFeed = new ArrayList<String>();
	private String filename_image_xml;
	private ArrayList<String> Nodes_Draw = new ArrayList<String>();
	private ArrayList<String> Nodes_Video = new ArrayList<String>();
	private ArrayList<String> Nodes_Text = new ArrayList<String>();
	private ArrayList<String> Nodes_Marqueue = new ArrayList<String>();
	private ArrayList<String> Nodes_List = new ArrayList<String>();
	private ArrayList<String> Nodes_Image = new ArrayList<String>();
	private ArrayList<String> Nodes_Slider = new ArrayList<String>();
	private ArrayList<String> Nodes_Time = new ArrayList<String>(); 
	private ArrayList<String> Nodes_TimeDigital = new ArrayList<String>();
	private ArrayList<String> Nodes_Weather = new ArrayList<String>();
	private ArrayList<String> Nodes_Weather_xml = new ArrayList<String>();
	private ArrayList<String> Nodes_Exchange = new ArrayList<String>();
	private ArrayList<String> Nodes_Exchange_xml = new ArrayList<String>();
	private ArrayList<String> Nodes_Slider_Images = new ArrayList<String>();
	private ArrayList<String> Nodes_ListHeader = new ArrayList<String>();
	private ArrayList<String> Nodes_ListPot = new ArrayList<String>();
	 
	private String xx;
	private String yy;
	private String ww;
	private String hh;
	 
	private Handler transparencyHandler = new Handler();
	private Handler timerHandler = new Handler();
	
	private Handler StatusHandler = new Handler();
	
	private String listeners_status_stat = "";
	public int rec_count;
	
	public Timer WeatherTimer = new Timer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//this.requestWindowFeature(Window.FEATURE_NO_TITLE); 

		/*
		String line = "";
		try {
		 Process ifc = Runtime.getRuntime().exec("getprop service.adb.tcp.port");
		 BufferedReader bis = new BufferedReader(new InputStreamReader(ifc.getInputStream()));
		 line = bis.readLine();
		} catch (java.io.IOException e) {
		}
		Log.d(TAG,line);
		*/
		

		

		//Calendar c = Calendar.getInstance();
		//c.getTime();
		
		
		//String device_md5_hash = androidId + c.getTime().toString().replace(" ","").replace(":","").trim();
		//device_md5_hash = md5(device_md5_hash);
		//Log.d(TAG,"Android Device Md5 Hash :" + device_md5_hash);

		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		Log.v(TAG, "onCreate maxMemory:" + Long.toString(maxMemory));
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	    this.getWindow().getDecorView().setSystemUiVisibility(RelativeLayout.SYSTEM_UI_FLAG_LOW_PROFILE);
	    
	    // Hide status bar
	    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    this.getWindow().addFlags(RelativeLayout.SYSTEM_UI_FLAG_LOW_PROFILE);
	    this.getWindow().addFlags(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	    this.getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.activity_listening);
	    
		WindowManager.LayoutParams paramss = getWindow().getAttributes();
		Rect rects = new Rect(); 
		Window wins = getWindow();
		wins.getDecorView().getWindowVisibleDisplayFrame(rects);
        DisplayMetrics metricss = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metricss);   
        int screenHeights = metricss.heightPixels;
        int screenWidths = metricss.widthPixels;
	    paramss.x = 0;
	    paramss.height = screenHeights;  
	    paramss.width = screenWidths;  
	    paramss.y = 0;
	       
	    this.getWindow().setAttributes(paramss);
	       
	    final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
	    
		if (android.os.Build.VERSION.SDK_INT > 9) {

        	StrictMode.ThreadPolicy policy =

        	new StrictMode.ThreadPolicy.Builder().permitAll().build();

        	StrictMode.setThreadPolicy(policy);

        }
	    
		String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		Log.d(TAG,"Device ID : " + androidId);
	    
		String files_name = "radison_" + androidId + "_150220131015.dsl";
		Log.d(TAG,"Cert File Name : " + files_name);
		
		File filePaths = getFileStreamPath(files_name);
		Log.d(TAG,filePaths.toString());
        
		if (filePaths.exists()) {
        	try{
        		BufferedReader br = new BufferedReader(new FileReader(filePaths));
        	    String line;

        	    int lineCount = 0;
        	    while ((line = br.readLine()) != null) {
        	        lineCount++;
        	    }
        	    br.close();
        	    
        	    if(lineCount == 27){
        	    	
        	    	Log.d(TAG,"Cert Line Count :" + String.valueOf(lineCount));
        	    	runUpdateListenAsService();
        			
                	final Handler handler = new Handler(); 
                    Timer t = new Timer(); 
                    t.schedule(new TimerTask() {
                            public void run() { 
                                    handler.post(new Runnable() { 
                                            public void run() { 
                                             updateUI();
                                            } 
                                    });
                            } 
                    }, 0,100); 
                    
                    final Handler handlers = new Handler(); 
                    Timer tt = new Timer(); 
                    tt.schedule(new TimerTask() { 
                            public void run() { 
                            		handlers.post(new Runnable() { 
                                            public void run() { 
                                            	Status_Check();
                                            } 
                                    }); 
                            } 
                    }, 0,60000);
                    
        	    }else if(lineCount > 27){
        	    	Log.d(TAG,"Cert Line Count :" + String.valueOf(lineCount));
        	    	listening_on();
        	    }else if(lineCount < 27){
        	    	Log.d(TAG,"Cert Line Count :" + String.valueOf(lineCount));
        	    	listening_on();
        	    }
        	    
        	}
        	catch (IOException e) {
        		Log.e(TAG,e.getMessage());
        	}

        }else {
        	
        	listening_on();
        
        }

		
	}
	
	final Runnable updateRunnable = new Runnable() {
        public void run() {
            //call the activity method that updates the UI
            updateUI();
        }
    };
    
    public void updateUI()
    {
    	
        if(listeners_status_stat != UpdateListenService.listener_status){
            //
            if(UpdateListenService.listener_status.equalsIgnoreCase("OFF")){
                Log.d(TAG,"Listener Status : " + UpdateListenService.listener_status);
                RelativeLayout roots = (RelativeLayout)findViewById(R.id.mainroot);
                ClearRelLayout(roots);
                
                File filePaths = getFileStreamPath("terminal_updater.xml");
        		Log.d(TAG,filePaths.toString());
                if (filePaths.exists()) {
                	listening_off();
                }else {
                	listening_on();
            		String dates_comp = compareDates("2013-06-17 06:00:01");
            		Log.d(TAG,"Dates Now : " + dates_comp);
                }
            }
            
            if(UpdateListenService.listener_status.equalsIgnoreCase("ON")){
                Log.d(TAG,"Listener Status : " + UpdateListenService.listener_status);
                RelativeLayout roots = (RelativeLayout)findViewById(R.id.mainroot);
                ClearRelLayout(roots);
                listening_on();
            }
            
            listeners_status_stat = UpdateListenService.listener_status;
            
        }
 
    }
    
    public void doSomeHardWorks()
    {
    	StatusHandler.post(updateRunnable);
    }
    
	public void ClearRelLayout(RelativeLayout RL){
		for(int x=0;x<RL.getChildCount();x++){
	        if(RL.getChildAt(x) instanceof FrameLayout){
	            FrameLayout FL = (FrameLayout) RL.getChildAt(x);
	            FL.removeAllViewsInLayout();
	        }
	    }
	    RL.removeAllViewsInLayout();
	}
	
	public String listening_on() {

		Measuredwidth = 0;
	    Measuredheight = 0;
	    Point size = new Point();
	    WindowManager w = getWindowManager();

	      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
	            w.getDefaultDisplay().getSize(size);

	            Measuredwidth = size.x;
	            Measuredheight = size.y; 
	    	    System.out.println(Measuredwidth);
	    	    System.out.println(Measuredheight);

	          }else{
	            Display d = w.getDefaultDisplay(); 
	            Measuredwidth = d.getWidth(); 
	            Measuredheight = d.getHeight();
	            
	    	    System.out.println(Measuredwidth);
	    	    System.out.println(Measuredheight);

	          }
		////
		
	    int imgvw_w = 200;
		int imgvw_h = 200;
	    int imgvw_x = Measuredwidth - 220;
	    int imgvw_y = Measuredheight - 132;
		final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
		final ImageView LogoImageView = new ImageView(this);
		final LinearLayout LogoImageViewlinearlayout = new LinearLayout(this);
		final LinearLayout.LayoutParams ImageViewlp = new LinearLayout.LayoutParams(imgvw_w, imgvw_h);
        ImageViewlp.setMargins(imgvw_x, imgvw_y, imgvw_x + imgvw_w, imgvw_y + imgvw_h);
        LogoImageView.setImageResource(R.drawable.egegen_logo_white);
        LogoImageView.setScaleType(ScaleType.FIT_END);
        LogoImageView.setAdjustViewBounds(true);
        root.addView(LogoImageViewlinearlayout);
        LogoImageViewlinearlayout.addView(LogoImageView, ImageViewlp);
    	
    	////
		final LinearLayout newTextViewLayout = new LinearLayout(this);
		final TextView textview = new TextView(this);
		final LinearLayout.LayoutParams TextViewlp = new LinearLayout.LayoutParams(Measuredwidth, Measuredheight);
		TextViewlp.setMargins(0, 0, Measuredwidth, Measuredheight);
        root.addView(newTextViewLayout);
        newTextViewLayout.addView(textview,TextViewlp);
    	textview.setVisibility(View.VISIBLE);
    	textview.setGravity(Gravity.CENTER);
    	textview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    	textview.setText("Güncelleniyor...");
    	textview.setTextColor(Color.WHITE);
    	textview.setBackgroundColor(Color.TRANSPARENT);
    	textview.setTextSize(50);
    	textview.setTypeface(null, Typeface.BOLD_ITALIC);
    	
	    final AlphaAnimation  newTextViewanimation=   new AlphaAnimation(0, 1);
	    newTextViewanimation.setDuration(2000);
	    newTextViewanimation.setInterpolator(new BounceInterpolator());
	    newTextViewanimation.setRepeatMode(Animation.RESTART);
	    newTextViewanimation.setRepeatCount(Animation.INFINITE);
    	
    	textview.setAnimation(newTextViewanimation);
    	textview.startAnimation(newTextViewanimation);
    	
		return "ON";
	
	
	}
	
	public String listening_off() {
		
		//WeatherTimer.cancel();
		Measuredwidth = 0;
	    Measuredheight = 0;
	    Point size = new Point();
	    WindowManager w = getWindowManager();

	      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
	            w.getDefaultDisplay().getSize(size);

	            Measuredwidth = size.x;
	            Measuredheight = size.y; 
	    	    System.out.println(Measuredwidth);
	    	    System.out.println(Measuredheight);

	          }else{
	            Display d = w.getDefaultDisplay(); 
	            Measuredwidth = d.getWidth(); 
	            Measuredheight = d.getHeight();
	            
	    	    System.out.println(Measuredwidth);
	    	    System.out.println(Measuredheight);

	          }
	      
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
	        	 
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder db;
					db = dbf.newDocumentBuilder();
					
					Document doc = db.parse(new InputSource(new ByteArrayInputStream(xml_data.getBytes("utf-8"))));
		        	 doc.getDocumentElement().normalize();
		        	 NodeList nodeList = doc.getElementsByTagName("activity");

		        	 for (int i = 0; i < nodeList.getLength(); i++) {
		        		 //Node node = nodeList.item(i);
		        		 //Element fstElmnt = (Element) node;
		        		 
		        		 NodeList nodeLists = doc.getElementsByTagName("activity");
		        		 Element componentElement = (Element) nodeLists.item(i);
		        		 
		        		 //Log.d(TAG,("Component Name= "
				         //		 + componentElement.getAttribute("name") + " X: " + componentElement.getAttribute("x") + " Y: " + componentElement.getAttribute("y") + " W: "+ componentElement.getAttribute("w") + " H: "+ componentElement.getAttribute("h")));
		        		 
		        		 
		        		 filename_image_xml = componentElement.getAttribute("file");
		        	     
		        	     
		        	 }
		        	 
		        	 
				} catch (ParserConfigurationException e) {
					
					Log.e(TAG,e.getMessage());
				} catch (SAXException e) {
					
					Log.e(TAG,e.getMessage());
				}
	        	
	          }
	          
	          runUpdateWLEService();
	          
	          Log.d(TAG,filename_image_xml);
	  		
		    	File filePath = getFileStreamPath(filename_image_xml);
				Log.d(TAG,filePath.toString());
		        if (filePath.exists()) {
		        	
					final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
					//final RelativeLayout relativeLayout = new RelativeLayout(this);
					final ImageView BackgroundImageView = new ImageView(this);
					final LinearLayout BackgroundImageViewlinearlayout = new LinearLayout(this);
					final LinearLayout.LayoutParams BackgroundImageViewlp = new LinearLayout.LayoutParams(Measuredwidth, Measuredheight);
					BackgroundImageViewlp.setMargins(0, 0, 0 + Measuredwidth, 0 + Measuredheight);
			        BackgroundImageView.setImageDrawable(Drawable.createFromPath(filePath.toString()));
			        BackgroundImageView.setScaleType(ScaleType.FIT_XY);
			        root.addView(BackgroundImageViewlinearlayout);
			        BackgroundImageViewlinearlayout.addView(BackgroundImageView, BackgroundImageViewlp);
			        
			        
		        	final String xml_reader = readTerminalXML("terminal_updater.xml");
		        	Log.d(TAG,xml_reader);
		        	
		        	/*
		        	Thread SetDrawViewThread = null;
		    	    Runnable SetDrawView_Runnable = new SetDrawView_Runs();
		    	    SetDrawViewThread= new Thread(SetDrawView_Runnable);   
		    	    SetDrawViewThread.start();
		        	*/
		        	
			  	    Handler SetVideoXmlHandler = new Handler();
					SetVideoXmlHandler.post(new Runnable() {
    		                @Override
    		            public void run() {
    		                	try {
    		                		SetVideoPlayerXml(xml_reader);
								} catch (IOException e) {
									Log.e(TAG,e.getMessage());
								} catch (Exception e) {
									Log.e(TAG,e.getMessage());
								}
    		                }
    		            });
    		    	    
					

					
					 
					 Handler SetTextXmlHandler = new Handler();
					 SetTextXmlHandler.post(new Runnable() {
    		                @Override
    		            public void run() {
    		                	try {
    		                		SetTextXml(xml_reader);
								} catch (IOException e) {
									Log.e(TAG,e.getMessage());
								} catch (Exception e) {
									Log.e(TAG,e.getMessage());
								}
    		                }
    		            });
					 
					
					 Handler SetMarqueueXmlHandler = new Handler();
					 SetMarqueueXmlHandler.postDelayed(new Runnable() {
    		                @Override
    		            public void run() {
    		                	try {
    		                		SetMarqueueXml(xml_reader);
								} catch (IOException e) {
									Log.e(TAG,e.getMessage());
								} catch (Exception e) {
									Log.e(TAG,e.getMessage());
								}
    		                }
    		            },2000);
					 
					 

					 Handler SetImageXmlHandler = new Handler();
					 SetImageXmlHandler.post(new Runnable() {
    		                @Override
    		            public void run() {
    		                	try {
    		                		SetImageXml(xml_reader);
								} catch (IOException e) {
									Log.e(TAG,e.getMessage());
								} catch (Exception e) {
									Log.e(TAG,e.getMessage());
								}
    		                }
    		            });

		        	
		    	    Handler SetWeatherXmlHandler = new Handler();
		    	    SetWeatherXmlHandler.postDelayed(new Runnable() {
		                @Override
		            public void run() {
		                	try {
		                		SetWeatherXml(xml_reader);
							} catch (IOException e) {
								Log.e(TAG,e.getMessage());
							} catch (Exception e) {
								Log.e(TAG,e.getMessage());
							}
		                }
		            },3000);
		    	    
		    	    Handler SetExchangeXmlHandler = new Handler();
		    	    SetExchangeXmlHandler.postDelayed(new Runnable() {
		                @Override
		            public void run() {
		                	try {
		                		SetExchangeXml(xml_reader);
							} catch (IOException e) {
								Log.e(TAG,e.getMessage());
							} catch (Exception e) {
								Log.e(TAG,e.getMessage());
							}
		                }
		            },3000);
		    	    
		    	
		    	    Handler SetSliderXmlHandler = new Handler();
		    	    SetSliderXmlHandler.postDelayed(new Runnable() {
		                @Override
		            public void run() {
		                	try {
		                		SetSliderXml(xml_reader);
							} catch (IOException e) {
								Log.e(TAG,e.getMessage());
							} catch (Exception e) {
								Log.e(TAG,e.getMessage());
							}
		                }
		            },4000);
		    	   

					Handler SetListXmlHandler = new Handler();
					SetListXmlHandler.postDelayed(new Runnable() {
   		                @Override
   		            public void run() {
   		                	try {
   		                		SetListXml(xml_reader);
								} catch (IOException e) {
									Log.e(TAG,e.getMessage());
								} catch (Exception e) {
									Log.e(TAG,e.getMessage());
								}
   		                }
   		            },5000);
		    	   
					
					 Handler SetTimeXmlHandler = new Handler();
					 SetTimeXmlHandler.postDelayed(new Runnable() {
   		                @Override
   		            public void run() {
   		                	try {
   		                		SetTimeXml(xml_reader);
								} catch (IOException e) {
									Log.e(TAG,e.getMessage());
								} catch (Exception e) {
									Log.e(TAG,e.getMessage());
								}
   		                }
   		            },5000);
					 
					 
		 	    }else{
		 			
		 		    int imgvw_w = 200;
		 			int imgvw_h = 200;
		 		    int imgvw_x = Measuredwidth - 220;
		 		    int imgvw_y = Measuredheight - 132;
		 			final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
		 			final ImageView LogoImageView = new ImageView(this);
		 			final LinearLayout LogoImageViewlinearlayout = new LinearLayout(this);
		 			final LinearLayout.LayoutParams ImageViewlp = new LinearLayout.LayoutParams(imgvw_w, imgvw_h);
		 	        ImageViewlp.setMargins(imgvw_x, imgvw_y, imgvw_x + imgvw_w, imgvw_y + imgvw_h);
		 	        LogoImageView.setImageResource(R.drawable.egegen_logo_white);
		 	        LogoImageView.setScaleType(ScaleType.FIT_END);
		 	        LogoImageView.setAdjustViewBounds(true);
		 	        root.addView(LogoImageViewlinearlayout);
		 	        LogoImageViewlinearlayout.addView(LogoImageView, ImageViewlp);
		 	    	
		 	    	////
		 			final LinearLayout newTextViewLayout = new LinearLayout(this);
		 			final TextView textview = new TextView(this);
		 			final LinearLayout.LayoutParams TextViewlp = new LinearLayout.LayoutParams(Measuredwidth, Measuredheight);
		 			TextViewlp.setMargins(0, 0, Measuredwidth, Measuredheight);
		 	        root.addView(newTextViewLayout);
		 	        newTextViewLayout.addView(textview,TextViewlp);
		 	    	textview.setVisibility(View.VISIBLE);
		 	    	textview.setGravity(Gravity.CENTER);
		 	    	textview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		 	    	textview.setText("Güncelleniyor...");
		 	    	textview.setTextColor(Color.WHITE);
		 	    	textview.setBackgroundColor(Color.TRANSPARENT);
		 	    	textview.setTextSize(50);
		 	    	textview.setTypeface(null, Typeface.BOLD_ITALIC);
		 	    	
		 		    final AlphaAnimation  newTextViewanimation=   new AlphaAnimation(0, 1);
		 		    newTextViewanimation.setDuration(2000);
		 		    newTextViewanimation.setInterpolator(new BounceInterpolator());
		 		    newTextViewanimation.setRepeatMode(Animation.RESTART);
		 		    newTextViewanimation.setRepeatCount(Animation.INFINITE);
		 	    	
		 	    	textview.setAnimation(newTextViewanimation);
		 	    	textview.startAnimation(newTextViewanimation);
		    		 }
		    		        
		    		        
		    		}catch(IOException e){
		    			Log.e(TAG,e.getMessage());
		    		} catch (Exception e) {
		    			Log.e(TAG,e.getMessage());
					}
		return "OFF Closed";
	}

	
	class SetDrawView_Runs implements Runnable{
	    public void run() {
	            while(!Thread.currentThread().isInterrupted()){
	                try {
	                    //Thread.sleep(60000);
	                	SetDrawViewXml("terminal_updater.xml");
	                    Thread.sleep(300000000);
	                } catch (InterruptedException e) {
	                        Thread.currentThread().interrupt();
	                }catch(Exception e){
	                	Thread.currentThread().interrupt();
	                }
	            }
	    }
	}
	


	public Runnable Listen_Status() {
		String ON = "ON";
		if(TextUtils.isEmpty(com.example.listening.UpdateListenService.listener_status)){
			Log.e(TAG,"Holy Crap! It's a bullshit! Listener status failed.");
		}
		if (com.example.listening.UpdateListenService.listener_status.trim().equals(ON)) {
			Log.d(TAG,"Listener Up Now");
			
		}else {
			Log.d(TAG,"Activity Mode On");
		}
		
		return null;
	
	}
	
	
	public Runnable log_Export() {
    	Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        String now_time = c.getTime().toString();
        
		 try {
             Process process = Runtime.getRuntime().exec("logcat -d");
             BufferedReader bufferedReader = new BufferedReader(
             new InputStreamReader(process.getInputStream()));

             StringBuilder log=new StringBuilder();
             String line = "";
             while ((line = bufferedReader.readLine()) != null) {
               log.append(line);
             }
             Log.d(TAG,log.toString());
           } catch (IOException e) {
           }
		 

		 
		 return null;
	
	}
	
	
	public String readTerminalXML(String filename){
		xmlread_d = "";
		try {
		      InputStream in=openFileInput(filename);

		        Reader read = new InputStreamReader(in);
		        StringWriter write = new StringWriter();

		        int c = -1;
		        while ((c = read.read()) != -1)
		        {
		            write.write(c);
		        }
		        write.flush();
		        xmlread_d = write.toString();
	
		    }
		    catch (java.io.FileNotFoundException e) {
		    	Log.e(TAG,e.getMessage());
		    } catch (IOException e) {
		    	Log.e(TAG,e.getMessage());
			}
		return xmlread_d;
	}

	
	public void SetTimeXml(String data) throws IOException,Exception {
		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	            	
   		        		 xx = componentElements.getAttribute("x");
   		        		 yy = componentElements.getAttribute("y");
   		        		 ww = componentElements.getAttribute("w");
   		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	        	              String subtype = elementtimeline.getAttribute("subtype");
	        	              String filesubtype = elementtimeline.getAttribute("filesubtype");
	        	              if(filesubtype.isEmpty())
	        	            	  filesubtype = "null";
	    		        		 if(subtype.equalsIgnoreCase("time")){
	    		        			 Log.d(TAG,"time was found");
	    		        			 //Nodes_Time.add(ww + "," + hh + "," + xx + "," + yy);
	    		        			 
	    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
		    		        			 
		    		        				Node node1 = nodeListtimeline.item(z);
		    		        			 	Element elementMain1=(Element) node1;
		    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
		    		        			 	
		    		        			 	String getir = null;
		    		        			 	String pull = null;
		 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
		 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
		 	        	                		  
		 	        	                		 getir += "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue");
		 	        	                		//Nodes_Text.add(ww + "," + hh + "," +xx + "," + yy + "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull);
		 	        	                		
		 	        	                	}
		 	        	                	
		 	        	                	getir = ww + "," + hh + "," +xx + "," + yy + "," + getir;
		 	        	                	Nodes_Time.add(getir.replace("null", "").trim());
		 	        	                	//Log.d(TAG,getir.replace("null", "").trim());
		 	        	                	
		    		        			 }
	    		        		 }
	        	            }


         }catch (ParserConfigurationException e) {
				Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			} 
		
		String[] stringArrays = Nodes_Time.toArray(new String[Nodes_Time.size()]);
		Log.d(TAG,"Time Sizes : " + Nodes_Time.size());
		for (int i=0; i<Nodes_Time.size(); i++) {
       	 if (TextUtils.isEmpty(stringArrays[i])) {
       		} else {
       			String[] separated = stringArrays[i].split(",");
       			Log.d(TAG,separated[0] + " --- " + separated[1] + " --- " + separated[2] + " --- " + separated[3]+ " --- " + separated[5]+ " --- " + separated[6]+ " --- " + separated[7]+ " --- " + separated[8]+ " --- " + separated[9]+ " --- " + separated[10]);
       			SetTime(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),separated[5],separated[6],separated[7],separated[8],separated[9],separated[10]);
       			
       		}
        }
		removeAll(Nodes_Time);
	}
	
	//DrawView
	public void SetDrawViewXml(String filename) throws IOException,Exception {
		FileInputStream fIn1 = null;
		Charset.forName("UTF-8").newEncoder();
        drawView = new DrawRect(this);
		try{

			  fIn1 = openFileInput("terminal_updater.xml");
			  InputStream in1 = fIn1;
			  //System.out.println(System.getProperty("file.encoding"));
	          byte[] buf1 = new byte[DEFAULT_BUFFER_SIZE];
	          int len1;
	          while ((len1 = in1.read(buf1)) > 0){
	        	 xml_data = new String(buf1, 0, len1);

				}
	          
	          try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder db;
					db = dbf.newDocumentBuilder();
					
					 Document doc = db.parse(new InputSource(new ByteArrayInputStream(xml_data.getBytes("utf-8"))));
		        	 doc.getDocumentElement().normalize();
		        	 System.out.println ("Root element of the doc is " + 
		             doc.getDocumentElement().getNodeName());
		        	
		        				NodeList nodeListCountry=doc.getElementsByTagName("component");
	
		        	            for (int i = 0; i < nodeListCountry.getLength(); i++) {
	
		        	             Element componentElement = (Element) nodeListCountry.item(i);
		        	            	
	    		        		 xx = componentElement.getAttribute("x");
	    		        		 yy = componentElement.getAttribute("y");
	    		        		 ww = componentElement.getAttribute("w");
	    		        		 hh = componentElement.getAttribute("h");
	    		        		 
	    		        		 Nodes_Draw.add(componentElement.getAttribute("x") + "," + componentElement.getAttribute("y") + "," + componentElement.getAttribute("w") + "," + componentElement.getAttribute("h"));

		        	            }


	          }catch (ParserConfigurationException e) {
					Log.e(TAG,e.getMessage());
				} catch (SAXException e) {
					Log.e(TAG,e.getMessage());
				} catch (NumberFormatException e) {
					Log.e(TAG,e.getMessage());
				} catch (Exception e) {
					Log.e(TAG,e.getMessage());		
				}
	          
				}catch(IOException e){
					Log.e(TAG,e.getMessage());
				}
		
		String [] stringArray = Nodes_Draw.toArray(new String[Nodes_Draw.size()]);
		Log.d(TAG,"Draw Size : " + Nodes_Draw.size());
         //Log.d(TAG,stringArray[0]);
         for (int i=0; i<Nodes_Draw.size(); i++) {
        	 //Log.d(TAG,stringArray[i]);
        	 if (TextUtils.isEmpty(stringArray[i])) {
        		    //Log.d(TAG, "String is empty or null!");
        		} else {
        			//Log.d(TAG,stringArray[i]);
        			String[] separated = stringArray[i].split(",");
        			//Log.d(TAG,separated[0] + "---" + separated[1] + "---" + separated[2]+ "---" + separated[3]);
        			
	        		 drawView.xx.add(separated[0]);
	        	     drawView.yy.add(separated[1]);
	        	     drawView.ww.add(separated[2]);
	        	     drawView.hh.add(separated[3]);
        		
        		}
         }
         
         removeAll(Nodes_Draw);
        // Custom draws view
         /*
		drawView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        LinearLayout linear = (LinearLayout)findViewById(R.id.background_content);
        linear.addView(drawView);
        */
	}
	
	public void SetImageXml(String data) throws IOException,Exception {
    	

		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	            	
   		        		 xx = componentElements.getAttribute("x");
   		        		 yy = componentElements.getAttribute("y");
   		        		 ww = componentElements.getAttribute("w");
   		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	        	              String subtype = elementtimeline.getAttribute("subtype");
	        	              String filesubtype = elementtimeline.getAttribute("file");
	    		        		 if(subtype.equalsIgnoreCase("image")){
	    		        			 //Log.d(TAG,"Image was found");
	    		        			 Nodes_Image.add(elementtimeline.getAttribute("file") + "," + ww + "," + hh + "," + xx + "," + yy);
	    		        		 }
	        	              

	        	            }


         }catch (ParserConfigurationException e) {
        	 Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
		
		String[] stringArrays = Nodes_Image.toArray(new String[Nodes_Image.size()]);
		Log.d(TAG,"Image Size : " + Nodes_Image.size());
		for (int i=0; i<Nodes_Image.size(); i++) {
       	 if (TextUtils.isEmpty(stringArrays[i])) {
       		} else {
       			//Log.d(TAG,stringArrays[i]);
       			String[] separated = stringArrays[i].split(",");
       			//Log.d(TAG,"Image : " + separated[0] + "" + Integer.parseInt(separated[1])+ "" + Integer.parseInt(separated[2])+ "" + Integer.parseInt(separated[3])+ "" + Integer.parseInt(separated[4]));
       			SetImage(separated[0],Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),Integer.parseInt(separated[4]));
       		 
       		}
        }
		removeAll(Nodes_Image);
		
	}

	
	public void SetSliderXml(String data) throws IOException,Exception {
    	

		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	            	
  		        		 xx = componentElements.getAttribute("x");
  		        		 yy = componentElements.getAttribute("y");
  		        		 ww = componentElements.getAttribute("w");
  		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	        	              String subtype = elementtimeline.getAttribute("subtype");
	        	              String filesubtype = elementtimeline.getAttribute("file");
	        	              String timeline_name = elementtimeline.getAttribute("name");
	    		        		 if(subtype.equalsIgnoreCase("slider:tip1")){
	    		        			 Log.d(TAG,"Slider was found");
	    		        			 Nodes_Slider.add(timeline_name + "," + ww + "," + hh + "," + xx + "," + yy);
	    		        		 }
	        	              

	        	            }


        }catch (ParserConfigurationException e) {
        		Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
		

		String[] stringArrays = Nodes_Slider.toArray(new String[Nodes_Slider.size()]);
		Log.d(TAG,"Slider Size : " + Nodes_Slider.size());
		for (int i=0; i<Nodes_Slider.size(); i++) {
      	 if (TextUtils.isEmpty(stringArrays[i])) {
      		} else {
      			//Log.d(TAG,stringArrays[i]);
      			String[] separated = stringArrays[i].split(",");
      			Log.d(TAG,"Slider : " + separated[0] + "  " + Integer.parseInt(separated[1])+ "  " + Integer.parseInt(separated[2])+ "  " + Integer.parseInt(separated[3])+ "  " + Integer.parseInt(separated[4]));
      			SetSlider(separated[0],Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),Integer.parseInt(separated[4]));
      		}
       }
		
		removeAll(Nodes_Slider);
		
	}
	//TextView
	public void SetTextXml(String data) throws IOException,Exception {



		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	            	
   		        		 xx = componentElements.getAttribute("x");
   		        		 yy = componentElements.getAttribute("y");
   		        		 ww = componentElements.getAttribute("w");
   		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	        	              String subtype = elementtimeline.getAttribute("subtype");
	        	              String filesubtype = elementtimeline.getAttribute("filesubtype");
	        	              if(filesubtype.isEmpty())
	        	            	  filesubtype = "null";
	    		        		 if(subtype.equalsIgnoreCase("text")){
	    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
	    		        			 
	    		        				Node node1 = nodeListtimeline.item(z);
	    		        			 	Element elementMain1=(Element) node1;
	    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
	    		        			 	
	    		        			 	String getir = null;
	    		        			 	String pull = null;
	 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
	 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
	 	        	                		  if (elementspecification.getAttribute("datakey").equalsIgnoreCase("value")){
	 	        	                			  
	 	 	        	                		    Node node2 = nodeListsspecification.item(c);
	 	 	        	                		    Element element = (Element) node2;
	 	 	        	                		    NodeList value = element.getElementsByTagName("value");
	 	 	        	                		    Element line = (Element) value.item(0);
	 	 	        	                		    pull = line.getTextContent().trim();
	 	        	                		  }
	 	        	                		  
	 	        	                		getir += "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull;
	 	        	                		
	 	        	                		//Nodes_Text.add(ww + "," + hh + "," +xx + "," + yy + "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull);
	 	        	                		
	 	        	                	}
	 	        	                	
	 	        	                	getir = ww + "," + hh + "," +xx + "," + yy + "," + getir;
	 	        	                	Nodes_Text.add(getir.replace("null", "").trim());
	 	        	                	//Log.d(TAG,getir.replace("null", "").trim());
	    		        			 }
	    		        			 
	    		        		 
	    		        		 }
	        	              

	        	            }


         }catch (ParserConfigurationException e) {
        	 	Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}  
		 
		String[] stringArrays = Nodes_Text.toArray(new String[Nodes_Text.size()]);
		Log.d(TAG,"Text Size : " + Nodes_Text.size());
		for (int i=0; i<Nodes_Text.size(); i++) {
       	 if (TextUtils.isEmpty(stringArrays[i])) {

       		} else {
       			String[] separated = stringArrays[i].split(",");
       			SetTexts(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),separated[5],separated[6],separated[7],separated[8],separated[9],separated[10]);
       		}
        }
		removeAll(Nodes_Text);
	}
	
	
	//List
	public void SetListXml(String data) throws IOException,Exception {



			 try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

		        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

		        	             Element componentElements = (Element) nodeListCountrys.item(i);
		        	            	
	   		        		 xx = componentElements.getAttribute("x");
	   		        		 yy = componentElements.getAttribute("y");
	   		        		 ww = componentElements.getAttribute("w");
	   		        		 hh = componentElements.getAttribute("h");


		        	                Node node=nodeListCountrys.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
		        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

		        	              String subtype = elementtimeline.getAttribute("subtype");
		        	              String filesubtype = elementtimeline.getAttribute("filesubtype");
		        	              String files = elementtimeline.getAttribute("file");
		        	              String filestype = elementtimeline.getAttribute("filetype");
		        	              String getir = null;
		        	              if(filesubtype.isEmpty())
		        	            	  filesubtype = "null";
		    		        		 if(subtype.equalsIgnoreCase("list")){
		    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
		    		        			 
		    		        				Node node1 = nodeListtimeline.item(z);
		    		        			 	Element elementMain1=(Element) node1;
		    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");

		    		        			 	String pull = null;
		 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
		 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
		 	        	                		  
		 	        	                		getir += "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull;
		 	        	                		
		 	        	                		//Nodes_Text.add(ww + "," + hh + "," +xx + "," + yy + "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull);
		 	        	                		
		 	        	                	}
		 	        	                	
		 	        	                	
		 	        	                	
		    		        			 }
		    		        			 	getir = ww + "," + hh + "," +xx + "," + yy + "," + "," + getir + "," + files + "," + filestype;
		    		        			 	Nodes_List.add(getir.replace("null", "").replace(",,",",").replace(",,",",").trim());
		 	        	                	Log.d(TAG,"List : " + getir.replace("null", "").replace(",,",",").replace(",,",",").trim());
		    		        		 
		    		        		 }
		        	              

		        	            }


	         }catch (ParserConfigurationException e) {
					Log.e(TAG,e.getMessage());
				} catch (SAXException e) {
					Log.e(TAG,e.getMessage());
				} catch (NumberFormatException e) {
					Log.e(TAG,e.getMessage());
				} catch (Exception e) {
					Log.e(TAG,e.getMessage());
				}  
			 
			String[] stringArrays_List = Nodes_List.toArray(new String[Nodes_List.size()]);
			Log.d(TAG,"List Size : " + Nodes_List.size());
			for (int i=0; i<Nodes_List.size(); i++) {
	       	 if (TextUtils.isEmpty(stringArrays_List[i])) {

	       		} else {
	       			String[] separated = stringArrays_List[i].split(",");
	       			SetList(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),separated[4],separated[5],separated[6],separated[7],separated[8],separated[9],separated[10],separated[11],separated[12],separated[13],separated[14]);
	       		}
	        }
			removeAll(Nodes_List);
	}
	
	//Marqueue
	public void SetMarqueueXml(String data) throws IOException,Exception {



			 try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

		        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

		        	             Element componentElements = (Element) nodeListCountrys.item(i);
		        	            	
	   		        		 xx = componentElements.getAttribute("x");
	   		        		 yy = componentElements.getAttribute("y");
	   		        		 ww = componentElements.getAttribute("w");
	   		        		 hh = componentElements.getAttribute("h");


		        	                Node node=nodeListCountrys.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
		        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

		        	              String subtype = elementtimeline.getAttribute("subtype");
		        	              String filesubtype = elementtimeline.getAttribute("filesubtype");
		        	              String files = elementtimeline.getAttribute("file");
		        	              String filestype = elementtimeline.getAttribute("filetype");
		        	              String getir = "";
		        	              if(filesubtype.isEmpty())
		        	            	  filesubtype = "null";
		    		        		 if(subtype.equalsIgnoreCase("prompter")){
		    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
		    		        			 
		    		        				Node node1 = nodeListtimeline.item(z);
		    		        			 	Element elementMain1=(Element) node1;
		    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
		    		        			 	
		    		        			 	
		    		        			 	String pull = "";
		 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
		 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
		 	        	                		  if (elementspecification.getAttribute("datakey").equalsIgnoreCase("value")){
		 	        	                			  
		 	 	        	                		    Node node2 = nodeListsspecification.item(c);
		 	 	        	                		    Element element = (Element) node2;
		 	 	        	                		    NodeList value = element.getElementsByTagName("value");
		 	 	        	                		    Element line = (Element) value.item(0);
		 	 	        	                		    pull = line.getTextContent().trim();
		 	        	                		  }
		 	        	                		  
		 	        	                		getir += "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull;
		 	        	                		
		 	        	                		//Nodes_Text.add(ww + "," + hh + "," +xx + "," + yy + "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") + pull);
		 	        	                		
		 	        	                	}
		 	        	                	
		 	        	                	
		 	        	                	
		    		        			 }
		    		        			 	getir = ww + "," + hh + "," +xx + "," + yy + "," + "," + getir + "," + files + "," + filestype;
		 	        	                	Nodes_Marqueue.add(getir.replace("null", "").trim());
		 	        	                	Log.d(TAG,"Marqueue : " + getir.replace("null", "").trim());
		    		        		 
		    		        		 }
		        	              

		        	            }


	         }catch (ParserConfigurationException e) {
					Log.e(TAG,e.getMessage());
				} catch (SAXException e) {
					Log.e(TAG,e.getMessage());
				} catch (NumberFormatException e) {
					Log.e(TAG,e.getMessage());
				} catch (Exception e) {
					Log.e(TAG,e.getMessage());
				}  
			 
			String[] stringArrays_Marqueue = Nodes_Marqueue.toArray(new String[Nodes_Marqueue.size()]);
			Log.d(TAG,"Marqueue Size : " + Nodes_Marqueue.size());
			for (int i=0; i<Nodes_Marqueue.size(); i++) {
	       	 if (TextUtils.isEmpty(stringArrays_Marqueue[i])) {

	       		} else {
	       			String[] separated = stringArrays_Marqueue[i].split(",");
	       			SetMarqueue(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),separated[6],separated[7],separated[8],separated[9],separated[10],separated[11],separated[12]);
	       		}
	        }
			removeAll(Nodes_Marqueue);
	}
	
	public void SetVideoPlayerXml(String data) throws IOException,Exception {

		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	            	
   		        		 xx = componentElements.getAttribute("x");
   		        		 yy = componentElements.getAttribute("y");
   		        		 ww = componentElements.getAttribute("w");
   		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	        	              String subtype = elementtimeline.getAttribute("subtype");
	        	              String filesubtype = elementtimeline.getAttribute("filesubtype");
	        	              if(filesubtype.isEmpty())
	        	            	  filesubtype = "null";
	        	              String getir = "";
	    		        		 if(subtype.equalsIgnoreCase("video")){
	    		        			 //Log.d(TAG,"videos was found");
	    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
		    		        			 
		    		        				Node node1 = nodeListtimeline.item(z);
		    		        			 	Element elementMain1=(Element) node1;
		    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
		    		        			 	
		    		        			 	
		    		        			 	String pull = "";
		 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
		 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
		 	        	                		  
		 	        	                		getir = "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue") ;

		 	        	                	}
		 	        	                	
		 	        	                	
		 	        	                	
		    		        			 }
	    		        			 getir = elementtimeline.getAttribute("filetype") + "," + elementtimeline.getAttribute("file") + ","  + ww + "," + hh + "," +xx + "," + yy + "," + filesubtype +  ","  + getir;
	 	        	                	
	    		        			 Nodes_Video.add(getir.replace("null", "").trim());
	    		        			 Log.d(TAG,"Video TAG : " + getir.replace("null", "").trim());
	    		        			 //Nodes_Video.add(elementtimeline.getAttribute("filetype") + "," + elementtimeline.getAttribute("file") + "," + xx + "," + yy + "," + ww + "," + hh + "," + filesubtype);
	    		        		 }
	        	              

	        	            }


         }catch (ParserConfigurationException e) {
				Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
		 
		String[] stringArrays = Nodes_Video.toArray(new String[Nodes_Video.size()]);
		Log.d(TAG,"Video Size : " + Nodes_Video.size());
		for (int i=0; i<Nodes_Video.size(); i++) {
       	 //Log.d(TAG,stringArray[i]);
       	 if (TextUtils.isEmpty(stringArrays[i])) {
       		    //Log.d(TAG, "String is empty or null!");
       		} else {
       			Log.d(TAG,stringArrays[i]);
       			String[] separated = stringArrays[i].split(",");
       			Log.d(TAG,"Video Geldi : " + separated[0] + " --- " + separated[1] + " --- " + separated[2] + " --- " + separated[3] + " --- " + separated[4] + " --- " + separated[5] + " --- " + separated[6] + " --- " + separated[7]+ " --- " + separated[8]);
       			
       			if(separated[6].equalsIgnoreCase("standartUrl")){
       				SetVideoPlayer(separated[0],separated[1],Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),Integer.parseInt(separated[4]),Integer.parseInt(separated[5]),separated[8]);
       			} else if(separated[6].equalsIgnoreCase("youtube")){
       				Log.d(TAG,"Holy Crap. is this bullshit ? Youtube ! Oh Lord !");
       				//SetVideoPlayer_Youtube(separated[0],separated[1],Integer.parseInt(separated[4]),Integer.parseInt(separated[5]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]));
       			}else if(separated[6].equalsIgnoreCase("")){
       				SetVideoPlayer(separated[0],separated[1],Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),Integer.parseInt(separated[4]),Integer.parseInt(separated[5]),separated[8]);
       			}
    			 
       		}
        }
	
		removeAll(Nodes_Video);
	
	}
	
	public void removeAll(Collection<?> c) {
	    Iterator<?> e = c.iterator();
	    while (e.hasNext()) {
	        if (c.contains(e.next())) {
	            e.remove();
	        }
	    }
	}
	
	public final int IncDay(int day) {
		if (day==1){
			day = 0;
		}
		if (day==2){
			day = 1;
		}
		if (day==3){
			day = 2;
		}
		if (day==4){
			day = 3;
		}
		if (day==5){
			day = 4;
		}
		if (day==6){
			day = 5;
		}
		if (day==7){
			day = 6;
		}
		if (day==8){
			day = 0;
		}
		if (day==9){
			day = 1;
		}
		if (day==10){
			day = 2;
		}
		return day;
	}
	
	public void SetWeatherXml(String data) throws IOException,Exception {

		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	             String subtype = componentElements.getAttribute("type");
	        	             if(subtype.equalsIgnoreCase("weather")){
	        	            	 xx = componentElements.getAttribute("x");
	       		        		 yy = componentElements.getAttribute("y");
	       		        		 ww = componentElements.getAttribute("w");
	       		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
	    		        			 
	    		        				Node node1 = nodeListtimeline.item(z);
	    		        			 	Element elementMain1=(Element) node1;
	    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
	    		        			 	
	    		        			 	String getir = null;
	    		        			 	String pull = null;
	 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
	 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
	 	        	                		getir += "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue");
	 	        	                		
	 	        	                		//Nodes_Weather.add(ww + "," + hh + "," +xx + "," + yy + "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue"));
	 	        	                		
	 	        	                	}
	 	        	                	
	 	        	                	getir = ww + "," + hh + "," +xx + "," + yy + "," + getir;
	 	        	                	Nodes_Weather.add(getir.replace("null", "").trim());
	 	        	                	Log.d(TAG,getir.replace("null", "").trim());
	    		        			 }
	    		        			 
	    		        		 
	    		        		 }
	        	              

	        	            }


         }catch (ParserConfigurationException e) {
				Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
		
		String weather_status ="";
		weather_status = WeatherXmlRead("weather.xml");
		if(weather_status.equalsIgnoreCase("success"))
		{
		String[] stringArrays = Nodes_Weather.toArray(new String[Nodes_Weather.size()]);
		Log.d(TAG,"Weather Size : " + Nodes_Weather.size());
		for (int i=0; i<Nodes_Weather.size(); i++) {
       	 //Log.d(TAG,stringArray[i]);
       	 if (TextUtils.isEmpty(stringArrays[i])) {
       		    //Log.d(TAG, "String is empty or null!");
       		} else {
       			//Log.d(TAG,stringArrays[i]);
       			String[] separated = stringArrays[i].split(",");
       			Log.d(TAG,separated[0] + "--" + separated[1] + "--" + separated[2] + "--" + separated[3] + "--" + separated[4] + "--" + separated[5] + "--" + separated[6] + "--" + separated[7] + "--" + separated[8]);
       			SetWeather(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),separated[5],separated[6],separated[7],separated[8]);
       		}
        }
		removeAll(Nodes_Weather);
		}
		else if(weather_status.equalsIgnoreCase("failed")){
			WeatherXmlRead("weather.xml");
		}else if(weather_status.equalsIgnoreCase("")){
			
		}
		
		
	}


	public String WeatherXmlRead (String filename) throws Exception,IOException{
		
		if(UpdateWLEService.WLE_listener_status.equalsIgnoreCase("ON")){
			Log.d(TAG,"UpdateWLEService running on");
			return "failed";
		} else if(UpdateWLEService.WLE_listener_status.equalsIgnoreCase("OFF")){
			Log.d(TAG,"UpdateWLEService running off");
			
			
			FileInputStream fIn11 = null;
			Charset.forName("UTF-8").newEncoder();
			try{

				  fIn11 = openFileInput(filename);
				  InputStream in11 = fIn11;
				  //System.out.println(System.getProperty("file.encoding"));
		          byte[] buf1 = new byte[DEFAULT_BUFFER_SIZE];
		          int len1;
		          while ((len1 = in11.read(buf1)) > 0){
		        	 xml_data = new String(buf1, 0, len1);

					}
		          
			
			 try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(xml_data.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	NodeList nodeListCountrys=docs.getElementsByTagName("weather");

		        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {




		        	                Node node=nodeListCountrys.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListweatherdata=elementMain.getElementsByTagName("weatherdata");
		        	                

		    		        			 for(int z =0;z < nodeListweatherdata.getLength();z++) {

		    		        				 Element elementweatherdata=(Element) nodeListweatherdata.item(z);
		    		        				  	        	                		
		    		        				 Nodes_Weather_xml.add(elementweatherdata.getAttribute("city").replace("null", "").trim() + 
		        	                				"," + elementweatherdata.getAttribute("view_country").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("view_city").replace("null", "").trim() +
		        	                				"," + elementweatherdata.getAttribute("current_condition").replace("null", "").trim() +
		        	                				"," + elementweatherdata.getAttribute("current_temp_c").replace("null", "").trim() +
		        	                				"," + elementweatherdata.getAttribute("current_wind_condition").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day0_low").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day0_high").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day0_condition").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day1_low").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day1_high").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day1_condition").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day2_low").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day2_high").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day2_condition").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day3_low").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day3_high").replace("null", "").trim()+
		        	                				"," + elementweatherdata.getAttribute("day3_condition").replace("null", "").trim());
		 	        	             
		 	        	                	
		 	        	                	
		    		        			 }

		    		        			 
		        	            }


	         }catch (ParserConfigurationException e) {
					Log.e(TAG,e.getMessage());
				} catch (SAXException e) {
					Log.e(TAG,e.getMessage());
				} catch (NumberFormatException e) {
					Log.e(TAG,e.getMessage());
				} catch (Exception e) {
					Log.e(TAG,e.getMessage());
					return "failed";
				}  
					}catch(IOException e){
					    Log.e(TAG,e.getMessage());
					    return "failed";
					}

			return "success";
		}
		return "";
	}

	public void SetExchangeXml(String data) throws IOException,Exception {

		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	             String subtype = componentElements.getAttribute("type");
	        	             if(subtype.equalsIgnoreCase("rate")){
	        	            	 xx = componentElements.getAttribute("x");
	       		        		 yy = componentElements.getAttribute("y");
	       		        		 ww = componentElements.getAttribute("w");
	       		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
	    		        			 
	    		        				Node node1 = nodeListtimeline.item(z);
	    		        			 	Element elementMain1=(Element) node1;
	    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
	    		        			 	
	    		        			 	String getir = null;
	    		        			 	String pull = null;
	 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {
	 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
	 	        	                		getir += "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue");
	 	        	                		
	 	        	                		if(elementspecification.getAttribute("datakey").equalsIgnoreCase("exchange_code")) {
	 	        	                			getir += "=" + elementspecification.getAttribute("dataorder");
	 	        	                		}
	 	        	                			
	 	        	                		//Nodes_Weather.add(ww + "," + hh + "," +xx + "," + yy + "," + elementspecification.getAttribute("datakey") + "=" + elementspecification.getAttribute("datavalue"));
	 	        	                		
	 	        	                	}
	 	        	                	
	 	        	                	getir = ww + "," + hh + "," +xx + "," + yy + "," + getir;
	 	        	                	Nodes_Exchange.add(getir.replace("null", "").trim());
	 	        	                	//Log.d(TAG,getir.replace("null", "").trim());
	    		        			 }
	    		        			 
	    		        		 
	    		        		 }
	        	              

	        	            }


        }catch (ParserConfigurationException e) {
				Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
		
		
		ExchangeXmlRead("rate.xml");
		
		String[] stringArrays = Nodes_Exchange.toArray(new String[Nodes_Exchange.size()]);
		Log.d(TAG,"Exchange Size : " + Nodes_Exchange.size());
		for (int i=0; i<Nodes_Exchange.size(); i++) {
      	 //Log.d(TAG,stringArray[i]);
      	 if (TextUtils.isEmpty(stringArrays[i])) {
      		    //Log.d(TAG, "String is empty or null!");
      		} else {
      			//Log.d(TAG,stringArrays[i]);
      			
      			String[] separated = stringArrays[i].split(",");
      			int a = stringArrays[i].split(",").length;
      			Log.d(TAG, "Exchange Splited Size : " + a);
      			Log.d(TAG,"Exchange : " + separated[0] + "--" + separated[1] + "--" + separated[2] + "--" + separated[3] + "--" + separated[5] + "--" + separated[6] + "--" + separated[7] + "--" + separated[8]+ "--" + separated[9]+ "--" + separated[10]);
      			
      			/*
      			for(int b=0; b < stringArrays[i].split(",").length; b++){
      				Log.d(TAG, "" + separated[b]);
      			}
      			*/
      			
      			SetExchange(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]),Integer.parseInt(separated[3]),separated[5],separated[6],separated[7],separated[8],separated[9],separated[10]);
      		
      		}
      	 
       }
		
		removeAll(Nodes_Exchange);
		
	}
	
	public void ExchangeXmlRead (String filename) throws Exception,IOException{
		
		FileInputStream fIn11 = null;
		Charset.forName("UTF-8").newEncoder();
		try{

			  fIn11 = openFileInput(filename);
			  InputStream in11 = fIn11;
			  //System.out.println(System.getProperty("file.encoding"));
	          byte[] buf1 = new byte[DEFAULT_BUFFER_SIZE];
	          int len1;
	          while ((len1 = in11.read(buf1)) > 0){
	        	 xml_data = new String(buf1, 0, len1);

				}
	          
		
		 try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(xml_data.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("exchange");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListweatherdata=elementMain.getElementsByTagName("exchangedata");
	        	                

	    		        			 for(int z =0;z < nodeListweatherdata.getLength();z++) {

	    		        				 Element elementweatherdata=(Element) nodeListweatherdata.item(z);
	    		        				  	        	                		
	    		        				 Nodes_Exchange_xml.add(elementweatherdata.getAttribute("exchange_code").replace("null", "").trim() + 
	        	                				"--" + elementweatherdata.getAttribute("exchange_date").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("exchange_time").replace("null", "").trim() +
	        	                				"--" + elementweatherdata.getAttribute("unit").replace("null", "").trim() +
	        	                				"--" + elementweatherdata.getAttribute("exchange_name").replace("null", "").trim() +
	        	                				"--" + elementweatherdata.getAttribute("exchange_buying").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("exchange_selling").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("effective_buying").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("effective_selling").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("free_buying").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("free_selling").replace("null", "").trim()+
	        	                				"--" + elementweatherdata.getAttribute("exchange_order").replace("null", "").trim());
	    		        			 }

	    		        			 
	        	            }


         }catch (ParserConfigurationException e) {
				Log.e(TAG,e.getMessage());
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			} catch (NumberFormatException e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}  
				}catch(IOException e){
				    Log.e(TAG,e.getMessage());
				}

		
	}
	
	public void SetImage(String file,int width,int height,int x,int y) throws IOException,Exception {

		//Log.d(TAG,"Image File : " + file + " w : " + width + " height : " + height + " x : " + x + " y : " + y);
		
		try {
			final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
			//final RelativeLayout relativeLayout = new RelativeLayout(this);
			final ImageView newImageView = new ImageView(this);
			final LinearLayout newImageViewlinearlayout = new LinearLayout(this);
			final LinearLayout.LayoutParams ImageViewlp = new LinearLayout.LayoutParams(width, height);
	        ImageViewlp.setMargins(x, y, x + width, y + height);
	        File filePath = getFileStreamPath(file);
	        newImageView.setImageDrawable(Drawable.createFromPath(filePath.toString()));
	        newImageView.setScaleType(ScaleType.FIT_XY);
	        root.addView(newImageViewlinearlayout);
	        newImageViewlinearlayout.addView(newImageView, ImageViewlp);
	        //setContentView(relativeLayout, videoviewlp);
		}catch(Exception e){
		    Log.e(TAG,e.getMessage());
		}

       
	}
	
	public void SetSlider(String timeline,int width,int height,int x,int y) throws IOException,Exception {

		Log.d(TAG,"Image File : " + timeline + " w : " + width + " height : " + height + " x : " + x + " y : " + y);

		final String xml_reader = readTerminalXML("terminal_updater.xml");
		
		try {
				DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dbs = null;
	        	dbs = dbfs.newDocumentBuilder();
				
				 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(xml_reader.getBytes("utf-8"))));
	        	 docs.getDocumentElement().normalize();
	        	
	        	NodeList nodeListCountrys=docs.getElementsByTagName("component");

	        	            for (int i = 0; i < nodeListCountrys.getLength(); i++) {

	        	             Element componentElements = (Element) nodeListCountrys.item(i);
	        	            	
		        		 xx = componentElements.getAttribute("x");
		        		 yy = componentElements.getAttribute("y");
		        		 ww = componentElements.getAttribute("w");
		        		 hh = componentElements.getAttribute("h");


	        	                Node node=nodeListCountrys.item(i);
	        	                Element elementMain=(Element) node;
	        	                NodeList nodeListtimeline=elementMain.getElementsByTagName("timeline");
	        	                Element elementtimeline=(Element) nodeListtimeline.item(0);

	        	              String subtype = elementtimeline.getAttribute("subtype");
	        	              String filesubtype = elementtimeline.getAttribute("file");
	        	              String timeline_name = elementtimeline.getAttribute("name");
	    		        		 if(timeline_name.equalsIgnoreCase(timeline)){
	    		        			 Log.d(TAG,"Slider images was found");
	    		        			 //Nodes_Slider.add(timeline_name + "," + ww + "," + hh + "," + xx + "," + yy);
	    		        			 
	    		        			 for(int z =0;z < nodeListtimeline.getLength();z++) {
		    		        			 
		    		        				Node node1 = nodeListtimeline.item(z);
		    		        			 	Element elementMain1=(Element) node1;
		    		        			 	NodeList nodeListsspecification=elementMain1.getElementsByTagName("specification");
		    		        			 	
		    		        			 	String getir = null;
		    		        			 	String pull = null;
		 	        	                	for(int c=0;c <nodeListsspecification.getLength();c++) {

		 	        	                		Element elementspecification=(Element) nodeListsspecification.item(c);
		 	        	                		Log.d(TAG,"Slider Images Spec : " +  elementspecification.getAttribute("datavalue"));
		 	        	                		Nodes_Slider_Images.add( elementspecification.getAttribute("datavalue"));
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
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
		 
		try {
			final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
			final ImageView newImageView = new ImageView(this);
			final LinearLayout newImageViewlinearlayout = new LinearLayout(this);
			final LinearLayout.LayoutParams ImageViewlp = new LinearLayout.LayoutParams(width, height);
	        ImageViewlp.setMargins(x, y, x + width, y + height);

			animate(newImageView, 0,true); 
			
	        newImageView.setScaleType(ScaleType.FIT_XY);
	        root.addView(newImageViewlinearlayout);
	        newImageViewlinearlayout.addView(newImageView, ImageViewlp);
		}catch(Exception e){
		    Log.e(TAG,e.getMessage());
		}
		
		removeAll(Nodes_Slider_Images);
		
	}	

	private void animate(final ImageView imageView, final int imageIndex, final boolean forever) {

			final int imagesToShow[];
			final String[] stringArrays = Nodes_Slider_Images.toArray(new String[Nodes_Slider_Images.size()]);
			final int images = Nodes_Slider_Images.size();
   			File filePath = getFileStreamPath(stringArrays[imageIndex]);
   			imageView.setImageDrawable(Drawable.createFromPath(filePath.toString()));
		
		    int fadeInDuration = 1;
		    int timeBetween = 8000;
		    int fadeOutDuration = 1;

		    //imageView.setVisibility(View.INVISIBLE);
		    
		    Animation fadeIn = new AlphaAnimation(0, 1);
		    fadeIn.setInterpolator(new DecelerateInterpolator());
		    fadeIn.setDuration(fadeInDuration);

		    Animation fadeOut = new AlphaAnimation(1, 0);
		    fadeOut.setInterpolator(new AccelerateInterpolator());
		    fadeOut.setStartOffset(fadeInDuration + timeBetween);
		    fadeOut.setDuration(fadeOutDuration);
		    
		    AnimationSet animation = new AnimationSet(false);
		    animation.addAnimation(fadeIn);
		    animation.addAnimation(fadeOut);
		    animation.setRepeatCount(1);
		    imageView.setAnimation(animation);
		    
		    animation.setAnimationListener(new AnimationListener() {
		        public void onAnimationEnd(Animation animation) {
		            if (images - 1 > imageIndex) {
		                animate(imageView, imageIndex + 1,forever);
		            }
		            else {
		                if (forever == true){
		                animate(imageView, 0,forever);  
		                }
		            }
		        }
		        public void onAnimationRepeat(Animation animation) {

		        }
		        public void onAnimationStart(Animation animation) {

		        }
		    });
		}
	
	public Animation AnimatorRandom(Animation animation) {
		
		
		return animation;
		
				
	}
	
	public void SetTime(int width,int height,int x,int y,String time_timeFormat,String time_timeGMT, String time_timeGrType,String time_timeGrStartColor,String time_timeGrFinishColor,String time_timeForeColor) throws IOException,Exception {

			String[] text_sizes = time_timeFormat.split("=");
			String[] time_timeGMTs = time_timeGMT.split("=");
			Log.d(TAG,"Time File :" + text_sizes[1]);
			
			if(text_sizes[1].equalsIgnoreCase("digital")) {
				Log.d(TAG,"digital clock was found");
				Log.d(TAG,"Time GMT : " + time_timeGMTs[1].substring(0, 1) + " -- " + time_timeGMTs[1].substring(1, 3));
				final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
				//final RelativeLayout relativeLayout = new RelativeLayout(this);
				final LinearLayout newDigitalClocklinearlayout = new LinearLayout(this);
				final DigitalClock newDigitalClock = new DigitalClock(this);
				//final MyDigitalClock newDigitalClock = new MyDigitalClock(this);
				final LinearLayout.LayoutParams newDigitalClocklp = new LinearLayout.LayoutParams(width, height);
		        newDigitalClocklp.setMargins(x, y, x + width, y + height);
		        root.addView(newDigitalClocklinearlayout);
		        newDigitalClocklinearlayout.addView(newDigitalClock,newDigitalClocklp);
		        
			}else if(text_sizes[1].equalsIgnoreCase("analog")){
				Log.d(TAG,"analog clock was found");
				Log.d(TAG,"Time GMT : " + time_timeGMTs[1].substring(0, 1) + " -- " + time_timeGMTs[1].substring(1, 3));
				
				
				final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
				//final RelativeLayout relativeLayout = new RelativeLayout(this);
				final RelativeLayout newAnalogClocklinearlayout = new RelativeLayout(this);
				final AnalogClock newAnalogClock = new AnalogClock(this);
				//final MyAnalogClock newAnalogClock = new MyAnalogClock(this);
				final RelativeLayout.LayoutParams AnalogClocklp = new RelativeLayout.LayoutParams(width, height);
				AnalogClocklp.leftMargin = x;
				AnalogClocklp.topMargin = y;
		        root.addView(newAnalogClocklinearlayout);
		        newAnalogClocklinearlayout.addView(newAnalogClock,AnalogClocklp);
		        
	        /*
			try {
			
				final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
				final RelativeLayout newAnalogClocklayout = new RelativeLayout(this);
				final WebView Analogclock_webwiews = new WebView(this);
				Analogclock_webwiews.clearCache(true);
				final RelativeLayout.LayoutParams Analogclocklp = new RelativeLayout.LayoutParams(width, height);
				Analogclocklp.leftMargin = x;
				Analogclocklp.topMargin = y;
		        roots.addView(newAnalogClocklayout);
		        newAnalogClocklayout.addView(Analogclock_webwiews, Analogclocklp);
		        
		        Analogclock_webwiews.setBackgroundColor(Color.TRANSPARENT);
				Analogclock_webwiews.getSettings().setJavaScriptEnabled(true);
		        
				String html_analog_clock = "<!DOCTYPE HTML>" +
						"<html>" +
						"<head>" +
						"<meta http-equiv='Content-type' content='text/html; charset=utf-8'/>" +
						  "     <title>jqClock</title>" +
						 "     <script src='file:///android_asset/jquery.js'></script>" +
						  "    <script src='file:///android_asset/jqclock.js'></script>" +
						  "    <link rel='stylesheet' href='file:///android_asset/jqclock.css'/>" +
						  "     <style>" +
						"#clock .jqc-clock-face {" +
						"    background-color: #66f;" +
						"}" +
						"#local .jqc-clock-face{" +
						"    background: url('file:///android_asset/local.png') no-repeat scroll 0 0 transparent;" +
						"}" +
						".jqc-clock-sec span {" +
						"    background: url('file:///android_asset/seconds.png') no-repeat scroll 0 0 transparent;" +
						"    box-shadow: 0 0;" +
						"    height: 106px;" +
						"    margin-top: 36px;" +
						"    width: 4px;" +
						"}" +
						".jqc-clock-min span {" +
						"    background: url('file:///android_asset/minutes.png') no-repeat scroll 0 0 transparent;" +
						"    box-shadow: 0 0;" +
						"    height: 106px;" +
						"    margin-top: 51px;" +
						"    width: 6px;" +
						"}" +
						".jqc-clock-hour span {" +
						"    background: url('file:///android_asset/hours.png') no-repeat scroll 0 0 transparent;" +
						"    box-shadow: 0 0;" +
						"    height: 106px;" +
						"    margin-top: 69px;" +
						"    width: 10px;" +
						"} "+
						"        </style>" +
						"        <script>" +
						"jQuery(function () {" +
						"    jQuery('#local').clock({" +
						"        graduations: 0," +
						"        size: 250" +
						"    });" +
						"});" +
						"        </script>" +
						"    </head>" +
						"    <body>" +
						"            <div id='local'></div>" +
						"    </body>" +
						"</html>";

		     	String mime = "text/html";
		 		String encoding = "utf-8";
				Analogclock_webwiews.loadDataWithBaseURL(null, html_analog_clock, mime, encoding, null);
			
			}catch(Exception e){
	        	 Log.e(TAG,e.getMessage());
	         }
		*/
		        
			}
       
	}
	
	public void SetVideoPlayer(String filetype,String url,int width,int height,int x,int y,final String sounds) throws IOException,Exception {
		Log.d(TAG,"Stream , width " + width + " height : " + height + " x : " + x + " y : " + y + " filetype: " + filetype  + "url: " + url + " sound:" + sounds);
		//Log.d(TAG,"Video Player toplam : " + (x + width));
		//final ProgressDialog progDailog;
		final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
		final RelativeLayout newVideoLayout = new RelativeLayout(this);
		final VideoView newVideoView = new VideoView(this);
        newVideoView.setId(x + y);
        final RelativeLayout.LayoutParams videoviewlp = new RelativeLayout.LayoutParams(width, height);
        videoviewlp.leftMargin = x;
        videoviewlp.topMargin = y;
        //videoviewlp.setMargins(x, y, x + width, y + height);
        root.addView(newVideoLayout);
        newVideoLayout.addView(newVideoView,videoviewlp);
        newVideoView.setLayoutParams(videoviewlp);
        
        
		try {
			if(filetype.equalsIgnoreCase("stream")){
				Uri video = Uri.parse(url);
	            newVideoView.setVideoURI(video);
	           Log.d(TAG,"Stream : " + video);
			} else if (filetype.equalsIgnoreCase("video")){
				Log.d(TAG,"Not Stream : " + getFilesDir().getAbsolutePath()+"/" +url);
				newVideoView.setVideoPath(getFilesDir().getAbsolutePath()+"/" +url);
			}
			
            newVideoView.setBackgroundColor(Color.TRANSPARENT);
            newVideoView.setOnErrorListener(mOnErrorListener);

            //progDailog = ProgressDialog.show(this, "Please wait ...", "Retrieving data ...", true);

            newVideoView.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                	//progDailog.dismiss();
                	newVideoView.start();
                    mp.setLooping(true);
       	        	String[] get_sound = sounds.split("=");
       	        	
                   if(get_sound[1].equalsIgnoreCase("off")){
                	   mp.setVolume(0, 0);
                	   Log.d(TAG, "sound off");
                   }else if(get_sound[1].equalsIgnoreCase("on")){
                	   mp.setVolume(0, 100);
                	   Log.d(TAG, "sound on");
                   }
                }
            });
            

         }catch(Exception e){
        	 Log.e(TAG,e.getMessage());
         }
	}

	private void SetVideoPlayer_Youtube(String filetype,String url,int width,int height,int x,int y) throws IOException,Exception{
		
		try {
		final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
		//final RelativeLayout relativeLayout = new RelativeLayout(this);
		final WebView webwiews = new WebView(this);
        final RelativeLayout.LayoutParams webviewlp = new RelativeLayout.LayoutParams(width, height);
        webviewlp.setMargins(x, y, x + width, y + height);
        roots.addView(webwiews, webviewlp);
        //setContentView(relativeLayout, videoviewlp);
        webwiews.clearCache(true);
        
        webwiews.setBackgroundColor(Color.TRANSPARENT); 
		String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
		webwiews.getSettings().setUserAgentString(ua);
		webwiews.getSettings().setPluginState(PluginState.ON);
		webwiews.getSettings().setLoadWithOverviewMode(true);
		webwiews.getSettings().setJavaScriptEnabled(true);
		
		//webwiews.loadUrl("https://www.youtube.com/embed/" + url + "?autoplay=1&controls=0&loop=1&rel=0&showinfo=0&autohide=1&playlist=" + url + "");
		
		String html = "<html><head></head><body>" +
		"<iframe width='640' height='360' src='https://www.youtube.com/embed/" + url + "?feature=player_embedded&autoplay=1&cc_load_policy=1&controls=0&loop=1&rel=0&showinfo=0&autohide=1&iv_load_policy=3&playlist=" + url + "' frameborder='0' allowfullscreen></iframe>" +
				"";
		
     	String mime = "text/html";
 		String encoding = "utf-8";
     	webwiews.loadDataWithBaseURL(null, html, mime, encoding, null);
     	
		//webwiews.loadUrl("http://www.egegen.com");
		}catch(Exception e){
			Log.e(TAG,e.getMessage());
        }
	}
	
	
	public void SetTexts(int width,int height,int x,int y, String text_size, String font,String font_weight, String text_bgcolor,String text_forecolor,String value) throws IOException,Exception {
		Log.d(TAG,"TextView : width " + width + " height : " + height + " x : " + x + " y : " + y + " text_size : " + text_size+ " font : " + font+ " font_weight : " + font_weight+ " text_bgcolor : " + text_bgcolor+ " text_forecolor : " + text_forecolor+ " value : " + value);
	
		
			final RelativeLayout root = (RelativeLayout) findViewById(R.id.mainroot);
			//final RelativeLayout relativeLayout = new RelativeLayout(this);
			final LinearLayout newlinearlayout = new LinearLayout(this);
			final TextView newTextView = new TextView(this);
	        final LinearLayout.LayoutParams settextlp = new LinearLayout.LayoutParams(width, height);
	        settextlp.setMargins(x,y,x + width ,y + height);
	        //relativeLayout.addView(newTextView, settextlp);
	        //setContentView(relativeLayout);
	        newTextView.setGravity(Gravity.CENTER);
	        newTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	        root.addView(newlinearlayout);
	        newlinearlayout.addView(newTextView, settextlp);
	        
	        String[] values = value.split("=");
	        newTextView.setText(values[1]);
	        Log.d(TAG,values[1]);
	        String[] text_bgcolors = text_bgcolor.split("=");
	        newTextView.setBackgroundColor(Color.parseColor(text_bgcolors[1]));
	       
	        String[] text_forecolors = text_forecolor.split("=");
	        newTextView.setTextColor(Color.parseColor(text_forecolors[1]));
	        
	        String[] text_sizes = text_size.split("=");
	        newTextView.setTextSize(Float.parseFloat(text_sizes[1]));
	 
	}
	
	
	public void SetList(final int width,final int height,final int x,final int y,final String list_name,final String list_bgcolor,final String list_caption,final String list_captionsize,final String list_captiontextcolor,final String list_bordersize,final String list_bordercolor,final String list_tablecaptionsize,final String list_tablecaptiontextcolor,final String list_tabletextsize,final String list_tabletextcolor) throws IOException,Exception {
	
		Log.d(TAG,"SetList!");
			
		try {
			//String pull = Marqueue_GetText(files);
    		final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
    		final LinearLayout newListlayout = new LinearLayout(this);
    		final WebView List_webwiews = new WebView(this);
    		List_webwiews.clearCache(true);
    		final LinearLayout.LayoutParams Listlp = new LinearLayout.LayoutParams(width, height);
    		Listlp.setMargins(x, y, x + width, y + height);
            roots.addView(newListlayout);
            newListlayout.addView(List_webwiews, Listlp);
            
            List_webwiews.setBackgroundColor(Color.TRANSPARENT); 
    		String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
    		List_webwiews.getSettings().setUserAgentString(ua);
    		//marqueue_webwiews.getSettings().setPluginState(PluginState.ON);
    		//marqueue_webwiews.getSettings().setLoadWithOverviewMode(true);
    		List_webwiews.getSettings().setJavaScriptEnabled(true);

            String[] list_bgcolors = list_bgcolor.split("=");
            String[] list_forecolors = list_captiontextcolor.split("=");
            String[] list_captiontextcolors = list_captiontextcolor.split("=");
            String[] list_tabletextsizes = list_tabletextsize.split("=");
            String[] list_tabletextcolors = list_tabletextcolor.split("=");
            String[] list_captionsizes = list_captionsize.split("=");
            String[] list_bordersizes = list_bordersize.split("=");
            String[] list_bordercolors = list_bordercolor.split("=");
            String[] list_tablecaptionsizes = list_tablecaptionsize.split("=");
            String[] list_tablecaptiontextcolors = list_tablecaptiontextcolor.split("=");
            String[] list_captions = list_caption.split("=");
            
            float scale = (height / 1.40f);
            String scales = "" + scale;
            Log.d(TAG, "Marqueue Scale : " + scales.substring(0, 2).trim());
            String list_xmlname = List_GetXmlFileName("terminal_updater.xml");
            List_GetXml(list_xmlname);

            //Log.d(TAG,"List Header : " + list_headers[0]);
            String recs ="";
    		String html_list = new String("<!DOCTYPE html>" +
	    				  "<html>" +
	    				  "<head>" +
	    				  "<meta http-equiv='content-type' content='text/html; charset=UTF-8' />" +
	    				  "<title>List</title>" +
	    				  "<style type='text/css'>" +
	    				  "html, body {overflow:hidden; border:0; margin: 0;padding: 0;height: 100%;width: auto;background-color: " + list_bgcolors[1] + ";font-family: Helvetica, Sans Serif; font-size: " + list_tabletextsizes[1] + "px;color: " + list_tabletextcolors[1] + ";}" +
	    				  ".listCaption {padding: 4px;color: " + list_captiontextcolors[1] + "; font-size:" + list_captionsizes[1] + "px;font-weight:bold;text-align: center;}" +
	    				  ".listTable {width:100%;border-collapse: collapse;}" +
	    				  ".listTable tr td {padding:4px;border:" + list_bordersizes[1] + "px solid " + list_bordercolors[1] + ";}" +
	    				  ".listTable tr th {padding:4px;font-weight:bold;font-size:" + list_tablecaptionsizes[1] + "px;border:" + list_bordersizes[1]+ "px solid " + list_bordercolors[1] + ";color:" + list_tablecaptiontextcolors[1] + ";}" +
	    				  "</style>" +
	    				  "</head>" +
	    				  "<body>" +
	    				  "<div class='listCaption'>" + list_captions[1] + "</div>" +
	    				  "<table class='listTable'>" +
	    				  "<tr>");
    		
					      String html1_list = "";
    					  String[] stringArrays = Nodes_ListHeader.toArray(new String[Nodes_ListHeader.size()]);
					    		Log.d(TAG,"List Header Size : " + Nodes_ListHeader.size());
					    		for (int i=0; i<Nodes_ListHeader.size(); i++) {
					          	 if (TextUtils.isEmpty(stringArrays[i])) {
					
					          		} else {
					          			//Log.d(TAG,stringArrays[i]);          			
					          			String[] separated = stringArrays[i].split("--");
					          			
					          			String[] separateds = separated[1].split(";;");
					          			//String[] separatedss = separated[1].split("^^");
					          			
					          			//Log.d(TAG,"List Headers : "  + separated[0] + "  " + separated[1]);
					          			html1_list += new String("<th style='text-align:" + separateds[1] + "'>" + separated[0] + "</th>");
					          		}
					          	 
					            }
		            
	    				  html_list = html_list + html1_list + "</tr>" +
	    				  "<tr>" +
	    				  "</tr><tr>";
	    				  
	    				 
	    				  String html2_list = "";
	   					  String[] stringArrayss_Pot = Nodes_ListPot.toArray(new String[Nodes_ListPot.size()]);
	   					  //Log.d(TAG,"List Pot TT Size : " + Nodes_ListPot.size());
	   					  
   			    		  for(int k=0; k<Nodes_ListPot.size(); k++){
   			    			  
   			    			//Log.d(TAG,"List Pot TT : " + stringArrayss_Pot[k]);
   			    			String[] separated_potsize = stringArrayss_Pot[k].split("''");
   			    			
   			    				html2_list += "<tr>";
				    		
   			    				for (int z=0; z<rec_count; z++) {
					          			
   			    					//Log.d(TAG,"List Pot TT Alt" + separated_potsize[z]);
   			    					
   			    					if(separated_potsize[z].length() <= 0){
   			    						recs = "&nbsp";
   			    					}else {
   			    						recs = separated_potsize[z];
   			    					}
   			    					html2_list += "<td style='text-align:center'>"+ recs + "</td>";
						          	 
						         }
				    			
				    			html2_list = html2_list + "<tr>";
				    		
				    			
				    		}
				    		
	    				  
				    	  html_list = html_list + html2_list +
	    				  "</tr>" +
	    				  "</table>" +
	    				  "</body>" +
	    				  "</html>";
	    				  //Log.d(TAG, "List HTML : " + html);
    		
         	String mime = "text/html";
     		String encoding = "utf-8";
     		List_webwiews.loadDataWithBaseURL(null, html_list, mime, encoding, null);
         	Log.d(TAG, "List created");
         	
         	//marqueue_webwiews.loadUrl("http://www.egegen.com");
    		}catch(Exception e){
    			Log.e(TAG,e.getMessage());
            }
		
		removeAll(Nodes_ListHeader);
		removeAll(Nodes_ListPot);
	
	}
	

	public void SetMarqueue(final int width,final int height,final int x,final int y,final String font,final String font_weight,final String prompter_bgcolor, String prompter_forecolor,final String prompter_speed,final String files,final String filestype) throws IOException,Exception {
	
		Log.d(TAG,"SetMarqueue!");
		//Log.d(TAG,"Marqueue width : " + width + " height : " + height + " x :" + x + " y :" + y + " font :" + font + " font_weight :" + font_weight + " prompter_bgcolor :" + prompter_bgcolor + " prompter_forecolor :" + prompter_forecolor + " prompter_speed :" + prompter_speed + " files :" + files);
		
		//Log.d(TAG,"Marqueue xml filestypesss : " + filestype);
		//Log.d(TAG,"Marqueue xml files : " + files);

		//Log.d(TAG,"Marquee textrss was found");
		if(filestype.equalsIgnoreCase("textrss")){
			
			try {
				String pull = Marqueue_GetText(files);
	    		final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
	    		final LinearLayout newMarqueuelayout = new LinearLayout(this);
	    		final WebView marqueue_webwiews = new WebView(this);
	    		marqueue_webwiews.clearCache(true);
	    		final LinearLayout.LayoutParams Marqueuelp = new LinearLayout.LayoutParams(width, height);
	            Marqueuelp.setMargins(x, y, x + width, y + height);
	            roots.addView(newMarqueuelayout);
	            newMarqueuelayout.addView(marqueue_webwiews, Marqueuelp);
	            
	            marqueue_webwiews.setBackgroundColor(Color.TRANSPARENT); 
	    		String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
	    		marqueue_webwiews.getSettings().setUserAgentString(ua);
	    		//marqueue_webwiews.getSettings().setPluginState(PluginState.ON);
	    		//marqueue_webwiews.getSettings().setLoadWithOverviewMode(true);
	    		marqueue_webwiews.getSettings().setJavaScriptEnabled(true);

	            String[] text_bgcolors = prompter_bgcolor.split("=");
	            String[] prompter_forecolors = prompter_forecolor.split("=");
	            String[] prompter_speeds = prompter_speed.split("=");
	            
	            String scllMnt="";
	            if(prompter_speeds[1].equalsIgnoreCase("low")){
	            	scllMnt = "1";
	            }else if(prompter_speeds[1].equalsIgnoreCase("medium")){
	            	scllMnt = "2";
	            }else if(prompter_speeds[1].equalsIgnoreCase("high")){
	            	scllMnt = "3";
	            }
	            
	            float scale = (height / 1.40f);
	            String scales = "" + scale;
	            Log.d(TAG, "Marqueue Scale : " + scales.substring(0, 2).trim());
	            
	    		String html_marq = "<html>" +
	    				"<head>" +
	    				"<title>Marqueue</title>"+
	    				"<script src='file:///android_asset/jquery.js' type='text/javascript'></script>"+
	    				"<script src='file:///android_asset/marqueue.js' type='text/javascript'></script>"+
	    				"<style type='text/css'>"+
	    				".listCaption {padding: 5px 0px 0px 0px;color: "+ prompter_forecolors[1] + "; font-size:"+ scales.substring(0, 2).trim() + "px;font-weight:bold;text-align: right; width: " + width + "; height: " + height + "}"+
	    				"</style>"+
	    				"</head>" +
	    				"<body bgcolor='"+ text_bgcolors[1] +"' leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>" +
	    				"<div class='listCaption'>" +
	    				"<marquee behavior='scroll' direction='left' scrollamount='"+ scllMnt +"'>"+ pull +"</marquee>" +
	    				"</div>"+
	    				"</body>" +
	    				"</html>";
	    		
	         	String mime = "text/html";
	     		String encoding = "utf-8";
	     		marqueue_webwiews.loadDataWithBaseURL(null, html_marq, mime, encoding, null);
	         	Log.d(TAG, "Marqueue created");
	         	
	         	//marqueue_webwiews.loadUrl("http://www.egegen.com");
	    		}catch(Exception e){
	    			Log.e(TAG,e.getMessage());
	            }
			
		}else if(filestype.equalsIgnoreCase("urlrss")){
			
			try {
				
				String pull = parserRss(files);
	    		final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
	    		final LinearLayout newMarqueuelayout = new LinearLayout(this);
	    		final WebView marqueue_webwiews = new WebView(this);
	    		marqueue_webwiews.clearCache(true);
	    		final LinearLayout.LayoutParams Marqueuelp = new LinearLayout.LayoutParams(width, height);
	            Marqueuelp.setMargins(x, y, x + width, y + height);
	            roots.addView(newMarqueuelayout);
	            newMarqueuelayout.addView(marqueue_webwiews, Marqueuelp);
	            
	            marqueue_webwiews.setBackgroundColor(Color.TRANSPARENT); 
	    		String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
	    		marqueue_webwiews.getSettings().setUserAgentString(ua);
	    		//marqueue_webwiews.getSettings().setPluginState(PluginState.ON);
	    		//marqueue_webwiews.getSettings().setLoadWithOverviewMode(true);
	    		marqueue_webwiews.getSettings().setJavaScriptEnabled(true);

	            String[] text_bgcolors = prompter_bgcolor.split("=");
	            String[] prompter_forecolors = prompter_forecolor.split("=");
	            String[] prompter_speeds = prompter_speed.split("=");
	            
	            String scllMnt="";
	            if(prompter_speeds[1].equalsIgnoreCase("low")){
	            	scllMnt = "1";
	            }else if(prompter_speeds[1].equalsIgnoreCase("medium")){
	            	scllMnt = "2";
	            }else if(prompter_speeds[1].equalsIgnoreCase("high")){
	            	scllMnt = "3";
	            }
	            
	            float scale = (height / 1.40f);
	            String scales = "" + scale;
	            //Log.d(TAG, "Marqueue Scale : " + scales.substring(0, 2).trim());
	            
	    		String html_marqs = new String("<html>" +
	    				"<head>" +
	    				"<title>Marqueue</title>"+
	    				"<style type='text/css'>"+
	    				".listCaption {padding: 5px 0px 0px 0px;color: "+ prompter_forecolors[1] + "; font-size:"+ scales.substring(0, 2).trim() + "px;font-weight:bold;text-align: right; width: " + width + "; height: " + height + "}"+
	    				"</style>"+
	    				"</head>" +
	    				"<body bgcolor='"+ text_bgcolors[1] +"' leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>" +
	    				"<div class='listCaption'>" +
	    				"<marquee behavior='scroll' direction='left' scrollamount='"+ scllMnt +"'>"+ pull +"</marquee>" +
	    				"</div>"+
	    				"</body>" +
	    				"</html>");
	    		
	         	String mime = "text/html";
	     		String encoding = "utf-8";
	     		marqueue_webwiews.loadDataWithBaseURL(null, html_marqs, mime, encoding, null);
	         	Log.d(TAG, "Marqueue created");
	         	
	         	//marqueue_webwiews.loadUrl("http://www.egegen.com");
	    		}catch(Exception e){
	    			Log.e(TAG,e.getMessage());
	            }
			
		}
			
		
	}
	
	public String stripHtml(String html) {
	    return Html.fromHtml(html).toString();
	}
	
	private String Marqueue_GetTextUrl(String files){
		String pull="";
		FileInputStream fIn = null;
		Charset.forName("UTF-8").newEncoder();
		String marqueue_xml_data = "";
		try{

			  fIn = openFileInput(files);
			  InputStream in = fIn;
	          byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
	          int len;
	          while ((len = in.read(buf)) > 0){
	        	 
	        	  marqueue_xml_data = new String(buf, 0, len);
	        	 
				}
	          
	          //Log.d(TAG,"Marqueue xml_data : " + marqueue_xml_data);
	          
	          try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(marqueue_xml_data.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	NodeList nodeListActivity=docs.getElementsByTagName("rss");
		        	Element elementactivity=(Element) nodeListActivity.item(0);

		        	            for (int i = 0; i < nodeListActivity.getLength(); i++) {

		        	            	
		        	                Node node=nodeListActivity.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListcomponent=elementMain.getElementsByTagName("channel");
		        	                Element elementcomponent=(Element) nodeListcomponent.item(i);
		        	              
		        	                for(int c = 0;c<nodeListcomponent.getLength();c++){
		        	                	
		        	                	Element componentElements = (Element) nodeListcomponent.item(c);

		        	                	
			               	                Node node1=nodeListcomponent.item(c);
				        	                Element elementMain1=(Element) node1;
				        	                NodeList nodeListtimeline=elementMain1.getElementsByTagName("item");
				        	                Element elementtimeline=(Element) nodeListtimeline.item(0);
				        	                
			        	                	for(int t = 0;t<nodeListtimeline.getLength();t++){
			        	                		
			        	                		
			        	                		Node node2 = nodeListtimeline.item(t);
			    		        			 	Element elementMain2=(Element) node2;
			    		        			 	NodeList nodeListsspecification=elementMain2.getElementsByTagName("description");
			 	        	                		Element line = (Element) nodeListsspecification.item(0);
	 	 	        	                		    pull = line.getTextContent().replace("\"", "").trim();
	 	 	        	                		    
			 	        	                		//Log.d(TAG,"Marqueue TEXT Find : " + pull);
			 	        	                	
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
		return pull;
	}
	
	private String Marqueue_GetText(String files){
		String pull="";
		FileInputStream fIn = null;
		Charset.forName("UTF-8").newEncoder();
		String marqueue_xml_data = "";
		try{

			  fIn = openFileInput(files);
			  InputStream in = fIn;
	          byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
	          int len;
	          while ((len = in.read(buf)) > 0){
	        	 
	        	  marqueue_xml_data = new String(buf, 0, len);
	        	 
				}
	          
	          Log.d(TAG,"Marqueue xml_data : " + marqueue_xml_data);
	          
	          try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(marqueue_xml_data.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	NodeList nodeListActivity=docs.getElementsByTagName("rss");
		        	Element elementactivity=(Element) nodeListActivity.item(0);

		        	            for (int i = 0; i < nodeListActivity.getLength(); i++) {

		        	            	
		        	                Node node=nodeListActivity.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListcomponent=elementMain.getElementsByTagName("channel");
		        	                Element elementcomponent=(Element) nodeListcomponent.item(i);
		        	              
		        	                for(int c = 0;c<nodeListcomponent.getLength();c++){
		        	                	
		        	                	Element componentElements = (Element) nodeListcomponent.item(c);

		        	                	
			               	                Node node1=nodeListcomponent.item(c);
				        	                Element elementMain1=(Element) node1;
				        	                NodeList nodeListtimeline=elementMain1.getElementsByTagName("item");
				        	                Element elementtimeline=(Element) nodeListtimeline.item(0);
				        	                
			        	                	for(int t = 0;t<nodeListtimeline.getLength();t++){
			        	                		
			        	                		
			        	                		Node node2 = nodeListtimeline.item(t);
			    		        			 	Element elementMain2=(Element) node2;
			    		        			 	NodeList nodeListsspecification=elementMain2.getElementsByTagName("description");
			 	        	                		Element line = (Element) nodeListsspecification.item(0);
	 	 	        	                		    pull = line.getTextContent().replace("\"", "").trim();
	 	 	        	                		    
			 	        	                		//Log.d(TAG,"Marqueue TEXT Find : " + pull);
			 	        	                	
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
		return pull;
	}
	
	private void List_GetXml(String files){
		
		String getir = "";
		String getir2 = "";
		FileInputStream fIn = null;
		String xmldatas_list ="";
		Charset.forName("UTF-8").newEncoder();
		try{

			  fIn = openFileInput(files);
			  InputStream in = fIn;
			  //System.out.println(System.getProperty("file.encoding"));
	          byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
	          int len;
	          while ((len = in.read(buf)) > 0){
	        	 
	        	  xmldatas_list = new String(buf, 0, len);
	        	 
				}
	          
	          try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(xmldatas_list.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	 NodeList nodeListlist=docs.getElementsByTagName("list");
		        	 Element elementlist=(Element) nodeListlist.item(0);

		        	            for (int i = 0; i < nodeListlist.getLength(); i++) {

		        	            	
		        	                Node node=nodeListlist.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListfield=elementMain.getElementsByTagName("field");
		        	                Element elementfield=(Element) nodeListfield.item(i);

		        	                
		        	                for(int c = 0;c<nodeListfield.getLength();c++){
		        	                	
		        	                	Element fieldElements = (Element) nodeListfield.item(c);
		        	                	String name = fieldElements.getAttribute("name");
		        	                	String dependency = fieldElements.getAttribute("dependency");
		        	                	getir += name + "^^" + dependency;
		        	                	Nodes_ListHeader.add(name + "--" + dependency);
		        	                }

		        	                
		        	                
		        	                NodeList nodeListrecord=elementMain.getElementsByTagName("record");
		        	                Element elementrecord=(Element) nodeListrecord.item(i);
		        	                String[] rec_rows = new String[nodeListrecord.getLength()];
		        	                
		        	                
		        	                for(int x = 0;x<nodeListrecord.getLength();x++){
		        	                	
		        	                	    Node node1=nodeListrecord.item(x);
				        	                Element elementMain1=(Element) node1;
				        	                NodeList nodeListrec=elementMain1.getElementsByTagName("rec");
				        	                Element recordElements=(Element) nodeListrec.item(0);
				        	                //Log.d(TAG,"Pots : Size" + nodeListrec.getLength());
				        	                rec_count = nodeListrec.getLength();
		        	                	  for(int r = 0;r<nodeListrec.getLength();r++){
				        	                	
				        	                	Element componentElements = (Element) nodeListrec.item(r);
				        	                	String field = componentElements.getAttribute("field");
				        	                	String value = componentElements.getAttribute("value");
				        	                	
				        	                	rec_rows[x] +=value + "''";
				        	                	
				        	                	
				        	                }
		        	                	  //Log.d(TAG,"LL Pots : " + rec_rows[x].replace("null",""));
		        	                	  Nodes_ListPot.add(rec_rows[x].replace("null","") + ",");
		        	                }
		        	                
		        	                
		        	                //Log.d(TAG,"List Xml Files Datas :" + getir.replace("null", "").trim());
		        	                
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
		
	}
	
	
	private String List_GetXmlFileName(String files){
		
		String getir = "";
		FileInputStream fIn = null;
		String xmldatas_list ="";
		Charset.forName("UTF-8").newEncoder();
		try{

			  fIn = openFileInput(files);
			  InputStream in = fIn;
			  //System.out.println(System.getProperty("file.encoding"));
	          byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
	          int len;
	          while ((len = in.read(buf)) > 0){
	        	 
	        	  xmldatas_list = new String(buf, 0, len);
	        	 
				}
	          
	          try {
					DocumentBuilderFactory dbfs = DocumentBuilderFactory.newInstance();
		        	DocumentBuilder dbs = null;
		        	dbs = dbfs.newDocumentBuilder();
					
					 Document docs = dbs.parse(new InputSource(new ByteArrayInputStream(xmldatas_list.getBytes("utf-8"))));
		        	 docs.getDocumentElement().normalize();
		        	
		        	NodeList nodeListActivity=docs.getElementsByTagName("activity");
		        	Element elementactivity=(Element) nodeListActivity.item(0);
		        	final String Act_Id = elementactivity.getAttribute("activityid");

		        	            for (int i = 0; i < nodeListActivity.getLength(); i++) {

		        	            	
		        	                Node node=nodeListActivity.item(i);
		        	                Element elementMain=(Element) node;
		        	                NodeList nodeListcomponent=elementMain.getElementsByTagName("component");
		        	                Element elementcomponent=(Element) nodeListcomponent.item(i);

		        	                
		        	                for(int c = 0;c<nodeListcomponent.getLength();c++){
		        	                	
		        	                	
		        	                	Element componentElements = (Element) nodeListcomponent.item(c);
		        	                	String subtype = componentElements.getAttribute("type");
    		        	                
    		        	                

	
			        	                
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
    			    		        			 	
    			    		        			 	
    			 	        	                	for(int s=0;s <nodeListsspecification.getLength();s++) {
    			 	        	                		Element elementspecification=(Element) nodeListsspecification.item(s);
    			 	        	                		if(elementspecification.getAttribute("datakey").equalsIgnoreCase("list_name"))
    			 	        	                		getir =elementspecification.getAttribute("datavalue");
    			 	        	                	}
    			 	        	                	
    			 	        	                	String Listname = "list_" + component_name + "_" + timeline_name + "_" + startdatetime + "_" + enddatetime + ".xml";
    			 	        	                	
    			 	        	                	getir = conver_string(Listname);
    			 	        	                	
    			 	        	                	Log.d(TAG,"List : " + getir.replace("null", "").trim());

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
	
		return getir.replace("null", "").trim();
		
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
	
	private byte[] readimage(String resource){
		
		byte[] imageRaw = null;
		  try {
				     InputStream in = new BufferedInputStream(getAssets().open(resource + ".png"));
				     ByteArrayOutputStream out = new ByteArrayOutputStream();

				     int c;
				     while ((c = in.read()) != -1) {
				         out.write(c);
				     }
				     out.flush();

				     imageRaw = out.toByteArray();
				     in.close();
				     out.close();

		  } catch (IOException e) {
		     Log.e(TAG,e.getMessage());
		  }
		return imageRaw;
		
	}
	
	
	private byte[] readimageExchange(String resource){
		
		byte[] imageRaw = null;
		  try {

		     InputStream in = new BufferedInputStream(getAssets().open("exchange."+resource + ".png"));
		     ByteArrayOutputStream out = new ByteArrayOutputStream();

		     int c;
		     while ((c = in.read()) != -1) {
		         out.write(c);
		     }
		     out.flush();

		     imageRaw = out.toByteArray();
		     in.close();
		     out.close();
		  } catch (IOException e) {
		     
		     Log.e(TAG,e.getMessage());
		  }
		return imageRaw;
		
	}
	
	private String conver_weather_name(String name) {
		name = name.trim();
		if ((name.equalsIgnoreCase("tornado")) || (name.equalsIgnoreCase("tropical storm")) || (name.equalsIgnoreCase("hurricane")))
				name = "storms";
		if ((name.equalsIgnoreCase("severe thunderstorms")) || (name.equalsIgnoreCase("isolated thunderstorms")) || (name.equalsIgnoreCase("scattered thunderstorms")) || (name.equalsIgnoreCase("lightrainwiththunder")))
				name = "thunderstorms02";
		if ((name.equalsIgnoreCase("thunderstorms")) || (name.equalsIgnoreCase("blustery")))
			name = "thunderstorms02";
		if ((name.equalsIgnoreCase("mixed rain and snow")))
			name = "thunderstorms02";
		if ((name.equalsIgnoreCase("mixed rain and snow")) || (name.equalsIgnoreCase("mixed rain and sleet")) || (name.equalsIgnoreCase("mixed snow and sleet")))
			name = "sleet";
		if ((name.equalsIgnoreCase("freezing drizzle")) || (name.equalsIgnoreCase("drizzle")))
			name = "flurries";
		if ((name.equalsIgnoreCase("freezing rain")))
			name = "freezingrain";
		if ((name.equalsIgnoreCase("showers")) || (name.equalsIgnoreCase("scattered showers")) || (name.equalsIgnoreCase("isolated thundershowers")))
			name = "rain03";
		if ((name.equalsIgnoreCase("lightrain")) || (name.equalsIgnoreCase("lightrainshower")))
			name = "rain01";
		if ((name.equalsIgnoreCase("snow flurries")) || (name.equalsIgnoreCase("light snow showers")))
			name = "sleet";
		if ((name.equalsIgnoreCase("hail")) || (name.equalsIgnoreCase("smoky")))
			name = "frost";		
		if ((name.equalsIgnoreCase("dust")))
			name = "hazy";		
		if ((name.equalsIgnoreCase("foggy")))
			name = "fog";		
		if ((name.equalsIgnoreCase("haze")))
			name = "hazy";		
		if ((name.equalsIgnoreCase("cold")))
			name = "freezing";		
		if ((name.equalsIgnoreCase("mostly cloudy (night)")))
			name = "partlycloudy";		
		if ((name.equalsIgnoreCase("mostly cloudy (day)")))
			name = "partlycloudy";		
		if ((name.equalsIgnoreCase("partly cloudy (night)")))
			name = "partlycloudy";		
		if ((name.equalsIgnoreCase("partly cloudy (day)")))
			name = "partlycloudy";		
		if ((name.equalsIgnoreCase("clear (night)")))
			name = "clear";		
		if ((name.equalsIgnoreCase("fair")))
			name = "clear";		
		if ((name.equalsIgnoreCase("fair (night)")))
			name = "clear";		
		if ((name.equalsIgnoreCase("fair (day)")))
			name = "clear";		
		if ((name.equalsIgnoreCase("mixed rain and hail")))
			name = "rain01";		
		if ((name.equalsIgnoreCase("rain")))
			name = "rain02";		
		if ((name.equalsIgnoreCase("hot")))
			name = "hot02";		
		if ((name.equalsIgnoreCase("heavy snow")) || (name.equalsIgnoreCase("scattered snow showers")) || (name.equalsIgnoreCase("snow showers")))
			name = "snow";
		if ((name.equalsIgnoreCase("partly cloudy")))
			name = "partlycloudy";	
		if ((name.equalsIgnoreCase("rainandsnow")))
			name = "rain02";	
		if ((name.equalsIgnoreCase("thundershowers")))
			name = "thunderstorms01";		
		if ((name.equalsIgnoreCase("not available")))
			name = "unknown";		
		if ((name.equalsIgnoreCase("sunny")))
			name = "clear";		
		if ((name.equalsIgnoreCase("mostlycloudy")))
			name = "partlycloudy";
		if ((name.equalsIgnoreCase("wind")))
			name = "windy";
		return name;
	}
	
	private void SetWeather(int width,int height,int x,int y,final String forecolors,final String gr_types,final String grstartcolors,final String grfinishcolors) throws IOException,Exception{
		
		try {
			
		final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
		final LinearLayout newWebViewlayout = new LinearLayout(this);
		final WebView webwiews = new WebView(this);
        final LinearLayout.LayoutParams Weatherlp = new LinearLayout.LayoutParams(width, height);
        Weatherlp.setMargins(x, y, x + width, y + height);
        roots.addView(newWebViewlayout);
        newWebViewlayout.addView(webwiews, Weatherlp);
        webwiews.clearCache(true);
        
        webwiews.setBackgroundColor(Color.TRANSPARENT); 
		String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
		webwiews.getSettings().setUserAgentString(ua);
		webwiews.getSettings().setPluginState(PluginState.ON);
		webwiews.getSettings().setLoadWithOverviewMode(true);
		webwiews.getSettings().setJavaScriptEnabled(true);
		webwiews.setBackgroundColor(Color.TRANSPARENT); 

		final String[] forecolor = forecolors.split("=");
		final String[] grstartcolor = grstartcolors.split("=");
		final String[] gr_type = gr_types.split("=");
		final String[] grfinishcolor = grfinishcolors.split("=");
        
   
		SetWeatherCityDatasXML();
    	SetWeatherCityDatas(webwiews,forecolor[1],grstartcolor[1],gr_type[1],grfinishcolor[1]); 
      

		}catch(Exception e){
			Log.e(TAG,e.getMessage());
        }
		
	
	}
	
	private void SetWeatherCityDatas(final WebView web,final String forecolor,final String grstartcolor,final String gr_type,final String grfinishcolor){
	     
		final Handler handlers = new Handler(); 
			WeatherTimer = new Timer(); 
			WeatherTimer.schedule(new TimerTask() { 
	                public void run() { 
	                		handlers.post(new Runnable() {
	                                public void run() {                               

	                                            Log.d(TAG,"Weather Check Listener Status : " + UpdateListenService.listener_status);
	                                            
	                                            String[] stringArrays = Nodes_Weather_xml.toArray(new String[Nodes_Weather_xml.size()]);
	    	                            		Log.d(TAG,"Weather XML Size : " + Nodes_Weather_xml.size());
	    	                            		String html = null;
	    	                                   	Random rnd = new Random();
	    	                                   	final Calendar calendar = Calendar.getInstance();
	    	                                   	final int day = calendar.get(Calendar.DAY_OF_WEEK);
	    	                                   	//Toast.makeText(getApplicationContext(), "Day Of Week : " + day ,Toast.LENGTH_SHORT).show();
	    	                                   	
	    	                                   	final String[] separated = stringArrays[rnd.nextInt(Nodes_Weather_xml.size())].split(",");
	    	                                   	Log.d(TAG,separated[0] + "--" + separated[1] + "--" + separated[2] + "--" + separated[3] + "--" + separated[4] + "--" + separated[5] + "--" + separated[6] + "--" + separated[7] + "--" + separated[8]+ "--" + separated[9]+ "--" + separated[10]+ "--" + separated[11]+ "--" + separated[12]+ "--" + separated[13]+ "--" + separated[14]+ "--" + separated[15]+ "--" + separated[16]+ "--" + separated[17]);

	    	                             		String image3 = Base64.encodeToString(readimage(conver_weather_name(separated[3]).trim().toLowerCase()), Base64.DEFAULT);
	    	                             		String image17 = Base64.encodeToString(readimage(conver_weather_name(separated[17]).trim().toLowerCase()), Base64.DEFAULT);
	    	                             		String image14 = Base64.encodeToString(readimage(conver_weather_name(separated[14]).trim().toLowerCase()), Base64.DEFAULT);
	    	                             		String image8 = Base64.encodeToString(readimage(conver_weather_name(separated[8]).trim().toLowerCase()), Base64.DEFAULT);

	    	                             		Log.d(TAG,"Images : " + conver_weather_name(separated[3]) + "  " + conver_weather_name(separated[17]) + "  " + conver_weather_name(separated[14]) + "  " + conver_weather_name(separated[8]));
	    	                             		 
	    	                             		html = "<html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><title>Weather Component</title>" +
	    	                            					"<style type='text/css'>" + 
	    	                            					"html, body {position:relative;overflow:hidden; border:0; margin: 0;padding: 0;height: 100%;width: auto;background-color: " + grstartcolor + ";font-family: 'Roboto'; font-size: 12px;color: " + forecolor + ";}" +
	    	                            					".gradient { background: ' + Fweather_grstartcolor + '; background: -ms-linear-gradient(top,  " + grstartcolor + " 0%," + grfinishcolor + " 100%); filter: progid:DXImageTransform.Microsoft.gradient( startColorstr=''" + grstartcolor + "'', endColorstr=''" + grfinishcolor + "'',GradientType=0 ); background: -webkit-gradient(linear, left top, left bottom, color-stop(0%," + grstartcolor + "), color-stop(100%," + grfinishcolor + ")); }" +
	    	                            					".listTable {width:100%;border-collapse: collapse;margin-top:4px;}" +
	    	                            					".listTable tr td {padding:2px;border:0;}" +
	    	                            					".listTableDays {width:100%;border-collapse: collapse;}" +
	    	                            					".listTableDays tr td {padding:2px;border:0;font-size:10px; font-weight:normal;text-align:center;}"+
	    	                            					".cityName {font-size:14px;font-weight: bold;}"+
	    	                            					".currentTemp {font-size:32px;font-weight: bold;}"+
	    	                            					".currentWind {font-size:10px; font-weight:normal;height: 40px;}"+
	    	                            					"#bottomGroup {position: absolute; bottom: 0; padding-bottom:4px;}"+
	    	                            					"@font-face {"+
	    	                            					"font-family: 'Roboto';"+
	    												    "font-style: normal;"+
	    												    "font-weight: 100;"+
	    												    "src: local('Roboto Thin'), local('Roboto-Thin'), url(file:///android_asset/idLYXfFa1c7oAPILDl4z0XYhjbSpvc47ee6xR_80Hnw.woff) format('woff');}"+
	    												    "@font-face {"+
	    												    "font-family: 'Roboto';"+
	    												    "font-style: normal;"+
	    												    "font-weight: 400;"+
	    												    "src: local('Roboto Regular'), local('Roboto-Regular'), url(file:///android_asset/Xu_FYwbs8k0teWf0uC8JpevvDin1pK8aKteLpeZ5c0A.woff) format('woff');}" +
	    	                            					"</style>"+
	    	                            					"</head>"+
	    	                            					"<body class='gradient'>" +
	    	                            					"<table class='listTable'>" +
	    	                            					"<tr>" +
	    	                            					"<td style='width:33%' rowspan='3'><img alt='' src='data:image/jpeg;base64," + image3 +"' height='80' width='80' /></td>"+
	    	                            					"<td style='width:33%' rowspan='3'>&nbsp;</td>"+
	    	                            					"<td class='cityName' style='width:67%;text-align:center'>" + separated[0] + "</td>" +
	    	                            					"</tr>" +
	    	                            					"<tr>" +
	    	                            					"<td class='currentTemp' style='width:67%;text-align:center'>" + separated[4]  + " °C</td>" +
	    	                            					"</tr>" +
	    	                            					"<tr>" +
	    	                            					"<td class='currentWind' style='width:67%;text-align:center'>" + separated[5]  + "</td>" +
	    	                            					"</tr>" +
	    	                            					"</table>" +
	    	                            					"<div id='bottomGroup'>" +
	    	                            					"<table class='listTableDays'>" +
	    	                            					"<tr>" +
	    	                            					"<td style='width:25%'>Bugün</td>" +
	    	                            					"<td style='width:25%'>" + arrWeekOfDays[IncDay(day+1)] +  "</td>" +
	    	                            					"<td style='width:25%'>" + arrWeekOfDays[IncDay(day+2)] +  "</td>" +
	    	                            					"<td style='width:25%'>" + arrWeekOfDays[IncDay(day+3)]  +  "</td>" +
	    	                            					"</tr>" +
	    	                            					"<tr>"+
	    	                            					"<td><img src='data:image/png;base64," + image3 +"' height='38' width='38' /></td>"+
	    	                            					"<td><img src='data:image/png;base64," + image8 +"' height='38' width='38' /></td>"+
	    	                            					"<td><img src='data:image/png;base64," + image14 +"' height='38' width='38' /></td>"+
	    	                            					"<td><img src='data:image/png;base64," + image17 +"' height='38' width='38' /></td>"+
	    	                            					"</tr>"+
	    	                            					"<tr>"+
	    	                            					"<td>" + separated[6] + " / " + separated[7] + "</td>"+
	    	                            					"<td>" + separated[9] + " / " + separated[10] + "</td>"+
	    	                            					"<td>" + separated[12] + " / " + separated[13] + "</td>"+
	    	                            					"<td>" + separated[15] + " / " + separated[16] + "</td>"+
	    	                            					"</tr>" +
	    	                            					"</table>"+
	    	                            					"</div>"+
	    	                            					"</body>"+
	    	                            					"</html>";

	    	                            		String mime = "text/html";
	    	                            		String encoding = "utf-8";
	    	                            		web.loadDataWithBaseURL(null, html, mime, encoding, null);
	                                } 
	                        }); 
	                } 
	        }, 0,8000);
		
	}
	
	private void SetWeatherCityDatasXML(){
	     
		final Handler handlers = new Handler(); 
	        Timer tt = new Timer(); 
	        tt.schedule(new TimerTask() { 
	                public void run() { 
	                		handlers.post(new Runnable() {
	                                public void run() {                               
	                                	try{
	                                	removeAll(Nodes_Weather_xml);
	                                	String weather_status ="";
	                            		weather_status = WeatherXmlRead("weather.xml");
	                            		if(weather_status.equalsIgnoreCase("success"))
	                            		{
	                            		}
	                            		else if(weather_status.equalsIgnoreCase("failed")){
	                            			WeatherXmlRead("weather.xml");
	                            		}else if(weather_status.equalsIgnoreCase("")){
	                            			
	                            		}
	                                	}catch(Exception e){
	                            			Log.e(TAG,e.getMessage());
	                                    }
	                                	
	                                } 
	                        }); 
	                } 
	        }, 0,30000);
		
	}
	
	private void SetExchange(int width,int height,int x,int y,final String exchange_header,final String exchange_textsize,final String exchange_forecolor,final String exchange_grtype,final String exchange_grstartcolor,final String exchange_grfinishcolor) throws IOException,Exception{
		
		Log.d(TAG, "SetExchange : " + width + " " + height  + " " + x  + " " + y  + " " + exchange_header  + " " + exchange_textsize  + " " + exchange_forecolor + " " + exchange_grtype + " " + exchange_grstartcolor + " " + exchange_grfinishcolor);
		
		try {
			
		final RelativeLayout roots = (RelativeLayout) findViewById(R.id.mainroot);
		final LinearLayout newExchangelayout = new LinearLayout(this);
		final WebView exchange_webwiews = new WebView(this);
        final LinearLayout.LayoutParams Exchangelp = new LinearLayout.LayoutParams(width, height);
        Exchangelp.setMargins(x, y, x + width, y + height);
        roots.addView(newExchangelayout);
        newExchangelayout.addView(exchange_webwiews, Exchangelp);
        exchange_webwiews.clearCache(true);
        
        exchange_webwiews.setBackgroundColor(Color.TRANSPARENT); 
		String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
		exchange_webwiews.getSettings().setUserAgentString(ua);
		exchange_webwiews.getSettings().setPluginState(PluginState.ON);
		exchange_webwiews.getSettings().setLoadWithOverviewMode(true);
		exchange_webwiews.getSettings().setJavaScriptEnabled(true);

		       			//Log.d(TAG,stringArrays[i]);

		       			String[] forecolor = exchange_forecolor.split("=");
			         	String[] grstartcolor = exchange_grstartcolor.split("=");
			         	String[] gr_type = exchange_grtype.split("=");
			         	String[] grfinishcolor = exchange_grfinishcolor.split("=");
			         	String[] textsize = exchange_textsize.split("=");
			         	String[] header = exchange_header.split("=");
			         	
			         	String html = null;



		         		html = "<!DOCTYPE html><html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8' /><title>Rate</title><style type='text/css'>" +
		         				"html, body {overflow:hidden; border:0; margin: 0;padding: 0;height: 100%;width: auto;background-color: " + grstartcolor[1] + ";font-family:Verdana, Helvetica, Sans Serif; font-size:" + textsize[1]  + "px;color: " + forecolor[1] + ";font-weight: bold;}" +
		         				".gradient { background: " + grstartcolor[1] + "; background: -ms-linear-gradient(top,  " + grstartcolor[1] + " 0%," + grfinishcolor[1] + " 100%); filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='" + grstartcolor[1] + "', endColorstr='" + grfinishcolor[1] + "',GradientType=0 ); }"+
		         				".listCaption {padding: 12px 4px 12px 4px;color: " + forecolor[1] + "; font-size:" + textsize[1] + "px;font-weight:bold;text-align: center;}"+
		         				".listTable {width:100%;border-collapse: collapse;}"+
		         				".listTable tr td {padding:4px;border:0;}"+
		         				".listTable tr th {padding:4px;font-weight:bold;font-size:" + textsize[1] + "px;border:0;color:" + forecolor[1] + ";}"+
		         				"</style></head><body class='gradient'><div class='listCaption'>" + header[1] + "</div>"+
		         				"<table class='listTable'>" +
		         				"<tr><th style='width:33%'>&nbsp;</th><th style='width:33%;text-align:center'>ALIŞ</th><th style='width:33%;text-align:center'>SATIŞ</th></tr>";
		         				

		         				String[] stringArrays = Nodes_Exchange_xml.toArray(new String[Nodes_Exchange_xml.size()]);
		         				Log.d(TAG,"Exchange XML Size : " + Nodes_Exchange_xml.size());

		        				
		        				String html_add = "";
		        				
		        				for (int z=0; z<Nodes_Exchange_xml.size();z++) {
		        		       	 //Log.d(TAG,stringArray[i]);
		        		       	 if (TextUtils.isEmpty(stringArrays[z])) {
		        		       		    //Log.d(TAG, "String is empty or null!");
		        		       		} else {
						       			final String[] separated = stringArrays[z].split("--");
						         		String image = new String(Base64.encodeToString(readimageExchange(conver_weather_name(separated[0]).trim().toLowerCase()), Base64.DEFAULT));
						         		Log.d(TAG,separated[0] + "  " + separated[5] + "  " +separated[6]+ "  " +separated[11]);
						         		
						         		html_add += "<tr><td style='text-align:left;padding-left:12px;'><img src='data:image/png;base64,"+ image+"' height='24' width='24' /></td>"+
		        		       					"<td style='text-align:center'>" + separated[5] + "</td>"+
		        		       					"<td style='text-align:center'>" + separated[6] + "</td></tr>";
		         				                 
		        		       		}
	       		
		        				}

		        				html = html + html_add.replace("null","") + "</table></body></html>";
		         		//Log.d(TAG,"Exchange HTML : " + html);
		         	String mime = "text/html";
	         		String encoding = "utf-8";
	         		exchange_webwiews.loadDataWithBaseURL(null, html, mime, encoding, null);
	

		}catch(Exception e){
			Log.e(TAG,e.getMessage());
        }
		
		removeAll(Nodes_Exchange_xml);
		
		
	}
	


	
	public InputStream getUrlData(String url) 

			throws URISyntaxException, ClientProtocolException, IOException {

			  DefaultHttpClient client = new DefaultHttpClient();

			  HttpGet method = new HttpGet(new URI(url));

			  HttpResponse res = client.execute(method);

			  return  res.getEntity().getContent();

			}

    
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}
	

	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface
	                .getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf
	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                //System.out.println("ip1--:" + inetAddress);
	                //System.out.println("ip2--:" + inetAddress.getHostAddress());

	      String ipv4;
		// for getting IPV4 format
	      if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {

	                    //String ip = inetAddress.getHostAddress().toString();
	                    //System.out.println("ip---::" + ip);
	                    return ipv4;
	                }
	            }
	        }
	        
	    } catch (Exception ex) {
	        Log.e("IP Address", ex.toString());
	    }
	    return null;
	}
	
	public void doWork() {
	    runOnUiThread(new Runnable() {
	        public void run() {
	            try{
	            	Calendar c = Calendar.getInstance();
	                System.out.println("Current time => "+c.getTime());

	                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                String formattedDate = df.format(c.getTime());

	        		
	            }catch (Exception e) {}
	        }
	    });
	}
	
	public void Status_Check() {

		Calendar c = Calendar.getInstance();
		System.gc();
		Log.d(TAG,"Garbage Collector Running on : " + c.getTime());
		    
	}
	
	private void runUpdateListenAsService() {
	      Log.d(TAG,"Update Service Starting...");
	      startService(new Intent(this, UpdateListenService.class));
	}
	 
	 private void runUpdateWLEService() {
	      Log.d(TAG,"Video Service Starting...");
	      startService(new Intent(this, UpdateWLEService.class));
	}
	 
	 
	 private String getDataSource(String path) throws IOException {
			if (!URLUtil.isNetworkUrl(path)) {
				return path;
			} else {
				URL url = new URL(path);
				URLConnection cn = url.openConnection();
				cn.connect();
				InputStream stream = cn.getInputStream();
				if (stream == null)
					throw new RuntimeException("stream is null");
				File temp = File.createTempFile("mediaplayertmp", "dat");
				temp.deleteOnExit();
				String tempPath = temp.getAbsolutePath();
				FileOutputStream out = new FileOutputStream(temp);
				byte buf[] = new byte[128];
				do {
					int numread = stream.read(buf);
					if (numread <= 0)
						break;
					out.write(buf, 0, numread);
				} while (true);
				//out.close();
				try {
					out.close();
					stream.close();
				} catch (IOException ex) {
					Log.e(TAG, "GetDataSource : " + ex.getMessage());
				}
				return tempPath;
			}
		}
	 
	 
	 public String getXmlFromUrl(String url) {
	        String xml = null;
	 
	        try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	 
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            xml = EntityUtils.toString(httpEntity);
	 
	        } catch (UnsupportedEncodingException e) {
	            Log.e(TAG,e.getMessage());
	        } catch (ClientProtocolException e) {
	            Log.e(TAG,e.getMessage());
	        } catch (IOException e) {
	            Log.e(TAG,e.getMessage());
	        }
	        return xml;
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
	 
	 private OnErrorListener mOnErrorListener = new OnErrorListener() {

		    @Override
		    public boolean onError(MediaPlayer mp, int what, int extra) {
		    	Toast.makeText(getApplicationContext(), "Video Player Error" + " " + String.valueOf(what) + "  " + String.valueOf(extra) + "",Toast.LENGTH_SHORT).show();
		    	//mp.start();
		        return true;
		    }
		};
		
	 @Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	        super.onWindowFocusChanged(hasFocus);
	        if (hasFocus) {
	            // Launch the IME after a bit
	            Toast.makeText(getApplicationContext(), "Windows Screen Size Changed",Toast.LENGTH_SHORT).show();

	        }
	 }
	 
	 private String compareDates(String givenDateString) {
		 String returns = "";
		 try{ 
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		    Date now = new Date();
			String strDate = curFormater.format(now);
			Date date2 = new Date();
			Date date1 = curFormater.parse(strDate);
			date2 = curFormater.parse(givenDateString);
			
			if(date1.compareTo(date2)>0){
        		
				Log.d(TAG,"Dates Now After : " + strDate + "----" + givenDateString);
				returns ="After";
			}else if(date1.compareTo(date2)<0){
        		
				Log.d(TAG,"Dates Now Before : " + strDate + "----" + givenDateString);
				returns ="Before";
        	}else if(date1.compareTo(date2)==0){
        		
        		Log.d(TAG,"Dates Now Equal : " + strDate + "----" + givenDateString);
        		returns ="Equal";
        	}else{
        	
        		Log.d(TAG,"Dates Now Types Not Found : " + strDate + "----" + givenDateString);
        		returns ="Nothing";
        	}
			
		 	}catch(ParseException ex){
	    		Log.d(TAG,ex.getMessage());
	    	}
		 
		 	return returns;
		}
	 
	 private String Activity_DateTime(String xml_data) {
		 String returns = "";
		 
		 return returns;
	}
	 
	 private String parserRss(String urls){
	     URL rssurl;
	     String get_data = "";
	  try {
		  
		  rssurl = new URL(urls);
		  URLConnection ucon = rssurl.openConnection();
		  ucon.connect();

	      	 Document dom = RssFunctions.XMLfromString(convertStreamToString(rssurl.openStream()));
	         Element root = dom.getDocumentElement();
	         NodeList items = root.getElementsByTagName("item");
	    
	         
	         for (int i=0;i<3;i++){
	             Element item = (Element)items.item(i);
	             String noHTMLString = RssFunctions.getValue(item, "description").replaceAll("\\<.*?\\>", "");
	          	 get_data += RssFunctions.getValue(item, "title") + ":" + noHTMLString;
	         }
	         
	  		} catch (MalformedURLException e) {
	  			Log.d(TAG,e.getMessage());
	  		} catch (IOException e) {
	 
	  			Log.d(TAG,e.getMessage());
	  		}
	  return get_data;
	  }
	  
	 
	 public String convertStreamToString(InputStream is) throws IOException {

	        if (is != null) {
	            StringBuilder sb = new StringBuilder();
	            String line;

	            try {
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	                while ((line = reader.readLine()) != null) {
	                    sb.append(line).append("\n");
	                }
	            } finally {
	                is.close();
	            }
	            return sb.toString();
	        } else {       
	            return "";
	        }
	    }

	 
	 
	 
}
