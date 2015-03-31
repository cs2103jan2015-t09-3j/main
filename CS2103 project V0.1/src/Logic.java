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
//		sortList(list);
		return feedback;
	}

	public String implementCommand(String command_type, Tasks cmd,
			ArrayList<Tasks> list, String feedback) {
		switch (command_type) {
		case "cd":{
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

	private void sortList(ArrayList<Tasks> list){
		try {
			sortByMark(list);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sortByMark(ArrayList<Tasks> list) throws ParseException {
		 ArrayList<Tasks> markList = new ArrayList<Tasks>();
		 markList.addAll(list);
		 list.clear();
		 
		 for(int i=0; i<markList.size();i++){
		 if(!markList.get(i).detail.contains("(completed)")){
			 list.add(markList.get(i));
		 	 markList.remove(i);
		 	 }
		 }
		 
		 if(!list.isEmpty())
			 sortByDate(list);
		 if(!markList.isEmpty()){
			 sortByDate(markList);
			 for(int j=0; j<markList.size();j++){
			 list.add(markList.get(j));
			 markList.remove(j);
			 }
		 }
	}
	
	private void sortByDate(ArrayList<Tasks> parselist) throws ParseException {
		 ArrayList<Tasks> dateList = new ArrayList<Tasks>();
		 dateList.addAll(parselist);
		 parselist.clear();
		 SimpleDateFormat ft = new SimpleDateFormat("EEE MMM DD HH:mm:SS ZZZ yyyy");
		 
		 //take out the floating tasks first 
		 for(int i=0; i<dateList.size();i++){ 
			  if(dateList.get(i).date.equals(DEFAULT_STRING)){
				  parselist.add(dateList.get(i));
				  dateList.remove(i); 
		       } 
		 }
		  
		 //Compare dates of the timed task while(!dateList.isEmpty()){
		 
		 while(!dateList.isEmpty()){
			 if(dateList.size() == 1){ 
				 parselist.add(dateList.get(0));
				 dateList.remove(0);
			 }
		  
			 else{ 
				 String dateX = dateList.get(0).date; 
				 dateX = dateX.substring(1,dateX.length()-1); 
				 Date date1 = ft.parse(dateX);
		  
				 int j = 1; 
				 while(j < dateList.size()){ 
					 String dateY = dateList.get(j).date; 
					 dateY = dateY.substring(1,dateY.length()-1);
					 Date date2 = ft.parse(dateY);
		 
					 if(date2.after(date1)){ 
						 if(j == dateList.size() -1){
							 parselist.add(dateList.get(j));
							 dateList.remove(j); 
						 }
						 else 
							 j++;
					 	 }
		  
				 	else{ 
				 		if(j == dateList.size() -1){
				 			parselist.add(dateList.get(j-1));
				 			dateList.remove(j-1);
				 		} 
				 		else{ 
				 			date1 = ft.parse(dateY);
				 			j++; 
				 		} 
				 	}
				}
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
					|| list.get(i).date.contains(cmd.detail)) {
				return "search " + detail;

			}
		}
		return FAILURE;

	}

	private void replaceItem(ArrayList<Tasks> list, Tasks cmd, int index) {
		list.add(index - 1, cmd);

		list.remove(index);

	}

}
