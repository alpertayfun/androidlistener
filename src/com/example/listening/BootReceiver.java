/**
 * @author egegen
 *
 */

package com.example.listening;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class BootReceiver extends BroadcastReceiver{
	
	private static final String firm_name = "Gen";
	private static final String TAG = firm_name + " Digital Signage Client";
	
@Override
public void onReceive(Context context, Intent intent) {
        	 Log.d(TAG, "Boot Service Started");
             Intent i = new Intent(context, com.example.listening.Listening.class);
             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(i);

    
             /*
             try {
            	
            	Runtime.getRuntime().exec("su");
            	Runtime.getRuntime().exec("adb kill-server");
            	Runtime.getRuntime().exec("stop adb");
            	Runtime.getRuntime().exec("stop adbd");
            	Runtime.getRuntime().exec("setprop service.adb.tcp.port 5555");
     			Runtime.getRuntime().exec("stop adbd");
     			Runtime.getRuntime().exec("start adbd");
     			Runtime.getRuntime().exec("start adb");
     			Runtime.getRuntime().exec("adb start-server");
     			
     			Log.d(TAG,"Runtime Exec completed");
     			} catch (IOException e) {
     			Log.e(TAG,e.getMessage());
     			}
     			*/
       }
}