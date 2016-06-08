package skype.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import skype.gui.popups.WarningPopup;

public class YoutubeHttpConnection {

	private HttpURLConnection httpConnection;

	public YoutubeHttpConnection(String stringURL) {
		setYoutubeURL(stringURL);
	}

	public YoutubeHttpConnection() {

	}

	public void setYoutubeURL(String stringURL) {
		try {
			httpConnection = (HttpURLConnection) new URL(stringURL).openConnection();
		} catch (MalformedURLException e) {
			new WarningPopup(e.getLocalizedMessage());
		} catch (IOException e) {
			new WarningPopup(e.getLocalizedMessage());
		}
	}


	public HttpURLConnection getHttpConnection() {
		return httpConnection;
	}

}
