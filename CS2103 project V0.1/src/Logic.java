import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.joestelmach.natty.DateGroup;

public class Logic {
	private static final String DEFAULT_STRING = "none";
	private static final String FAILURE = "OPERATION FAILED!!";
	Parser p = new Parser();
	Storage s = new Storage();
	Task cmd = new Task();

	ArrayList<Task> list = new ArrayList<Task>();
	ArrayList<Task> tempList = new ArrayList<Task>();
	ArrayList<Task> currList = new ArrayList<Task>();
	static String command_type = DEFAULT_STRING;

	int edit = 0;

	/**
	 * This method will identify the command type and details from the user
	 * input and send it to implement command method
	 * 
	 * @param cmd
	 *            This initially contains user input and will be containing
	 *            command type and details after parser
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * @return This method will return the feedback to the UI to be processed.
	 */
	public String executeCommand(String input, ArrayList<Task> list) {
		String feedback = DEFAULT_STRING;
		assert list != null;
		cmd = new Task();
		command_type = p.parse(input, cmd);
		feedback = implementCommand(command_type, cmd, list, feedback);

		sortList(list);
		return feedback;
	}

	/**
	 * This method will process the identified command type and call an
	 * appropriate method for implementation.
	 * 
	 * @param command_type
	 *            This is the command type that is entered by the user (eg. add,
	 *            delete)
	 * 
	 * @param cmd
	 *            This is the Task object where the current input details are
	 *            stored.
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 * @return It returns the feedback that is to be processed in UI.
	 */
	public String implementCommand(String command_type, Task cmd,
			ArrayList<Task> list, String feedback) {
		switch (command_type) {
		case "import": {
			return importCommand(list, cmd.detail);
		}
		case "export": {
			return exportCommand(list, cmd.detail);
		}
		case "add": {
			return addCommand(cmd, list);
		}
		case "delete": {

			return deleteCommand(cmd.detail, list);
		}
		case "edit": {
			return editCommand(cmd, list);
		}
		case "clear": {
			return clearCommand(list, cmd.detail);
		}
		case "save": {
			return saveCommand(list);
		}
		case "done": {
			return doneCommand(cmd, list);
		}
		case "undo": {
			return undoCommand(list);
		}
		case "redo": {
			return redoCommand(list);
		}
		case "search": {
			return searchCommand(list, cmd.detail);
		}
		case "display": {
			return "display " + cmd.detail;
		}
		case "help": {
			return "help";
		}
		case "recur": {
			return recurCommand(list, cmd);
		}
		case "collapse": {
			return collapseCommand(list);
		}
		case "expand": {
			return expandCommand(list);
		}
		case "background": {
			return backgroundChange(cmd.detail);
		}
		default: {
			return "Invalid Command";
		}
		}
	}

	/**
	 * This method export the current list of task into text file
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param directory
	 *            The location of text file to be stored.
	 * 
	 * 
	 * @return This method will return the feedback to be processed in UI
	 *
	 */
	private String exportCommand(ArrayList<Task> list, String directory) {
		s.changeDirectory(directory);
		s.writeToFile(list);
		return "The output textfile has been saved at: " + directory;
	}

	/**
	 * This method import a list of tasks from text file in a given location to
	 * the list.
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param directory
	 *            The location of text file to be read from.
	 * 
	 * 
	 * @return This method will return the feedback to be processed in UI
	 *
	 */
	private String importCommand(ArrayList<Task> list, String directory) {
		s.changeDirectory(directory);
		list.clear();
		list = s.readFromFile();
		return "Imported the existing Cone.txt file from: " + directory;
	}

	/**
	 * This method will change the background picture to the specified image
	 * file
	 * 
	 * 
	 * @param directory
	 *            This is the directory of image file to be read
	 * 
	 * @return This method will return the feedback to be processed in UI
	 *
	 */
	private String backgroundChange(String directory) {

		return "background__ " + directory;
	}

