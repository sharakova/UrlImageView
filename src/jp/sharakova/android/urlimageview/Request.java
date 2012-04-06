package jp.sharakova.android.urlimageview;

import java.io.File;

public final class Request {
	private final String url;
	private final File cacheDir;
	private final Runnable runnable;
	private Status status = Status.WAIT;

	public enum Status {
		WAIT, LOADING, LOADED
	}

	public Request(String url, File cacheDir) {
		this.url = url;
		this.cacheDir = cacheDir;
		this.runnable = getDefaultRunnable();
	}

	public Request(String url, File cacheDir, Runnable runnable) {
		this.url = url;
		this.cacheDir = cacheDir;
		this.runnable = runnable;
	}

	public synchronized void setStatus(Status status) {
		this.status = status;
	}

	public synchronized Status getStatus() {
		return status;
	}

	public String getUrl() {
		return url;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	public Runnable getRunnable() {
		return (runnable != null) ? runnable : getDefaultRunnable();
	}

	private Runnable getDefaultRunnable() {
		return new Runnable() {
			public void run() {
			}
		};
	}
}
