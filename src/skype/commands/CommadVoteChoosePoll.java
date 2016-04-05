package skype.commands;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

import skype.exceptions.CommandException;
import skype.gui.popups.ErrorPopup;
import skype.gui.popups.WarningPopup;

public class CommadVoteChoosePoll extends Command {

	private static final int USER_VOTE_POSITION = 0;

	private Chat outputChat = null;

	private String choice = null;

	private User voter = null;

	private CommandChoosePoll pollCommand = null;

	public CommadVoteChoosePoll() {
		super(
			"vote",
			"Whenever a choose poll running you can use that command to register your vote.",
			"!vote <number>");
	}

	@Override
	public void execute() throws CommandException {
		addVote();
	}

	@Override
	public void setData(CommandData data) {
		initializeCommand(data);
	}

	/**
	 * Receives a string with the user's vote. If the text contains a valid number it
	 * subtracts 1 from it because the choices are starting from 1.
	 *
	 * @param choice
	 *            the number of choice that user voted
	 * @param voter
	 *            the user who voted
	 */
	private void addVote() {
		if (!canAddVote())
			return;

		int vote = -1;
		try {
			vote = Integer.parseInt(choice) - 1;
		} catch (NumberFormatException e) {
			printToChat("Invalid number");
			return;
		}

		pollCommand.addVote(vote, voter);

	}

	private boolean canAddVote() {
		if (!pollCommand.isRunning()) {
			printToChat("No polls are running at the moment");
			return false;
		}
		return true;
	}

	private void initializeCommand(CommandData data) {
		final String options[] = data.getCommandOptions();
		outputChat = data.getOutputChat();
		voter = data.getSender();

		try {
			pollCommand = (CommandChoosePoll) data.getCommand();
		} catch (ClassCastException e) {
			new ErrorPopup(
				"Command handler passed wrong command to vote. CALL PROGRAMMER!");
		}

		if (options.length >= 1) {
			choice = options[USER_VOTE_POSITION];
		}else{
			choice = "";
		}

	}

	private void printToChat(String text) {
		try {
			outputChat.send(text);
		} catch (SkypeException e) {
			new WarningPopup(e.getLocalizedMessage());
		}
	}

}
