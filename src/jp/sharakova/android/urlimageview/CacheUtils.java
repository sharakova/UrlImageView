package jp.sharakova.android.urlimageview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class CacheUtils {
	
	private static String getFileName(String url) {
		int hash = url.hashCode();
		return String.valueOf(hash);
	}
	
	public static void saveByteData (Context context, String url, byte[] w) {
		String fileName = getFileName(url);
		FileOutputStream fos = null;
		try {
			File file = new File(context.getCacheDir(), fileName);
			fos = new FileOutputStream(file);
			fos.write(w);
			fos.close();
		} catch (Exception e) {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					Log.w("save", "finally");
				}
			}
		}
	}
	
	public static void saveBitmap(Context context, String url, SoftReference<Bitmap> bitmap) {
		String fileName = getFileName(url);
		FileOutputStream fos = null;
		try {
			File file = new File(context.getCacheDir(), fileName);
			fos = new FileOutputStream(file);
			bitmap.get().compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		} catch (Exception e) {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					Log.w("save", "finally");
				}
			}
		}
	}
	
	public static SoftReference<Bitmap> getFile(Context context, String url) {
		String fileName = getFileName(url);
		SoftReference<Bitmap> bitmap = null;
		try {
			String filePath = context.getCacheDir() + "/" + fileName;
			bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static void deleteAll(Context context) {
		File dir = context.getCacheDir();
		if (!dir.isDirectory()){
			return;
		}
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++){
			File file = files[i];
			if (file.isFile()){
				file.delete();
			}
		}
	}
}