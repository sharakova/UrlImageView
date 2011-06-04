package jp.sharakova.android.urlimageview;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class ImageCache {  
    private static HashMap<String,SoftReference<Bitmap>> cache = new HashMap<String,SoftReference<Bitmap>>();  
      
    public static SoftReference<Bitmap> getImage(Context context, String key) {
    	Log.d("ImageCache::getImage", key);
    	SoftReference<Bitmap> bitmap = cache.get(key);
    	if (bitmap == null || bitmap.get() == null) {
    		bitmap = CacheUtils.getFile(context, key);
        }
        return bitmap;
    }
      
    public static void setImage(Context context, String key, SoftReference<Bitmap> bitmap) {
    	try {
	    	Log.d("ImageCache::setImage", key);
	        cache.put(key, bitmap);
	        CacheUtils.saveBitmap(context, key, bitmap);
    	} catch (Exception e) {
    		e.printStackTrace();
    	} catch (OutOfMemoryError e) {
    		e.printStackTrace();
    	}
    }
    
    public static void clear(Context context) {
    	cache.clear();
    	CacheUtils.deleteAll(context);
    }
}