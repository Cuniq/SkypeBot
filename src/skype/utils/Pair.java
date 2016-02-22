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

/**
 * The Class Pair. Very simple class to store objects like Pairs.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class Pair<F, S> {

	/** The first. */
	private final F first;

	/** The second. */
	private final S second;

	/**
	 * Instantiates a new pair.
	 *
	 * @param first
	 *            the first
	 * @param second
	 *            the second
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public F getFirst() {
		return first;
	}

	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public S getSecond() {
		return second;
	}
}