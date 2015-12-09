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

package khs.trouble.codeblock;

import java.util.logging.Logger;

public class LoadBlock extends BaseCodeBlock {

	Logger LOG = Logger.getLogger(LoadBlock.class.getName());

	public LoadBlock(long timeout) {
		super(timeout);
	}

	@Override
	public void eval() {

		long sleep = 1;
		String msg = timeout == 0 ? "NEVER" : "" + (timeout / 60000)
				+ " minute(s)";
		LOG.info("STARTING Trouble Maker Load thread, will timeout in " + msg);

		long start = System.currentTimeMillis();
		// block for specified period of time (milliseconds)
		while (true) {
			// log.info("Thread "+t+" executing...");
			if (System.currentTimeMillis() - start >= timeout && timeout > 0) {
				break;
			}

		}

		LOG.info("DONE Trouble Maker Load thread");

	}

}
