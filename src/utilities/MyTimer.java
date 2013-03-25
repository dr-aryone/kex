package utilities;

import javax.swing.event.EventListenerList;

public class MyTimer extends Thread {
	private EventListenerList listenerList;

	public MyTimer() {
		listenerList = new EventListenerList();
	}

	public void listenToCycles(TimeListener l) {
		listenerList.add(TimeListener.class, l);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			tellListeners();
		}
	}

	private void tellListeners() {
		TimeListener[] listeners =  listenerList.getListeners(TimeListener.class);
		for(TimeListener l : listeners) {
			l.newCycle();
		}
	}

}
