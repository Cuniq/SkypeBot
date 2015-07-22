/*
 *    Copyright [2015] [Thanasis Argyroudis]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package skype.utils;

import java.util.LinkedList;

import com.skype.ChatMessage;

//The Class Producer. This class is here only for completeness to producer-consumer
// design. It is useless for the current bot design but future iteration may use it.
/**
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class Producer {

	/** The list. */
	private final LinkedList<ChatMessage> list = new LinkedList<ChatMessage>();

	/**
	 * Instantiates a new producer.
	 */
	public Producer() {

	}

	/**
	 * Adds the.
	 *
	 * @param msg
	 *            the msg
	 */
	public void add(ChatMessage msg) {
		list.addLast(msg);
	}

	/**
	 * Gets the list.
	 *
	 * @return the list
	 */
	public LinkedList<ChatMessage> getList() {
		return list;
	}

}
