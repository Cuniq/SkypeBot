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
package skype.utils.timers;

import java.util.TimerTask;

import org.apache.commons.lang.mutable.MutableLong;

import skype.utils.Config;

/**
 * The Class WarningsReseters. This class on every check will test if the value of
 * the warnings has changed.
 * <p>
 * If the last ten minutes has changed then:
 * <ul>
 * <li>Keep the same value</li> else
 * <li>Reset it to zero</li>
 * </ul>
 * 
 * @see RepetitiveTask
 * @see java.util.TimerTask
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class WarningsReseters extends RepetitiveTask {

	/**
	 * Warning clean up time is given from user from config file. The user gives the
	 * time in minutes.
	 */
	private final long resetingTime = Config.WarningResetTime * MINUTE_TO_MILLS;

	/** The Warnings that may reset. */
	private MutableLong warnings = null;

	/** Keeps the previous value of warnings. */
	private MutableLong lastValueOfWarning = null;

	/**
	 * Instantiates a new ten minutes reseter. The 24 hours countdown will be started
	 * Immediately.
	 *
	 * @param item
	 *            the item to be reseted
	 */
	public WarningsReseters(MutableLong warningsRef) {
		this.warnings = warningsRef;
		lastValueOfWarning = (MutableLong) warnings.getValue();
	}

	/**
	 * Starts the timer instantly and cleans the warnings every resetingTime minutes.
	 * 
	 * @see skype.utils.timers.RepetitiveTask#startTimer()
	 */
	@Override
	public void startTimer() {
		getTimer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (!checkIfChanged())
					warnings.setValue(0);

				lastValueOfWarning = (MutableLong) warnings.getValue();
			}
		}, 0, resetingTime);

	}

	/**
	 * Stop the timer after that iteration.
	 * 
	 * @see skype.utils.timers.RepetitiveTask#stopTimer()
	 */
	@Override
	public void stopTimer() {
		getTimer().cancel();
	}

	/**
	 * Checks if the value of the warnings have changed.
	 *
	 * @return true, if they are different.
	 */
	private boolean checkIfChanged() {
		return !warnings.equals(lastValueOfWarning);
	}

}
