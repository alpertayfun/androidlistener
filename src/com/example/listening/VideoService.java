/**
 * @author egegen
 *
 */


package com.example.listening;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class VideoService extends Service {
	
	private static final String TAG = "Video Service";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		//Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		VideoStarted();
		/*
		Thread VideoStart = null;
	    Runnable VideoStartrunnable = new VideoStartHandle();
	    VideoStart= new Thread(VideoStartrunnable);   
	    VideoStart.start();
	    */
	}
	
	class VideoStartHandle implements Runnable{
	    public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                	VideoStarted();
                    Thread.sleep(30000000);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }catch(Exception e){
                }
	    }
	}
	}
	
	private void VideoStarted() {
		

	}

}
