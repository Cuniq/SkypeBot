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
package skype.commands;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;
import skype.utils.users.UserInformation;
/**
 * The Class CommandSpam.
 * <p>
 * Read <b>description</b> declared in constructor for further informations.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandSpam extends Command {

	private static final int TEXT_POSITION = 0;

	private static final int TIMES_POSITION = 1;

	private static final int RECEIVER_POSITION = 2;

	/**
	 * Maximum times of spam text that can be repeated. I recommend keeping that
	 * number low because it can crush skype or keep the text being spammed for
	 * hours.
	 */
	private static int MAX_REPEATED_TIMES = 20;

	private Chat outputChat = null;

	private String text = null;

	/**
	 * The receiver of the spam. It can be empty. If it is empty then the output chat
	 * is the chat from with the command was called.
	 */
	private User receiver = null;

	private int timesToBeRepeated = 0;

	public CommandSpam() {
		super(
				"spam",
				"Repeats the <text> for <times> times. "
				+ "If an user from the chat was given it repeats <text> in user's private chat. "
				+ "If no user or invalid user was given then it repeats <text> in the chat from which the command was called.",
				"!spam <text> <times> <optional_user>");
	}

	public CommandSpam(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws NullOutputChatException {
		try {

			if (!canBeExecuted())
				return;

			for (int i = 0; i < timesToBeRepeated; i++)
				outputChat.send(text);

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private boolean canBeExecuted() throws NullOutputChatException, SkypeException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		if (timesToBeRepeated >= MAX_REPEATED_TIMES) {
			outputChat.send("Big number of times.");
			return false;
		}

		if (timesToBeRepeated < 0) {
			outputChat.send("Invalid number of times.");
			return false;
		}

		return true;
	}

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
		final String options[] = data.getCommandOptions();
		
		if (options.length >= 2) {
			this.text = options[TEXT_POSITION];
			this.timesToBeRepeated = parseTimesFromString(options[TIMES_POSITION]);

			if (options.length >= 3) {
				findOptionalReceiver(data, options);
			}
		}
		else{
			this.text = "";
			this.timesToBeRepeated = 0;
		}

		defineOutputChat();
	}

	private void findOptionalReceiver(CommandData data, String options[]) {
		final UserInformation userInfo = data.getUserInformation(options[RECEIVER_POSITION]);

		if (userInfo != null)
			this.receiver = userInfo.getUser();
		else
			this.receiver = null;
	}

	private int parseTimesFromString(String times) {
		try {
			return Integer.parseInt(times);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private void defineOutputChat() {
		try {
			if (receiver != null)
				outputChat = receiver.chat();
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}
}