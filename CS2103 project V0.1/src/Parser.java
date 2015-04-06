

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.runners.model.FrameworkField;

import com.joestelmach.natty.DateGroup;
public class Parser {
	DateGroup group= new DateGroup();
	static String command_type;
	
	//Parser part -- LIQI
	public String parse(String input, Tasks cmd) {
		input.toLowerCase();
		int index = input.indexOf(' ');
		if(index!=-1){
			identifyType(input, cmd, index);

		}
		else{
			commandOnly(input);
		}	
		return command_type.toLowerCase();
	}
	
	public void commandOnly (String input){
		command_type = input;
	}
	
	public void identifyType (String input, Tasks cmd, int index){
		command_type = input.substring(0,index);
		if(input.contains("-")){
			timed(input, cmd, index);
		}
		else if (index!= input.length()-1){
			floating(input, cmd, index);
		}
	}
	
	public void timed(String input, Tasks cmd, int index){
	
			int index2 = input.indexOf("-");
			cmd.detail = input.substring(index+1,index2);
		
			String date_input;
			date_input=input.substring(index2+2, input.length());	
			
			group=getNattyDateGroup(date_input);
			String date = group.getDates().toString();
			int pos = date.indexOf(",");
			if(pos!=-1){
				String start = date.substring(1, pos); 
				cmd.startDate = formattedDate(start);
				String end = date.substring(pos+2, date.length()-1);
				cmd.endDate = formattedDate(end);
			}
			else{
				String due =  date.substring(1, date.length()-1);
				cmd.endDate = formattedDate(due);
			}
	}
	
	public void floating(String input, Tasks cmd, int index){
		
			cmd.detail = input.substring(index+1, input.length());
		
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
	
	public Date getDate(String date_input){
		DateGroup group= new DateGroup();
		
		group = getNattyDateGroup(date_input);
		List<Date> dates = group.getDates();
		Date date = dates.get(0);
		
		return date;
	}
	
	public String formattedDate (String date_input){
		Date date = getDate(date_input);
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");
		String s = formatter.format(date);
		
		return s;
	}
	
}
