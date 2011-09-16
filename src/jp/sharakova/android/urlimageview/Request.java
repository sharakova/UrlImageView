package jp.sharakova.android.urlimageview;

import java.io.File;


public class Request {
	private final String url;
	private final File cacheDir;
	private final Runnable runnable;
	private int status = Status.WAIT;
		
    public interface Status {
        int WAIT = 0;
        int LOADING = 1;
        int LOADED = 2;
    }
    	
	public Request(String url, File cacheDir, Runnable runnable) {
		this.url = url;
		this.cacheDir = cacheDir;
		this.runnable = runnable;
	}
	
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getUrl() {
		return url;
	}
	
	public File getCacheDir() {
		return cacheDir;
	}
	
	public Runnable getRunnable() {
		return (runnable != null)? runnable : getDefaultRunnable();
	}
	
	private Runnable getDefaultRunnable() {
		return new Runnable() {
			public void run() {
			}
		};
	}
}
