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
package skype.utils.timers.reseters;

/**
 * The Interface Reseter. Implements the two very basic commands of a reseter.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public interface Reseter {

	/**
	 * Stops reseter. Basically will cancel Timer and {@link #startReseter()} will
	 * create a new instance of timer.
	 */
	public void stopReseter();

	/**
	 * Starts reseter.
	 */
	public void startReseter();
}
