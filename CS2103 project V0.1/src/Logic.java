
public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();
	
public void executeCommand(Cone_Organizer item) {
		
		p.parse(item);
		implementCommand(item);
		
		
	}
public void implementCommand(Cone_Organizer item) {
	switch (item.command_type){
	case "add" :{
		s.addCommand(item);
		System.out.println("yay");
	}
	case "display" :{
		s.displayCommand(item);
	}
	
	}
	}

}
