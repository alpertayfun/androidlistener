/**
 * @author egegen
 *
 */

package com.example.listening;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
 
public class DrawRect extends View {
	Paint paint = new Paint();
	private static final String TAG = "Digital Signage Client";
	public ArrayList<String> xx = new ArrayList<String>();
	public ArrayList<String> yy = new ArrayList<String>();
	public ArrayList<String> ww = new ArrayList<String>();
	public ArrayList<String> hh = new ArrayList<String>();
	private static RectF rect;
    public DrawRect(Context context) {
		super(context);
	}

	@Override
    public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		//final Random rnd = new Random();
		//p.setColor(Color.BLACK);
		p.setARGB(130, 0, 0, 0);
		 
        String [] xxxx = xx.toArray(new String[xx.size()]);
        String [] yyyy = yy.toArray(new String[yy.size()]);
        String [] wwww = ww.toArray(new String[ww.size()]);
        String [] hhhh = hh.toArray(new String[hh.size()]);

        for (int i=0; i<xx.size();i++) {
        	
       	 if (TextUtils.isEmpty(xxxx[i])) {

       		} else {
       			//p.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       			rect = new RectF(Float.parseFloat(xxxx[i]),Float.parseFloat(yyyy[i]),Float.parseFloat(wwww[i]) + Float.parseFloat(xxxx[i]), Float.parseFloat(hhhh[i]) + Float.parseFloat(yyyy[i]));
    			Log.d(TAG,"" + rect);
    			canvas.drawRect(rect,p);
    			//canvas.drawBitmap(ball, Float.parseFloat(xxxx[i]), Float.parseFloat(yyyy[i]), null);
    			
       		}
        }
	}
}