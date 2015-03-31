import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logic {
	private static final String DEFAULT_STRING = "none";
	private static final String FAILURE = "OPERATION FAILED!!";
	Parser p = new Parser();
	Storage s = new Storage();
	Tasks cmd = new Tasks();

	ArrayList<Tasks> list = new ArrayList<Tasks>();
	ArrayList<Tasks> tempList = new ArrayList<Tasks>();
	ArrayList<Tasks> currList = new ArrayList<Tasks>();
	static String command_type = DEFAULT_STRING;
	int tmp;
	int index;

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
	public String executeCommand(String input, ArrayList<Tasks> list) {
		String feedback = DEFAULT_STRING;
		assert list != null;
		cmd = new Tasks();
		command_type = p.parse(input, cmd);
		feedback = implementCommand(command_type, cmd, list, feedback);
		sortList(list);
		return feedback;
	}

	public String implementCommand(String command_type, Tasks cmd,
			ArrayList<Tasks> list, String feedback) {
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

	private void sortList(ArrayList<Tasks> list) {
		
		sortByDate(list);
		sortByMark(list);
	}

	private void sortByMark(ArrayList<Tasks> list) {
		ArrayList<Tasks> temp = new ArrayList<Tasks>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.contains("(completed)")) {
				temp.add(list.get(i));
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).detail.contains("(completed)")) {
				temp.add(list.get(i));
			}
		}
		list.clear();
		list.addAll(temp);

	}

	private void sortByDate(ArrayList<Tasks> list) {
		DateSorter sorter = new DateSorter();
		ArrayList<Integer> indexlist = new ArrayList<Integer>();
		ArrayList<Tasks> nodatelist = new ArrayList<Tasks>();
		ArrayList<DateSorter> datelist = new ArrayList<DateSorter>();

		for (int i = 0; i < list.size(); i++) {
			sorter = new DateSorter();
			if (list.get(i).endDate.equals("none")) {
				nodatelist.add(list.get(i));
				list.remove(i);
				i--;
			} 
			else {

				sorter.index = i;
				sorter.year = Integer.parseInt(list.get(i).endDate.substring(
						24, 28));
				sorter.month = returnNumMonth(list.get(i).endDate.substring(4,
						7));
				sorter.day = Integer.parseInt(list.get(i).endDate.substring(8,
						10));
				sorter.time = Integer.parseInt(list.get(i).endDate.substring(
						11, 13)
						+ list.get(i).endDate.substring(14, 16)
						+ list.get(i).endDate.substring(17, 19));
				datelist.add(sorter);
			}

		}
		while (!datelist.isEmpty()) {
			boolean value_same = false;
			compareYear(indexlist, value_same, datelist);
		}
			modifyList(indexlist, list, nodatelist);
		

	}

	private int returnNumMonth(String month) {
		if (month.equals("Jan")) {
			return 1;
		}
		if (month.equals("Feb")) {
			return 2;
		}
		if (month.equals("Mar")) {
			return 3;
		}
		if (month.equals("Apr")) {
			return 4;
		}
		if (month.equals("May")) {
			return 5;
		}
		if (month.equals("Jun")) {
			return 6;
		}
		if (month.equals("Jul")) {
			return 7;
		}
		if (month.equals("Aug")) {
			return 8;
		}
		if (month.equals("Sep")) {
			return 9;
		}
		if (month.equals("Oct")) {
			return 10;
		}
		if (month.equals("Nov")) {
			return 11;
		}
		if (month.equals("Dec")) {
			return 12;
		} else {
			return 0;
		}

	}

	private void modifyList(ArrayList<Integer> indexlist,
			ArrayList<Tasks> list, ArrayList<Tasks> nodatelist) {
		ArrayList<Tasks> temp = new ArrayList<Tasks>();
		for (int i = 0; i < indexlist.size(); i++) {
			temp.add(list.get(indexlist.get(i)));
		}
		if (!nodatelist.isEmpty()) {
			for (int i = 0; i < nodatelist.size(); i++) {
				temp.add(nodatelist.get(i));
			}
			if(!list.isEmpty())
			list.clear();
			list.addAll(temp);

		}
	}

	private void compareTimes(ArrayList<Integer> indexlist, boolean value_same,
			ArrayList<DateSorter> datelist) {
		ArrayList<DateSorter> temp = new ArrayList<DateSorter>();
		if (!datelist.isEmpty()) {
			tmp = datelist.get(0).time;
			index = 0;
			for (int i = 1; i < datelist.size(); i++) {

				if (tmp > datelist.get(i).time) {
					tmp = datelist.get(i).time;
					index = i;
				}

			}
			indexlist.add(datelist.get(index).index);
			datelist.remove(index);
			for (int i = 0; i < datelist.size(); i++) {

				indexlist.add(datelist.get(i).index);
				datelist.remove(i);
				i--;

			}

		}
	}

	private void compareDay(ArrayList<Integer> indexlist, boolean value_same,
			ArrayList<DateSorter> datelist) {
		ArrayList<DateSorter> temp = new ArrayList<DateSorter>();
		if (!datelist.isEmpty()) {
			tmp = datelist.get(0).day;
			index = 0;
			for (int i = 1; i < datelist.size(); i++) {

				if (tmp > datelist.get(i).day) {
					tmp = datelist.get(i).day;
					index = i;
				}

			}
			temp.add(datelist.get(index));
			datelist.remove(index);
			for (int i = 0; i < datelist.size(); i++) {

				if (datelist.get(i).day == tmp) {
					value_same = true;
					temp.add(datelist.get(i));
					datelist.remove(i);
					i--;
				}

			}
			if (value_same == false) {
				indexlist.add(datelist.get(index).index);

			} else {
				compareTimes(indexlist, value_same, temp);
				compareDay(indexlist, value_same, datelist);
			}
		}
	}

	private void compareMonth(ArrayList<Integer> indexlist, boolean value_same,
			ArrayList<DateSorter> datelist) {
		ArrayList<DateSorter> temp = new ArrayList<DateSorter>();
		if (!datelist.isEmpty()) {
			tmp = datelist.get(0).month;
			index = 0;
			for (int i = 1; i < datelist.size(); i++) {

				if (tmp > datelist.get(i).month) {
					tmp = datelist.get(i).month;
					index = i;
				}

			}
			temp.add(datelist.get(index));
			datelist.remove(index);
			for (int i = 0; i < datelist.size(); i++) {

				if (datelist.get(i).month == tmp) {
					value_same = true;
					temp.add(datelist.get(i));
					datelist.remove(i);
					i--;
				}

			}
			if (value_same == false) {
				indexlist.add(datelist.get(index).index);
			} else {
				compareDay(indexlist, value_same, temp);
				compareMonth(indexlist, value_same, datelist);
			}
		}
	}

	private void compareYear(ArrayList<Integer> indexlist, boolean value_same,
			ArrayList<DateSorter> datelist) {
		ArrayList<DateSorter> temp = new ArrayList<DateSorter>();

		if (!datelist.isEmpty()) {
			tmp = datelist.get(0).year;
			index = 0;
			for (int i = 1; i < datelist.size(); i++) {

				if (tmp > datelist.get(i).year) {
					tmp = datelist.get(i).year;
					index = i;
				}

			}
			temp.add(datelist.get(index));
			datelist.remove(index);

			for (int i = 0; i < datelist.size(); i++) {

				if (datelist.get(i).year == tmp) {
					value_same = true;
					temp.add(datelist.get(i));
					datelist.remove(i);
					i--;

				}

			}

			if (value_same == false) {
				indexlist.add(datelist.get(index).index);

			} else {
				compareMonth(indexlist, value_same, temp);
				compareYear(indexlist, value_same, datelist);
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
	public ArrayList<Tasks> import_From_File(ArrayList<Tasks> list) {
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

	private String redoCommand(ArrayList<Tasks> list) {
		list.clear();
		list.addAll(currList);
		return " Redo to latest 1 undo(s)!";
	}

	private String addCommand(Tasks cmd, ArrayList<Tasks> list) {
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

	private String deleteCommand(Tasks cmd, ArrayList<Tasks> list) {

		tempList.clear();
		tempList.addAll(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		list.remove(index - 1);
		return cmd.detail + "has been deleted successfully!";
	}

	private String saveCommand(ArrayList<Tasks> list) {

		s.writeToFile(list);
		return "All changes saved!";
	}

	private String clearCommand(ArrayList<Tasks> list) {
		tempList.clear();
		tempList.addAll(list);

		list.clear();
		return "All contents are cleared";
	}

	private String undoCommand(ArrayList<Tasks> list) {

		currList.clear();
		currList.addAll(list);
		list.clear();
		list.addAll(tempList);
		return " Undo to latest 1 change(s)!";
	}

	private String editCommand(Tasks cmd, ArrayList<Tasks> list) {

		tempList.clear();
		tempList.addAll(list);

		int index;
		index = Integer.parseInt(cmd.detail);
		edit = index;
		return "edit " + index;

	}

	private String markCommand(Tasks cmd, ArrayList<Tasks> list) {

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

	private String searchCommand(ArrayList<Tasks> list, String detail) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.toLowerCase().contains(cmd.detail)
					|| list.get(i).startDate.contains(cmd.detail)
					|| list.get(i).endDate.contains(cmd.detail)) {
				return "search " + detail;

			}
		}
		return FAILURE;

	}

	private void replaceItem(ArrayList<Tasks> list, Tasks cmd, int index) {
		list.add(index - 1, cmd);

		list.remove(index);

	}

	public void import_instruction(ArrayList<String> text) {
		s.importInstruction(text);

	}

}
