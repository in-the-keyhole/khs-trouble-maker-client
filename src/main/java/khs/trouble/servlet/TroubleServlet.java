/*
 * Copyright 2015 Keyhole Software LLC.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package khs.trouble.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TroubleServlet extends HttpServlet {

	Logger LOG = Logger.getLogger(TroubleServlet.class.getName());

	private static final long serialVersionUID = 7378911082235628782L;

	private static final String KILL = "KILL";
	private static final String LOAD = "LOAD";
	private static final String MEMORY = "MEMORY";
	private static final String EXCEPTION = "EXCEPTION";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestToken = request.getHeader("token");

		String token = getServletConfig().getInitParameter("token");

		// Validate token access
		if (requestToken == null || token == null
				|| !token.equals(requestToken)) {
			throw new RuntimeException(
					"Access tokens don't match, TROUBLE API aborted");
		}

		String uri = request.getRequestURI();
		String[] parts = uri.split("/");
		String action = parts[2];
		if (action.toUpperCase().equals(KILL)) {

			Runnable run = new Runnable() {

				public void run() {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.exit(-1);
				}

			};

			Thread thread = new Thread(run);
			thread.start();
		}

		if (action.toUpperCase().equals(LOAD)) {

			long timeout = new Long(request.getHeader("timeout"));
			long sleep = 1;
			String msg = timeout == 0 ? "NEVER" : "" + (timeout / 60000)
					+ " minute(s)";
			LOG.info("STARTING Trouble Maker Load thread, will timeout in "
					+ msg);

			long start = System.currentTimeMillis();
			// block for specified period of time (milliseconds)
			while (true) {
				// log.info("Thread "+t+" executing...");
				if (System.currentTimeMillis() - start >= timeout
						&& timeout > 0) {
					break;
				}

			}

			LOG.info("DONE Trouble Maker Load thread");

			// ThreadBlocker blocker = new ThreadBlocker(sleep,timeout);
			// blocker.block(100);

		}

		if (action.toUpperCase().equals(EXCEPTION)) {

			Runnable run = new Runnable() {

				public void run() {
					try {
						Thread.sleep(1000);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					LOG.info("throwing Trouble Maker runtime exception");

					throw new RuntimeException(
							"Trouble Maker Runtime Exception");

				}

			};

			Thread thread = new Thread(run);
			thread.setName("TroubleMaker");
			thread.start();

		}

		if (action.toUpperCase().equals(MEMORY)) {

			final long timeout = new Long(request.getHeader("timeout"));
			Runnable run = new Runnable() {

				public void run() {

					long memory = Runtime.getRuntime().freeMemory();
					LOG.info("Eating Memory Started at: " + memory);

					List<char[]> buffer = new ArrayList<char[]>();

					while (true) {

						try {
							char[] c = new char[Integer.MAX_VALUE / 4];
							for (int i = 0; i < Integer.MAX_VALUE / 4; i++) {
								c[i] = (char) i;

								if (i % 1000000 == 0) {
									LOG.info("Memory Eaten -"
											+ (Runtime.getRuntime()
													.freeMemory() - memory));
								}
								buffer.add(c);
							}
						} catch (OutOfMemoryError e) {
							long start = System.currentTimeMillis();
							String msg = timeout == 0 ? "NEVER" : ""
									+ (timeout / 60000) + " minute(s)";
							LOG.info("Heap MEMORY limit reached...will stay in low memory condition for "
									+ msg);
							while (true) {

								try {

									long sleep = 1000;
									if (System.currentTimeMillis() - start >= timeout
											&& timeout > 0) {
										buffer = null;
										break;
									}

									Thread.sleep(sleep);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}
							buffer = null;
							LOG.info("Memory Consumption stopped");

							Thread.currentThread().stop();

						}

					}

				}

			};

			Thread thread = new Thread(run);
			thread.setName("TroubleMaker");
			thread.start();

		}

		Writer writer = response.getWriter();
		writer.write(action + ": executed");

	}

	public void spawnBlockThread(final long sleep) {

		Runnable run = new Runnable() {

			public void run() {
				try {

					Thread.sleep(sleep);
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
