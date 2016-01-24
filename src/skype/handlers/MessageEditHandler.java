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

import com.skype.ChatMessage;
import com.skype.SkypeException;

import skype.gui.popups.WarningPopup;
import skype.listeners.GroupChatListener;
import skype.utils.Config;
import skype.utils.FileUtil;
import skype.utils.users.BotUserInfo;

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
	private static final int method = Config.EditOutput;

	/**
	 * The edit path if method 3 is chosen. It is static because all edit handler
	 * will right in the same file.
	 */
	private static BufferedWriter writer;

	/** Default logger file name */
	private static final String DEFAULT_EDIT_FILE = "LogEdits.txt";

	/**
	 * Instantiates a new message edit handler.
	 *
	 * @param messages
	 *            the messages
	 */
	public MessageEditHandler(ConcurrentHashMap<ChatMessage, String> messages) {
		this.messages = messages;

		if (method == Config.EDIT_OUTPUT_FILE) {
			writer = openLogFile();
		} else { //No need file
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
				handle(msg);
			} catch (IOException e) {
				new WarningPopup(e.getMessage());
			}
			messages.put(msg, msg.getContent()); // update the message that just changed.
		}
	}

	/**
	 * This method will send the edited message to the right place based on user's
	 * option in config. If the user has disables self edits then it will return.
	 * 
	 * @throws SkypeException
	 * @throws IOException
	 */
	private void handle(ChatMessage msg) throws SkypeException, IOException {
		if (!Config.EnableSelfEdits && msg.getId().equals(BotUserInfo.getUserSkypeID()))
			return;

		if (method == Config.EDIT_OUTPUT_SAME_CHAT) {
			msg.getChat().send("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg));
		} else if (method == Config.EDIT_OUTPUT_FILE) {
			writer.write("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg) + "\r\n");
			writer.flush();
		} else { //Config.EDIT_OUTPUT_PRIVATE_CHAT
			msg.getSender().send("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg));
		}
	}

	/**
	 * Opens the log file if method {@link Config.EDIT_OUTPUT_FILE} is chosen. First
	 * of all checks if a name was given inside the config file. If not it open a log
	 * file with default name
	 *
	 * @return the buffered writer
	 */
	private final BufferedWriter openLogFile() {
		String strPath = null;
		Path path = null;

		if (Config.EditPath.equals(""))
			strPath = DEFAULT_EDIT_FILE;
		else
			strPath = Config.EditPath;

		path = Paths.get(strPath);

		if (Files.exists(path) && Files.isRegularFile(path)) { // If exists append
			BufferedWriter tmp = FileUtil.openWriteFile(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
			try {
				tmp.write("\r\n");
			} catch (IOException e) {
				new WarningPopup(e.getMessage());
			}
			return tmp;
		} else {
			return FileUtil.openWriteFile(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		}
	}

}
