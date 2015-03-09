
public class Parser {
	
	//Parser part -- LIQI
	public void parse(Cone_Organizer cmd) {
		int index = cmd.command.indexOf(' ');
		cmd.command_type = cmd.command.substring(0,index);
		cmd.detail = cmd.command.substring(index, cmd.command.length());
		int index2 = cmd.command.indexOf("");
		cmd.date = cmd.command.substring(index2, cmd.command.length());
	}
}
