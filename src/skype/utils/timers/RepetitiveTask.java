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

import java.util.Timer;


/**
 * The Class RepetitiveTask. This is the upper class for classes which implements a
 * repetitive task ( classes like reseters ).
 *
 * @author Thanasis Argyroudis
 * @see Timer
 * @since 1.0
 */
public abstract class RepetitiveTask {

	/** The Constant MINUTE. Represent one minute in milliseconds */
	protected static final long MINUTE_TO_MILLS = 60 * 1000;

	/** The Constant ONE_HOUR. Represent one hour in milliseconds */
	protected static final long HOUR_TO_MILLS = 60 * MINUTE_TO_MILLS;

	/** The Constant ONE_DAY. Represent one day in milliseconds */
	protected final static long DAY_TO_MILLS = 24 * HOUR_TO_MILLS;

	/** The timer who is responsible to repeat the task. */
	private final Timer timer;

	/**
	 * Instantiates a new repetitive task.
	 */
	public RepetitiveTask() {
		this("");
	}

	/**
	 * Instantiates a new repetitive task with name.
	 *
	 * @param timerName
	 *            timer's name
	 */
	public RepetitiveTask(String timerName) {
		timer = new Timer(timerName);
	}

	/**
	 * Starts the timer to start repeating the task. This method is abstract because
	 * every and task and time between executions is unique to every task.
	 */
	abstract public void startTimer();

	/**
	 * Stops the timer. This method is abstract because you may need the task to
	 * instantly stop or stop after the execution of this iteration.
	 */
	abstract public void stopTimer();

	/**
	 * Gets the timer. We need only subclass to be able to change the timer.
	 *
	 * @return the timer
	 */
	protected final Timer getTimer() {
		return timer;
	}
}
