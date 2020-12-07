package common;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleThreads{

	static final int threadCount = 5;
	static final Object TOKEN = new Object();
	
	static int waitingThreads;
	static AtomicInteger tc = new AtomicInteger();
	
	public static void main(String[] args) {
		for(int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				int n = tc.incrementAndGet();
				for ( int i = 1; i < 100; i++ ) {
			    	System.out.println(n + ": " +  i);
			    	if(i%20 == 0) {
			    		synchronized(TOKEN) {
			    			++waitingThreads;
			    			if(waitingThreads == threadCount) {
			    				waitingThreads = 0;
			    				TOKEN.notifyAll();
			    			} else {
								try {
									System.out.println(n + ": yo im waiting");
									TOKEN.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
			    			}
			    		}
			    	}
			    }
			}).start();
		}
//		Thread t1 = new Thread( new Runner(1) );
//		Thread t2 = new Thread( new Runner(2) );
//		Thread t3 = new Thread( new Runner(3) );
//		Thread t4 = new Thread( new Runner(4) );
//		Thread t5 = new Thread( new Runner(5) );
//		t1.start();		
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
	}
}
