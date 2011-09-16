package jp.sharakova.android.urlimageview;

import java.lang.ref.SoftReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;


public class UrlImageView extends ImageView {
	private final Context context;
	
	private Request request;
	private String url;
	private final Handler handler = new Handler();
	private OnImageLoadListener listener = new OnImageLoadListener() {
		public void onStart(String url) {
		}
		public void onComplete(String url) {
		}		
	};
	
    public static interface OnImageLoadListener {
        public void onStart(String url);
        public void onComplete(String url);
    }
	
	private final Runnable threadRunnable = new Runnable() {
		public void run() {
			handler.post(imageLoadRunnable);
		}
	};
	
	private final Runnable imageLoadRunnable = new Runnable(){
		public void run(){
			setImageLocalCache();
		}
	};
	
	private boolean setImageLocalCache() {
		SoftReference<Bitmap> image = ImageCache.getImage(context.getCacheDir(),url);
		if(image != null && image.get() != null) {
			setImageBitmap(image.get());
			listener.onComplete(url);
			return true;
		}
		return false;
	}
	
	public UrlImageView(Context context) {
		super(context);
		this.context = context;
	}
	
	public UrlImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public UrlImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	public void setOnImageLoadListener(OnImageLoadListener listener) {
		this.listener = listener;
	}
		
	public void setImageUrl(String url, OnImageLoadListener listener) {
		setOnImageLoadListener(listener);
		setImageUrl(url);
	}
	
	public void setImageUrl(String url) {
		this.url = url;
		request = new Request(url, context.getCacheDir(), threadRunnable);
		if(setImageLocalCache()){
			return;
		}
		
		listener.onStart(url);
		Channel.getInstance().putRequest(request);
	}
}