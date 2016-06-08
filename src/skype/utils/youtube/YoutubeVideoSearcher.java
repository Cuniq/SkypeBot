package skype.utils.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import skype.exceptions.InvalidYoutubeKeywordsException;
import skype.exceptions.NoSuchVideoException;
import skype.net.YoutubeHttpConnection;
import skype.utils.StringUtil;

/**
 * This class creates a http connection with youtube's search url and reads the
 * source code to find the first id of the videos which found.
 * 
 * Youtube's source format: All the videos are inside a list. The list id is:
 * 
 * <pre>
 *  "< ol id="item-section-XXXXXX" class="item-section" >"
 * </pre>
 * 
 * Now inside that list we are searching for
 * 
 * <pre>
 * "data-context-item-id"
 * </pre>
 * 
 * div tag. If we can find that tag then our list contains videos. The first
 * occurrence of this tag is the first video, the second occurrence is the second
 * video and so on. If this list does not contain that tag then youtube was not able
 * to find any videos with the given keywords.
 * 
 * @author UniQ
 * @since 1.0
 */
public class YoutubeVideoSearcher {

	private static final String YOUTUBE_SEARCH_URL = "https://www.youtube.com/results?search_query=";

	private static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";

	/**
	 * This magic number is the length of "<li><div class="yt-lockup yt-lockup-tile
	 * yt-lockup-video vve-check clearfix" data-context-item-id=" inside the source
	 * code.
	 */
	private static final int VIDEO_ID_STARTING_POS = 99;

	/**
	 * The size of video ID is always 11 so we 99+11=110
	 */
	private static final int VIDEO_ID_ENDING_POS = 110;

	private final YoutubeHttpConnection youtubeConnection;

	public YoutubeVideoSearcher() {
		this.youtubeConnection = new YoutubeHttpConnection();
	}

	public String getFirstVideoURL() throws NoSuchVideoException {
		try {
			BufferedReader in =
				new BufferedReader(new InputStreamReader(youtubeConnection.getHttpConnection().getInputStream()));
			String inputLine;

			//Find the item section
			while (!in.readLine().contains("<ol id=\"item-section"));
			//Search if it is contains "data-context-item-id" tag
			while ((inputLine = in.readLine()) != null){
				if(inputLine.contains("data-context-item-id"))
					return YOUTUBE_VIDEO_URL + inputLine.substring(VIDEO_ID_STARTING_POS, VIDEO_ID_ENDING_POS);
			}
		} catch (IOException e) {
			//Some http error ignore
		}

		throw new NoSuchVideoException("Can not find a find a video with the given keywords");
	}

	public void setKeywords(String[] words) throws InvalidYoutubeKeywordsException {
		final String searchKeywords = StringUtil.wordConcatenation(words, '+').replace("\\", "\\\\");
		if (searchKeywords.length() == 0)
			throw new InvalidYoutubeKeywordsException("You must send at least one keyword");

		String searchQuery = YOUTUBE_SEARCH_URL + searchKeywords;
		youtubeConnection.setYoutubeURL(searchQuery);
	}

}
