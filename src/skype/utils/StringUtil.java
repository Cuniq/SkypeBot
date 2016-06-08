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
package skype.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Class StringUtil. This class provides useful utilities for managing String.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class StringUtil {

	/**
	 * No need of instances.
	 */
	private StringUtil() {

	}

	/**
	 * Split method will split the given string to substrings every time it finds the
	 * space character (' '). If space found inside quotes it will be ignored and
	 * therefore not split will happen on that space.
	 * <p>
	 * This class will <b>trim</b> the sting.
	 * <p>
	 * Example: <blockquote>
	 * 
	 * <pre>
	 * "  This is       a   \"Example    String\"   !   \"Test1 \"  "
	 * 
	 * Will yield: [This] [is] [a] [Example    String] [!] [Test1 ]
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param str
	 *            the string to be split
	 * 
	 * @return a newly allocated String[] containing each word inside the string.
	 */
	public static String[] splitIgoringQuotes(String str) {
		ArrayList<String> strings = new ArrayList<String>(5);
		StringBuilder sb = new StringBuilder();
		str = str.trim();

		try {
			for (int i = 0; i < str.length(); i++) {

				while (str.charAt(i) == ' ') //Skip spaces that may exist between.
					i++;

				if (str.charAt(i) == '"') {
					i++;
					while (str.charAt(i) != '"')
						sb.append(str.charAt(i++));
				} else {
					while (str.charAt(i) != ' ')
						sb.append(str.charAt(i++));
				}

				strings.add(sb.toString());
				sb.setLength(0); //Clear string builder
			}
		} catch (IndexOutOfBoundsException e) { //Easy way to detect if string end
			strings.add(sb.toString());
		} catch (NullPointerException nullString) {
			throw nullString;
		}
		return strings.toArray(new String[0]); //Passing 0-size string for performance reasons.
	}

	/**
	 * Takes a string array and returns a new copy from it. The copy of array starts
	 * from startingPos(inclusive) to end of the string. If any exception happen it
	 * returns an empty array.
	 * 
	 * @param str
	 *            The string array to be copied
	 * @param startingPos
	 *            The starting of the array (inclusive)
	 * @return the new copied array or a new allocated empty array.
	 */
	public static String[] copyStringArray(String[] str,int startingPos) {
		try{
			return Arrays.copyOfRange(str, startingPos, str.length);
		}catch (NullPointerException e){
			return new String[0];
		}catch (IllegalArgumentException e){
			return new String[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return new String[0];
		}
	}

	/**
	 * Receives a String array with words and concatenates them using between them
	 * the 'concatenator' char.
	 * 
	 * @return A Sting with all words and concatenator between them.
	 */
	public static String wordConcatenation(String[] words, char concatenator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.length - 1; i++) {
			sb.append(words[i]).append(concatenator);
		}
		return sb.append(words[words.length - 1]).toString();
	}

}
