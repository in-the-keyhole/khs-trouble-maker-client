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

public class ExceptionBlock extends BaseCodeBlock {

	Logger LOG = Logger.getLogger(ExceptionBlock.class.getName());

	public ExceptionBlock(long timeout) {
		super(timeout);

	}

	@Override
	public void eval() {
		try {
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("throwing Trouble Maker runtime exception");

		throw new RuntimeException("Trouble Maker Runtime Exception");

	}

}
