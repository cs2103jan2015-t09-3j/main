
public class Parser {
	
	//Parser part -- LIQI
	public void parse(Cone_Organizer item) {
		int index = item.command.indexOf(' ');
		item.command_type = item.command.substring(0,index);
		item.detail = item.command.substring(index, item.command.length());
	}
}
