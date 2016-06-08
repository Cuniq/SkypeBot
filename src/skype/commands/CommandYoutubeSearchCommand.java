package skype.commands;

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.InvalidYoutubeKeywordsException;
import skype.exceptions.NoSuchVideoException;
import skype.gui.popups.WarningPopup;
import skype.utils.youtube.YoutubeVideoSearcher;

public class CommandYoutubeSearchCommand extends Command {

	private Chat outputChat = null;

	private String[] keywords = null;

	private YoutubeVideoSearcher youtubeSearcher = new YoutubeVideoSearcher();

	public CommandYoutubeSearchCommand() {
		super(
			"youtubesearch",
			"This command will return the first video found in youtube with your given keywords",
			"Syntax: !youtubesearch [<keyword1>,<keyword2>,...]");
	}

	@Override
	public void execute() throws CommandException {
		if (!canExecuteCommand())
			return;

		try {
			printToChat(outputChat, youtubeSearcher.getFirstVideoURL());
		} catch (NoSuchVideoException e) {
			printToChat(outputChat, e.getMessage());
		}
	}

	@Override
	public void setData(CommandData data) {
		outputChat = data.getOutputChat();
		keywords = data.getCommandOptions();

		try {
			youtubeSearcher.setKeywords(keywords);
		} catch (InvalidYoutubeKeywordsException e) {
			printToChat(outputChat, "Invalid keywords");
		}
	}

	private boolean canExecuteCommand() {
		if (outputChat == null)
			return false;

		if (keywords == null || keywords.length == 0)
			return false;

		return true;
	}

	private void printToChat(Chat chat, String message) {
		try {
			chat.send(message);
		} catch (SkypeException e) {
			new WarningPopup(e.getLocalizedMessage());
		}
	}

}
