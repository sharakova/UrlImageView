package jp.sharakova.android.urlimageview;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class WorkerThread extends Thread {
	private final Channel channel;

	public WorkerThread(String name, Channel channel) {
		super(name);
		this.channel = channel;
	}

	@Override
	public void run() {
		while (true) {
			Request request = channel.takeRequest();
			request.setStatus(Request.Status.LOADING);
			SoftReference<Bitmap> image = ImageCache.getImage(
					request.getCacheDir(), request.getUrl());
			if (image == null || image.get() == null) {
				image = getImage(request.getUrl());
				if (image != null && image.get() != null) {
					ImageCache.saveBitmap(request.getCacheDir(),
							request.getUrl(), image.get());
				}
			}
			request.setStatus(Request.Status.LOADED);
			request.getRunnable().run();
		}
	}

	private SoftReference<Bitmap> getImage(String url) {
		try {
			return new SoftReference<Bitmap>(getBitmapFromURL(url));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	private Bitmap getBitmapFromURL(String strUrl) throws IOException {
		HttpURLConnection con = null;
		InputStream in = null;

		try {
			URL url = new URL(strUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(500000);
			con.setConnectTimeout(50000);
			con.connect();
			in = con.getInputStream();
			return BitmapFactory.decodeStream(in);
		} finally {
			try {
				if (con != null)
					con.disconnect();
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}