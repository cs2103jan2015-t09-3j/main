import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.generated.DateParser.parse_return;

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
	 *            This is list of Tasks object. each element contains different
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
		case "cd": {
			s.changeDirectory(cmd.detail);
			return saveCommand(list);
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
			return clearCommand(list);
		}
		case "save": {
			return saveCommand(list);
		}
		case "mark": {
			return markCommand(cmd, list);
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
		default: {
			return "Invalid Command";
		}
		}
	}

	
	
	/**
	 * This method imports items in the text file currently saved in same
	 * directory and put them in the array list.
	 * 
	 * @param list
	 *            This is list of Tasks object. each element contains different
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
	 *            This is list of Tasks object. each element contains different
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
		tempList.clear();
		tempList.addAll(list);
		if (edit == 0) {
			list.add(cmd);
			return cmd.detail + " has been successfully added!";
		} else {

			replaceItem(list, cmd, edit);
			edit = 0;
			return list.get(edit).detail + " has been successfully edited!";
		}

	}

	private String deleteCommand(Task cmd, ArrayList<Task> list) {

		tempList.clear();
		tempList.addAll(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		list.remove(index - 1);
		return cmd.detail + "has been deleted successfully!";
	}

	private String saveCommand(ArrayList<Task> list) {

		s.writeToFile(list);
		return "All changes saved!";
	}

	private String clearCommand(ArrayList<Task> list) {
		tempList.clear();
		tempList.addAll(list);

		list.clear();
		return "All contents are cleared";
	}

	private String undoCommand(ArrayList<Task> list) {

		currList.clear();
		currList.addAll(list);
		list.clear();
		list.addAll(tempList);
		return " Undo to latest 1 change(s)!";
	}

	private String editCommand(Task cmd, ArrayList<Task> list) {

		tempList.clear();
		tempList.addAll(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		edit = index;
		return "edit " + index;

	}

	private String markCommand(Task cmd, ArrayList<Task> list) {

		tempList.clear();
		tempList.addAll(list);

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
		
		todoSort.addAll(getFloatTasks(todo));
		todoSort.addAll(getTimedTasks(todo));
		markSort.addAll(getFloatTasks(mark));
		markSort.addAll(getTimedTasks(mark));
		
		list.clear();
		list.addAll(todoSort);
		list.addAll(markSort);
	}
	
	private ArrayList<Task> getFloatTasks(ArrayList<Task> list){
		ArrayList<Task> floating = new ArrayList<Task>();
		
		for(int k=0; k<list.size();k++){
			if(list.get(k).endDate.equals(DEFAULT_STRING))
				floating.add(list.get(k));
		}
		return floating;
	}
	
	private ArrayList<Task> getTimedTasks(ArrayList<Task> list){
		ArrayList<Task> timed = new ArrayList<Task>();
		
		for(int k=0; k<list.size();k++){
			if(!list.get(k).endDate.equals(DEFAULT_STRING))
				timed.add(list.get(k));
		}
		
		sortByDate(timed);
		return timed;
	}
	
	private void sortByDate(ArrayList<Task> list) {
		for(int i=1; i<list.size();i++){
			for(int j=0; j<list.size()-i; j++){
				String date_input1 = list.get(j).endDate.toString();
				Date date1 = p.getDate(date_input1);
				
				String date_input2 = list.get(j+1).endDate.toString();
				Date date2 = p.getDate(date_input2);
				
				if(date2.before(date1))
					Collections.swap(list, j, j+1);
			}
		}
	}
	
	public Date getDate(String date_input){
		DateGroup group= new DateGroup();
		
		group = p.getNattyDateGroup(date_input);
		List<Date> dates = group.getDates();
		Date date = dates.get(0);
		
		return date;
	}
}
