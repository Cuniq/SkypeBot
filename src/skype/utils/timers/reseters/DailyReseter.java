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

package skype.utils.timers.reseters;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.lang.mutable.MutableLong;

/**
 * The Class DailyReseter. This class is receives a reference to an item and resets
 * it to zero every 24 hours. The item can be either integer or long.
 * <p>
 * Please note that this class will <b>NOT</b> throw exceptions or it will <b>NOT</b>
 * make any content correctness checking. Programmer is responsible for passing the
 * right value.
 * 
 * @see java.util.Timer
 * @see java.util.TimerTask
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class DailyReseter implements Reseter {

	private final static long ONE_DAY = 24 * 60 * 60 * 1000;
	
	/** The Long item that would be set 0 every 24 hours. */
	private MutableLong lItem = null;

	/** The Integer item that would be set 0 every 24 hours. */
	private MutableInt iItem = null;

	/** The starting date; if given. */
	private Date startDate = null;

	/** The today messages timer. */
	private final Timer dailyTimer = new Timer();

	/**
	 * Instantiates a new daily reseter. The 24 hours countdown will be started
	 * Immediately.
	 *
	 * @param item
	 *            the item
	 */
	public DailyReseter(MutableInt item) {
		this.iItem = item;
		item = iItem;
	}

	/**
	 * Instantiates a new daily reseter. The 24 hours countdown will be started
	 * Immediately.
	 *
	 * @param item
	 *            the item
	 */
	public DailyReseter(MutableLong item) {
		this.lItem = item;
		item = lItem;
	}

	/**
	 * Instantiates a new daily reseter. The 24 hours countdown will be started at
	 * the specified Date.
	 *
	 * @param item
	 *            the item
	 * @param start
	 *            the start date
	 */
	public DailyReseter(MutableInt item, Date start) {
		this.iItem = item;
		startDate = start;
		item = iItem;
	}

	/**
	 * Instantiates a new daily reseter. The 24 hours countdown will be started at
	 * the specified Date.
	 *
	 * @param item
	 *            the item
	 * @param start
	 *            the start date
	 */
	public DailyReseter(MutableLong item, Date start) {
		this.lItem = item;
		startDate = start;
		item = lItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.reseters.Reseter#startReseter()
	 */
	@Override
	public void startReseter() {
		if (lItem == null) {

			if (startDate == null) {

				dailyTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						iItem.setValue(0);
					}
				}, 0, ONE_DAY);

			} else {

				dailyTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						iItem.setValue(0);
					}
				}, startDate, ONE_DAY);

			}

		} else { //iItem

			if (startDate == null) {

				dailyTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						lItem.setValue(0);
					}
				}, 0, ONE_DAY);

			} else {

				dailyTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						lItem.setValue(0);
					}
				}, startDate, ONE_DAY);

			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.reseters.Reseter#stopReseter()
	 */
	@Override
	public void stopReseter() {
		dailyTimer.cancel();
	}

}
