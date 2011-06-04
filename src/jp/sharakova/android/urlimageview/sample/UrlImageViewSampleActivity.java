package jp.sharakova.android.urlimageview.sample;

import jp.sharakova.android.urlimageview.CacheUtils;
import jp.sharakova.android.urlimageview.R;
import jp.sharakova.android.urlimageview.UrlImageView;
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
        mImageView.setOnLoadStartRunnable(startRunnable);
        mImageView.setOnLoadEndRunnable(endRunnable);
        mImageView.setImageUrl("http://pic.prcm.jp/gazo/bN9/fAqy87.jpeg");
    }
    
    @Override
    public void onDestroy() {
    	CacheUtils.deleteAll(this);
    	super.onDestroy();
    }
    
    private final Runnable startRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
		}
    };

    private final Runnable endRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
		}
    };

    
}