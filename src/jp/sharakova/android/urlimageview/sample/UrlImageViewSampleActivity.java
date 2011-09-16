package jp.sharakova.android.urlimageview.sample;

import jp.sharakova.android.urlimageview.ImageCache;
import jp.sharakova.android.urlimageview.R;
import jp.sharakova.android.urlimageview.UrlImageView;
import jp.sharakova.android.urlimageview.UrlImageView.OnImageLoadListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class UrlImageViewSampleActivity extends Activity {
	
	UrlImageView mImageView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mImageView = (UrlImageView)findViewById(R.id.imageView);
        mImageView.setImageUrl("http://k.yimg.jp/images/top/sp/logo.gif", imageLoadListener);
    }
    
    @Override
    public void onDestroy() {
    	ImageCache.deleteAll(getCacheDir());
    	super.onDestroy();
    }

    final private OnImageLoadListener imageLoadListener = new OnImageLoadListener() {
		@Override
		public void onStart(String url) {
			Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(String url) {
			Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
		}
    };  
}