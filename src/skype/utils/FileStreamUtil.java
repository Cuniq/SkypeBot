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
package skype.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import skype.gui.popups.ErrorPopup;
import skype.gui.popups.WarningPopup;

/**
 * The Class FileStreamUtil. A simple utility for fast opening and closing input
 * streams.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class FileStreamUtil {

	/** The input stream. */
	private static FileInputStream inputStream = null;

	/**
	 * No Instantiates. Only static usage.
	 */
	private FileStreamUtil() {


	}

	/**
	 * Creates a <code>FileInputStream</code> from given path.
	 *
	 * @param path
	 *            the path
	 * @return the input stream. Null if an error occurred.
	 */
	public static InputStream fileAsInputStream(String path) {

		try{
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			new ErrorPopup(e.getMessage());
			return null;
		} catch (SecurityException e) {
			new ErrorPopup(e.getMessage());
			return null;
		}
		return inputStream;

	}

	/**
	 * Creates a <code>FileInputStream</code> from given file.
	 *
	 * @param file
	 *            the file
	 * @return the input stream for file. Null if an error occurred.
	 */
	public static InputStream fileAsInputStream(File file) {

		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			new ErrorPopup(e.getMessage());
			return null;
		} catch (SecurityException e) {
			new ErrorPopup(e.getMessage());
			return null;
		}
		return inputStream;

	}

	/**
	 * Close input stream.
	 *
	 * @param stream
	 *            the stream
	 */
	public static void closeInputStream(FileInputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			new WarningPopup(e.getMessage());
		}
	}


	/**
	 * Closes the last opened stream.
	 */
	public static void closeLastStream() {
		try {
			inputStream.close();
		} catch (IOException e) {
			new WarningPopup(e.getMessage());
		}
	}

}
