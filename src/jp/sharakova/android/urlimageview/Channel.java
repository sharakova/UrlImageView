package jp.sharakova.android.urlimageview;

import java.util.LinkedList;

public final class Channel {

	public enum Priority {
		HIGH, LOW
	}

	private final static Channel instance = new Channel();

	static {
		instance.startWorkers();
	}

	private static final int MAX_THREAD = 5;
	private final LinkedList<Request> requestQueue = new LinkedList<Request>();
	private final WorkerThread[] threadPool;

	private Channel() {
		threadPool = new WorkerThread[MAX_THREAD];
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new WorkerThread("Worker-" + i, this);
		}
	}

	public static Channel getInstance() {
		return instance;
	}

	public synchronized void removeQueueAll() {
		requestQueue.clear();
	}

	public void startWorkers() {
		for (WorkerThread thread : threadPool) {
			thread.start();
		}
	}

	public void stopWorkers() {
		for (WorkerThread thread : threadPool) {
			thread.interrupt();
		}
	}

	public synchronized void putRequest(Request request, Priority priority) {
		if (priority == Priority.HIGH) {
			requestQueue.addFirst(request);
		} else {
			requestQueue.addLast(request);
		}
		notifyAll();
	}

	public synchronized Request takeRequest() {
		while (requestQueue.size() <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return requestQueue.poll();
	}
}
