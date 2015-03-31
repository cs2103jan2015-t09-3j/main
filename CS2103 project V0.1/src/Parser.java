

import java.util.List;

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
			int start = input.indexOf("from");
			int end = input.indexOf("to");
			if(start!=-1){
				String startDate = input.substring(start+4, end);
				String endDate = input.substring(end+2, input.length());
				group=getNattyDateGroup(startDate);
				cmd.startDate = group.getDates().toString();	
				group=getNattyDateGroup(endDate);
				cmd.endDate = group.getDates().toString();	
			}
			else{
				String date_input;
				date_input=input.substring(index2+2, input.length());
			
				if(!date_input.equals("none")){			
					group=getNattyDateGroup(date_input);
					cmd.endDate = group.getDates().toString();	
				}
			}
			
	}
	
	public void floating(String input, Tasks cmd, int index){
		
			cmd.detail = input.substring(index+1, input.length());
		
	}
	private DateGroup getNattyDateGroup(String date_input) {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
		List<DateGroup> groups = parser.parse(date_input);
		if (!groups.isEmpty()) {
			DateGroup group = groups.get(0);
			return group;
		} else {
			return null;
		}
	}
}
