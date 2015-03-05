
public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();
	
public void executeCommand(Cone_Organizer cmd) {
		
		p.parse(cmd);
		implementCommand(cmd);
		
		
	}
public void implementCommand(Cone_Organizer cmd) {
	switch (cmd.command_type){
	case "add" :{
		s.addCommand(cmd);
		System.out.println("yay");
	}
	case "display" :{
		s.displayCommand(cmd);
	}
	
	}
	case "delete" :{
		s.deleteCommand(cmd);
	}
	case "edit" :{
		s.editCommand(cmd);
	}
	}

}
