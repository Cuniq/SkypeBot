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
package skype.handlers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

import skype.gui.popups.WarningPopup;
import skype.listeners.GroupChatListener;
import skype.utils.Config;
import skype.utils.FileUtil;

import com.skype.ChatMessage;
import com.skype.SkypeException;

/**
 * The class MessageEditHandler. This class will handle the edited message based on
 * user's config.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class MessageEditHandler {

	/** Reference at messages received from {@link GroupChatListener}. */
	private ConcurrentHashMap<ChatMessage, String> messages = null;

	/** The edit output method. */
	static final byte method = Config.EditOutput;

	/** The edit path if method 3 is chosen. */
	private final BufferedWriter writer;

	/**
	 * Instantiates a new message edit handler.
	 *
	 * @param messages
	 *            the messages
	 */
	public MessageEditHandler(ConcurrentHashMap<ChatMessage, String> messages) {
		this.messages = messages;

		if (method == 3) {
			String strPath = null;
			Path path = null;

			if (Config.EditPath.equals(""))
				strPath = "LogEdits.txt";
			else
				strPath = Config.EditPath;

			path = Paths.get(strPath);
			System.out.println(path.toAbsolutePath());
			if (Files.exists(path) && Files.isRegularFile(path)) { // If exists append
				writer = FileUtil.openWriteFile(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);

				try {
					writer.write("\r\n");
				} catch (IOException e) {
					new WarningPopup(e.getMessage());
				}

			} else {
				writer = FileUtil.openWriteFile(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			}


		} else {
			writer = null;
		}
	}

	/**
	 * The main handler's method. This method does the job of processing the message.
	 *
	 * @param msg
	 *            the message which was edited
	 * @throws SkypeException
	 *             the skype exception
	 */
	public void handleEdit(ChatMessage msg) throws SkypeException {
		if (messages.containsKey(msg)) {
			try {
				displayEditMessage(msg);
			} catch (IOException e) {
				new WarningPopup(e.getMessage());
			}
			messages.put(msg, msg.getContent()); // update the message that just changed.
		}
	}

	/**
	 * This method will send the edited message to the right place based on user's
	 * option in config.
	 * 
	 * @throws SkypeException
	 * @throws IOException
	 */
	private void displayEditMessage(ChatMessage msg) throws SkypeException, IOException {
		if (method == 1) {
			msg.getChat().send("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg));
		} else if (method == 3) { //file
			writer.write("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg) + "\r\n");
			writer.flush();
		} else {
			msg.getSender().send("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg));
		}
	}

}
