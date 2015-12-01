package khs.trouble.servlet;

import java.util.logging.Logger;

public class ThreadBlocker {

	private Logger log = Logger.getLogger(TroubleServlet.class.getName());

	private long sleep;
	private long timeout;

	public ThreadBlocker(long sleep, long timeout) {
		this.sleep = sleep;
		this.timeout = timeout;
	}

	public void block(int threads) {

		for (int i = 0; i < threads; i++) {

			final int t = i;
			log.info("Starting Blocking Thread " + i);

			Runnable run = new Runnable() {

				public void run() {
					try {

						long block = new Long(timeout);
						long start = System.currentTimeMillis();
						// block for specified period of time (milliseconds)
						while (System.currentTimeMillis() - start < block) {
                            log.info("Thread "+t+" executing...");
							Thread.sleep(sleep);
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};

			Thread thread = new Thread(run);
			thread.start();

		}

	}

}
