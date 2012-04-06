package jp.sharakova.android.urlimageview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageCache {

	private ImageCache() {
	}

	private static HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

	private static String getFileName(String url) {
		int hash = url.hashCode();
		return String.valueOf(hash);
	}

	public static void saveBitmap(File cacheDir, String url, Bitmap bitmap) {
		cache.put(url, new SoftReference<Bitmap>(bitmap));

		String fileName = getFileName(url);
		File localFile = new File(cacheDir, fileName);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(localFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					Log.w("save", "finally");
				}
			}
		}
	}

	public static SoftReference<Bitmap> getImage(File cacheDir, String url) {

		SoftReference<Bitmap> ref = cache.get(url);
		if (ref != null && ref.get() != null) {
			return ref;
		}

		String fileName = getFileName(url);
		File localFile = new File(cacheDir, fileName);
		SoftReference<Bitmap> bitmap = null;
		try {
			bitmap = new SoftReference<Bitmap>(
					BitmapFactory.decodeFile(localFile.getPath()));
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			cache.clear();
		}
		return bitmap;
	}

	public static void memoryCacheClear() {
		cache.clear();
	}

	public static void deleteAll(File cacheDir) {
		if (!cacheDir.isDirectory()) {
			return;
		}
		File[] files = cacheDir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (!file.delete()) {
					Log.v("file", "file delete false");
				}
			}
		}
	}

	public static long dirSize(File cacheDir) {
		long size = 0L;
		if (cacheDir == null) {
			return size;
		}
		if (cacheDir.isDirectory()) {
			for (File file : cacheDir.listFiles()) {
				size += file.length();
			}
		} else {
			size = cacheDir.length();
		}
		return size;
	}
}