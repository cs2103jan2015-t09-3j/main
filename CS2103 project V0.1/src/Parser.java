
public class Parser {
	
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
	
	public void commandOnly (cmd){
		cmd.command_type = cmd.command;
	}
	
	public void identifyType (Cone_Organizer cmd, int index){
		cmd.command_type = cmd.command.substring(0,index);
		if(cmd.command.contains("by")){
			timed(cmd, index);
		}
		else if (index!= cmd.command.length()-1){
			floating(cmd, index);
		}
	}
	
	public void timed(Cone_Organizer cmd, int index){
	
			int index2 = cmd.command.indexOf("by");
			cmd.detail = cmd.command.substring(index+1,index2);
			cmd.date = cmd.command.substring(index2+3, cmd.command.length());		
	}
	
	public void floating(Cone_Organizer cmd, int index){
		
			cmd.detail = cmd.command.substring(index+1, cmd.command.length());
		
	}
}
