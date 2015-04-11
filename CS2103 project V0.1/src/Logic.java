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
	 * @param gUI
	 * @return
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

	public String implementCommand(String command_type, Task cmd,
			ArrayList<Task> list, String feedback) {
		switch (command_type) {
		case "import": {
			return importCommand(list,cmd);
		}
		case "export" :{
			return exportCommand(list,cmd);
		}
		case "add": {
			return addCommand(cmd, list);
		}
		case "delete": {
			
			return deleteCommand(cmd, list);
		}
		case "edit": {
			return editCommand(cmd, list);
		}
		case "clear": {
			return clearCommand(list, cmd);
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
		case "collapse":{
			return collapseCommand(list);
		}
		case "expand":{
			return expandCommand(list);
		}
		case "background":{
			return backgroundChange(cmd);
		}
		default: {
			return "Invalid Command";
		}
		}
	}

	private String exportCommand(ArrayList<Task> list, Task cmd) {
		s.changeDirectory(cmd.detail);
		s.writeToFile(list);
		return "The output textfile has been saved at: "+cmd.detail;
	}

	private String importCommand(ArrayList<Task> list, Task cmd) {
		s.changeDirectory(cmd.detail);
		list.clear();
		list=s.readFromFile();
		return "Imported the existing Cone.txt file from: "+cmd.detail;
	}

	private String backgroundChange(Task cmd) {
		
		return "background " +cmd.detail;
	}

	private String expandCommand(ArrayList<Task> list) {
		for(int i=0; i<list.size(); i++){
			if(list.get(i).isPrimaryRecurringTask==true){
				int index = list.get(i).detail.indexOf("- every");
				if(index!=-1){
				list.get(i).detail = list.get(i).detail.substring(0,index);
				Task cmd = new Task();
				cmd = list.get(i);
				list.remove(i);
				addRecurring(cmd, list);
				}
			}
		}
		return "All recurring tasks are expanded";
	}

	private String collapseCommand(ArrayList<Task> list) {
		tempList = cloneToTempList(list);
		for(int i =0; i<list.size(); i++){
			if(list.get(i).isPrimaryRecurringTask==true){
				list.get(i).detail = list.get(i).detail + " - every "+list.get(i).recurring_interval+" "+list.get(i).recurring_period+" from "+list.get(i).recurring_from+" until "+list.get(i).recurring_until;
			}
			if(list.get(i).recurring_interval!=0){
				if(list.get(i).isPrimaryRecurringTask==false){
					list.remove(i);
					i--;
				}
			}
		}
		return "All recurring tasks are collapsed!";
	}

	private String recurCommand(ArrayList<Task> list, Task cmd) {
		int index = Integer.parseInt(cmd.detail.trim());
		cmd.detail = list.get(index).detail;
		addRecurring(cmd, list);
		list.remove(index);
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
	 */
	public ArrayList<Task> import_From_File(ArrayList<Task> list) {
		list = s.readFromFile();
		return list;
	}

	/**
	 * This method assess the command type and assign an appropriate method for
	 * each commandtype for implementation.
	 * 
	 * @param command_type
	 * @param cmd
	 *            The current command that is being processed
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * @param GUI
	 * @return
	 */

	private String redoCommand(ArrayList<Task> list) {
		list.clear();
		list.addAll(currList);
		return " Redo to latest 1 undo(s)!";
	}

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

	private void addRecurring(Task cmd, ArrayList<Task> list) {


		if (cmd.recurring_until != DEFAULT_STRING) {
			Task t = new Task();
			t.detail = cmd.detail;
			t.endDate = cmd.recurring_from;
			t.recurring_interval = cmd.recurring_interval;
			t.recurring_period= cmd.recurring_period;
			t.recurring_until = cmd.recurring_until;
			t.recurring_from = cmd.recurring_from;
			t.isPrimaryRecurringTask=true;
			list.add(t);


			while (getDate(t.endDate).before(
					getDate("one day after "+cmd.recurring_until))) {
			
				String date = getDate(
						cmd.recurring_interval + " " + cmd.recurring_period
								+ " after " + t.endDate).toString();
				date = date.substring(0, date.length());
				t = new Task();
				t.detail = cmd.detail;
				t.recurring_interval = cmd.recurring_interval;
				t.endDate = p.trimDate(date);
				

				if (getDate(t.endDate).before(
						getDate("23:59 " + cmd.recurring_until))) {

					list.add(t);
				}

			}

		} else {
			Task t = new Task();
			t.detail = cmd.detail;
			t.endDate = cmd.recurring_from;
			t.recurring_interval = cmd.recurring_interval;
			t.recurring_period= cmd.recurring_period;
			t.recurring_until = cmd.recurring_until;
			t.recurring_from = cmd.recurring_from;
			t.isPrimaryRecurringTask=true;
			list.add(t);

			for (int i = 1; i < 10; i++) {
				String date = getDate(cmd.recurring_interval + " " + cmd.recurring_period+ " after " + t.endDate).toString();
				date = date.substring(0, date.length());
				t = new Task();
				t.detail = cmd.detail;
				t.recurring_interval = cmd.recurring_interval;
				t.endDate = p.trimDate(date);
				list.add(t);

			}
		}

	}

	private String deleteCommand(Task cmd, ArrayList<Task> list) {

		tempList = cloneToTempList(list);
		
		String temp = cmd.detail;
		String deleting = DEFAULT_STRING;
		int index=1;
		while (index != -1) {
			index = p.takeoutFirstInt(temp);
			
			
			if (index != -1) {
				deleting = list.get(index-1).detail;
				temp = temp.substring(temp.indexOf(" ") + 1, temp.length());
				removeFromList(list, deleting);
			}
			else{
				index = Integer.parseInt(temp);
				deleting = list.get(index-1).detail;
				removeFromList(list, deleting);
				index =-1;
			}
			
			
		}
		
		return "taks number " + cmd.detail + "have been deleted successfully!";
	}

	private void removeFromList(ArrayList<Task> list, String deleting) {
		for(int i =0; i<list.size(); i++){
			if(list.get(i).detail.equals(deleting)){
				list.remove(i);
				i--;
			}
		}
		
	}

	private ArrayList<Task> cloneToTempList(ArrayList<Task> list) {
		tempList = new ArrayList<Task>();
		tempList.addAll(list);
		return tempList;
	}


	private String saveCommand(ArrayList<Task> list) {

		s.writeToFile(list);
		return "All changes saved at !";
	}

	private String clearCommand(ArrayList<Task> list, Task cmd) {
		tempList = cloneToTempList(list);
		if(cmd.detail==DEFAULT_STRING){
			

		list.clear();
		return "All contents are cleared";
		}
		else{
			if(cmd.detail.equals("completed")){
				clearCompleted(list);
				return "All completed contents are cleared";
				
			}
			else if(cmd.detail.equals("recurring")){
				clearRecurring(list);
				return "All recurring contents are cleared";
			}
		}
		return FAILURE;
	}

	private void clearRecurring(ArrayList<Task> list) {
		for (int i=0; i<list.size(); i++){
			if(list.get(i).recurring_interval!=0){
				list.remove(i);
				i--;
			}
		}
		
	}

	private void clearCompleted(ArrayList<Task> list) {
		for (int i=0; i<list.size(); i++){
			if(list.get(i).detail.contains("(completed)")){
				list.remove(i);
				i--;
			}
		}
		
	}

	private String undoCommand(ArrayList<Task> list) {

		currList.clear();
		currList.addAll(list);
		list.clear();
		list.addAll(tempList);
		return " Undo to latest 1 change(s)!";
	}

	private String editCommand(Task cmd, ArrayList<Task> list) {

		tempList = cloneToTempList(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		edit = index;
		return "edit recur " + index;

	}

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

	private void replaceItem(ArrayList<Task> list, Task cmd, int index) {
		list.add(index - 1, cmd);

		list.remove(index);

	}

	public void import_instruction(ArrayList<String> text) {
		s.importInstruction(text);

	}

	public String parseDate(String date) {
		return (p.getNattyDateGroup(date)).getDates().toString();

	}

	private void sortList(ArrayList<Task> list) {
		sortByMark(list);
	}

	public Date getDate(String date_input) {
		DateGroup group = new DateGroup();

		group = p.getNattyDateGroup(date_input);
		Date date = new Date();
		date = group.getDates().get(0);

		return date;
	}

	private ArrayList<Task> getFloatTask(ArrayList<Task> list) {
		ArrayList<Task> floating = new ArrayList<Task>();

		for (int k = 0; k < list.size(); k++) {
			if (list.get(k).endDate.equals(DEFAULT_STRING))
				floating.add(list.get(k));
		}
		return floating;
	}

	private ArrayList<Task> getTimedTask(ArrayList<Task> list) {
		ArrayList<Task> timed = new ArrayList<Task>();

		for (int k = 0; k < list.size(); k++) {
			if (!list.get(k).endDate.equals(DEFAULT_STRING))
				timed.add(list.get(k));
		}

		sortByDate(timed);
		return timed;
	}

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
