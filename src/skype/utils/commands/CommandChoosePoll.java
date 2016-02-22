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
package skype.utils.commands;

import java.util.HashSet;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;
import skype.handlers.CommandHandler;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

/**
 * The Class CommandChoosePoll. This class will create a poll for the given question.
 * Each user can vote only one time. The poll will last for 10 minutes and <b>ONLY
 * ONE</b> poll can be ongoing. The {@link CommandHandler} is responsible for
 * duration and keep one poll going per time.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandChoosePoll extends Command {

	/** The output chat. */
	private Chat outputChat = null;

	/** The question. */
	private String question = null;

	/** The choices. */
	private String[] choices = null;

	/** The votes. */
	private int[] votes = null;

	/** The users that have already voted. */
	private HashSet<User> voted = new HashSet<User>(10);

	/**
	 * Instantiates a new command choose poll.
	 */
	public CommandChoosePoll() {
		name = "choosepoll";
		description = "Creates a poll with given question and choices of which you choose one.";
		usage = "!choosepoll <question> [<choice>,<choice>,...]";
	}
	
	/**
	 * Instantiates a new command choose poll.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param question
	 *            the question
	 * @param choices
	 *            the choices
	 */
	public CommandChoosePoll(Chat outputChat, String question, String[] choices) {
		this();
		this.outputChat = outputChat;
		this.question = question;
		this.choices = choices;
		this.votes = new int[choices.length];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat");

		try {
			outputChat.send(question + "\r\n");
			for (int i = 0; i < choices.length; i++) {
				outputChat.send(i + 1 + ") " + choices[i] + "\r\n");
			}
			outputChat.send("Use !vote <number> to vote for your answer.");
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}

	/**
	 * Adds the vote for "number" choice.
	 *
	 * @param number
	 *            the number of choice
	 * @param voter
	 *            the voter
	 */
	public void addVote(String number, User voter){
		String error = null;

		if (voted.contains(voter))
			return;

		try{
			int vote = Integer.parseInt(number) - 1;
			votes[vote]++;
		} catch (NumberFormatException e) {
			error = "Invalid number";
		} catch (ArrayIndexOutOfBoundsException e) {
			error = "Invalid choice";
		} finally {
			try {
				if (error != null) {
					outputChat.send(error);
				} else {
					outputChat.send("Added " + voter.getFullName() + "'s vote.");
					voted.add(voter);
				}
			} catch (SkypeException e) {
				new WarningPopup(e.getMessage());
			}
		}
	}

	/**
	 * Prints the results.
	 */
	public void printResults() {
		try {
			outputChat.send("Results: " + "\r\n");
			for (int i = 0; i < choices.length; i++) {
				outputChat.send(choices[i] + ": " + votes[i] + "\r\n");
			}
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

}
