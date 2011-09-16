package jp.sharakova.android.urlimageview;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WorkerThread extends Thread {
	private final Channel channel;

	public WorkerThread(String name, Channel channel) {
		super(name);
		this.channel = channel;
	}
	
	public void run() {
		while (true) {
			Request request = channel.takeRequest();
			request.setStatus(Request.Status.LOADING);
			SoftReference<Bitmap> image = ImageCache.getImage(request.getCacheDir(), request.getUrl());
			if(image == null || image.get() == null) {
				image = getImage(request.getUrl());
				if(image != null && image.get() != null) {
					ImageCache.saveBitmap(request.getCacheDir(), request.getUrl(), image.get());
				}
			}
            request.setStatus(Request.Status.LOADED);
        	request.getRunnable().run();
		}
	}
	
    private SoftReference<Bitmap> getImage(String url) {
    	try {
	   		byte[] byteArray = getByteArrayFromURL(url);
	    	return new SoftReference<Bitmap>(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	} catch (OutOfMemoryError e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    private byte[] getByteArrayFromURL(String strUrl) {
        byte[] byteArray = new byte[1024];
        HttpURLConnection con = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        int size = 0;
        try {
            URL url = new URL(strUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(true);
            con.setRequestMethod("GET");
            con.connect();
            in = con.getInputStream();

            out = new ByteArrayOutputStream();
            while ((size = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, size);
            }
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } catch (OutOfMemoryError e) {
        	e.printStackTrace();
        	return new byte[0];
        } finally {
            try {
                if (con != null)
                    con.disconnect();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}