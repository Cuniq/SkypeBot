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

import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.lang.mutable.MutableLong;

/**
 * The Class DailyReseter. This class is receives a reference to an item and resets
 * it to zero every 24 hours. The item can be <b>only</b> MutableLong.
 * <p>
 * 
 * @see RepetitiveTask
 * @see java.util.TimerTask
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class DailyReseter extends RepetitiveTask {

	/** The Long item that would be set 0 every 24 hours. */
	private MutableLong item = null;

	/** The starting date (if given). */
	private Date startDate = null;

	/**
	 * Instantiates a new daily reseter. The 24 hours countdown will be started
	 * Immediately.
	 *
	 * @param item
	 *            the item to be reseted
	 */
	public DailyReseter(MutableLong item) {
		this(item, new Date());
	}

	/**
	 * Instantiates a new daily reseter. The 24 hours countdown will be started at
	 * the specified Date.
	 *
	 * @param item
	 *            the item to be reseted
	 * @param start
	 *            the starting date
	 */
	public DailyReseter(MutableLong item, Date start) {
		super("Daily Reseter");
		this.item = item;
		startDate = start;
	}


	/**
	 * Starts the timer and the given start date (if given any) and cleans the time
	 * on daily basis.
	 * 
	 * @see skype.utils.timers.RepetitiveTask#startTimer()
	 */
	@Override
	public void startTimer() {
		getTimer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				item.setValue(0);
			}

		}, startDate, DAY_TO_MILLS);
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

}
