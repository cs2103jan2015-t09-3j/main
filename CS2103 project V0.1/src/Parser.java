import java.util.List;

import com.joestelmach.natty.DateGroup;

public class Parser {
	DateGroup group = new DateGroup();
	static String command_type;

	// Parser part -- LIQI

	/**
	 * This method will process and call the other methods to identify the type
	 * of command and puts the corresponding components inside Task object
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored
	 * 
	 * @return It returns the type of command that is to be processed in Logic
	 */
	public String parse(String input, Task cmd) {
		input.toLowerCase();
		int index = input.indexOf(' ');
		if (index != -1) {
			identifyType(input, cmd, index);

		} else {
			commandOnly(input);
		}
		return command_type.toLowerCase();
	}

	/**
	 * This method will process user input which contains command only
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 */
	public void commandOnly(String input) {
		command_type = input;
	}

	/**
	 * This method will process user input which contains more than command only
	 * and identify the type of command
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param index
	 *            This is the point in a String whereby before this point, it is
	 *            the command type
	 * 
	 */
	public void identifyType(String input, Task cmd, int index) {
		command_type = input.substring(0, index);
		if (input.contains("-")) {
			timed(input, cmd, index);
		} else if (index != input.length() - 1) {
			floating(input, cmd, index);
		}
	}

	/**
	 * This method will process timed and deadline tasks and call other
	 * functions to process recurring tasks
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param index
	 *            This is the point in a String whereby before this point, it is
	 *            the command type
	 * 
	 */
	public void timed(String input, Task cmd, int index) {

		int index2 = input.indexOf("-");
		cmd.detail = input.substring(index + 1, index2);
		if (input.contains("every")) {
			recurring(input, cmd, index2);
		} else {

			nonRecurring(input, cmd, index2);
		}
	}

	/**
	 * This method will process non-recurring tasks
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored
	 * 
	 * @param index2
	 *            This is the point in a String whereby after this point, it is
	 *            time/ and date that the task is to be added to
	 * 
	 */
	private void nonRecurring(String input, Task cmd, int index2) {
		String date_input;
		date_input = input.substring(index2 + 2, input.length());

		group = getNattyDateGroup(date_input);
		if (group == null) {
			cmd.detail += input.substring(index2, input.length());;
		} else {
			String date = group.getDates().toString();

			int start = date.indexOf(",");
			if (start != -1) {
				cmd.startDate = date.substring(1, start);
				cmd.startDate = trimDate(cmd.startDate);
				cmd.endDate = date.substring(start + 2, date.length() - 1);
				cmd.endDate = trimDate(cmd.endDate);
			} else {
				cmd.startDate = "";
				cmd.endDate = date.substring(1, date.length() - 1);
				cmd.endDate = trimDate(cmd.endDate);
			}
		}
	}

	/**
	 * This method will process recurring tasks
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored
	 * 
	 * @param index2
	 *            This is the point in a String whereby after this point, it is
	 *            time/ and date that the task is to be added to
	 * 
	 */
	private void recurring(String input, Task cmd, int index2) {
		int index3 = input.indexOf("every", index2) + 6;
		int recurring_time = Integer.parseInt(input.substring(index3,
				input.indexOf(' ', index3)));
		String recurringperiod = input.substring(
				input.indexOf(' ', input.indexOf(' ', index3)), input.length());
		findRecurringPeriod(cmd, recurringperiod);

		int index4 = recurringperiod.indexOf("until");

		if (index4 != -1) {
			index4 = findEndDateRecur(cmd, recurringperiod, index4);
		}
		if (recurringperiod.contains("from") || recurringperiod.contains("on")) {
			int index5 = recurringperiod.indexOf("from");
			if (index5 != -1) {
				startingDateFrom(cmd, recurringperiod, index4, index5);
			} else {
				startingDateOn(cmd, recurringperiod, index4);
			}
		} else {
			noStartingDate(cmd);

		}

		cmd.recurring_interval = recurring_time;

	}

	/**
	 * This method will find the recurring period of the recurring task (ie.
	 * month, day, week)
	 * 
	 *
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param user_input
	 *            The user input which contains the recurring period
	 * 
	 */
	private void findRecurringPeriod(Task cmd, String user_input) {
		if (user_input.contains(" day") || user_input.contains(" days")) {
			cmd.recurring_period = "day";
		} else if (user_input.contains(" week")
				|| user_input.contains(" weeks")) {
			cmd.recurring_period = "week";
		} else if (user_input.contains(" month")
				|| user_input.contains(" months")) {
			cmd.recurring_period = "month";
		}
	}

