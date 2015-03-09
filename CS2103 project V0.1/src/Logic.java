import java.util.ArrayList;



public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();
	

	public void executeCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {

		p.parse(cmd);
		implementCommand(cmd, list);

	}

	public void implementCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {
		switch (cmd.command_type) {
		case "add": {
			s.addCommand(cmd, list);
			break;
			
		}
		case "display": {
			s.displayCommand(list);
			break;
		}

		case "delete": {
			int index;
			index=Integer.parseInt(cmd.detail);
			s.deleteCommand(index, list);
			break;
		}
		case "edit": {
			s.editCommand(cmd);
			break;
		}
		}

	}
}
