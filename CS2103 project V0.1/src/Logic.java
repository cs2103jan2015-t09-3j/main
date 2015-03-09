public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();

	public void executeCommand(Cone_Organizer cmd) {

		p.parse(cmd);
		implementCommand(cmd);

	}

	public void implementCommand(Cone_Organizer cmd) {
		switch (cmd.command_type) {
		case "add": {
			s.addCommand(cmd);
			
		}
		case "display": {
			s.displayCommand();
		}

		case "delete": {
			int index;
			index=Integer.parseInt(cmd.detail);
			s.deleteCommand(index);
		}
		case "edit": {
			s.editCommand(cmd);
		}
		}

	}
}
