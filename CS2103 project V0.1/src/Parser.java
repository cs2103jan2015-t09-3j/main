import java.util.List;

import com.joestelmach.natty.DateGroup;

public class Parser {
	DateGroup group = new DateGroup();
	static String command_type;

	// Parser part -- LIQI
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

	public void commandOnly(String input) {
		command_type = input;
	}

	public void identifyType(String input, Task cmd, int index) {
		command_type = input.substring(0, index);
		if (input.contains("-")) {
			timed(input, cmd, index);
		} else if (index != input.length() - 1) {
			floating(input, cmd, index);
		}
	}

	public void timed(String input, Task cmd, int index) {

		int index2 = input.indexOf("-");
		cmd.detail = input.substring(index + 1, index2);
		if (input.contains("every")) {
			recurring(input, cmd, index2);
		} 
		else {

			nonRecurring(input, cmd, index2);
		}
	}

	private void nonRecurring(String input, Task cmd, int index2) {
		String date_input;
		date_input = input.substring(index2 + 2, input.length());

		group = getNattyDateGroup(date_input);
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

	private void recurring(String input, Task cmd, int index2) {
		int index3 = input.indexOf("every", index2)+6;
		int recurring_time= Integer.parseInt(input.substring(index3, input.indexOf(' ',index3)));
		String recurringperiod = input.substring(input.indexOf(' ',input.indexOf(' ',index3)),input.length());
		if(recurringperiod.contains(" day")||recurringperiod.contains(" days")){
			cmd.recurring_period="day";
		}
		else if(recurringperiod.contains(" hour")||recurringperiod.contains(" hours")){
			cmd.recurring_period="hour";
		}
		else if(recurringperiod.contains(" week")||recurringperiod.contains(" weeks")){
			cmd.recurring_period="week";
		}
		else if(recurringperiod.contains(" month")||recurringperiod.contains(" months")){
			cmd.recurring_period="month";
		}
		
		int index4 = recurringperiod.indexOf("until");
		
		if(index4!=-1){
			index4+=6;
			cmd.recurring_until = recurringperiod.substring(index4,recurringperiod.length());
			group = getNattyDateGroup(cmd.recurring_until);
			String date = group.getDates().toString();
			date = date.substring(1, date.length() - 1);
			cmd.recurring_until = trimDate(date);
		}
		if(recurringperiod.contains("from")||recurringperiod.contains("on")){
			int index5 = recurringperiod.indexOf("from");
			if(index5!=-1){
				index5+=5;
				String date;
				if(index4 == -1){
				cmd.recurring_from = recurringperiod.substring(index5,recurringperiod.length());
				}
				else{
					cmd.recurring_from = recurringperiod.substring(index5,recurringperiod.indexOf("until")-1);
				}
				group = getNattyDateGroup(cmd.recurring_from);
				date = group.getDates().toString();
				date = date.substring(1, date.length() - 1);
				cmd.recurring_from = trimDate(date);
			}
			else{
				index5=recurringperiod.indexOf("on");
				index5+=3;
				String date;
				if(index4 == -1){
				cmd.recurring_from = recurringperiod.substring(index5,recurringperiod.length());
				}
				else{
					cmd.recurring_from = recurringperiod.substring(index5,recurringperiod.indexOf("until")-1);
				}
				group = getNattyDateGroup(cmd.recurring_from);
				date = group.getDates().toString();
				date = date.substring(1, date.length() - 1);
				cmd.recurring_from = trimDate(date);
			}
		}
		
		cmd.recurring_interval = recurring_time;
		
	}

	public void floating(String input, Task cmd, int index) {

		cmd.detail = input.substring(index + 1, input.length());

	}

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

	public String trimDate(String temp) {
		int first = temp.indexOf("SGT");
		temp = temp.substring(0, first - 4);
		return temp;
	}

}
