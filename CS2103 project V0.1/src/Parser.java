

import java.util.List;

import com.joestelmach.natty.DateGroup;
public class Parser {
	DateGroup group= new DateGroup();
	
	//Parser part -- LIQI
	public void parse(Cone_Organizer cmd) {
		int index = cmd.command.indexOf(' ');
		if(index!=-1){
			identifyType(cmd, index);

		}
		else{
			commandOnly(cmd);
		}	
		
	}
	
	public void commandOnly (Cone_Organizer cmd){
		cmd.command_type = cmd.command;
	}
	
	public void identifyType (Cone_Organizer cmd, int index){
		cmd.command_type = cmd.command.substring(0,index);
		if(cmd.command.contains("-")){
			timed(cmd, index);
		}
		else if (index!= cmd.command.length()-1){
			floating(cmd, index);
		}
	}
	
	public void timed(Cone_Organizer cmd, int index){
	
			int index2 = cmd.command.indexOf("-");
			cmd.detail = cmd.command.substring(index+1,index2);
			String date_input;
			date_input=cmd.command.substring(index2+2, cmd.command.length());
			if(!date_input.equals("none")){			
				group=getNattyDateGroup(date_input);
				cmd.date = group.getDates().toString();	
			}
			
	}
	
	public void floating(Cone_Organizer cmd, int index){
		
			cmd.detail = cmd.command.substring(index+1, cmd.command.length());
		
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
