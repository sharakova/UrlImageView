package jp.sharakova.android.urlimageview;

import java.lang.ref.SoftReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UrlImageView extends ImageView {
	private Context context;
	private Runnable mEndListener;
	private Runnable mStartListener;
	
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
		
	public void setOnLoadStartRunnable (Runnable runable) {
		mStartListener = runable;
	}

	public void setOnLoadEndRunnable (Runnable runnable) {
		mEndListener = runnable;
	}
	
	public void setImageUrl(String Url) {
		SoftReference<Bitmap> image = ImageCache.getImage(this.context,Url);
		if(image != null && image.get() != null) {
			setImageBitmap(image.get());
			return;
		}
		if (mStartListener != null) {
			mStartListener.run();
		}
		ImageDownloadTask task = new ImageDownloadTask();
		task.execute(Url);
	}
	
	private class ImageDownloadTask extends AsyncTask<String, Void, SoftReference<Bitmap>> {
		
	    // バックグラウンドで実行する処理 
	    @Override
	    protected SoftReference<Bitmap> doInBackground(String... urls) {
	    	SoftReference<Bitmap> image = null;
	    	try {
       			image = HttpClient.getImage(urls[0]);
	            ImageCache.setImage(context, urls[0], image);
	            return image;
	    	} catch (Exception e) {
				e.printStackTrace();
	    	} catch (OutOfMemoryError e) {
	    		e.printStackTrace();
	    	}
			return null;
	    }
	  
	    // メインスレッドで実行する処理  
	    @Override
	    protected void onPostExecute(SoftReference<Bitmap> result) {
	    	if(result != null && result.get() != null) {
	    		setImageBitmap(result.get());
	    	}
	    	
	    	if(mEndListener != null) {
	    		mEndListener.run();
	    	}
	    }
	}
}