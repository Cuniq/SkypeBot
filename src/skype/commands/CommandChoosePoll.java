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

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;
import skype.handlers.CommandHandler;

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

	private static final int QUESTION_POSITION = 0;

	private static final long POLL_DURATION_IN_MINUTES = 1;

	private Chat outputChat = null;

	private String question = null;

	private String[] choices = null;

	private boolean pollRunning = false;

	/** The votes each choice has taken so far. */
	private int[] votesOfChoices = null;

	private HashSet<User> usersAlreadyVoted = new HashSet<User>(10);

	/**
	 * The timer who is responsible to keep the poll running only for ten minutes and
	 * and the end must print the results.
	 */
	private final Timer pollTimer = new Timer("Poll timer");

	public CommandChoosePoll() {
		super(
				"choosepoll",
				"Creates a poll with given question and choices of which you choose one.",
				"Syntax: !choosepoll <question> [<choice>,<choice>,...]");
	}
	
	public CommandChoosePoll(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws CommandException {
		if (!canExecuteCommand())
			return;

		printToChat((question + "\r\n"));
		for (int i = 0; i < choices.length; i++) {
			printToChat(i + 1 + ") " + choices[i] + "\r\n");
		}
		printToChat("Use !vote <number> to vote for your answer.");
		startTimer();
	}

	/**
	 * Adds the "vote" number for its choice.
	 *
	 * @param vote
	 *            the number of choice that user voted
	 * @param voter
	 *            the user who voted
	 */
	public void addVote(int vote, User voter) {
		if (usersAlreadyVoted.contains(voter)) {
			printToChat("You can vote only once!");
			return;
		}

		try{
			votesOfChoices[vote]++;
			printToChat("Added " + voter.getFullName() + "'s vote.");
			usersAlreadyVoted.add(voter);
		} catch (ArrayIndexOutOfBoundsException e) {
			printToChat("Invalid choice");
		}catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	public boolean isRunning() {
		return pollRunning;
	}

	private boolean canExecuteCommand() throws NullOutputChatException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat");

		if (choices.length <= 0)
			return false;

		if (pollRunning)
			return false;

		return true;
	}

	private void initializeCommand(CommandData data) {
		if (pollRunning)
			return; //another command is running.

		this.outputChat = data.getOutputChat();
		String options[] = data.getCommandOptions();

		//At least one question and one possible answer.
		if (options.length >= 2) {
			this.question = options[QUESTION_POSITION];
			this.choices = new String[options.length - 1];//-1 without the question
			this.votesOfChoices = new int[options.length - 1];
			for (int i = 1; i < options.length; i++) {
				this.choices[i - 1] = options[i];
			}
		} else {
			this.question = "";
			this.choices = new String[0];
		}
	}

	private void startTimer() {
		pollRunning = true;
		pollTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				pollRunning = false;
				usersAlreadyVoted.clear();
				printResults();
			}
		}, POLL_DURATION_IN_MINUTES * 60 * 1000);
	}

	private void printResults() {
		printToChat("Results: " + "\r\n");
		for (int i = 0; i < choices.length; i++) {
			printToChat(choices[i] + ": " + votesOfChoices[i] + "\r\n");
		}
	}

	private void printToChat(String message) {
		try {
			outputChat.send(message);
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

}
