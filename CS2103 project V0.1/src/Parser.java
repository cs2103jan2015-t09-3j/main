
public class Parser {
	
	//Parser part -- LIQI
	public void parse(Cone_Organizer cmd) {
		int index = cmd.command.indexOf(' ');
		if(index!=-1){
			cmd.command_type = cmd.command.substring(0,index);
			if(cmd.command.contains("by")){
				int index2 = cmd.command.indexOf("by");
				cmd.detail = cmd.command.substring(index+1,index2);
				cmd.date = cmd.command.substring(index2+3, cmd.command.length());
				
			}
			else if(index!= cmd.command.length()-1){
				cmd.detail = cmd.command.substring(index+1, cmd.command.length());
			}
		}
		else{
			cmd.command_type=cmd.command;
		}
		
		
		
		
	}
}
