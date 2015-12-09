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
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khs.trouble.codeblock.ExceptionBlock;
import khs.trouble.codeblock.KillBlock;
import khs.trouble.codeblock.LoadBlock;
import khs.trouble.codeblock.MemoryBlock;

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
		// optional overrides
		String killBlock = getServletConfig().getInitParameter("kill");
		String memoryBlock = getServletConfig().getInitParameter("memory");
		String exceptionBlock = getServletConfig()
				.getInitParameter("exception");
		String loadBlock = getServletConfig().getInitParameter("load");
		long timeout = 0;
		String stimeout = request.getHeader("timeout");
		if (stimeout != null)  {
			timeout = new Long(stimeout);
		}

		// Validate token access
		if (requestToken == null || token == null
				|| !token.equals(requestToken)) {
			throw new RuntimeException(
					"Access tokens don't match, TROUBLE API aborted");
		}

		String uri = request.getRequestURI();
		String[] parts = uri.split("/");
		String action = parts[2];
		CodeBlock code = null;

		LOG.info("Executing Trouble Action: " + action);

		if (action.toUpperCase().equals(KILL)) {

			if (killBlock == null) {
				code = new KillBlock(timeout);
			} else {
				code = create(killBlock);
			}

		}

		if (action.toUpperCase().equals(LOAD)) {

			if (loadBlock == null) {
				code = new LoadBlock(timeout);
			} else {
				code = create(loadBlock);
			}

			this.spawnCodeBlockThread(code);

		}

		if (action.toUpperCase().equals(EXCEPTION)) {

			if (exceptionBlock == null) {
				code = new ExceptionBlock(timeout);
			} else {
				code = create(exceptionBlock);
			}

		}

		if (action.toUpperCase().equals(MEMORY)) {

			if (memoryBlock == null) {
				code = new MemoryBlock(timeout);
			} else {
				code = create(memoryBlock);
			}

		}

		if (code != null) {

			this.spawnCodeBlockThread(code);
		}

		Writer writer = response.getWriter();
		writer.write(action + ": executed");

	}

	public void spawnCodeBlockThread(final CodeBlock block) {

		Runnable run = new Runnable() {

			public void run() {
				try {

					block.eval();

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};

		Thread thread = new Thread(run);
		thread.start();
	}

	private CodeBlock create(String clazzName) {

		CodeBlock codeBlock = null;
		try {
			codeBlock = (CodeBlock) Class.forName(clazzName).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"ERROR Creating Trouble Maker Class "
							+ clazzName
							+ " Make sure it implements khs.trouble.sevlet.CodeBlock and is in classpath...");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"ERROR Creating Trouble Maker Class "
							+ clazzName
							+ " Make sure it extends from khs.trouble.sevlet.BaseCodeBlock and is in classpath...");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"ERROR Creating Trouble Maker Class "
							+ clazzName
							+ " Make sure it extends from khs.trouble.sevlet.BaseCodeBlock and is in classpath...");
		} catch (ClassCastException e) {
			throw new RuntimeException(
					"ERROR Creating Trouble Maker Class "
							+ clazzName
							+ " Make sure it extends from khs.trouble.sevlet.BaseCodeBlock and is in classpath...");
		}

		return codeBlock;

	}

}
