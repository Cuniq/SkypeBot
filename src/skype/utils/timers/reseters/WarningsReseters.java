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

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.lang.mutable.MutableLong;

import skype.utils.Config;

/**
 * The Class WarningsReseters. This class will check every ten minutes if the value
 * of the given item has changed.
 * <p>
 * If the last ten minutes has changed then:
 * <ul>
 * <li>Keep the same value</li>
 * </ul>
 * else
 * <p>
 * <ul>
 * <li>Reset it to zero</li>
 * </ul>
 * <p>
 * 
 *
 * Please note that this class will <b>NOT</b> throw exceptions or it will <b>NOT</b>
 * make any content correctness checking. Programmer is responsible for passing the
 * right value.
 * 
 * @see java.util.Timer
 * @see java.util.TimerTask
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class WarningsReseters implements Reseter {

	/** The Constant TEN_MINUTES. */
	private static final long MINUTES = Config.WarningResetTime * 60 * 1000;
	
	/** The Long item that may reset every ten minutes. */
	private MutableLong lItem = null;

	/** The Integer item that may reset every ten minutes. */
	private MutableInt iItem = null;

	/** The last value. Can be MutableInt or MutableLong */
	private Object lastValue = null;

	/** The today messages timer. */
	private final Timer tenMinuteTimer = new Timer();

	/**
	 * Instantiates a new ten minutes reseter. The 24 hours countdown will be started
	 * Immediately.
	 *
	 * @param item
	 *            the item
	 */
	public WarningsReseters(MutableInt item) {
		this.iItem = item;
		item = iItem;
		lastValue = iItem.getValue();
	}

	/**
	 * Instantiates a new ten minutes reseter. The 24 hours countdown will be started
	 * Immediately.
	 *
	 * @param item
	 *            the item
	 */
	public WarningsReseters(MutableLong item) {
		this.lItem = item;
		item = lItem;
		lastValue = lItem.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.reseters.Reseter#startReseter()
	 */
	@Override
	public void startReseter() {
		if (lItem == null) {

			tenMinuteTimer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if (!checkIfChanged())
						iItem.setValue(0);
					lastValue = iItem.getValue();
				}
			}, 0, MINUTES);

		} else {

			tenMinuteTimer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if (!checkIfChanged())
						lItem.setValue(0);
					lastValue = lItem.getValue();
				}
			}, 0, MINUTES);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.reseters.Reseter#stopReseter()
	 */
	@Override
	public void stopReseter() {
		tenMinuteTimer.cancel();
	}

	/**
	 * Checks if the value of the given item has changed.
	 *
	 * @return true, if they are different.
	 */
	private boolean checkIfChanged() {
		if (lastValue instanceof MutableInt) {
			return !iItem.equals(lastValue);
		} else {
			return !lItem.equals(lastValue);
		}

	}

}
