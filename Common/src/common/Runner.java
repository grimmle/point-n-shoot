package common;
import java.lang.Runnable;

public class Runner implements Runnable {
	
	int thread;
	public Runner(int i) {
		this.thread = i;
	}

	public static final Object TOKEN = new Object();

	@Override public void run() {
//		synchronized(TOKEN) {
//		    for ( int i = 0; i <= 100; i++ ) {
//		    	System.out.println(thread + ": " +  i);
//		    	if(i > 0 && i%20 == 0) {
//					try {
//						TOKEN.wait();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//		    	}
//		    }
//		  }
	}
}
