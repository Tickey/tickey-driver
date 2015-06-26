package com.tickey.driver.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tickey.driver.R;
import com.tickey.driver.utility.MyLog;
 
public class CircleNetworkImageView extends NetworkImageView {
	Context mContext;
	boolean drawTick;
	Bitmap tick;
	
	public CircleNetworkImageView(Context context) {
		super(context);
		mContext = context;
	}
 
	public CircleNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}
 
	public CircleNetworkImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}
 
	@Override
	public void setImageBitmap(Bitmap bm) {
		if(bm==null) return;
		setImageDrawable(new BitmapDrawable(mContext.getResources(),
				getCircularBitmap(bm)));
	}
 
	/**
	 * Creates a circular bitmap and uses whichever dimension is smaller to determine the width
	 * <br/>Also constrains the circle to the leftmost part of the image
	 * 
	 * @param bitmap
	 * @return bitmap
	 */
	public Bitmap getCircularBitmap(Bitmap bitmap) {
		
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    
	    int param = getWidth() > getHeight() ? getWidth() : getHeight();
	    
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            bitmap.getWidth() / 2, paint);




	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	    //return _bmp;
	    if(drawTick) {
//	    	if(tick == null) {
//	    		Options options = new BitmapFactory.Options();
//	    	    options.inScaled = false;
//	    		tick = BitmapFactory.decodeResource(mContext.getResources(),
//	                    R.drawable.tick, options);
//	    	}
//		    float params = (bitmap.getWidth() / 4) * 3;
//		    MyLog.i("CyrcleImageView", "Drawning tick with params: " + this.getMeasuredHeight() );
//		    Paint tickPaint = new Paint();
//		    paint.setAntiAlias(true);
//		    paint.setFilterBitmap(true);
//		    paint.setDither(true);
//		    Rect rectTickStart = new Rect(0, 0, tick.getWidth(), tick.getHeight());
//		    int bmW = (bitmap.getWidth() / 3);
//		    int bmH = (bitmap.getHeight() / 3);
//		    int bm = (bmW < bmH ) ? bmW : bmH;
//		    Rect rectTickEnd = new Rect(bmW * 2 ,bmH * 2, bmW * 2 + bm, bmH * 2 + bm ) ;
//		    canvas.drawBitmap(tick, null, rectTickEnd, null);

	    }
	    return output;
	    
//		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//				bitmap.getHeight(), Config.ARGB_8888);
//		Canvas canvas = new Canvas(output);
//		int width = bitmap.getWidth();
//		if(bitmap.getWidth()>bitmap.getHeight())
//			width = bitmap.getHeight();
//		final int color = 0xff424242;
//		final Paint paint = new Paint();
//		final Rect rect = new Rect(0, 0, getHeight(), getHeight());
//		final RectF rectF = new RectF(rect);
//		final float roundPx = getHeight() / 2;
// 
//		paint.setAntiAlias(true);
//		canvas.drawARGB(0, 0, 0, 0);
//		paint.setColor(color);
//		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
// 
//		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//		canvas.drawBitmap(bitmap, rect, rect, paint);
// 
//		return output;
	}
 
	
    public void setImageUrl(String url, ImageLoader imageLoader, boolean drawTick) {
    	this.drawTick = drawTick;
    	setImageUrl(url, imageLoader);
    }
}