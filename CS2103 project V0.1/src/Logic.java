
public class Logic {
private static void executeCommand(Cone_Organizer item) {
		
		parse(item);
		implementCommand(item);
		
		
	}
private static void implementCommand(Cone_Organizer item) {
	switch (item.command_type){
	case "add" :{
		addCommand(item);
	}
	case "display" :{
		displayCommand(item);
	}
	
	}
	}

}