	/**
	 * This method will find the end of recurring period of the recurring task
	 * 
	 *
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param user_input
	 *            The user input which contains the recurring period
	 * @param index4
	 *            this contains the index of the string "until" in the user
	 *            input
	 * 
	 */

	// when end of recurring task is given by until
	private int findEndDateRecur(Task cmd, String user_input, int index4) {
		index4 += 6;
		cmd.recurring_until = user_input.substring(index4, user_input.length());
		group = getNattyDateGroup(cmd.recurring_until);
		String date = group.getDates().toString();
		date = date.substring(1, date.length() - 1);
		cmd.recurring_until = trimDate(date);
		return index4;
	}

	/**
	 * This method will find the end of recurring period of the recurring task
	 * 
	 *
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param user_input
	 *            The user input which contains the recurring period
	 * @param index4
	 *            this contains the index of the string "until" in the user
	 *            input
	 * @param index5
	 *            This contains the index of the string "from"
	 * 
	 */
	// when "from" is typed by user as starting date of recurring task
	private void startingDateFrom(Task cmd, String user_input, int index4,
			int index5) {
		index5 += 5;
		String date;
		if (index4 == -1) {
			cmd.recurring_from = user_input.substring(index5,
					user_input.length());
		} else {
			cmd.recurring_from = user_input.substring(index5,
					user_input.indexOf("until") - 1);
		}
		group = getNattyDateGroup(cmd.recurring_from);
		date = group.getDates().toString();
		date = date.substring(1, date.length() - 1);
		cmd.recurring_from = trimDate(date);
	}

	/**
	 * This method will find the end of recurring period of the recurring task
	 * 
	 *
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param user_input
	 *            The user input which contains the recurring period
	 * @param index4
	 *            this contains the index of the string "until" in the user
	 *            input
	 * @param index5
	 *            This contains the index of the string "on"
	 * 
	 */
	// when "on" is typed by user as starting date of recurring task
	private void startingDateOn(Task cmd, String user_input, int index4) {
		int index5;
		index5 = user_input.indexOf("on");
		index5 += 3;
		String date;
		if (index4 == -1) {
			cmd.recurring_from = user_input.substring(index5,
					user_input.length());
		} else {
			cmd.recurring_from = user_input.substring(index5,
					user_input.indexOf("until") - 1);
		}
		group = getNattyDateGroup(cmd.recurring_from);
		date = group.getDates().toString();
		date = date.substring(1, date.length() - 1);
		cmd.recurring_from = trimDate(date);
	}

	/**
	 * This method will find the end of recurring period of the recurring task
	 * 
	 *
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 *
	 * 
	 */
	// When there is no starting date given
	private void noStartingDate(Task cmd) {
		String date = "today";
		group = getNattyDateGroup(date);
		date = group.getDates().toString();
		date = date.substring(1, date.length() - 1);
		cmd.recurring_from = trimDate(date);
	}

	/**
	 * This method will process floating tasks
	 * 
	 * @param input
	 *            This is the command line entered by the user
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param index
	 *            This is the point in a String whereby before this point, it is
	 *            the command type
	 * 
	 */
	public void floating(String input, Task cmd, int index) {

		cmd.detail = input.substring(index + 1, input.length());

	}

	/**
	 * This method will process the date of the tasks
	 * 
	 * @param date_input
	 *            This is the date extracted from the command line entered by
	 *            the user
	 * 
	 * @return It returns the DateGroup object if there is date inside the list
	 *         of DateGroup else it will return null
	 * 
	 */
	public DateGroup getNattyDateGroup(String date_input) {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
		List<DateGroup> groups = parser.parse(date_input);
		if (!groups.isEmpty()) {
			DateGroup group = groups.get(0);
			return group;
		} else {
			return null;
		}
	}

	/**
	 * This method will trim the date so that the unnecessary details for our
	 * target users (i.e. SGT, seconds, milliseconds) will be removed
	 * 
	 * @param temp
	 *            This is the date and time before removing the unnecessary
	 *            details
	 * 
	 * @return It returns the trimmed date
	 */
	public String trimDate(String temp) {
		assert temp != null;
		int first = temp.indexOf("SGT");
		temp = temp.substring(0, first - 4);
		return temp;
	}

	/**
	 * This method will take out the first integer to be deleted
	 * 
	 * @param detail
	 *            This is the list of task number to be deleted
	 * 
	 * @return It returns the first integer to be deleted in the Logic
	 */
	public int takeoutFirstInt(String detail) {
		int index = detail.indexOf(' ');
		if (index != -1) {
			return Integer.parseInt(detail.substring(0, detail.indexOf(' ')));
		} else {
			return -1;

		}
	}

}
