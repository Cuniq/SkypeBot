/*
 *    Copyright [2016] [Thanasis Argyroudis]

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

	private static final int editOutputMethod = Config.EditOutput;

	/**
	 * It is static because all edit handler will right in the same file.
	 */
	private static BufferedWriter writer;

	private static final String DEFAULT_EDIT_LOG_FILE_NAME = "LogEdits.txt";

	/**
	 * Instantiates a new message edit handler.
	 *
	 * @param messages
	 *            the messages to keep track of.
	 */
	public MessageEditHandler(ConcurrentHashMap<ChatMessage, String> messages) {
		this.messages = messages;

		if (editOutputMethod == Config.EDIT_OUTPUT_TO_FILE) {
			writer = openLogFile();
		} else { //No file needed
			writer = null;
		}
	}

	/**
	 * The main handler's method. This method does the job of processing the message.
	 *
	 * @param msg
	 *            the message which was edited
	 * @throws SkypeException
	 */
	public void handleEdit(ChatMessage msg) throws SkypeException {
		if (messages.containsKey(msg)) {
			handle(msg);
			messages.put(msg, msg.getContent()); // update the message that just changed.
		}
	}

	/**
	 * This method will send the edited message to the right place based on user's
	 * option in config. If the user has disables self edits then it will return.
	 * 
	 * @throws SkypeException
	 */
	private void handle(ChatMessage msg) throws SkypeException {
		if (!Config.EnableSelfEdits && msg.getId().equals(BotUserInfo.getBotUserID()))
			return;

		if (editOutputMethod == Config.EDIT_OUTPUT_SAME_CHAT) {
			msg.getChat().send("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg));
		} else if (editOutputMethod == Config.EDIT_OUTPUT_TO_FILE) {
			writeToFile(writer, "Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg) + "\r\n");
			flushWriter();
		} else { //Config.EDIT_OUTPUT_PRIVATE_CHAT
			msg.getSender().send("Original from " + msg.getSenderDisplayName() + ": " + messages.get(msg));
		}
	}

	/**
	 * Opens the log file if method {@link Config.EDIT_OUTPUT_TO_FILE} is chosen.
	 * First of all checks if a name was given inside the config file. If not it open
	 * a log file with default name
	 *
	 * @return the buffered writer
	 */
	private BufferedWriter openLogFile() {
		String strPath = null;
		Path path = null;

		if (Config.EditPath.equals(""))
			strPath = DEFAULT_EDIT_LOG_FILE_NAME;
		else
			strPath = Config.EditPath;

		path = Paths.get(strPath);

		if (Files.exists(path) && Files.isRegularFile(path)) { // If exists append
			BufferedWriter tmp = FileUtil.openWriteFile(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
			writeToFile(tmp, "\r\n");
			return tmp;
		} else {
			return FileUtil.openWriteFile(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		}
	}
	
	private void writeToFile(BufferedWriter fileWriter, String text) {
		try {
			fileWriter.write(text);
		} catch (IOException e) {
			new WarningPopup(e.getMessage());
		}
	}

	private void flushWriter() {
		try {
			writer.flush();
		} catch (IOException e) {
			new WarningPopup(e.getLocalizedMessage());
		}
	}

}