	/**
	 * This method expands the recurring tasks that are collapsed by "collapse"
	 * command
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * 
	 * @return This method will return the feedback to be processed in UI
	 *
	 */
	private String expandCommand(ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isPrimaryRecurringTask == true) {
				int index = list.get(i).detail.indexOf("- every");
				if (index != -1) {
					list.get(i).detail = list.get(i).detail.substring(0, index);
					Task cmd = new Task();
					cmd = list.get(i);
					list.remove(i);
					addRecurring(cmd, list);
					i--;
				}
			}
		}
		return "All recurring tasks are expanded";
	}

	/**
	 * This method will compress each recurring tasks in the list into one task
	 * for editing and display.
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * 
	 * 
	 * @return This method will return the feedback to be processed in UI
	 *
	 */
	private String collapseCommand(ArrayList<Task> list) {
		tempList = cloneToTempList(list);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isPrimaryRecurringTask == true) {
				list.get(i).detail = list.get(i).detail + " - every "
						+ list.get(i).recurring_interval + " "
						+ list.get(i).recurring_period + " from "
						+ list.get(i).recurring_from;
				list.get(i).endDate = list.get(i).recurring_until;
			}
			if (list.get(i).recurring_interval != 0) {
				if (list.get(i).isPrimaryRecurringTask == false) {
					list.remove(i);
					i--;
				}
			}
		}
		return "All recurring tasks are collapsed!";
	}

	/**
	 * This method allows user to change any task into recurring task
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param cmd
	 *            This Task variable contains the user input
	 * 
	 * 
	 * @return This method will return the feedback to be processed in UI
	 *
	 */
	private String recurCommand(ArrayList<Task> list, Task cmd) {
		tempList = cloneToTempList(list);
		int index = Integer.parseInt(cmd.detail.trim());
		cmd.detail = list.get(index - 1).detail;
		addRecurring(cmd, list);
		list.remove(index - 1);
		return "The task no. " + index
				+ " has been changed to the recurring task!";
	}

	/**
	 * This method imports items in the text file currently saved in same
	 * directory and put them in the array list.
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @return It returns the feedback to be processed in UI
	 */
	public ArrayList<Task> import_From_File(ArrayList<Task> list) {
		list = s.readFromFile();
		return list;
	}

	/**
	 * This method will redo the last undo done by user
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @return It returns the feedback to be processed in UI
	 */
	private String redoCommand(ArrayList<Task> list) {
		list.clear();
		list.addAll(currList);
		return " Redo to latest 1 undo(s)!";
	}

	/**
	 * This method adds new task to the list, by calling appropriate method
	 * depending on the type of task
	 * 
	 * @param cmd
	 *            This variable contains the information of current task to be
	 *            added
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @return It returns the feedback to be processed in UI
	 */
	private String addCommand(Task cmd, ArrayList<Task> list) {
		tempList = cloneToTempList(list);

		if (cmd.recurring_interval != 0) {
			addRecurring(cmd, list);
			return "Recurring task " + cmd.detail
					+ " has been successfully added!";
		} else if (edit == 0) {
			list.add(cmd);
			return cmd.detail + " has been successfully added!";
		} else {

			replaceItem(list, cmd, edit);
			edit = 0;
			return list.get(edit).detail + " has been successfully edited!";
		}

	}

	/**
	 * This method will add the recurring task to the list
	 * 
	 * @param cmd
	 *            This variable contains the information of task such as
	 *            description and endDate, and recurring information
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 */
	private void addRecurring(Task cmd, ArrayList<Task> list) {

		Task temp = new Task();
		temp.detail = cmd.detail;
		temp.endDate = cmd.recurring_from;
		temp.recurring_interval = cmd.recurring_interval;
		temp.recurring_period = cmd.recurring_period;
		temp.recurring_until = cmd.recurring_until;
		temp.recurring_from = cmd.recurring_from;
		temp.isPrimaryRecurringTask = true;
		list.add(temp);
		if (cmd.recurring_until != DEFAULT_STRING) {
			addRecurringGivenUntil(cmd, list, temp);

		} else {

			addRecurringWithoutUntil(cmd, list, temp);
		}

	}

	/**
	 * This method will add the recurring without ending date into the list. It
	 * will add the task 10 times
	 * 
	 * @param cmd
	 *            This variable contains the information of task such as
	 *            description and endDate, and recurring information
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @param temp
	 *            this is the temporary task object to be used
	 * 
	 */
	private void addRecurringWithoutUntil(Task cmd, ArrayList<Task> list,
			Task temp) {
		for (int i = 1; i < 10; i++) {
			String date = getDate(
					cmd.recurring_interval + " " + cmd.recurring_period
							+ " after " + temp.endDate).toString();
			date = date.substring(0, date.length());
			temp = new Task();
			temp.detail = cmd.detail;
			temp.recurring_interval = cmd.recurring_interval;
			temp.endDate = p.trimDate(date);
			list.add(temp);

		}
		for(int i=0; i<list.size(); i++){
			if(list.get(i).isPrimaryRecurringTask==true && list.get(i).detail.equals( temp.detail)){
				list.get(i).recurring_until = temp.endDate;
			}
		}
		
	}

	/**
	 * This method will add the recurring with ending date into the list
	 * 
	 * @param cmd
	 *            This variable contains the information of task such as
	 *            description and endDate, and recurring information
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @param temp
	 *            this is the temporary task object to be used
	 * 
	 */
	private void addRecurringGivenUntil(Task cmd, ArrayList<Task> list,
			Task temp) {
		while (getDate(temp.endDate).before(
				getDate("one day after " + cmd.recurring_until))) {

			String date = getDate(
					cmd.recurring_interval + " " + cmd.recurring_period
							+ " after " + temp.endDate).toString();
			date = date.substring(0, date.length());
			temp = new Task();
			temp.detail = cmd.detail;
			temp.recurring_interval = cmd.recurring_interval;
			temp.endDate = p.trimDate(date);

			if (getDate(temp.endDate).before(
					getDate("23:59 " + cmd.recurring_until))) {

				list.add(temp);
			}

		}
		
	}

	/**
	 * This method will delete the item that is specified by user
	 * 
	 * @param indice
	 *            This String contains all the task numbers to be deleted,
	 *            separate by one space.
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @return This method will return the feedback to be processed by UI
	 */
	private String deleteCommand(String indice, ArrayList<Task> list) {

		tempList = cloneToTempList(list);
		ArrayList<Task> deleteList = new ArrayList<Task>(list);
		

		String temp = indice;
		String deleting_detail = DEFAULT_STRING;
		String deleting_date = DEFAULT_STRING;
		int index = 1;
		while (index != -1) {
			index = p.takeoutFirstInt(temp);

			if (index != -1) {
				deleting_detail = list.get(index - 1).detail;
				deleting_date = list.get(index-1).endDate;
				temp = temp.substring(temp.indexOf(" ") + 1, temp.length());
				removeFromList(deleteList, deleting_detail,deleting_date);
			} else {
				index = Integer.parseInt(temp);
				deleting_detail = list.get(index - 1).detail;
				deleting_date = list.get(index-1).endDate;
				removeFromList(deleteList, deleting_detail,deleting_date);
				index = -1;
			}

		}
		list.clear();
		list.addAll(deleteList);

		return "taks number " + indice + "have been deleted successfully!";
	}

	/**
	 * This method remove all tasks marked as deleting in deleteCommand()
	 * 
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @param deleting
	 *            this is the guide for which task to remove from list.
	 * @param deleting_date 
	 * 
	 */
	private void removeFromList(ArrayList<Task> list, String deleting, String deleting_date) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.equals(deleting) && list.get(i).endDate.equals(deleting_date)) {
				list.remove(i);
				i--;
			}
		}

	}

	/**
	 * This method will create a templist that is containing the information of
	 * the list, mainly for undoing purpose
	 * 
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 */
	private ArrayList<Task> cloneToTempList(ArrayList<Task> list) {
		tempList = new ArrayList<Task>();
		tempList.addAll(list);
		return tempList;
	}

	/**
	 * This method saves all changes to the list into the text file
	 * 
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 */
	private String saveCommand(ArrayList<Task> list) {

		s.writeToFile(list);
		return "All changes saved at !";
	}

	/**
	 * This method will clear all specified items in the list(recurring, all, completed)
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @param cmd
	 *            This contains information on which tpye of tasks to be cleared
	 *
	 * 
	 */
	private String clearCommand(ArrayList<Task> list, String cmd) {
		tempList = cloneToTempList(list);
		if (cmd == DEFAULT_STRING) {

			list.clear();
			return "All contents are cleared";
		} else {
			if (cmd.equals("completed")) {
				clearCompleted(list);
				return "All completed contents are cleared";

			} else if (cmd.equals("recurring")) {
				clearRecurring(list);
				return "All recurring contents are cleared";
			}
		}
		return FAILURE;
	}

	/**
	 * This method will clear All recurring tasks from the list
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * 
	 */
	private void clearRecurring(ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).recurring_interval != 0) {
				list.remove(i);
				i--;
			}
		}

	}

	/**
	 * This method will clear all tasks marked as completed by the user
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private void clearCompleted(ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.contains("(completed)")) {
				list.remove(i);
				i--;
			}
		}

	}

	/**
	 * This method will undo the last change made to the list
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 */
	private String undoCommand(ArrayList<Task> list) {

		currList.clear();
		currList.addAll(list);
		list.clear();
		list.addAll(tempList);
		return " Undo to latest 1 change(s)!";
	}

	/**
	 * This method will allow user to edit the existing item in the list
	 * 
	 *  @param cmd
	 *            This contains information of new task
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private String editCommand(Task cmd, ArrayList<Task> list) {

		tempList = cloneToTempList(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		
		edit = index;
		if(list.get(index-1).isPrimaryRecurringTask==true){
		return "edit recur " + index;
		}
		else{
			return "edit "+index;
		}

	}

	/**
	 * This method will allow user to mark a task as completed
	 * 
	 *  @param cmd
	 *            This contains user command input
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private String doneCommand(Task cmd, ArrayList<Task> list) {

		tempList = cloneToTempList(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		if (list.get(index - 1).detail.contains("(completed)")) {
			list.get(index - 1).detail = list.get(index - 1).detail
					.substring(list.get(index - 1).detail.indexOf(')') + 1);
			return "The task no." + index + " has been marked incompleted!";
		} else {
			list.get(index - 1).detail = "(completed)"
					+ list.get(index - 1).detail;
			return "The task no." + index + " has been marked completed!";
		}
	}

	/**
	 * This method will allow user to search the existing items in the list by keyword
	 * 
	 *  @param cmd
	 *            This contains information of new task
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private String searchCommand(ArrayList<Task> list, String detail) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.toLowerCase().contains(cmd.detail)
					|| list.get(i).startDate.contains(cmd.detail)
					|| list.get(i).endDate.contains(cmd.detail)) {
				return "search " + detail;

			}
		}
		return FAILURE;

	}

	/**
	 * This method replaces the old item in the list with the new item
	 * 
	 *  @param cmd
	 *            This contains information of new task
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private void replaceItem(ArrayList<Task> list, Task cmd, int index) {
		list.add(index - 1, cmd);

		list.remove(index);

	}

	/**
	 * This method will import the instruction file into the program
	 * 
	 *  @param cmd
	 *            This contains information of new task
	 *
	 * @param text
	 * 			this is arraylist of string which contains all the instructions line by line
	 *
	 * 
	 */
	public void import_instruction(ArrayList<String> text) {
		s.importInstruction(text);

	}

	/**
	 * This method will sort the list by its date and completion
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */

	private void sortList(ArrayList<Task> list) {
		sortByMark(list);
	}

	/**
	 * This method will return the date object of the given string
	 * 
	 *  @param date_input
	 *            A string containing the date information to be passed into natty parser
	 *
	 *@return
	 *			It returns the date object of the given date input
	 * 
	 */
	public Date getDate(String date_input) {
		DateGroup group = new DateGroup();

		group = p.getNattyDateGroup(date_input);
		Date date = new Date();
		date = group.getDates().get(0);

		return date;
	}

	
	/**
	 * This method will return arraylist consisting of floating tasks
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * @return
	 * 			It returns an arraylist consisting of floating tasks
	 */
	private ArrayList<Task> getFloatTask(ArrayList<Task> list) {
		ArrayList<Task> floating = new ArrayList<Task>();

		for (int k = 0; k < list.size(); k++) {
			if (list.get(k).endDate.equals(DEFAULT_STRING))
				floating.add(list.get(k));
		}
		return floating;
	}

	/**
	 * This method will return arraylist consisting of timed tasks
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * @return
	 * 			Returns arraylist of tasks consisting only of timed tasks
	 */
	private ArrayList<Task> getTimedTask(ArrayList<Task> list) {
		ArrayList<Task> timed = new ArrayList<Task>();

		for (int k = 0; k < list.size(); k++) {
			if (!list.get(k).endDate.equals(DEFAULT_STRING))
				timed.add(list.get(k));
		}

		sortByDate(timed);
		return timed;
	}

	/**
	 * This method will sort the list of tasks in order of date.
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private void sortByDate(ArrayList<Task> list) {
		for (int i = 1; i < list.size(); i++) {
			for (int j = 0; j < list.size() - i; j++) {
				String date_input1 = list.get(j).endDate.toString();
				Date date1 = getDate(date_input1);

				String date_input2 = list.get(j + 1).endDate.toString();
				Date date2 = getDate(date_input2);

				if (date2.before(date1))
					Collections.swap(list, j, j + 1);
			}
		}
	}

	
	/**
	 * This method will sort the task list and gather all completed tasks together at the end
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 *
	 * 
	 */
	private void sortByMark(ArrayList<Task> list) {
		ArrayList<Task> todo = new ArrayList<Task>();
		ArrayList<Task> todoSort = new ArrayList<Task>();
		ArrayList<Task> mark = new ArrayList<Task>();
		ArrayList<Task> markSort = new ArrayList<Task>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.contains("(completed)"))
				mark.add(list.get(i));
			else
				todo.add(list.get(i));
		}

		todoSort.addAll(getFloatTask(todo));
		todoSort.addAll(getTimedTask(todo));
		markSort.addAll(getFloatTask(mark));
		markSort.addAll(getTimedTask(mark));

		list.clear();
		list.addAll(todoSort);
		list.addAll(markSort);

	}
}
