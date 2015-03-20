
public class Parser {
	
	//Parser part -- LIQI
	
	/**
	* This method will recognize whether the commands are tasks or simply commands
	* @param cmd  	This object contains just the user's command	 
	*/
	public void parse(Cone_Organizer cmd) {
		int index = cmd.command.indexOf(' ');
		if(index!=-1){
			identifyType(cmd, index);

		}
		else{
			commandOnly(cmd);
		}	
		
	}

	/**
	* This method will assign user's command to command type since there is only command
	* @param cmd  	This object contains command only and no tasks are specified 
	*/
	public void commandOnly (Cone_Organizer cmd){
		cmd.command_type = cmd.command;
	}
	
	/**
	* This method will recognize whether it is a floating or timed task
	* @param cmd  	This object contains the task content and date of the new task
	* @param index	Index separates the command from task details
	*/
	public void identifyType (Cone_Organizer cmd, int index){
		cmd.command_type = cmd.command.substring(0,index);
		if(cmd.command.contains("by")){
			timed(cmd, index);
		}
		else if (index!= cmd.command.length()-1){
			floating(cmd, index);
		}
	}
	
	/**
	* This method will handle timed task and task details
	* @param cmd  	This object contains the task type, task content and date of the new task
	* @param index	Index separates the command from task details	 
	*/
	public void timed(Cone_Organizer cmd, int index){
	
			int index2 = cmd.command.indexOf("by");
			cmd.detail = cmd.command.substring(index+1,index2);
			cmd.date = cmd.command.substring(index2+3, cmd.command.length());		
	}
	
	/**
	* This method will handle floating task and task details
	* @param cmd  	This object contains the task type and task content of the new task
	* @param index	Index separates the command from task details	 
	*/
	public void floating(Cone_Organizer cmd, int index){
		
			cmd.detail = cmd.command.substring(index+1, cmd.command.length());
		
	}
}
