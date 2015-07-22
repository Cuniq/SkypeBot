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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Class StringUtil. This class will split the given string at the
 * specified token only if that token is not inside quotes (" ")
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class StringUtil {

	/**
	 * Instantiates a new splitter.
	 */
	public StringUtil() {

	}

	/**
	 * Split will split given string to substring every time it finds the specific
	 * token unless if it inside quotes.
	 *
	 * @param str
	 *            the string
	 * @param token
	 *            the split token
	 * @return the string[]
	 */
	public static String[] split(String str, char token) {
		ArrayList<String> strings = new ArrayList<String>(5);
		StringBuilder sb = new StringBuilder();
		str = str.trim();

		try {
			for (int i = 0; i < str.length(); i++) {

				while (str.charAt(i) == ' ') {
					i++;
				}

				if (str.charAt(i) == '"') {
					i++;
					while (str.charAt(i) != '"')
						sb.append(str.charAt(i++));
				} else {
					while (str.charAt(i) != ' ')
						sb.append(str.charAt(i++));
				}

				strings.add(sb.toString());
				sb.setLength(0);
			}
		} catch (IndexOutOfBoundsException e) { //Easy way to detect if string end
			strings.add(sb.toString());
		}
		return arrayListToString(strings);
	}

	/**
	 * Array list to string.
	 *
	 * @param array
	 *            the ArrayList
	 * @return the string[]
	 */
	private static String[] arrayListToString(ArrayList<String> array) {
		String[] strings = new String[array.size()];
		Iterator<String> it = array.iterator();
		int i = 0;
		while(it.hasNext()){
			strings[i++] = it.next();
		}
		return strings;
	}

}
